package by.pda.demoapp.ui.espresso

import androidx.test.espresso.Espresso
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import by.pda.demoapp.android.R
import by.pda.demoapp.android.view.fragments.AboutFragment
import by.pda.demoapp.ui.common.test.FragmentTest
import io.qameta.allure.Epic
import io.qameta.allure.Feature
import io.qameta.allure.Story
import org.hamcrest.Matchers.containsString
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

@Epic("UI-Testing (Espresso)")
@Feature("About screen")
@Tag("espresso")
@Tag("componentUiTests")
class AboutFragmentTest : FragmentTest<AboutFragment>(AboutFragment::class.java) {

    @Story("AF-STORY-1: Display basic information about product")
    @Test
    fun applicationVersionIsDisplayed() {
        Espresso.onView(withId(R.id.versionTV))
            .check(matches(isDisplayed()))
            .check(matches(withText(containsString("V.1.0.0-build 1"))))
    }

    @Story("AF-STORY-1: Display basic information about product")
    @Test
    fun webLinkIsDisplayed() {
        Espresso.onView(withId(R.id.webTV))
            .check(matches(isDisplayed()))
            .check(matches(withText(R.string.go_to_website)))
    }
}
