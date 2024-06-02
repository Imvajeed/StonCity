package com.example.stonecityi

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.stonecityi.databinding.ActivityChatLayoutBinding
import com.example.stonecityi.stonecityRecycler.ChatAdapter
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import com.google.firebase.database.getValue

class ChatLayout : AppCompatActivity() {
    lateinit var binding : ActivityChatLayoutBinding
    lateinit var messages : ArrayList<Message>
    //live database
    val db = Firebase.database
    val msgReference = db.getReference("Messages")
    lateinit var auth : FirebaseAuth
    private lateinit var database: DatabaseReference


    //adaptor
    lateinit var adapter: ChatAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_chat_layout)
        messages = arrayListOf()
        setSupportActionBar(binding.toolBar)
        val reciverUserid = intent.getStringExtra("userId")!!
        val userName = intent.getStringExtra("userName")!!
        supportActionBar?.title = userName
        val actionBar = supportActionBar!!
        actionBar.setDisplayHomeAsUpEnabled(true)
        //Firebase
        auth = Firebase.auth
        val senderUserId = auth.currentUser?.uid
        val forSenderReference = senderUserId+reciverUserid
        val forReciverReference = reciverUserid+senderUserId

        database = Firebase.database.reference
        adapter = ChatAdapter(this,messages)
        binding.chatRecycler.layoutManager = LinearLayoutManager(this)
        binding.chatRecycler.adapter = adapter


        //getchats
        database.child("Chats").child(forSenderReference).addValueEventListener(object: ValueEventListener{

            override fun onDataChange(snapshot: DataSnapshot) {
                messages.clear()
                for (sp in snapshot.children){
                    val msg = sp.getValue(Message::class.java)
                    messages.add(msg!!)
                }
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                Log.i("KingKong","Dont KNow")
            }

        })








        binding.sendMsgButton.setOnClickListener{
            sendMessage(forSenderReference,forReciverReference)

            binding.messageBox.setText("")
        }


    }

    private fun getData(sender:String) {
        database.child("Chats").child(sender).addValueEventListener(object: ValueEventListener{

            override fun onDataChange(snapshot: DataSnapshot) {
                messages.clear()
                for (sp in snapshot.children){
                    val msg = sp.getValue(Message::class.java)
                    messages.add(msg!!)
                }
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                Log.i("KingKong","Dont KNow")
            }

        })
    }

    private fun sendMessage(sender:String, reciver:String) {
        val msg = binding.messageBox.text.toString()

        if (msg!=null){
            val msgBox = Message(
                msg,
                Firebase.auth.currentUser?.uid.toString()
            )
            database.child("Chats").child(sender).push().setValue(msgBox).addOnSuccessListener {
                database.child("Chats").child(reciver).push().setValue(msgBox)
            }


        }
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home-> {
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}