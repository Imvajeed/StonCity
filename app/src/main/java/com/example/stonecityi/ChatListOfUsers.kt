package com.example.stonecityi

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.stonecityi.databinding.ActivityChatListOfUsersBinding
import com.example.stonecityi.stonecityRecycler.ChatListOfUsersAdapter
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore

class ChatListOfUsers : AppCompatActivity() {
    lateinit var binding : ActivityChatListOfUsersBinding

    //firebase
    val db = FirebaseFirestore.getInstance()
    //firebase auth
    lateinit var auth : FirebaseAuth

    //adaptor related
    lateinit var data : ArrayList<ChatListUserInfo>
    lateinit var adappter :ChatListOfUsersAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_chat_list_of_users)
        data = arrayListOf()
        auth = Firebase.auth
        val currentUser = auth.currentUser
        val collectionReference = db.collection("ChatList${currentUser?.uid.toString()}")

        collectionReference.get().addOnSuccessListener {

            if(!it.isEmpty){
                for (doc in it){
                    val user = ChatListUserInfo(
                        doc.data.get("userName").toString(),
                        doc.data.get("userId").toString(),
                        doc.data.get("userImgUrl").toString()

                    )
                    data.add(user)
                }
                adappter = ChatListOfUsersAdapter(this,data)
                binding.chatlistRecycler.layoutManager = LinearLayoutManager(this)
                binding.chatlistRecycler.adapter = adappter
            }

        }
        binding.backToHome.setOnClickListener {
            startActivity(Intent(this,MainActivity::class.java))
            finish()
        }
        Log.i("ChatsVajeed","ChatsVajeed $data")


    }
}