package com.example.myapplication

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.databinding.ChatItemBinding

class MessageAdapter(private val messageList: List<Message>) :
    RecyclerView.Adapter<MessageAdapter.MyViewHolder>() {

    // 1. Update the ViewHolder to use the binding object
    // It takes ChatItemBinding as a parameter instead of a raw View
    class MyViewHolder(private val binding: ChatItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(message: Message) {
            if (message.sentBy == Message.SENT_BY_ME) {
                binding.leftChatView.visibility = View.GONE
                binding.rightChatView.visibility = View.VISIBLE
                binding.rightChatTextView.text = message.messageContent
            } else {
                binding.rightChatView.visibility = View.GONE
                binding.leftChatView.visibility = View.VISIBLE
                binding.leftChatTextView.text = message.messageContent
            }
        }
    }

    // 2. Inflate the layout using the binding class in onCreateViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        // Use the inflate method provided by the generated binding class
        val binding = ChatItemBinding.inflate(inflater, parent, false)

        return MyViewHolder(binding)
    }

    // 3. Update onBindViewHolder to use the bind function we added to the ViewHolder
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val message = messageList[position]
        holder.bind(message)
    }

    override fun getItemCount(): Int {
        return messageList.size
    }
}