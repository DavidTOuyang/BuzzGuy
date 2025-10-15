package com.example.myapplication

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import android.view.View
import androidx.annotation.RequiresApi
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.databinding.ActivityMainBinding
import com.google.firebase.Firebase
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.analytics
import java.time.Instant

class MainActivity (): AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val messageList: MutableList<Message> = mutableListOf()
    private lateinit var myMessageAdapter: MessageAdapter
    private lateinit var analytics: FirebaseAnalytics

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        analytics = Firebase.analytics
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // setup recycler view
        myMessageAdapter = MessageAdapter(messageList)
        binding.recyclerView.adapter = myMessageAdapter
        val llm = LinearLayoutManager(this)
        llm.stackFromEnd = true
        binding.recyclerView.layoutManager = llm

        binding.sendButton.setOnClickListener {
            val question = binding.messageEditText.text.toString().trim()
            if (question.isNotEmpty()) {
                val nowInstant = Instant.now()
                addToChat(question, Message.SENT_BY_ME, nowInstant.toEpochMilli())
                binding.messageEditText.setText("")
                binding.welcomeText.visibility = View.GONE
            }
        }
    }

    // method that adds a new chat
    fun addToChat(message: String, sentBy: String, timeStamp: Long) {
        runOnUiThread {
            messageList.add(Message(message, sentBy, timeStamp))
            val newItemPosition = myMessageAdapter.itemCount
            myMessageAdapter.notifyItemInserted(newItemPosition)
            binding.recyclerView.smoothScrollToPosition(newItemPosition)
        }
    }
}