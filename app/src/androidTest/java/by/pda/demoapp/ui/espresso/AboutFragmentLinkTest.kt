package by.pda.demoapp.ui.espresso

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.scrollTo
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.*
import androidx.test.espresso.intent.matcher.IntentMatchers.hasAction
import androidx.test.espresso.intent.matcher.IntentMatchers.hasData
import androidx.test.espresso.intent.matcher.UriMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import by.pda.demoapp.android.R
import by.pda.demoapp.android.view.activities.MainActivity
import by.pda.demoapp.android.view.activities.SplashActivity
import by.pda.demoapp.core.extensions.InstantExecutionExtension
import by.pda.demoapp.ui.common.test.ActivityTest
import io.qameta.allure.Epic
import io.qameta.allure.Feature
import io.qameta.allure.Story
import org.hamcrest.Matchers.allOf
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@Epic("UI-Testing (Espresso)")
@Feature("About screen")
@Tag("espresso")
@Tag("integration-ui")
@ExtendWith(InstantExecutionExtension::class)
class AboutFragmentLinkTest : ActivityTest<SplashActivity>(SplashActivity::class.java) {

    @BeforeEach
    fun setupIntents() {
        Intents.init()
        openAboutFragment()
    }

    @AfterEach
    fun tearDownIntents() {
        Intents.release()
    }

    @Story("AF-STORY-2: Display basic information about product")
    @Test
    fun startWebBrowserIntentOnLinkClick() {
        onView(withId(R.id.webTV)).perform(scrollTo(), click())
        intended(allOf(
            hasAction("android.intent.action.VIEW"),
            hasData(UriMatchers.hasHost("saucelabs.com"))
        ))
    }
}