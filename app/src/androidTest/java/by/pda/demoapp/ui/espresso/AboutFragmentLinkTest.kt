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
import by.pda.demoapp.core.annotations.IntegrationUiTest
import by.pda.demoapp.ui.common.matchers.ToastMatcher
import by.pda.demoapp.ui.common.test.ActivityTest
import io.qameta.allure.Feature
import io.qameta.allure.kotlin.Allure.step
import io.qameta.allure.Story
import org.hamcrest.Matchers
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

@IntegrationUiTest
@Feature("About screen")
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
        step("Scroll to and click on the website link") {
            Espresso.onView(withId(R.id.webTV))
                .perform(ViewActions.scrollTo(), click())
        }

        step("Verify that the browser intent was sent with the correct URL") {
            Intents.intended(
                Matchers.allOf(
                    IntentMatchers.hasAction("android.intent.action.VIEW"),
                    IntentMatchers.hasData(UriMatchers.hasHost("saucelabs.com"))
                )
            )
        }
    }

    @Story("AF-STORY-2: Display basic information about product")
    @Test
    fun browserIsNotFoundTest() {
        step("Stub the browser intent to simulate a 'Not Found' error") {
            val intentResult = Instrumentation.ActivityResult(Activity.RESULT_CANCELED, null)
            Intents.intending(IntentMatchers.hasAction("android.intent.action.VIEW")).respondWith(intentResult)
        }

        step("Scroll to and click on the website link") {
            Espresso.onView(withId(R.id.webTV))
                .perform(ViewActions.scrollTo(), click())
        }

        step("Verify that the 'No application' toast message is displayed") {
            Espresso.onView(ViewMatchers.withText(R.string.no_application_can_handle_this_request))
                .inRoot(ToastMatcher()).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        }
    }


}