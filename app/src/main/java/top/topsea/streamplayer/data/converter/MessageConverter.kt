package top.topsea.streamplayer.data.converter

import androidx.room.TypeConverter
import com.google.gson.Gson
import top.topsea.streamplayer.data.table.ChatMessage
import top.topsea.streamplayer.data.table.MessageType
import java.sql.Date

class MessageConverter {
    @TypeConverter
    fun convertDate(value: ChatMessage): String {
        return Gson().toJson(value)
    }

    @TypeConverter
    fun revertDate(value: String): ChatMessage {
        return Gson().fromJson(value, ChatMessage::class.java)
    }
}