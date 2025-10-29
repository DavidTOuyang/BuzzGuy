package com.example.myapplication

import com.google.firebase.Timestamp
import org.junit.Test
import org.junit.jupiter.api.Assertions.*

// This test is for the MessageAdapter class
class MessageAdapterTest {

    val testDifference = MessageAdapter.MessageDiffCallback()

    @Test
    fun `areItemsTheSame function return true test`() {
        val testCase = testDifference.areItemsTheSame(Message("test1"),
            Message("test1"))

        assertTrue(testCase)
    }

    @Test
    fun `areItemsTheSame function return false test`() {
        val testCase = testDifference.areItemsTheSame(Message("test1", timestamp=null),
            Message("test1", timestamp=Timestamp.now()))

        assertFalse(testCase)
    }

    @Test
    fun `areContentsTheSame function return true test`() {
        val messageExample = Message("context", timestamp=Timestamp.now())
        val testCase = testDifference.areItemsTheSame(messageExample, messageExample)

        assertTrue(testCase)
    }

    @Test
    fun `areContentsTheSame function return false test`() {
        val messageExample1 = Message("context", timestamp=Timestamp.now())
        val messageExample2 = Message("bogus", timestamp=null)
        val testCase = testDifference.areItemsTheSame(messageExample1, messageExample2)

        assertFalse(testCase)
    }
}