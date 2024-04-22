package top.topsea.streamplayer.data

import androidx.room.Database
import androidx.room.RoomDatabase
import top.topsea.streamplayer.data.table.ChatInfo
import top.topsea.streamplayer.data.table.ChatInfoDao

@Database(entities = [ChatInfo::class], version = 1,)
abstract class AppDatabase: RoomDatabase() {
    abstract fun chatInfoDao(): ChatInfoDao
}