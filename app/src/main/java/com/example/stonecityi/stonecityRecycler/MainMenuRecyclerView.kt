package com.example.stonecityi.stonecityRecycler

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.stonecityi.ChatLayout
import com.example.stonecityi.R
import com.example.stonecityi.StoreData
import com.example.stonecityi.databinding.MainMenuCardsDesignBinding
import com.example.stonecityi.databinding.MyStoreCardDesignBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore

class MainMenuRecyclerView(val data: List<StoreData>,val context: Context,private val onItemClick: (StoreData) -> Unit) : RecyclerView.Adapter<MainMenuRecyclerView.MyViewHolder>() {

    inner class MyViewHolder(val binding : MainMenuCardsDesignBinding):RecyclerView.ViewHolder(binding.root){

        fun assignValue(data: StoreData){
            binding.data = data
            if(data.userId == Firebase.auth.currentUser?.uid.toString()){
                binding.messageButton.visibility = View.GONE
            }
            binding.messageButton.setOnClickListener{
                onItemClick(data)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = MainMenuCardsDesignBinding.inflate(LayoutInflater.from(parent.context),parent,false)
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
}