package by.pda.demoapp.ui.espresso

import androidx.test.espresso.Espresso
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import by.pda.demoapp.android.R
import by.pda.demoapp.android.view.fragments.AboutFragment
import by.pda.demoapp.core.annotations.ComponentUiTest
import by.pda.demoapp.ui.common.test.FragmentTest
import io.qameta.allure.kotlin.Allure.step
import io.qameta.allure.Feature
import io.qameta.allure.Story
import org.hamcrest.Matchers.containsString
import org.junit.jupiter.api.Test

@ComponentUiTest
@Feature("About screen")
class AboutFragmentTest : FragmentTest<AboutFragment>(AboutFragment::class.java) {

    @Story("AF-STORY-1: Display basic information about product")
    @Test
    fun applicationVersionIsDisplayed() {
        step("Check that the version text view is displayed") {
            Espresso.onView(withId(R.id.versionTV))
                .check(matches(isDisplayed()))
                .check(matches(withText(containsString("V.1.0.0-build 1"))))
        }
    }

    @Story("AF-STORY-1: Display basic information about product")
    @Test
    fun webLinkIsDisplayed() {
        step("Check that the website link is displayed with correct text") {
            Espresso.onView(withId(R.id.webTV))
                .check(matches(isDisplayed()))
                .check(matches(withText(R.string.go_to_website)))
        }
    }
}
