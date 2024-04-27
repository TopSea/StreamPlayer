package top.topsea.streamplayer.util

import android.content.Context
import android.icu.text.DateFormat
import android.icu.util.Calendar
import java.io.File
import java.sql.Date
import java.text.SimpleDateFormat
import java.util.Locale

class FileUtil {
    companion object {
        fun getVoiceFile(context: Context, fileName: String) :String {
            return context.filesDir.absolutePath + File.separator + "voice" + File.separator + fileName
        }

        fun formatDate(date: Date, fstr: String): String {
            return SimpleDateFormat(fstr, Locale.getDefault()).format(date)
        }

        fun formatDate(date: Date, fstr: String, locale: Locale): String {
            return SimpleDateFormat(fstr, locale).format(date)
        }
    }
}