package com.example.myapplication

import android.text.method.PasswordTransformationMethod
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.closeSoftKeyboard
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.replaceText
import androidx.test.espresso.action.ViewActions.scrollTo
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LoginScreenTest {
    @Test fun nextOpensLoginAndBackReturnsToWelcome() {
        ActivityScenario.launch(MainActivity::class.java).use {
            onView(withId(R.id.nextButton)).perform(scrollTo(), click())
            onView(withId(R.id.loginHeading)).check(matches(isDisplayed()))
            androidx.test.espresso.Espresso.pressBack()
            onView(withId(R.id.nextButton)).check(matches(isDisplayed()))
        }
    }

    @Test fun emptySubmitShowsBothRequiredErrors() {
        ActivityScenario.launch(LoginActivity::class.java).use { scenario ->
            onView(withId(R.id.loginButton)).perform(scrollTo(), click())
            scenario.onActivity { activity ->
                assertEquals(activity.getString(R.string.error_identifier_required),
                    activity.findViewById<TextInputLayout>(R.id.emailInputLayout).error)
                assertEquals(activity.getString(R.string.error_password_required),
                    activity.findViewById<TextInputLayout>(R.id.passwordInputLayout).error)
            }
        }
    }

    @Test fun passwordToggleChangesVisibilityAndPreservesText() {
        ActivityScenario.launch(LoginActivity::class.java).use { scenario ->
            onView(withId(R.id.passwordInput)).perform(scrollTo(), replaceText("Example password"))
            closeSoftKeyboard()
            scenario.onActivity { activity ->
                assertTrue(activity.findViewById<TextInputEditText>(R.id.passwordInput)
                    .transformationMethod is PasswordTransformationMethod)
                activity.findViewById<TextInputLayout>(R.id.passwordInputLayout)
                    .findViewById<android.view.View>(com.google.android.material.R.id.text_input_end_icon)
                    .performClick()
                val input = activity.findViewById<TextInputEditText>(R.id.passwordInput)
                assertFalse(input.transformationMethod is PasswordTransformationMethod)
                assertEquals("Example password", input.text.toString())
            }
        }
    }

    @Test fun validatedInputShowsHonestUnavailableNotice() {
        ActivityScenario.launch(LoginActivity::class.java).use {
            onView(withId(R.id.emailInput)).perform(scrollTo(), replaceText("student@example.com"))
            onView(withId(R.id.passwordInput)).perform(scrollTo(), replaceText("Example password"))
            closeSoftKeyboard()
            onView(withId(R.id.loginButton)).perform(scrollTo(), click())
            onView(withText(R.string.login_unavailable_title)).check(matches(isDisplayed()))
        }
    }

    @Test fun providerButtonsExplainAvailability() {
        ActivityScenario.launch(LoginActivity::class.java).use {
            onView(withId(R.id.googleButton)).perform(scrollTo(), click())
            onView(withText("Continue with Google")).check(matches(isDisplayed()))
            onView(withText(R.string.got_it)).perform(click())
            onView(withId(R.id.facebookButton)).perform(scrollTo(), click())
            onView(withText("Continue with Facebook")).check(matches(isDisplayed()))
        }
    }

    @Test fun signUpLinkOpensFormAndNextOpensCitySelection() {
        ActivityScenario.launch(MainActivity::class.java).use {
            onView(withId(R.id.nextButton)).perform(scrollTo(), click())
            onView(withId(R.id.signUpButton)).perform(scrollTo(), click())
            onView(withId(R.id.signUpHeading)).check(matches(isDisplayed()))
            onView(withId(R.id.signUpNextButton)).perform(scrollTo(), click())
            onView(withText(R.string.error_name_required)).perform(scrollTo()).check(matches(isDisplayed()))
            onView(withId(R.id.nameInput)).perform(scrollTo(), replaceText("Demo Student"))
            onView(withId(R.id.emailInput)).perform(scrollTo(), replaceText("student@example.com"))
            onView(withId(R.id.passwordInput)).perform(scrollTo(), replaceText("DemoPass2026"))
            closeSoftKeyboard()
            onView(withId(R.id.signUpNextButton)).perform(scrollTo(), click())
            onView(withId(R.id.locationHeading)).check(matches(isDisplayed()))
            onView(withId(R.id.citySelector)).check(matches(withText("Jaipur"))).perform(click())
            onView(withText("Delhi")).perform(click())
            onView(withId(R.id.citySelector)).check(matches(withText("Delhi")))
        }
    }
}
