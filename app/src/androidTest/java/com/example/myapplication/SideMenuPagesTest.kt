package com.example.myapplication

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
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
class SideMenuPagesTest {

    @get:Rule
    val activityRule = ActivityScenarioRule(MainActivity::class.java)

    // To dismiss the system tips, you need to manually command it
    @Before
    fun setup() {
        dismissSystemTips()
    }

    // Testing the Terms of Use page
    @Test
    fun open_terms_of_use_page_test() {

        onView(ViewMatchers.withContentDescription(R.string.navigation_drawer_open)) // Or use the hardcoded string "Open navigation drawer"
            .perform(click())

        onView(withId(R.id.nav_term))
            .perform(click())

        onView(withText(R.string.term_title))
            .check(matches(isDisplayed()))
    }

    // Testing the Privacy Policy page
    @Test
    fun open_privacy_policy_page_test() {

        onView(ViewMatchers.withContentDescription(R.string.navigation_drawer_open)) // Or use the hardcoded string "Open navigation drawer"
            .perform(click())

        onView(withId(R.id.nav_policy))
            .perform(click())

        onView(withText(R.string.policy_title))
            .check(matches(isDisplayed()))
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