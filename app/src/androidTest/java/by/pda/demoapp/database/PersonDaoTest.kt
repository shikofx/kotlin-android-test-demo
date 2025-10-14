package by.pda.demoapp.database

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import by.pda.demoapp.android.database.AppDao
import by.pda.demoapp.android.database.AppDatabase
import by.pda.demoapp.test.extensions.InstantExecutionExtension
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(InstantExecutionExtension::class)
class PersonDaoTest {
    private lateinit var database: AppDatabase
    private lateinit var appDao: AppDao

    @BeforeEach
    fun createDatabase() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
            .allowMainThreadQueries()
            .build()

        appDao = database.personDao()
    }

    @AfterEach
    fun closeDatabase() {
        database.close()
    }
}