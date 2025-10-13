package com.example.myapplication

import com.google.android.material.color.utilities.SchemeContent

class Message {
    companion object {
        const val SENT_BY_ME = "me"
        const val SENT_BY_BOT = "bot"
    }

    var messageContent: String = ""
    var sentBy: String  = ""

    // Time stamp
    var timestamp: Long = 0L

    // Get message
    fun getMessageContent(): String {
        return messageContent
    }

    // Set message
    fun getMessageContent(newMessage: String) {
        messageContent = newMessage
    }

    // Get sent by
    fun getSentBy(): String {
        return sentBy
    }

    // Set sent by
    fun setSentBy(newSentBy: String) {
        sentBy = newSentBy
    }

    // Get current time
    fun getTime(): Long {
        return timestamp
    }

    // Set current time
    fun setTime(newTime: Long) {
        timestamp = newTime
    }
}