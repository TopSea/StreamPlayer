package top.topsea.streamplayer.util

import android.icu.text.DateFormat
import android.icu.util.Calendar
import java.sql.Date
import java.text.SimpleDateFormat
import java.util.Locale

class DateUtil {
    companion object {
        fun getDateMonthStart(date: Date) {
            val calendar = Calendar.getInstance()
            calendar.time = date

        }

        fun formatDate(date: Date, fstr: String): String {
            return SimpleDateFormat(fstr, Locale.getDefault()).format(date)
        }

        fun formatDate(date: Date, fstr: String, locale: Locale): String {
            return SimpleDateFormat(fstr, locale).format(date)
        }
    }
}