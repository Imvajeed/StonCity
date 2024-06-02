package com.example.stonecityi

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.stonecityi.databinding.ActivityUserProfileBinding
import com.example.stonecityi.roomdatabase.RoomDataBaseSingle
import com.example.stonecityi.roomdatabase.UserDao
import com.example.stonecityi.roomdatabase.UserData
import com.example.stonecityi.roomdatabase.UserRepository
import com.example.stonecityi.ui.StonecityViewModel
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore

class UserProfile : AppCompatActivity() {
    lateinit var binding : ActivityUserProfileBinding

    //firebase
    lateinit var auth : FirebaseAuth
    lateinit var userInfo: UserData
    //firebase db
    val db = FirebaseFirestore.getInstance()
    val collectionReference = db.collection("Users")

    //localdatabase
    lateinit var repository: UserRepository
    lateinit var dao: UserDao
    lateinit var viewmodel : StonecityViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_user_profile)

        auth = Firebase.auth
        val user = auth.currentUser
        dao = RoomDataBaseSingle.getInstance(application).userDao
        repository = UserRepository(dao)
        viewmodel = StonecityViewModel(repository,this)
        userInfo = viewmodel.getUserInfo()
        assignInfo()

        binding.apply {
            backToHomeButton.setOnClickListener{
                startActivity(Intent(this@UserProfile,MainActivity::class.java))
                finish()
            }
            editProfile.setOnClickListener {
                startActivity(Intent(this@UserProfile,EditProfile::class.java))
                finish()
            }
            myStoreButton.setOnClickListener {
                startActivity(Intent(this@UserProfile,MyStore::class.java))
            }
        }
    }
    fun assignInfo(){
        binding.apply {
            userName.text = userInfo.userName
            bannerName.text = "@ ${userInfo.userName}"
            userEmail.text = userInfo.userEmail
            userPhNumber.text = userInfo.contactNum
            userAddress.text = userInfo.userAddress

            Glide.with(binding.root)
                .load(userInfo.imageUrl)
                .apply(
                    RequestOptions()
                        .placeholder(R.drawable.profile) // Placeholder image
                        .error(R.drawable.logout) // Error image in case of loading failure
                )
                .into(binding.userImage)
        }
    }
}