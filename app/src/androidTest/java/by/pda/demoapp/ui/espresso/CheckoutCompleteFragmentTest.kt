package by.pda.demoapp.ui.espresso

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withContentDescription
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import by.pda.demoapp.android.R
import by.pda.demoapp.android.view.fragments.CheckoutCompleteFragment
import by.pda.demoapp.ui.common.test.FragmentTest
import io.qameta.allure.Epic
import io.qameta.allure.Feature
import io.qameta.allure.Story
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

@Epic("UI-Testing (Espresso)")
@Feature("Checkout complete screen")
@Tag("espresso")
@Tag("component-ui")
class CheckoutCompleteFragmentTest: FragmentTest<CheckoutCompleteFragment>(CheckoutCompleteFragment::class.java) {

    @Story("CCF-STORY-1: Display order approving elements")
    @Test
    fun headerIsDisplayedTest() {
        onView(withId(R.id.completeTV))
            .check(matches(isDisplayed()))
            .check(matches(withText(R.string.checkout_complete)))
    }

    @Story("CCF-STORY-1: Display order approving elements")
    @Test
    fun thankYouTextIsDisplayedTest() {
        onView(withId(R.id.thankYouTV))
            .check(matches(isDisplayed()))
            .check(matches(withText(R.string.thank_you_for_your_order)))
    }

    @Story("CCF-STORY-1: Display order approving elements")
    @Test
    fun swagTextIsDisplayedTest() {
        onView(withId(R.id.swagTV))
            .check(matches(isDisplayed()))
            .check(matches(withText(R.string.your_new_swag_is_on_its_way)))
    }

    @Story("CCF-STORY-1: Display order approving elements")
    @Test
    fun orderTextIsDisplayedTest() {
        onView(withId(R.id.orderTV))
            .check(matches(isDisplayed()))
            .check(matches(withText(R.string.your_order_has_been_dispatched_and_will_arrive_as_fast_as_the_pony_gallops)))
    }

    @Story("CCF-STORY-1: Display order approving elements")
    @Test
    fun buttonContinueIsDisplayedTest() {
        onView(withId(R.id.shoopingBt))
            .check(matches(isDisplayed()))
            .check(matches(withText(R.string.continue_shopping)))
            .check(matches(withContentDescription(R.string.tap_to_open_catalog)))
    }
}
