package top.topsea.streamplayer.data.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import com.tencent.mmkv.MMKV
import dagger.hilt.android.lifecycle.HiltViewModel
import top.topsea.streamplayer.data.state.UISetsState
import javax.inject.Inject


sealed interface UISetsEvent {
    data class ClickItem(
        val itemIndex: Long,
    ) : UISetsEvent
}

sealed interface PlayerEvent {
    data class PlayItem(
        val itemIndex: Long,
        val mediaItem: MediaItem,
    ) : PlayerEvent
}

@HiltViewModel
class UISetsViewModel @Inject constructor(
    private val kv: MMKV,
    private val player: ExoPlayer,
) : ViewModel() {
    init {
        player.prepare()
        player.repeatMode = Player.REPEAT_MODE_OFF
    }


    var uiSetsState: UISetsState = UISetsState()
        private set


    fun onUISetsEvent(event: UISetsEvent) {
        uiSetsState.onUISetsEvent(event)
    }

    fun onPlayerEvent(event: PlayerEvent) {
        when (event) {
            is PlayerEvent.PlayItem -> {
                playItem(event.itemIndex, event.mediaItem)
            }
        }
    }

    private fun playItem(
        itemIndex: Long,
        mediaItem: MediaItem,
    ) {
        if (uiSetsState.itemIndex == itemIndex) {
            if (player.isPlaying) {
                player.pause()
            } else {
                if (player.mediaItemCount == 0) {
                    player.addMediaItem(mediaItem)
                }
                player.play()
            }
        } else {
            if (player.isPlaying) {
                player.pause()
            }
            // 移除旧的，添加新的
            player.removeMediaItems(0, player.mediaItemCount)
            player.addMediaItem(mediaItem)
            player.play()
        }
    }

    override fun onCleared() {
        super.onCleared()
        player.release()
    }
}