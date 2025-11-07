package com.mygroup.buzzguy

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mygroup.buzzguy.databinding.ChatReceivingViewBinding
import com.mygroup.buzzguy.databinding.ChatSendingViewBinding

class MessageAdapter : ListAdapter<Message,
        RecyclerView.ViewHolder>(MessageDiffCallback()) {

    companion object {
        private const val VIEW_TYPE_SENT = 1
        private const val VIEW_TYPE_RECEIVED = 2
    }

    // This method determines which layout (view type) to use for each item
    override fun getItemViewType(position: Int): Int {
        val message = getItem(position)
        return if (message.sentBy == Message.SENT_BY_ME) {
            VIEW_TYPE_SENT
        } else {
            VIEW_TYPE_RECEIVED
        }
    }

    // This method inflates the correct layout and creates the corresponding ViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            VIEW_TYPE_SENT -> {
                val binding = ChatSendingViewBinding.inflate(inflater, parent, false)
                SentMessageViewHolder(binding)
            }
            VIEW_TYPE_RECEIVED -> {
                val binding = ChatReceivingViewBinding.inflate(inflater, parent, false)
                ReceivedMessageViewHolder(binding)
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    // This method binds the data to the correct ViewHolder based on its type
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val message = getItem(position)
        when (holder) {
            is SentMessageViewHolder -> holder.bind(message)
            is ReceivedMessageViewHolder -> holder.bind(message)
        }
    }

    // Separate ViewHolder for messages sent by the user
    class SentMessageViewHolder(private val binding: ChatSendingViewBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(message: Message) {
            // Bind the message content to the TextView in the sent message layout
            binding.rightChatTextView.text = message.messageContent
        }
    }

    // Separate ViewHolder for messages received from the bot
    class ReceivedMessageViewHolder(private val binding: ChatReceivingViewBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(message: Message) {
            // Bind the message content to the TextView in the received message layout
            binding.leftChatTextView.text = message.messageContent
        }
    }

    class MessageDiffCallback : DiffUtil.ItemCallback<Message>() {
        override fun areItemsTheSame(oldItem: Message, newItem: Message): Boolean {
            // Assuming 'timestamp' is a reliable, unique identifier for a message.
            // For production, a unique message ID (e.g., from Firestore) is better.
            return oldItem.timestamp == newItem.timestamp
        }

        override fun areContentsTheSame(oldItem: Message, newItem: Message): Boolean {
            // Compare the content fields to detect if the item's data has changed.
            return oldItem == newItem
        }
    }
}