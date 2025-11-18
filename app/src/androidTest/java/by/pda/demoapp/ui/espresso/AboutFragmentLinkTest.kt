package by.pda.demoapp.ui.espresso

import android.app.Activity
import android.app.Instrumentation
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.intent.matcher.UriMatchers
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import by.pda.demoapp.android.R
import by.pda.demoapp.android.view.activities.SplashActivity
import by.pda.demoapp.ui.common.matchers.ToastMatcher
import by.pda.demoapp.ui.common.test.ActivityTest
import io.qameta.allure.Epic
import io.qameta.allure.Feature
import io.qameta.allure.Story
import org.hamcrest.Matchers
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

@Epic("UI-Testing (Espresso)")
@Feature("About screen")
@Tag("espresso")
@Tag("integrationUiTests")
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
        Espresso.onView(withId(R.id.webTV))
            .perform(ViewActions.scrollTo(), click())
        Intents.intended(
            Matchers.allOf(
                IntentMatchers.hasAction("android.intent.action.VIEW"),
                IntentMatchers.hasData(UriMatchers.hasHost("saucelabs.com"))
            )
        )
    }

    @Story("AF-STORY-2: Display basic information about product")
    @Test
    fun browserIsNotFoundTest() {
        val intentResult = Instrumentation.ActivityResult(Activity.RESULT_CANCELED, null)
        Intents.intending(IntentMatchers.hasAction("android.intent.action.VIEW")).respondWith(intentResult)

        Espresso.onView(withId(R.id.webTV))
            .perform(ViewActions.scrollTo(), click())
        Espresso.onView(ViewMatchers.withText(R.string.no_application_can_handle_this_request))
            .inRoot(ToastMatcher()).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }


}