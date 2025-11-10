package by.pda.demoapp.ui.espresso

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.scrollTo
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers.hasAction
import androidx.test.espresso.intent.matcher.IntentMatchers.hasData
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import by.pda.demoapp.android.R
import by.pda.demoapp.android.view.activities.MainActivity
import by.pda.demoapp.android.view.fragments.AboutFragment
import by.pda.demoapp.ui.common.test.ActivityTest
import by.pda.demoapp.ui.common.test.FragmentTest
import io.qameta.allure.Epic
import io.qameta.allure.Feature
import io.qameta.allure.Story
import org.hamcrest.Matchers
import org.hamcrest.Matchers.allOf
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

@Epic("UI-Testing (Espresso)")
@Feature("About screen")
@Tag("espresso")
@Tag("component-ui")
class AboutFragmentLinkTest : ActivityTest<MainActivity>(MainActivity::class.java) {

    @BeforeEach
    fun setupIntents() {
        Intents.init()
        openAboutFragment()
    }

    @AfterEach
    fun tearDownIntents() {
        Intents.release()
    }

    @Story("AF-STORY-1: Display basic information about product")
    @Test
    fun startWebBrowserIntentOnLinkClick() {
        onView(withId(R.id.webTV)).perform(scrollTo(), click())
        intended(allOf(
            hasAction("android.intent.action.VIEW"),
            hasData("https://www.saucelabs.com")
        ))
    }
}