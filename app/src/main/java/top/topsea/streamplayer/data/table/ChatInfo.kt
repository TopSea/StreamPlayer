package top.topsea.streamplayer.data.table

import androidx.annotation.Keep
import androidx.compose.runtime.Immutable
import androidx.room.ColumnInfo
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.TypeConverters
import kotlinx.coroutines.flow.Flow
import top.topsea.streamplayer.data.converter.DateConverter
import top.topsea.streamplayer.data.converter.MessageConverter
import java.sql.Date


// 信息类型
enum class MessageType{
    // 播放、下载、分享
    TEXT,
    // 播放、下载、分享
    AUDIO,
    // 打开、下载、分享
    VIDEO,
    // 打开、下载、分享
    IMAGE,
    // 打开、分享
    LINK
}

class ChatMessage(
    val type: MessageType,
    val content: String,
)


@Keep
@Immutable
@TypeConverters(MessageConverter::class, DateConverter::class)
@Entity
data class ChatInfo(
    @PrimaryKey
    val id: Long?,
    @ColumnInfo(name = "from_who")
    val fromWho: String,
    @ColumnInfo(name = "to_who")
    val toWho: String,
    @ColumnInfo(name = "message_content")
    val messageContent: ChatMessage,
    @ColumnInfo(name = "send_time")
    val sendTime: Date,

    )

@Dao
interface ChatInfoDao {
    @Query("SELECT * FROM ChatInfo ")
    fun allChat(): Flow<List<ChatInfo>>

    @Query("SELECT * FROM ChatInfo ORDER BY send_time DESC LIMIT 50")
    fun chats50Latest(): Flow<List<ChatInfo>>

    @Insert
    suspend fun insert(chatInfo: ChatInfo): Long
    @Delete(ChatInfo::class)
    suspend fun delete(chatInfo: ChatInfo): Int

}