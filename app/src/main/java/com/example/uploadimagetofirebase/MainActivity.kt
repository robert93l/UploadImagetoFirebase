package com.example.uploadimagetofirebase

import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.storage.StorageReference
import android.app.ProgressDialog
import android.os.Bundle
import com.google.firebase.storage.FirebaseStorage

import android.widget.Toast

import android.content.Intent
import android.net.Uri
import com.example.uploadimagetofirebase.databinding.ActivityMainBinding
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {
    var binding: ActivityMainBinding? = null
    var imageUri: Uri? = null
    var storageReference: StorageReference? = null
    var progressDialog: ProgressDialog? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(
            layoutInflater
        )
        setContentView(binding!!.root)

        binding!!.selectImagebtn.setOnClickListener { selectImage() }

        binding!!.uploadimagebtn.setOnClickListener { uploadImage() }
    }

    private fun uploadImage() {
        progressDialog = ProgressDialog(this)
        progressDialog!!.setTitle("uploading file...")
        progressDialog!!.show()

        val formatter = SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.getDefault())
        val now = Date()
        val fileName = formatter.format(now)
        storageReference = FirebaseStorage.getInstance().reference
        storageReference = FirebaseStorage.getInstance().getReference("images/$fileName")
        storageReference!!.putFile(imageUri!!).addOnSuccessListener {
            binding!!.firebaseimage.setImageURI(null)
            Toast.makeText(this@MainActivity, "succesfull uploaded", Toast.LENGTH_SHORT).show()
            if (progressDialog!!.isShowing) progressDialog!!.dismiss()
        }.addOnFailureListener {
            if (progressDialog!!.isShowing) progressDialog!!.dismiss()
            Toast.makeText(this@MainActivity, "Fail upload", Toast.LENGTH_SHORT).show()
        }
    }

    private fun selectImage() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(intent, 100)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 100 && data != null && data.data != null) {
            imageUri = data.data
            binding!!.firebaseimage.setImageURI(imageUri)
        }
    }

}