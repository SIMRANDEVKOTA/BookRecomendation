package com.example.bookrecommendation

import androidx.compose.ui.test.hasClickAction
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.bookrecommendation.view.LoginActivity
import com.example.bookrecommendation.view.RegisterActivity
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class RegisterInstrumentedTest {
    @get:Rule
    val composeRule = createAndroidComposeRule<RegisterActivity>()

    @Before
    fun setup() {
        Intents.init()
    }

    @After
    fun tearDown() {
        Intents.release()
    }

    @Test
    fun testRegisterFlow() {
        // Find email field by its placeholder and enter text
        composeRule.onNodeWithText("abc@gmail.com")
            .performTextInput("newuser@gmail.com")

        // Find password field by its placeholder and enter text
        composeRule.onNodeWithText("********")
            .performTextInput("password123")

        // Interaction with Checkbox (Terms & Conditions)
        composeRule.onNodeWithText("I agree to terms & conditions")
            .performClick()

        // FIX: Specifically find the "Register" node that has a click action (the Button)
        // This avoids confusion with the "Register" Header text at the top.
        composeRule.onNode(hasText("Register") and hasClickAction())
            .performClick()
    }

    @Test
    fun testNavigateToLogin() {
        // Click on "Already a member? Sign In" text
        composeRule.onNodeWithText("Already a member? Sign In", substring = true)
            .performClick()

        // Verify navigation back to LoginActivity
        Intents.intended(hasComponent(LoginActivity::class.java.name))
    }
}
