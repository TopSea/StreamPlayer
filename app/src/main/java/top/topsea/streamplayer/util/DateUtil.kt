package top.topsea.streamplayer.util

import android.icu.util.Calendar
import java.sql.Date

class DateUtil {
    companion object {
        fun getDateMonthStart(date: Date) {
            val calendar = Calendar.getInstance()
            calendar.time = date

        }
    }
}