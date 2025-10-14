package by.pda.demoapp.test.utils

import android.icu.util.TimeUnit
import androidx.lifecycle.LiveData

fun <T> LiveData<T>.getOrAwaitValue(
    time: Long = 2,
    unit: TimeUnit = TimeUnit.SECOND
) {
    var data: T? = null

}