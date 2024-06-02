package com.example.stonecityi

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.ContextMenu
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.databinding.DataBindingUtil
import com.example.stonecityi.databinding.ActivityMainBinding
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
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.stonecityi.stonecityRecycler.MainMenuRecyclerView


class MainActivity : AppCompatActivity() {

    //viewModel
    lateinit var viewModel: StonecityViewModel
    lateinit var dao : UserDao
    lateinit var repository: UserRepository
    lateinit var storeData: MutableList<StoreData>
    lateinit var adaptor : MainMenuRecyclerView
    //binding
    lateinit var binding : ActivityMainBinding

    //firebase
    lateinit var auth : FirebaseAuth

    //firebase firestore
    val db = FirebaseFirestore.getInstance()
    val collectionReference = db.collection("Users")
    lateinit var userInfo : UserData
    val storeCollectionReference = db.collection("Stores")
    //drawer toggle

    lateinit var actionBarDrawerToggle : ActionBarDrawerToggle


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this,R.layout.activity_main)
        storeData = arrayListOf()
        //drawerlayout
        setSupportActionBar(null)

        setSupportActionBar(binding.toolBar)
        actionBarDrawerToggle = ActionBarDrawerToggle(this,binding.drawerLayout,binding.toolBar,0,0)
        binding.drawerLayout.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.syncState()

        //item selected handling
        binding.navView.setNavigationItemSelectedListener(){
            onItemSelecteHandling(it)
            true
        }
        //firebase auth
        auth = Firebase.auth
        val user = auth.currentUser
        Log.i("LoginVajeed","${user?.uid}")
        if (user?.uid==null){

            startActivity(Intent(this,LogIn::class.java))
            finish()
        }
        //viewmodel and repo and room database
        binding.lifecycleOwner = this
        dao = RoomDataBaseSingle.getInstance(application).userDao

        repository = UserRepository(dao)


        viewModel = StonecityViewModel(repository,this)

//        Log.i("FuckerVajeed","${viewModel.findUser(user?.uid.toString())}")
        collectionReference.whereEqualTo("userId",user?.uid.toString()).get().addOnSuccessListener {
//            Log.i("VajeedUid","shadow ${it.documents[0].get("userId")}")
            if (!it.isEmpty && auth.currentUser?.uid == it.documents[0].get("userId")){
                Log.i("VajeedUid","vajeed ${it.documents}")
                startTrigger()

            }else{
                startActivity(Intent(this,RegesterUser::class.java))
            }
        }.addOnFailureListener {
            Toast.makeText(
                this,
                "opps! something went wrong",
                Toast.LENGTH_SHORT
            ).show()
        }

        //recyclerViw work
        binding.mainMenuRecycler.layoutManager = LinearLayoutManager(this)
        storeCollectionReference.get().addOnSuccessListener {
            if (!it.isEmpty){
                for(document in it){
                    val sdata = StoreData(
                        document.data["userId"].toString(),
                        document.data["storeImgUrl"].toString(),
                        document.data["storeName"].toString(),
                        document.data["storeContact"].toString(),
                        document.data["storeLocation"].toString(),
                        document.data["storeId"].toString()
                    )
                    storeData.add(sdata)
                }
                adaptor = MainMenuRecyclerView(storeData,this){
                    onItemClicked(it)
                }
                binding.mainMenuRecycler.adapter = adaptor
            }
        }




    }

    private fun onItemClicked(data: StoreData) {
        val currentUser = auth.currentUser
        var chatUser = ChatListUserInfo(
            null,
            data.userId,
            null
        )
        collectionReference.whereEqualTo("userId",data.userId).get().addOnSuccessListener {
            if(!it.isEmpty){
                chatUser.userName = it.documents[0].get("userName").toString()
                chatUser.userImgUrl = it.documents[0].get("imageUrl").toString()
                val chatListReference = db.collection("ChatList${currentUser?.uid.toString()}")

                chatListReference.whereEqualTo("userId",data.userId).get().addOnSuccessListener {king->

                    if (king.isEmpty){
                        chatListReference.document(data.userId).set(chatUser)
                        val userInfo = viewModel.getUserInfo()
                        val reciver = ChatListUserInfo(
                            userInfo.userName,
                            userInfo.userId,
                            userInfo.imageUrl
                        )
                        val reciverReference = db.collection("ChatList${data.userId}")
                        reciverReference.document(currentUser?.uid.toString()).set(reciver)
                    }
                }
                val intent = Intent(this,ChatLayout::class.java)
                intent.putExtra("userId",chatUser.userId)
                intent.putExtra("userName",chatUser.userName.toString())
                startActivity(intent)
            }
        }




    }

    fun startTrigger(){
        if (viewModel.findUser(auth.currentUser?.uid.toString()) > 0){
            Log.i("User","User exist")
        }else{

            addUserInfoToDb()

        }
    }

    private fun addUserInfoToDb() {
        val user = auth.currentUser
        if(user!=null){
            collectionReference.whereEqualTo("userId",user.uid).get().addOnSuccessListener {
                if (!it.isEmpty){
                    val name = it.documents[0].get("userName").toString()
                    val email = it.documents[0].get("userEmail").toString()
                    val phNum = it.documents[0].get("contactNum").toString()
                    val address = it.documents[0].get("userAddress").toString()
                    val imageUrl = it.documents[0].get("imageUrl").toString()
                    val userId = it.documents[0].get("userId").toString()
                    userInfo = UserData(userId,name,email,phNum,address,imageUrl)

                    viewModel.addUserInfoToDb(userInfo)

                }
            }.addOnFailureListener {
                Toast.makeText(
                    this,
                    "Opps! something went wrong",
                    Toast.LENGTH_SHORT
                ).show()

            }

        }
    }





    private fun onItemSelecteHandling(it: MenuItem) {
        when(it.itemId){
            R.id.userProfile ->{
                val intent = Intent(this,UserProfile::class.java)
                startActivity(intent)
            }
            R.id.logout ->{
                auth.signOut()
                viewModel.deleteUser()
                val intent = Intent(this,LogIn::class.java)
                startActivity(intent)
            }
            R.id.addStore ->{
                startActivity(Intent(this,AddStore::class.java))
            }
            R.id.chatsPage -> {
                startActivity(Intent(this,ChatListOfUsers::class.java))
            }
        }

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // check conndition for drawer item with menu item
        return if (actionBarDrawerToggle.onOptionsItemSelected(item)){
            true
        }else{
            super.onOptionsItemSelected(item)
        }

    }

//    override fun onStart() {
//        super.onStart()
//        auth = Firebase.auth
//        val user = auth.currentUser
//
//    }


}