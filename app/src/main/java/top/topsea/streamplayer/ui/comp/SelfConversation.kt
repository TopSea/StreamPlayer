package top.topsea.streamplayer.ui.comp

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.paddingFrom
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.LastBaseline
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import top.topsea.streamplayer.R
import top.topsea.streamplayer.data.state.UISetsState
import top.topsea.streamplayer.data.table.ChatInfo
import top.topsea.streamplayer.data.viewmodel.PlayerEvent
import top.topsea.streamplayer.data.viewmodel.UISetsEvent
import top.topsea.streamplayer.util.DateUtil


@Composable
fun SelfMessageItem(
    uiSetsState: UISetsState,
    uiSetsEvent: (UISetsEvent) -> Unit,
    onAuthorClick: (String) -> Unit,
    msg: ChatInfo,
    isUserMe: Boolean,
    lastMessageFromMe: Boolean,
    playerEvent: (PlayerEvent) -> Unit
) {
    val borderColor = if (isUserMe) {
        MaterialTheme.colorScheme.primary
    } else {
        MaterialTheme.colorScheme.tertiary
    }

    val spaceBetweenAuthors =
        if (lastMessageFromMe) Modifier.padding(top = 8.dp) else Modifier.padding(top = 16.dp)
    Row(modifier = spaceBetweenAuthors) {
        SelfMessage(
            modifier = Modifier
                .padding(start = 16.dp)
                .weight(1f),
            uiSetsEvent = uiSetsEvent,
            uiSetsState = uiSetsState,
            msg = msg,
            isUserMe = isUserMe,
            isLastMessageByAuthor = lastMessageFromMe,
            authorClicked = onAuthorClick,
            playerEvent = playerEvent
        )
        if (!lastMessageFromMe) {
            // Avatar
            Image(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .size(42.dp)
                    .border(1.5.dp, borderColor, CircleShape)
                    .border(3.dp, MaterialTheme.colorScheme.surface, CircleShape)
                    .clip(CircleShape)
                    .align(Alignment.Top),
                painter = painterResource(id = if (msg.fromWho == "me") R.drawable.baseline_person else R.drawable.baseline_android),
                contentScale = ContentScale.Crop,
                contentDescription = null,
            )
        } else {
            // Space under avatar
            Spacer(modifier = Modifier.width(74.dp))
        }
    }
}

@Composable
fun SelfMessage(
    modifier: Modifier = Modifier,
    uiSetsState: UISetsState,
    uiSetsEvent: (UISetsEvent) -> Unit,
    msg: ChatInfo,
    isUserMe: Boolean,
    isLastMessageByAuthor: Boolean,
    authorClicked: (String) -> Unit,
    playerEvent: (PlayerEvent) -> Unit
) {
    Column(modifier = modifier) {
        if (!isLastMessageByAuthor) {
            SelfTimestamp(msg)
        }
        SelfChatItemBubble(
            uiSetsState = uiSetsState,
            uiSetsEvent = uiSetsEvent,
            chatMessage = msg,
            isUserMe = isUserMe,
            authorClicked = authorClicked,
            playerEvent = playerEvent
        )

//        if (isLastMessageByAuthor) {
//            // Last bubble before next author
//            Spacer(modifier = Modifier.height(8.dp))
//        } else {
//            // Between bubbles
//            Spacer(modifier = Modifier.height(4.dp))
//        }
    }
}


private val SelfChatBubbleShape = RoundedCornerShape(10.dp, 4.dp, 10.dp, 10.dp)

@Composable
fun SelfChatItemBubble(
    uiSetsState: UISetsState,
    chatMessage: ChatInfo,
    uiSetsEvent: (UISetsEvent) -> Unit,
    isUserMe: Boolean,
    authorClicked: (String) -> Unit,
    playerEvent: (PlayerEvent) -> Unit
) {

    val backgroundBubbleColor = if (isUserMe) {
        MaterialTheme.colorScheme.primary
    } else {
        MaterialTheme.colorScheme.surfaceVariant
    }

    Box(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.align(Alignment.CenterEnd),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                // 对聊天信息的操作
                MessageFunction(
                    uiSetsState = uiSetsState,
                    chatInfo = chatMessage,
                    uiSetsEvent = uiSetsEvent,
                    playerEvent = playerEvent
                )

                Row(
                    modifier = Modifier.background(
                        color = backgroundBubbleColor,
                        shape = SelfChatBubbleShape
                    ),
                ) {
                    ClickableMessage(
                        chatMessage = chatMessage,
                        isUserMe = isUserMe,
                        authorClicked = authorClicked
                    )
                }
            }
        }
    }
}

@Composable
private fun SelfTimestamp(msg: ChatInfo) {
    val context = LocalContext.current
    val fstr = context.getString(R.string.ee_mm_dd_yy)
    Box(modifier = Modifier.fillMaxWidth()) {
        Row(modifier = Modifier.align(Alignment.CenterEnd)) {
            Text(
                text = DateUtil.formatDate(msg.sendTime, fstr),
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.alignBy(LastBaseline),
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = msg.fromWho,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier
                    .alignBy(LastBaseline)
                    .paddingFrom(LastBaseline, after = 8.dp) // Space to 1st bubble
            )
        }
    }
}
