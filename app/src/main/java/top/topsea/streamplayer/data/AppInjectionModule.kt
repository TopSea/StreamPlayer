package top.topsea.streamplayer.data

import android.content.Context
import androidx.media3.exoplayer.ExoPlayer
import androidx.room.Room
import com.tencent.mmkv.MMKV
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import top.topsea.streamplayer.data.table.ChatInfoDao
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppInjectionModule {

    @Singleton
    @Provides
    fun provideMMKV(): MMKV = MMKV.defaultMMKV()

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext appContext: Context): AppDatabase {
        return Room.databaseBuilder(
            appContext,
            AppDatabase::class.java,
            "AppDatabase.db"
        )
//            .createFromAsset("AppDatabase.db")
            .build()
    }

    @Provides
    @Singleton
    fun provideExoPlayer(@ApplicationContext appContext: Context): ExoPlayer {
        return ExoPlayer.Builder(appContext).build()
    }

    @Provides
    fun provideUserPromptDao(appDatabase: AppDatabase): ChatInfoDao {
        return appDatabase.chatInfoDao()
    }
}