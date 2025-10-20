package com.example.myapplication.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.launch

import com.example.myapplication.GenerativeAIHelper
import com.example.myapplication.Message
import com.google.firebase.Timestamp

class ChatViewModel : ViewModel() {

    private val generativeModel =  GenerativeAIHelper.generativeModel

    // Use MutableLiveData internally to post updates
    private val generatedResponse = MutableLiveData<Message>()

    // Expose LiveData publicly for the UI to observe.
    // Using live data can remove the ordinary return types.
    val chatResponse: LiveData<Message> = generatedResponse

    fun generateContent(prompt: String) {
        // Launch a coroutine in the viewModelScope
        viewModelScope.launch {
            try {
                // Call the API and handle the response
                val response = generativeModel.generateContent(prompt)
                val responseText = response.text ?: "Sorry, can you try again?"
                val explanation = Message(
                    responseText,
                    Message.SENT_BY_BOT,
                    Timestamp.now())
                generatedResponse.postValue(explanation)
            } catch (e: Exception) {
                // TODO: Handle the error
                generatedResponse.postValue(Message(
                    "Error: ${e.message}",
                    Message.SENT_BY_BOT, // Or handle differently
                    Timestamp.now()
                ))
            }
        }
    }
}