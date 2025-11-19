package by.pda.demoapp

import android.app.Application
import android.content.Context
import io.qameta.allure.android.runners.AllureAndroidJUnitRunner

class IntegrationTestRunner : AllureAndroidJUnitRunner() {
    override fun newApplication(cl: ClassLoader?, className: String?, context: Context?): Application {
        return super.newApplication(cl, TestApp::class.java.name, context)
    }
}