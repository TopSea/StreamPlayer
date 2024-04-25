package top.topsea.streamplayer.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import kotlinx.coroutines.launch
import top.topsea.streamplayer.R
import top.topsea.streamplayer.data.table.ChatInfo
import top.topsea.streamplayer.data.table.ChatMessage
import top.topsea.streamplayer.data.table.MessageType
import top.topsea.streamplayer.data.viewmodel.ChatEvent
import top.topsea.streamplayer.data.viewmodel.ChatInfoViewModel
import top.topsea.streamplayer.data.viewmodel.UISetsViewModel
import top.topsea.streamplayer.ui.comp.ChatMessages
import top.topsea.streamplayer.ui.comp.UserInput
import java.sql.Date

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    navController: NavController,
    chatInfoViewModel: ChatInfoViewModel = hiltViewModel(),
    navigateToProfile: (String) -> Unit,
) {
    val scrollState = rememberLazyListState()
    val scope = rememberCoroutineScope()
    val chatUIState by chatInfoViewModel.state.collectAsState()
    val uiSetsViewModel: UISetsViewModel = hiltViewModel()

    Scaffold(
        topBar = { MainTopBar(navController) }
    ) { paddingValues ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(paddingValues)) {
            ChatMessages(
                modifier = Modifier.fillMaxWidth().weight(1f),
                chatMessages = chatUIState.chats,
                uiSetsState = uiSetsViewModel.uiSetsState,
                navigateToProfile = navigateToProfile,
                scrollState = scrollState,
                uiSetsEvent = uiSetsViewModel::onUISetsEvent
            )
            UserInput(
                onMessageSent = { content ->
                                // TODO 保存和发送信息
                    val newMessage = ChatMessage(
                        MessageType.TEXT,
                        content
                    )
                    val newChat = ChatInfo(
                        id = null,
                        fromWho = "me",
                        toWho = "Furina",
                        messageContent = newMessage,
                        sendTime = Date(System.currentTimeMillis())
                    )
                    chatInfoViewModel.onChatEvent(ChatEvent.AddChat(
                        newChat
                    ))
                },
                resetScroll = {
                    scope.launch {
                        scrollState.scrollToItem(0)
                    }
                },
                // let this element handle the padding so that the elevation is shown behind the
                // navigation bar
                modifier = Modifier
                    .navigationBarsPadding()
                    .imePadding()
            )
        }
    }

}

@Composable
fun MainTopBar(
    navController: NavController
) {
    val context = LocalContext.current

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(52.dp)
            .background(color = Color(dynamicLightColorScheme(context).primary.toArgb()))
    ) {
        Text(
            text = stringResource(id = R.string.app_name),
            modifier = Modifier
                .padding(start = 16.dp)
                .align(Alignment.CenterStart),
            fontSize = TextUnit(value = 20F, type = TextUnitType.Sp),
            fontWeight = FontWeight.Bold,
            color = Color.White
        )

        Icon(
            imageVector = Icons.Rounded.Settings,
            contentDescription = null,
            modifier = Modifier
                .padding(end = 16.dp, top = 4.dp, bottom = 8.dp)
                .size(32.dp)
                .align(Alignment.CenterEnd)
                .clickable {
                    navController.navigate("settings")
                },
            tint = Color.White
        )
    }
}
