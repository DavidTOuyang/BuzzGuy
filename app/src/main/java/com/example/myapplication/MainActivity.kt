package com.example.myapplication

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.databinding.ActivityMainBinding

// Firebase packages
import com.google.firebase.Firebase
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.analytics
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth

import java.time.Instant

// Tag for logging
private const val TAG = "AnonymousAuth"

class MainActivity (): AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val messageList: MutableList<Message> = mutableListOf()
    private lateinit var myMessageAdapter: MessageAdapter
    private lateinit var analytics: FirebaseAnalytics
    private lateinit var auth: FirebaseAuth     // Authentication

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        analytics = Firebase.analytics
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize Firebase Auth
        auth = Firebase.auth

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

                // to Firestore API
            }
        }
    }


    override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        updateUI(currentUser)
    }

    private fun signInAnonymously() {
        auth.signInAnonymously()
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInAnonymously:success")
                    val user = auth.currentUser
                    updateUI(user)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInAnonymously:failure", task.exception)
                    Toast.makeText(baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT).show()
                    updateUI(null)
                }
            }
    }

    private fun updateUI(user: FirebaseUser?) {
        if (user != null) {
            // User is signed in. You can now access their UID: user.uid
            Log.d(TAG, "User UID: ${user.uid}")
            // Proceed with chatbot logic, fetch chat history from Firestore using this UID
            // Example: fetchChatHistory(user.uid)
        } else {
            // No user is signed in, sign them in anonymously
            signInAnonymously()
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