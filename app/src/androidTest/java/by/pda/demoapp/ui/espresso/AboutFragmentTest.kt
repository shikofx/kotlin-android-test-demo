package by.pda.demoapp.ui.espresso

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import by.pda.demoapp.android.R
import by.pda.demoapp.android.view.fragments.AboutFragment
import by.pda.demoapp.ui.common.test.FragmentTest
import org.junit.jupiter.api.Test

class AboutFragmentTest: FragmentTest<AboutFragment>(AboutFragment::class.java) {

    @Test
    fun elementsAreDisplayed() {
        onView(withId(R.id.versionTV))
            .check(matches(isDisplayed()))
    }

}