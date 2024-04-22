package top.topsea.streamplayer.data.converter

import androidx.room.TypeConverter
import java.sql.Date

class DateConverter {
    @TypeConverter
    fun convertDate(value: Date): Long {
        return value.time
    }

    @TypeConverter
    fun revertDate(value: Long): Date {
        return Date(value)
    }
}