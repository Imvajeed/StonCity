package com.example.stonecityi.stonecityRecycler

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Telephony.Mms.Intents
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.stonecityi.EditStore
import com.example.stonecityi.LogIn
import com.example.stonecityi.MyStore
import com.example.stonecityi.R
import com.example.stonecityi.StoreData
import com.example.stonecityi.UserProfile
import com.example.stonecityi.databinding.MyStoreCardDesignBinding
import com.google.firebase.firestore.FirebaseFirestore

class MyStoreRecyclerView(val data : MutableList<StoreData>,val context: Context,private val onItemClick: (StoreData) -> Unit):RecyclerView.Adapter<MyStoreRecyclerView.MyViewHolder>() {

    //firebase db
    val db = FirebaseFirestore.getInstance()
    val collectionReference = db.collection("Stores")
    inner class MyViewHolder(val binding : MyStoreCardDesignBinding):RecyclerView.ViewHolder(binding.root){


        fun assignValue(data: StoreData){
            binding.data = data

            binding.deleteStorePost.setOnClickListener{
                Toast.makeText(
                    context,
                    "You cliked on Delte Post",
                    Toast.LENGTH_SHORT
                ).show()
                deletePost(data)
                
            }
            binding.editStorePost.setOnClickListener {
                onItemClick(data)
                Toast.makeText(
                    context,
                    "You cliked on Edit Post",
                    Toast.LENGTH_SHORT
                ).show()
            }

        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = MyStoreCardDesignBinding.inflate(LayoutInflater.from(parent.context),parent,false)

        return MyViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.assignValue(data[position])
        Glide.with(holder.binding.root)
            .load(data[position].storeImgUrl)
            .apply(
                RequestOptions()
                    .placeholder(R.drawable.profile) // Placeholder image
                    .error(R.drawable.logout) // Error image in case of loading failure
            ).into(holder.binding.storeImage)

    }
    fun deletePost(data: StoreData) {
        val builder = AlertDialog.Builder(context)
        builder.setMessage("Do you want Delete this post?")
            .setTitle("Delete Post?")
            .setPositiveButton("Yes"){_,_->
                collectionReference.whereEqualTo("storeId",data.storeId).get().addOnSuccessListener {
                    if (!it.isEmpty){
                        for (document in it){
                            collectionReference.document(document.data["storeId"].toString()).delete()

                        }
                    }
                }
                
            }
            .setNegativeButton("No"){_,_->
                Toast.makeText(
                    context,
                    "You cliked on Delte Post",
                    Toast.LENGTH_SHORT
                ).show()

            }
        val dailog = builder.create()
        dailog.show()
    }

}