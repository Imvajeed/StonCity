package com.example.stonecityi

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import androidx.recyclerview.widget.RecyclerView.RecyclerListener
import com.example.stonecityi.databinding.ActivityMyStoreBinding
import com.example.stonecityi.stonecityRecycler.MyStoreRecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase

class MyStore : AppCompatActivity() {
    //binding
    lateinit var binding : ActivityMyStoreBinding
    //RecyclerView
    lateinit var recyclerView: MyStoreRecyclerView
    //mutableList
    lateinit var storeList: MutableList<StoreData>

    //firebase
    lateinit var auth: FirebaseAuth
    //firebase databse
    val db = FirebaseFirestore.getInstance()
    val collectionReference = db.collection("Stores")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_my_store)
        storeList = arrayListOf()
        auth = Firebase.auth
        binding.mystoreRecyclerView.layoutManager = LinearLayoutManager(this)
        getData()
        Log.i("ViratKholi","${storeList}")


        binding.backToProfile.setOnClickListener {
            startActivity(Intent(this,UserProfile::class.java))
            finish()
        }




//        Log.i("FukingHard","$storeList")

//        else{
//            Toast.makeText(
//                this,
//                "Opps! something went wrong",
//                Toast.LENGTH_LONG
//            ).show()
//        }


    }

    fun getData(){
        collectionReference.whereEqualTo("userId",auth.currentUser?.uid).get().addOnSuccessListener {

            if (!it.isEmpty) {
                for (document in it) {
                    val data = StoreData(
                        document.data["userId"].toString(),
                        document.data["storeImgUrl"].toString(),
                        document.data["storeName"].toString(),
                        document.data["storeContact"].toString(),
                        document.data["storeLocation"].toString(),
                        document.data["storeId"].toString()

                    )
                    storeList.add(data)

                }

                if (storeList.isNotEmpty()){
                    recyclerView = MyStoreRecyclerView(storeList,this){
                        onStoreClicked(it)
                    }
                    binding.mystoreRecyclerView.adapter = recyclerView
                }else{
                    showEmptyUi()
                }

            }
        }.addOnFailureListener {
            showEmptyUi()
            Toast.makeText(
                this@MyStore,
                "No data Found",
                Toast.LENGTH_LONG
            ).show()
        }

    }

    private fun onStoreClicked(it: StoreData) {
        val bundle = Bundle()
        bundle.putString("storeId",it.storeId)
        bundle.putString("userId",it.userId)
        bundle.putString("location",it.storeLocation)
        bundle.putString("storeImg",it.storeImgUrl)
        bundle.putString("storeName",it.storeName)
        bundle.putString("storeContact",it.storeContact)
        val intent = Intent(this,EditStore::class.java)
        intent.putExtras(bundle)
        startActivity(intent)
    }

    private fun showEmptyUi() {
        binding.mystoreRecyclerView.visibility = View.GONE
        binding.noStores.visibility = View.VISIBLE
    }

}