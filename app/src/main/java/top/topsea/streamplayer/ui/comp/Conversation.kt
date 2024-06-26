package top.topsea.streamplayer.ui.comp

import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshContainer
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.media3.common.MediaItem
import kotlinx.coroutines.launch
import top.topsea.streamplayer.R
import top.topsea.streamplayer.data.state.UISetsState
import top.topsea.streamplayer.data.table.ChatInfo
import top.topsea.streamplayer.data.viewmodel.PlayerEvent
import top.topsea.streamplayer.data.viewmodel.UISetsEvent
import top.topsea.streamplayer.util.FileUtil
import java.io.File

const val ConversationTestTag = "ConversationTestTag"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatMessages(
    modifier: Modifier = Modifier,
    uiSetsState: UISetsState,
    chatMessages: List<ChatInfo>,
    navigateToProfile: (String) -> Unit,
    scrollState: LazyListState,
    uiSetsEvent: (UISetsEvent) -> Unit,
    playerEvent: (PlayerEvent) -> Unit,
) {
    val scope = rememberCoroutineScope()

    val state = rememberPullToRefreshState()
    if (state.isRefreshing) {
        LaunchedEffect(true) {

        }
    }

    // 需要 Box 下拉刷新
    Box(modifier = modifier) {
        LazyColumn(
            reverseLayout = true,
            state = scrollState,
            modifier = Modifier
                .testTag(ConversationTestTag)
                .fillMaxSize()
        ) {
            for (index in chatMessages.indices) {
                val chatMessage = chatMessages[index]

                // 因为是倒序，所以 +1
                val prevAuthor = chatMessages.getOrNull(index + 1)?.fromWho
                val lastMessageFromMe = prevAuthor == "me"

                if (index == 0) {
                    item {
                        Spacer(modifier = Modifier.size(8.dp))
                    }
                }
                // Hardcode day dividers for simplicity
//                if (index == chatMessages.size - 1) {
//                    item {
//                        DayHeader("20 Aug")
//                    }
//                } else if (index == 2) {
//                    item {
//                        DayHeader("Today")
//                    }
//                }

                if (chatMessage.fromWho == "me") {
                    item {
                        SelfMessageItem(
                            uiSetsState = uiSetsState,
                            uiSetsEvent = uiSetsEvent,
                            onAuthorClick = { name -> navigateToProfile(name) },
                            msg = chatMessage,
                            isUserMe = true,
                            lastMessageFromMe = lastMessageFromMe,
                            playerEvent = playerEvent
                        )
                    }
                } else {
                    item {
                        DroidMessageItem(
                            onAuthorClick = { name -> navigateToProfile(name) },
                            msg = chatMessage,
                            isUserMe = false,
                            lastMessageFromDroid = !lastMessageFromMe
                        )
                    }
                }
            }
        }
        // Jump to bottom button shows up when user scrolls past a threshold.
        // Convert to pixels:
        val jumpThreshold = with(LocalDensity.current) {
            JumpToBottomThreshold.toPx()
        }

        // Show the button if the first visible item is not the first one or if the offset is
        // greater than the threshold.
        val jumpToBottomButtonEnabled by remember {
            derivedStateOf {
                scrollState.firstVisibleItemIndex != 0 ||
                        scrollState.firstVisibleItemScrollOffset > jumpThreshold
            }
        }

        JumpToBottom(
            // Only show if the scroller is not at the bottom
            enabled = jumpToBottomButtonEnabled,
            onClicked = {
                scope.launch {
                    scrollState.animateScrollToItem(0)
                }
            },
            modifier = Modifier.align(Alignment.BottomCenter)
        )

        PullToRefreshContainer(
            modifier = Modifier.align(Alignment.TopCenter),
            state = state,
            shape = RoundedCornerShape(10.dp),
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = Color.White,
        )
    }
}

@Composable
fun DayHeader(dayString: String) {
    Row(
        modifier = Modifier
            .padding(vertical = 8.dp, horizontal = 16.dp)
            .height(16.dp)
    ) {
        DayHeaderLine()
        Text(
            text = dayString,
            modifier = Modifier.padding(horizontal = 16.dp),
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        DayHeaderLine()
    }
}

@Composable
private fun RowScope.DayHeaderLine() {
    HorizontalDivider(
        modifier = Modifier
            .weight(1f)
            .align(Alignment.CenterVertically),
        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f)
    )
}

@Composable
fun ClickableMessage(
    chatMessage: ChatInfo,
    isUserMe: Boolean,
    authorClicked: (String) -> Unit
) {
    val uriHandler = LocalUriHandler.current

    val styledMessage = messageFormatter(
        text = chatMessage.messageContent.content,
        primary = isUserMe
    )

    ClickableText(
        text = styledMessage,
        style = TextStyle(
            color = Color.White,
            fontSize = 13.sp,
        ),
        modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
        onClick = {
            styledMessage
                .getStringAnnotations(start = it, end = it)
                .firstOrNull()
                ?.let { annotation ->
                    when (annotation.tag) {
                        SymbolAnnotationType.LINK.name -> uriHandler.openUri(annotation.item)
                        SymbolAnnotationType.PERSON.name -> authorClicked(annotation.item)
                        else -> Unit
                    }
                }
        }
    )
}

@Composable
fun MessageFunction(
    modifier: Modifier = Modifier,
    chatInfo: ChatInfo,
    uiSetsEvent: (UISetsEvent) -> Unit,
    uiSetsState: UISetsState,
    playerEvent: (PlayerEvent) -> Unit
) {
    val context = LocalContext.current
    if (uiSetsState.itemIndex == chatInfo.id) {
        Row(
            modifier = modifier,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            if (chatInfo.messageContent.voiceFile.isNotEmpty()) {
                Icon(
                    painter = painterResource(id = R.drawable.chat_func_mic),
                    contentDescription = "Play message.",
                    Modifier.clickable {
                        val voiceFile = FileUtil.getVoiceFile(context, chatInfo.messageContent.voiceFile)
                        playerEvent(
                            PlayerEvent.PlayItem(
                                itemIndex = chatInfo.id,
                                MediaItem.fromUri(Uri.fromFile(File(voiceFile)))
                            )
                        )
                    }
                )
            }
            Icon(
                painter = painterResource(id = R.drawable.chat_func_download),
                contentDescription = "Delete message."
            )
            Icon(
                painter = painterResource(id = R.drawable.chat_func_link_open),
                contentDescription = "Delete message."
            )
            Icon(
                imageVector = Icons.Outlined.MoreVert,
                contentDescription = "Open message func.",
                Modifier.clickable {
                    uiSetsEvent(UISetsEvent.ClickItem(chatInfo.id))
                }
            )
        }
    } else {
        Icon(
            imageVector = Icons.Outlined.MoreVert,
            contentDescription = "Open message func.",
            Modifier.clickable {
                uiSetsEvent(UISetsEvent.ClickItem(chatInfo.id!!))
            }
        )
    }
}

private val JumpToBottomThreshold = 56.dp
