package by.pda.demoapp.ui.common.test

import android.app.Activity
import androidx.test.core.app.ActivityScenario
import by.pda.demoapp.core.extensions.InstantExecutionExtension
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(InstantExecutionExtension::class)
abstract class ActivityTest<T: Activity>(private val activityClass: Class<T>) {
    lateinit var scenario: ActivityScenario<T>

    @BeforeEach
    fun setUp() {
        scenario = ActivityScenario.launch(activityClass)
    }

    @AfterEach
    fun tearDown() {
        scenario.close()
    }
}