package com.example.myapplication

import com.google.firebase.Timestamp

// primary constructor
data class Message (
    var messageContent: String = "",
    var sentBy: String = "",
    var timestamp: Timestamp? = null,
) {
    companion object {
        const val SENT_BY_ME = "me"
        const val SENT_BY_BOT = "bot"
    }
}
