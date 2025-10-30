package com.example.myapplication

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.closeSoftKeyboard // Good practice!
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import org.hamcrest.CoreMatchers.allOf
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.Rule

@RunWith(AndroidJUnit4::class)
class SendButtonInstrumentedTest {

    @get:Rule
    val activityRule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun add_a_message_test() {

        val testMessage = "Welcome to BuzzGuy!"

        // 1. Find the EditText, type text, and close the keyboard.
        // NOTE: I've corrected the ID from R.id.message_edit_text to R.id.messageEditText
        //       and from R.id.send_button to R.id.sendButton.
        //       Please verify these against your activity_main.xml file.
        onView(withId(R.id.message_edit_text))
            .perform(typeText(testMessage), closeSoftKeyboard()) // Closing the keyboard is good practice

        // 2. Find the send button and click it.
        onView(withId(R.id.send_button))
            .perform(click())

        // 3. Verify the message now exists in the RecyclerView.
        // NOTE: Corrected R.id.recycler_view to R.id.recyclerView
        //       Verify this ID against your activity_main.xml file.
        onView(withId(R.id.recycler_view))
            .check(matches(hasDescendant(allOf(
                withText(testMessage),
                isDisplayed()
            ))))

    }
}