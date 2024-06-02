package com.example.stonecityi

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.stonecityi.databinding.ActivityEditProfileBinding
import com.example.stonecityi.roomdatabase.RoomDataBaseSingle
import com.example.stonecityi.roomdatabase.UserDao
import com.example.stonecityi.roomdatabase.UserData
import com.example.stonecityi.roomdatabase.UserRepository
import com.example.stonecityi.ui.StonecityViewModel
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import java.lang.Exception

class EditProfile : AppCompatActivity() {
    //binding
    lateinit var bindding : ActivityEditProfileBinding

    //firebase
    lateinit var auth : FirebaseAuth
    //firebase database
    val db = FirebaseFirestore.getInstance()
    val collectionReference = db.collection("Users")
    lateinit var userInfo: UserData


    //local database
    lateinit var viewModel : StonecityViewModel
    lateinit var dao : UserDao
    lateinit var repository: UserRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindding = DataBindingUtil.setContentView(this,R.layout.activity_edit_profile)

        auth = Firebase.auth
        dao = RoomDataBaseSingle.getInstance(application).userDao
        repository = UserRepository(dao)
        viewModel = StonecityViewModel(repository,this)
        userInfo = viewModel.getUserInfo()
        assignInfo()

        bindding.updateProfile.setOnClickListener {
            if (bindding.userName.text != null && bindding.userAddress!=null && bindding.userPhNumber.text!=null){
                val userInfoDup = UserData(
                    userInfo.userId,
                    bindding.userName.text.toString(),
                    userInfo.userEmail,
                    bindding.userPhNumber.text.toString(),
                    bindding.userAddress.text.toString(),
                    userInfo.imageUrl

                )
                try {
                    collectionReference.document(userInfoDup.userId).update("userName",userInfoDup.userName)
                    collectionReference.document(userInfoDup.userId).update("contactNum",userInfoDup.contactNum)
                    collectionReference.document(userInfoDup.userId).update("userAddress",userInfoDup.userAddress)
                    viewModel.updateData(userInfoDup)
                    Toast.makeText(
                        this,
                        "Update Success",
                        Toast.LENGTH_LONG
                    ).show()
                    startActivity(Intent(this,UserProfile::class.java))
                    finish()
                }
                catch (e : Exception){
                    Toast.makeText(
                        this,
                        "Opps! something went wrong",
                        Toast.LENGTH_LONG
                    ).show()

                }

            }
        }
        bindding.backToProfile.setOnClickListener {
            startActivity(Intent(this,UserProfile::class.java))
            finish()
        }


    }

    private fun assignInfo() {
        Glide.with(bindding.root)
            .load(userInfo.imageUrl)
            .apply(
                RequestOptions()
                    .placeholder(R.drawable.profile) // Placeholder image
                    .error(R.drawable.logout) // Error image in case of loading failure
            ).into(bindding.userImage)
        bindding.apply {
            userName.setText(userInfo.userName)
            userPhNumber.setText( userInfo.contactNum)
            userAddress.setText( userInfo.userAddress)
            bannerName.text = userInfo.userName
        }
    }
}