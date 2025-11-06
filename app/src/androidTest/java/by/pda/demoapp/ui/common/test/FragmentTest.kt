package by.pda.demoapp.ui.common.test

import androidx.fragment.app.Fragment
import androidx.fragment.app.testing.FragmentScenario
import by.pda.demoapp.core.extensions.InstantExecutionExtension
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(InstantExecutionExtension::class)
abstract class FragmentTest<T : Fragment>(private val fragmentClass: Class<T>) {
    lateinit var scenario: FragmentScenario<T>

    @BeforeEach
    fun setUp() {
        scenario = FragmentScenario.launchInContainer(fragmentClass)
    }

    @AfterEach
    fun tearDown() {
        scenario.close()
    }
}
