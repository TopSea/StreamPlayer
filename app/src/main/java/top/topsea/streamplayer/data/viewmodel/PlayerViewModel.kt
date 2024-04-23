package top.topsea.streamplayer.data.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.media3.common.MediaItem
import androidx.media3.common.Player.REPEAT_MODE_OFF
import androidx.media3.exoplayer.ExoPlayer
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


sealed interface PlayerEvent {
    data class PlayItem(
        val mediaItem: MediaItem,
    ) : PlayerEvent
    data object StopOrPlay : PlayerEvent
}

@HiltViewModel
class PlayerViewModel @Inject constructor(
    private val player: ExoPlayer,
) : ViewModel() {
    init {
        player.prepare()
        player.repeatMode = REPEAT_MODE_OFF
    }

    fun onChatEvent(event: PlayerEvent) {
        when (event) {
            is PlayerEvent.PlayItem -> {
                if (player.isPlaying) {
                    player.pause()
                }
                // 移除旧的，添加新的
                player.removeMediaItems(0, player.mediaItemCount)
                player.addMediaItem(event.mediaItem)
                player.play()
            }

            PlayerEvent.StopOrPlay -> {
                if (player.isPlaying) {
                    player.pause()
                } else {
                    player.play()
                }
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        player.release()
    }
}