package com.example.myapplication

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.databinding.ActivityMainBinding
import android.widget.Toast


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // variables from xml
        val myRecyclerView = binding.recyclerView
        val myWelcomeText = binding.welcomeText
        val myEditText = binding.messageEditText
        val mySendButton = binding.sendButton

        mySendButton.setOnClickListener {
            val question = myEditText.text.toString().trim()

            if (question.isNotEmpty()) {
                Toast.makeText(this, question, Toast.LENGTH_LONG).show()
            }
        }
    }
}