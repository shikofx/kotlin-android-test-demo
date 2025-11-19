package by.pda.demoapp.core.annotations

import by.pda.demoapp.core.extensions.InstantExecutionExtension
import io.qameta.allure.Epic
import org.junit.jupiter.api.extension.ExtendWith

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.CLASS, AnnotationTarget.FUNCTION)
@Epic("Data Layer")
@ExtendWith(InstantExecutionExtension::class)
annotation class DatabaseTest
