package by.pda.demoapp.core.annotations

import io.qameta.allure.Epic

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.CLASS, AnnotationTarget.FUNCTION)
@Epic("Component UI-Testing")
annotation class ComponentUiTest
