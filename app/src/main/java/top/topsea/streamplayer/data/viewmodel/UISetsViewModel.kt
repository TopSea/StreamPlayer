package top.topsea.streamplayer.data.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.tencent.mmkv.MMKV
import dagger.hilt.android.lifecycle.HiltViewModel
import top.topsea.streamplayer.data.state.UISetsState
import javax.inject.Inject


sealed interface UISetsEvent {
    data class ClickItem(
        val itemIndex: Long,
    ): UISetsEvent
}

@HiltViewModel
class UISetsViewModel @Inject constructor(
    private val kv: MMKV,
    ) : ViewModel() {
    var uiSetsState: UISetsState = UISetsState()
        private set


    fun onUISetsEvent(event: UISetsEvent) {
        uiSetsState.onUISetsEvent(event)
    }
}