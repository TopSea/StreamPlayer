package top.topsea.streamplayer

import android.app.Application
import com.tencent.mmkv.MMKV
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class BaseApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        MMKV.initialize(this)
    }
}