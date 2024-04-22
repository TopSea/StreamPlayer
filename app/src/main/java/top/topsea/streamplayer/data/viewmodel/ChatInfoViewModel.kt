package top.topsea.streamplayer.data.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import top.topsea.streamplayer.data.state.ChatUIState
import top.topsea.streamplayer.data.table.ChatInfo
import top.topsea.streamplayer.data.table.ChatInfoDao
import javax.inject.Inject


sealed interface ChatEvent {
    data class AddChat(
        val chatInfo: ChatInfo,
    ): ChatEvent
}

@HiltViewModel
class ChatInfoViewModel @Inject constructor(
    private val dao: ChatInfoDao,
    ) : ViewModel() {
    private val _state = MutableStateFlow(ChatUIState())
    private val _chats50 = dao.chats50Latest()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())
    val state = combine(_state, _chats50) { state, chats ->
        state.copy(
            chats = chats,
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), ChatUIState())


    fun onChatEvent(event: ChatEvent) {
        when(event) {
            is ChatEvent.AddChat -> {
                viewModelScope.launch {
                    dao.insert(event.chatInfo)
                }
            }
        }
    }
}