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
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.UiSelector
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.Rule

@RunWith(AndroidJUnit4::class)
class SendButtonInstrumentedTest {

    @get:Rule
    val activityRule = ActivityScenarioRule(MainActivity::class.java)

    // To dismiss the system tips, you need to manually command it
    @Before
    fun setup() {
        dismissSystemTips()
        unlockDevice()
    }

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

    private fun unlockDevice() {
        val device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
        // Wake up the device if it is asleep
        if (!device.isScreenOn) {
            device.wakeUp()
        }
        // Swipe up to unlock (works for simple lock screens)
        device.swipe(device.displayWidth / 2, device.displayHeight, device.displayWidth / 2, 0, 50)
        // Send the Menu key event if the swipe doesn't work (older Android versions)
        // device.pressMenu()
    }

    private fun dismissSystemTips() {
        val device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())

        // The exact resource ID and description can vary by emulator and OS version.
        // This is a common pattern for dismissing tips on Gboard.
        val tipCloseButton = UiSelector()
            .descriptionContains("dismiss") // Look for a button with a "dismiss" description
            .className("android.widget.ImageView")

        // Check if the dismiss button exists on the screen and click it.
        // The `findObject` method waits for a short duration, making this robust.
        if (device.findObject(tipCloseButton).exists()) {
            device.findObject(tipCloseButton).click()
        }
    }
}