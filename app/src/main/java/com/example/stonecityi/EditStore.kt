package com.example.stonecityi

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.stonecityi.databinding.ActivityEditStoreBinding
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class EditStore : AppCompatActivity() {
    //binding
    lateinit var binding: ActivityEditStoreBinding


    //firebase
    lateinit var auth : FirebaseAuth
    //firestore
    val db = FirebaseFirestore.getInstance()
    val collectionReference = db.collection("Stores")
    lateinit var imageUri: Uri

    lateinit var storageReference: StorageReference
     
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_edit_store)
        storageReference = FirebaseStorage.getInstance().getReference()
        val bundle = intent.extras
        var storeData = StoreData(
            bundle?.getString("userId").toString(),
            bundle?.getString("storeImg").toString(),
            bundle?.getString("storeName").toString(),
            bundle?.getString("storeContact").toString(),
            bundle?.getString("location").toString(),
            bundle?.getString("storeId").toString(),
        )

        assignValue(storeData)

        binding.exhangeImage.setOnClickListener{
            pickimage()

        }
        binding.backToStore.setOnClickListener {
            startActivity(Intent(this,MyStore::class.java))
            finish()
        }
        binding.apply {
            cancelImage.setOnClickListener{
                Glide.with(root)
                    .load(storeData.storeImgUrl)
                    .apply(
                        RequestOptions()
                            .placeholder(R.drawable.profile) // Placeholder image
                            .error(R.drawable.logout) // Error image in case of loading failure
                    )
                    .into(storeImage)
                binding.cancelImage.visibility = View.GONE
                binding.saveImage.visibility = View.GONE
            }
            saveImage.setOnClickListener{
                val filepath : StorageReference = storageReference.child("MyStoreImg").child("my_store"+ Timestamp.now().seconds)
                filepath.putFile(imageUri).addOnSuccessListener {
                    filepath.downloadUrl.addOnSuccessListener {
                        imageUri = it
                        collectionReference.document(storeData.storeId).update("storeImgUrl",imageUri.toString())
                        Toast.makeText(
                            this@EditStore,
                            "ImageUpdated",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
                storeData.storeImgUrl = imageUri.toString()
                binding.cancelImage.visibility = View.GONE
                binding.saveImage.visibility = View.GONE
            }
        }

        binding.updateStore.setOnClickListener {
            updateProfileData(storeData)
            storeData.storeLocation = binding.storeAddress.text.toString()
            storeData.storeContact = binding.storePhoneNum.text.toString()
            storeData.storeName = binding.storeName.text.toString()
            Log.i("EditStoreLoude","${storeData}")
        }





    }

    private fun updateProfileData(storeData: StoreData) {
        binding.apply {
            if (storeAddress.text!=null && storeName.text !=null && storePhoneNum!=null){
                collectionReference.document(storeData.storeId).update("storeName",storeName.text.toString())
                collectionReference.document(storeData.storeId).update("storeLocation",storeAddress.text.toString())
                collectionReference.document(storeData.storeId).update("storeContact",storePhoneNum.text.toString())

            }
        }
    }
    private fun pickimage() {
        val i : Intent = Intent(Intent.ACTION_GET_CONTENT)
        i.setType("image/*")
        startActivityForResult(i,1)
    }


    private fun assignValue(storeData: StoreData) {

        binding.apply {
            storeName.setText(storeData.storeName)
            storeAddress.setText(storeData.storeLocation)
            storePhoneNum.setText(storeData.storeContact)
            Glide.with(root)
                .load(storeData.storeImgUrl)
                .apply(
                    RequestOptions()
                        .placeholder(R.drawable.profile) // Placeholder image
                        .error(R.drawable.logout) // Error image in case of loading failure
                )
                .into(storeImage)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == 1 && resultCode == RESULT_OK){
            if(data!=null){
                imageUri = data.data!! //getting th actual image path
                binding.storeImage.setImageURI(imageUri) //showing the imgae
                binding.storePhotoView.visibility = View.VISIBLE
                binding.cancelImage.visibility = View.VISIBLE
                binding.saveImage.visibility = View.VISIBLE
            }
        }
    }

}