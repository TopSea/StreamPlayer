package top.topsea.streamplayer.data.state

import android.util.Log
import androidx.compose.runtime.MutableLongState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.setValue
import top.topsea.streamplayer.data.table.ChatInfo
import top.topsea.streamplayer.data.viewmodel.UISetsEvent

class UISetsState {
    var itemIndex by mutableLongStateOf(-1L)
        private set



    fun onUISetsEvent(event: UISetsEvent) {
        when(event) {
            is UISetsEvent.ClickItem -> {
                itemIndex = if (itemIndex == event.itemIndex) {
                    -1
                } else {
                    event.itemIndex
                }
            }
        }
    }
}