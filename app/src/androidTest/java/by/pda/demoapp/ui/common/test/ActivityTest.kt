package by.pda.demoapp.ui.common.test

import android.app.Activity
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.contrib.DrawerActions
import androidx.test.espresso.contrib.NavigationViewActions
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItem
import androidx.test.espresso.matcher.ViewMatchers.hasDescendant
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import by.pda.demoapp.android.R
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach

abstract class ActivityTest<T : Activity>(private val activityClass: Class<T>) : BaseUITest() {
    lateinit var scenario: ActivityScenario<T>

    @BeforeEach
    fun setUp() {
        scenario = ActivityScenario.launch(activityClass)
    }

    @AfterEach
    fun tearDown() {
        scenario.close()
    }

    fun openAboutFragment() {
        onView(withId(R.id.container)).perform(DrawerActions.open())

        onView(withId(R.id.menuRV))
            .perform(
                actionOnItem<ViewHolder>(
                    hasDescendant(withText("About")),
                    click()
                )
            )
    }
}