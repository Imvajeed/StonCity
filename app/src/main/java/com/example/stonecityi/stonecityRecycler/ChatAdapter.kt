package com.example.stonecityi.stonecityRecycler

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.stonecityi.Message
import com.example.stonecityi.databinding.MessageRecivedCardViewBinding
import com.example.stonecityi.databinding.MessageSentCardViewBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.auth

class ChatAdapter(val context: Context, val data : ArrayList<Message>):RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val SENT_MSG = 1
    val RECIVED_MSG = 2
    class RecivedViewHolder(val binding: MessageRecivedCardViewBinding):RecyclerView.ViewHolder(binding.root){
        fun assignMsg(msg: Message){
            binding.recivedMsg.text = msg.msg
        }
    }
    class SentViewHolder(val binding: MessageSentCardViewBinding):RecyclerView.ViewHolder(binding.root){
        fun assignMsg(msg: Message){
            binding.sentMsg.text = msg.msg
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == SENT_MSG){
            val binding = MessageSentCardViewBinding.inflate(LayoutInflater.from(parent.context),parent,false)
            return SentViewHolder(binding)
        }else{
            val binding = MessageRecivedCardViewBinding.inflate(LayoutInflater.from(parent.context),parent,false)
            return RecivedViewHolder(binding)
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder.javaClass == SentViewHolder::class.java){
            val viewHolder = holder as SentViewHolder
            viewHolder.assignMsg(data[position])
        }else{
            val viewHolder = holder as RecivedViewHolder
            viewHolder.assignMsg(data[position])
        }
    }

    override fun getItemViewType(position: Int): Int {
        val currentMessage = data[position]
        if (currentMessage.userId == Firebase.auth.currentUser?.uid){
            return SENT_MSG
        }else{
            return RECIVED_MSG
        }
    }
}