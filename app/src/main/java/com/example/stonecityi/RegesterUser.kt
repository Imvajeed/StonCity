package com.example.stonecityi

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.example.stonecityi.databinding.ActivityRegesterUserBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore

class RegesterUser : AppCompatActivity() {
    lateinit var binding : ActivityRegesterUserBinding
    //firebaseauth
    lateinit var auth : FirebaseAuth
    //firebase database
    val db = FirebaseFirestore.getInstance()
    val collectionReference = db.collection("Users")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_regester_user)
        auth = Firebase.auth
        val user = auth.currentUser
        binding.apply {
            userName.setText(user?.displayName.toString())
            userEmail.text = user?.email.toString()

        }
        binding.userRegester.setOnClickListener{
            binding.apply {
                if (userName.text != null && userAddress.text !=null &&userPhNumber.text!=null){
                    val name = userName.text.toString()
                    val email = userEmail.text.toString()
                    val contact = userPhNumber.text.toString()
                    val address = userAddress.text.toString()
                    val image = user?.photoUrl.toString()
                    val userId  = user?.uid.toString()
                    val userInfo = UserInfo(name,email,contact,address,image,userId)
                    collectionReference.document(userInfo.userId).set(userInfo).addOnSuccessListener {

                        startActivity(Intent(this@RegesterUser,MainActivity::class.java))
                        finish()

                    }.addOnFailureListener{
                        Toast.makeText(
                            this@RegesterUser,
                            "Opps! something went wrong",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                }else{
                    Toast.makeText(
                        this@RegesterUser,
                        "fill all the fields",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }
}