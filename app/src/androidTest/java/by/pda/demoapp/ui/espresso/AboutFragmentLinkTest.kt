package by.pda.demoapp.ui.espresso

import android.app.Activity.RESULT_CANCELED
import android.app.Instrumentation
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.scrollTo
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.Intents.intending
import androidx.test.espresso.intent.matcher.IntentMatchers.hasAction
import androidx.test.espresso.intent.matcher.IntentMatchers.hasData
import androidx.test.espresso.intent.matcher.UriMatchers
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import by.pda.demoapp.android.R
import by.pda.demoapp.android.view.activities.SplashActivity
import by.pda.demoapp.ui.common.matchers.ToastMatcher
import by.pda.demoapp.ui.common.test.ActivityTest
import io.qameta.allure.Epic
import io.qameta.allure.Feature
import io.qameta.allure.Story
import org.hamcrest.Matchers.allOf
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

@Epic("UI-Testing (Espresso)")
@Feature("About screen")
@Tag("espresso")
@Tag("integration-ui")
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
    fun startWebBrowserIntentOnLinkClickTest() {
        onView(withId(R.id.webTV)).perform(scrollTo(), click())
        intended(allOf(
            hasAction("android.intent.action.VIEW"),
            hasData(UriMatchers.hasHost("saucelabs.com"))
        ))
    }

    @Story("AF-STORY-2: Display basic information about product")
    @Test
    fun browserIsNotFoundTest() {
        val intentResult = Instrumentation.ActivityResult(RESULT_CANCELED, null)
        intending(hasAction("android.intent.action.VIEW")).respondWith(intentResult)

        onView(withId(R.id.webTV)).perform(scrollTo(), click())
        onView(withText(R.string.no_application_can_handle_this_request))
            .inRoot(ToastMatcher()).check(matches(isDisplayed()))
    }


}