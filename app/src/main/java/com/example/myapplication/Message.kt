package com.example.myapplication

import com.google.android.material.color.utilities.SchemeContent

// primary constructor
data class Message (
    var messageContent: String = "",
    var sentBy: String = "",
    var timestamp: Long = 0L
) {
    companion object {
        const val SENT_BY_ME = "me"
        const val SENT_BY_BOT = "bot"
    }
}

    // Code below may not needed since Kotlin doesn't require
    // manual implementations.
//    // Get message
//    fun getMessageContent(): String {
//        return messageContent
//    }
//
//    // Set message
//    fun getMessageContent(newMessage: String) {
//        messageContent = newMessage
//    }
//
//    // Get sent by
//    fun getSentBy(): String {
//        return sentBy
//    }
//
//    // Set sent by
//    fun setSentBy(newSentBy: String) {
//        sentBy = newSentBy
//    }
//
//    // Get current time
//    fun getTime(): Long {
//        return timestamp
//    }
//
//    // Set current time
//    fun setTime(newTime: Long) {
//        timestamp = newTime
//    }
