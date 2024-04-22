package top.topsea.streamplayer.data.state

import top.topsea.streamplayer.data.table.ChatInfo

data class ChatUIState(
    val chats: List<ChatInfo> = emptyList(),
)