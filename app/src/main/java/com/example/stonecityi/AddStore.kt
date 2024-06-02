package com.example.stonecityi

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.databinding.DataBindingUtil
import com.example.stonecityi.databinding.ActivityAddStoreBinding
import com.google.firebase.Firebase
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class AddStore : AppCompatActivity() {
    //binding
    lateinit var binding: ActivityAddStoreBinding

    //image uri
    lateinit var imageUri : Uri


    //firebase
    lateinit var auth: FirebaseAuth
    //database references firebase
    val db = FirebaseFirestore.getInstance()
    val collectionReference = db.collection("Stores")

    //cloud storage reference firebase
    lateinit var storageReference: StorageReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_add_store)

        storageReference = FirebaseStorage.getInstance().getReference()
        auth=Firebase.auth

        binding.apply {
            pickImage.setOnClickListener{
                pickimage()
            }

            exhangeImage.setOnClickListener{
                pickimage()
            }

            addStoreButton.setOnClickListener{
                addStoreToDataBase()
            }
        }




    }

    private fun addStoreToDataBase() {
        val storeName = binding.storeName.text.toString()
        val storeLocation = binding.storeAddress.text.toString()
        val storeContact = binding.storePhoneNum.text.toString()
        val storeId = auth.currentUser?.uid.toString()+Timestamp.now().seconds
        if (
            !TextUtils.isEmpty(storeName) &&
            !TextUtils.isEmpty(storeLocation) &&
            !TextUtils.isEmpty(storeContact) &&
            imageUri!=null
        ){

            val filepath : StorageReference = storageReference.child("MyStoreImg").child("my_store"+Timestamp.now().seconds)
            filepath.putFile(imageUri).addOnSuccessListener {
                filepath.downloadUrl.addOnSuccessListener {
                    val imgUrl = it.toString()
                    val storeData = StoreData(
                        auth.currentUser?.uid.toString(),
                        imgUrl,
                        storeName,
                        storeContact,
                        storeLocation,
                        storeId
                    )

                    collectionReference.document(storeId).set(storeData).addOnSuccessListener {
                        Toast.makeText(
                            this,
                            "Store added Succefully",
                            Toast.LENGTH_LONG
                        ).show()
                        startActivity(Intent(this,MainActivity::class.java))
                    }
                }
            }.addOnFailureListener {
                Toast.makeText(
                    this,
                    "Opps! something wrnt wrong",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    private fun pickimage() {
        val i : Intent = Intent(Intent.ACTION_GET_CONTENT)
        i.setType("image/*")
        startActivityForResult(i,1)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == 1 && resultCode == RESULT_OK){
            if(data!=null){
                imageUri = data.data!! //getting th actual image path
                binding.storeImage.setImageURI(imageUri) //showing the imgae
                binding.storePhotoView.visibility = View.VISIBLE
            }
        }
    }
}