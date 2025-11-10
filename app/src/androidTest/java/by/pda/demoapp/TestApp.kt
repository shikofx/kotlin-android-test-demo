package by.pda.demoapp

import by.pda.demoapp.android.MyApplication
import by.pda.demoapp.android.database.AppDatabase

class TestApp : MyApplication() {
    override fun onCreate() {
        super.onCreate()
        AppDatabase.getTestInstance(this)
    }
}
