package by.pda.demoapp.ui.common.test

import androidx.test.espresso.Espresso
import by.pda.demoapp.core.extensions.InstantExecutionExtension
import io.qameta.allure.Epic
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(InstantExecutionExtension::class)
@Epic("UI-Testing (Espresso)")
abstract class BaseUITest {

    @BeforeEach
    fun setupAllure() {
        Espresso.setFailureHandler { error, viewMatcher ->
//            TODO("Need to be implemented")
        }
    }
}