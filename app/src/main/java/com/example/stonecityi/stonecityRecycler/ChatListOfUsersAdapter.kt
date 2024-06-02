package com.example.stonecityi.stonecityRecycler

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.stonecityi.ChatLayout
import com.example.stonecityi.ChatListOfUsers
import com.example.stonecityi.ChatListUserInfo
import com.example.stonecityi.R
import com.example.stonecityi.databinding.ChatUsersListCardDesignBinding

class ChatListOfUsersAdapter(val context: Context , val data : ArrayList<ChatListUserInfo>):RecyclerView.Adapter<ChatListOfUsersAdapter.MyViewHolder>() {

    class MyViewHolder(val binding: ChatUsersListCardDesignBinding):RecyclerView.ViewHolder(binding.root){
            fun assignValues(data : ChatListUserInfo){
                binding.userName.text = data.userName
                Glide.with(binding.root)
                    .load(data.userImgUrl)
                    .apply(
                        RequestOptions()
                            .placeholder(R.drawable.profile) // Placeholder image
                            .error(R.drawable.logout) // Error image in case of loading failure
                    ).into(binding.userImage)

            }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ChatUsersListCardDesignBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return MyViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.assignValues(data[position])
        holder.binding.chatWith.setOnClickListener{
            val intent = Intent(context,ChatLayout::class.java)
            intent.putExtra("userId",data[position].userId)
            intent.putExtra("userName",data[position].userName)
            context.startActivity(intent)
        }
    }
}