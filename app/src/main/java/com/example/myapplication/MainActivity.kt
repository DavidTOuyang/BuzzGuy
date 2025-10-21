package com.example.myapplication

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.databinding.ActivityMainBinding
import com.google.android.material.navigation.NavigationView
import androidx.activity.viewModels
import com.example.myapplication.viewmodel.ChatViewModel

// Firebase packages
import com.google.firebase.Firebase
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.analytics
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.Timestamp

// Tag for logging
private const val TAG = "AnonymousAuth"

// For safety concern
private val auth: FirebaseAuth by lazy { Firebase.auth }
private val fireDb: FirebaseFirestore by lazy { Firebase.firestore }

class MainActivity (): AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var binding: ActivityMainBinding
    private lateinit var myMessageAdapter: MessageAdapter
    private lateinit var analytics: FirebaseAnalytics
    private var currentUserId: String? = null
    private var firestoreListener: ListenerRegistration? = null
    private lateinit var drawerLayout: DrawerLayout
    private val myAIModel: ChatViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        analytics = Firebase.analytics
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set up the toolbar
        val toolbar: Toolbar = binding.toolbar
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        // setup drawer layout and navigation view
        drawerLayout = binding.drawerLayout
        val navigationView: NavigationView = binding.navView

        // Create and set up the ActionBarDrawerToggle
        val toggle = ActionBarDrawerToggle(
            this,
            drawerLayout,
            toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        navigationView.setNavigationItemSelectedListener(this)

        // Handle back presses for the navigation drawer
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    drawerLayout.closeDrawer(GravityCompat.START)
                } else {
                    // Let the system handle the back press as it normally would
                    isEnabled = false // Disable this callback
                    onBackPressedDispatcher.onBackPressed() // Trigger the default back behavior
                }
            }
        })

        // setup recycler view
        myMessageAdapter = MessageAdapter()
        binding.recyclerView.adapter = myMessageAdapter
        val llm = LinearLayoutManager(this)
        binding.recyclerView.layoutManager = llm

        // Clicking the send button.
        binding.sendButton.setOnClickListener {
            val question = binding.messageEditText.text.toString().trim()
            val userId = auth.currentUser?.uid ?: ""

            if (question.isNotEmpty() && userId.isNotEmpty()) {
                myAIModel.generateContent(question)
                addMessageToFirestore(userId, question, Message.SENT_BY_ME)
                binding.messageEditText.setText("")
            }
        }

        // Observe the LiveData for changes
        myAIModel.chatResponse.observe(this) { aiResponse ->
            aiResponse?.let { message ->
                addMessageToFirestore(
                    auth.currentUser?.uid ?: "",
                    message.messageContent,
                    Message.SENT_BY_BOT
                )
            }
        }
    }

    // This function will clear the Firestore dialogues.
    fun clearChats() {
        val userId = auth.currentUser?.uid ?: ""
        if (userId.isEmpty()) {
            Log.e("ClearChats", "Error: User not authenticated.")
            return
        }
        val chatRef = fireDb.collection("users")
            .document(userId).collection("chats")

        // Retrieve all documents in the chats subcollection
        chatRef.get().addOnSuccessListener { querySnapshot ->
            if (querySnapshot.isEmpty) {
                Log.d("ClearChats", "No chat dialogues to clear for user $userId.")
                return@addOnSuccessListener
            }

            // Create a new batched write operation
            val batch = fireDb.batch()
            for (document in querySnapshot.documents) {
                // Add each document's delete operation to the batch
                batch.delete(document.reference)
            }

            // Commit the batch
            batch.commit()
                .addOnSuccessListener {
                    Log.d("ClearChats", "Successfully cleared all chat dialogues for user $userId.")
                }
                .addOnFailureListener { e ->
                    Log.e("ClearChats", "Error clearing chat dialogues", e)
                }
        }
        .addOnFailureListener { e ->
            Log.e("ClearChats", "Error getting documents for user $userId", e)
        }
    }

    override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        updateUI(currentUser)
    }

    // Detach Firestore listener to prevent memory leaks
    override fun onStop() {
        super.onStop()
        // preventing memory leak
        firestoreListener?.remove()
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
            myMessageAdapter.submitList(emptyList<Message>())
            // User is signed in. You can now access their UID: user.uid
            Log.d(TAG, "User UID: ${user.uid}")
            // Proceed with chatbot logic, fetch chat history from Firestore using this UID
            currentUserId = user.uid
            fetchLatestMessages(currentUserId)
        } else {
            // No user is signed in, sign them in anonymously
            signInAnonymously()
        }
    }

    private fun fetchLatestMessages(userId: String?) {
        if (userId == null) {
            Log.e(TAG, "Cannot fetch messages: user ID is null.")
            return
        }

        val chatRef = fireDb.collection("users")
            .document(userId).collection("chats")

        chatRef.orderBy("timestamp", Query.Direction.DESCENDING)
            .limit(20).addSnapshotListener { snapshots, e ->
                if (e != null) {
                    Log.e(TAG, "Listen failed.", e)
                    return@addSnapshotListener
                }

                if (snapshots != null && !snapshots.isEmpty) {
                    // Clear the list and add new messages to avoid duplicates.
                    binding.welcomeText.visibility = View.GONE

                    // Map documents to Message objects
                    val newMessages = snapshots.documents.mapNotNull {
                        it.toObject(Message::class.java)
                    }

                    // Reverse the list so the oldest of the 20 messages is first
                    val reversedMessages = newMessages.reversed()

                    // Submit the new list to the adapter for DiffUtil to handle.
                    myMessageAdapter.submitList(reversedMessages) {
                        binding.recyclerView.smoothScrollToPosition(reversedMessages.size - 1)
                    }
                } else {
                    Log.d(TAG, "Current chat is empty.")
                }
            }
    }

    // method that adds a new chat
    fun addMessageToFirestore(userId: String, message: String, sentBy: String) {
        val chatRef = fireDb.collection("users")
            .document(userId).collection("chats")

        val messageData = Message(
            message,
            sentBy,
            Timestamp.now()
        )

        // send the message directly to Firestore
        chatRef.add(messageData)
            .addOnSuccessListener { documentReference ->
                Log.d(TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error adding document", e)
                Toast.makeText(this, "Failed to send message.", Toast.LENGTH_SHORT).show()
            }
    }

    // Handle clicks on the navigation drawer menu items
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_new_chat -> {
                clearChats()
                myMessageAdapter.submitList(emptyList<Message>())
                binding.messageEditText.setText("")
                binding.welcomeText.visibility = View.VISIBLE
            }
            R.id.nav_term -> {
                // Handle the home action, for example, navigating to the main screen
                Toast.makeText(this, "Term clicked", Toast.LENGTH_SHORT).show()
            }

            // Add more cases for other menu items as needed
        }
        // Close the navigation drawer
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }
}