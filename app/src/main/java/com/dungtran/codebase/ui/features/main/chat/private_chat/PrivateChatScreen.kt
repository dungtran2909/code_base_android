package com.dungtran.codebase.ui.features.main.chat.private_chat

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dungtran.codebase.domain.model.Message
import com.dungtran.codebase.ui.common.UserAvatarView
import com.dungtran.codebase.ui.navigation.Screen
import com.dungtran.codebase.ui.theme.Primary
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun PrivateChatRoute(
    modifier: Modifier = Modifier,
    viewModel: PrivateChatViewModel = hiltViewModel(),
    onBack: () -> Unit,
    dataScreen: Screen.PrivateChat
) {
    LaunchedEffect(dataScreen) {
        viewModel.initData(dataScreen)
    }
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    PrivateChatScreen(
        modifier = modifier,
        uiState = uiState,
        onSendMessage = viewModel::sendMessage,
        onBack = onBack
    )
}

@Composable
fun PrivateChatScreen(
    modifier: Modifier = Modifier,
    uiState: PrivateChatUiState,
    onSendMessage: (String) -> Unit,
    onBack: () -> Unit
) {
    var textState by remember { mutableStateOf("") }
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(uiState.messageList.size) {
        if (uiState.messageList.isNotEmpty()) {
            listState.animateScrollToItem(uiState.messageList.size - 1)
        }
    }

    val showScrollToBottom by remember(uiState.messageList.size) {
        derivedStateOf {
            val lastVisibleItem = listState.layoutInfo.visibleItemsInfo.lastOrNull()
            if (lastVisibleItem == null) {
                false
            } else {
                lastVisibleItem.index < uiState.messageList.size - 1
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding(),
            topBar = {
                PrivateChatHeader(uiState, onBack)
            },
            bottomBar = {
                ChatInput(
                    value = textState,
                    onValueChange = { textState = it },
                    onSend = {
                        if (textState.isNotBlank()) {
                            onSendMessage(textState)
                            textState = ""
                        }
                    }
                )
            }
        ) { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .background(Color.White)
            ) {
                LazyColumn(
                    state = listState,
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                ) {
                    itemsIndexed(uiState.messageList) { index, message ->
                        val prevMsg = uiState.messageList.getOrNull(index - 1)
                        val nextMsg = uiState.messageList.getOrNull(index + 1)

                        if (shouldShowTimeHeader(message, prevMsg)) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 12.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = formatChatTimestamp(message.timestamp),
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Color.Black
                                )
                            }
                        }

                        val isMine = message.senderId == uiState.myUid

                        // Logic Grouping mới:
                        val belongsToPrev = isSameGroup(message, prevMsg)
                        val belongsToNext = isSameGroup(message, nextMsg)

                        val position = when {
                            !belongsToPrev && !belongsToNext -> MessagePosition.SINGLE
                            !belongsToPrev && belongsToNext -> MessagePosition.FIRST
                            belongsToPrev && belongsToNext -> MessagePosition.MIDDLE
                            else -> MessagePosition.LAST
                        }

                        MessageItem(
                            message = message,
                            position = position,
                            isMine = isMine,
                            partnerAvatar = uiState.dataScreen?.partnerAvatar
                        )
                    }
                }
            }
        }

        AnimatedVisibility(
            visible = showScrollToBottom,
            enter = fadeIn() + scaleIn(),
            exit = fadeOut() + scaleOut(),
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 100.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(Color.DarkGray.copy(alpha = 0.8f), CircleShape)
                    .clickable {
                        coroutineScope.launch {
                            listState.animateScrollToItem(uiState.messageList.size - 1)
                        }
                    },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.KeyboardArrowDown,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}

@Composable
fun PrivateChatHeader(
    uiState: PrivateChatUiState,
    onBack: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .background(MaterialTheme.colorScheme.surface),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onBack) {
            Icon(
                modifier = Modifier.size(20.dp),
                imageVector = Icons.Default.ArrowBackIosNew,
                contentDescription = "Back",
                tint = Color.Black
            )
        }

        UserAvatarView(
            imageUrl = uiState.dataScreen?.partnerAvatar,
            size = 36.dp,
            borderWidth = 0.dp
        )

        Text(
            modifier = Modifier.padding(start = 8.dp),
            text = uiState.dataScreen?.partnerName ?: "Người dùng",
            style = MaterialTheme.typography.labelMedium.copy(
                color = Color.Black,
                fontWeight = FontWeight.Black
            )
        )
    }
}

@Composable
fun ChatInput(
    value: String,
    onValueChange: (String) -> Unit,
    onSend: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        tonalElevation = 2.dp,
        color = Color.White
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 8.dp, vertical = 8.dp)
                .navigationBarsPadding() // Tránh bị đè bởi thanh điều hướng hệ thống
                .imePadding(), // Tự động đẩy lên khi bàn phím hiện
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Nút icon cộng hoặc ảnh (optional)
            IconButton(onClick = { /* Handle attachment */ }) {
                Icon(
                    imageVector = Icons.Default.AddCircle,
                    contentDescription = null,
                    tint = Primary,
                    modifier = Modifier.size(24.dp)
                )
            }

            androidx.compose.material3.TextField(
                value = value,
                onValueChange = onValueChange,
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 4.dp),
                placeholder = { Text("Nhắn tin...", style = MaterialTheme.typography.bodyMedium) },
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color(0xFFF0F0F0),
                    unfocusedContainerColor = Color(0xFFF0F0F0),
                    disabledContainerColor = Color(0xFFF0F0F0),
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                ),
                shape = RoundedCornerShape(20.dp),
                maxLines = 4
            )

            IconButton(
                onClick = onSend,
                enabled = value.isNotBlank()
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Default.Send,
                    contentDescription = "Send",
                    tint = if (value.isNotBlank()) Primary else Color.Gray
                )
            }
        }
    }
}

enum class MessagePosition {
    FIRST, MIDDLE, LAST, SINGLE
}

fun isSameGroup(current: Message, other: Message?): Boolean {
    if (other == null) return false
    if (current.senderId != other.senderId) return false

    val sdf = java.text.SimpleDateFormat("HH:mm", java.util.Locale.getDefault())
    val currentTime = sdf.format(java.util.Date(current.timestamp))
    val otherTime = sdf.format(java.util.Date(other.timestamp))

    return currentTime == otherTime
}

@Composable
fun getBubbleShape(position: MessagePosition, isMine: Boolean): RoundedCornerShape {
    val default = 18.dp
    val small = 4.dp

    return if (isMine) {
        when (position) {
            MessagePosition.FIRST -> RoundedCornerShape(default, default, small, default)
            MessagePosition.MIDDLE -> RoundedCornerShape(default, small, small, default)
            MessagePosition.LAST -> RoundedCornerShape(default, small, default, default)
            MessagePosition.SINGLE -> RoundedCornerShape(default, default, default, default)
        }
    } else {
        when (position) {
            MessagePosition.FIRST -> RoundedCornerShape(default, default, default, small)
            MessagePosition.MIDDLE -> RoundedCornerShape(small, default, default, small)
            MessagePosition.LAST -> RoundedCornerShape(small, default, default, default)
            MessagePosition.SINGLE -> RoundedCornerShape(default, default, default, default)
        }
    }
}

@Composable
fun MessageItem(
    message: Message,
    position: MessagePosition,
    isMine: Boolean,
    partnerAvatar: String?
) {
    val topPadding = when (position) {
        MessagePosition.MIDDLE, MessagePosition.LAST -> 1.dp
        else -> 10.dp
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = topPadding)
            .padding(start = if (isMine) 50.dp else 0.dp, end = if (isMine) 0.dp else 50.dp),
        horizontalArrangement = if (isMine) Arrangement.End else Arrangement.Start,
        verticalAlignment = Alignment.Bottom
    ) {
        if (!isMine) {
            if (position == MessagePosition.LAST || position == MessagePosition.SINGLE) {
                UserAvatarView(imageUrl = partnerAvatar, size = 32.dp, borderWidth = 0.dp)
            } else {
                Spacer(modifier = Modifier.width(32.dp))
            }
            Spacer(modifier = Modifier.width(8.dp))
        }

        Row(
            modifier = Modifier.weight(1f, fill = false),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                color = if (isMine) Primary else Color(0xFFF0F0F0),
                shape = getBubbleShape(position, isMine)
            ) {
                Text(
                    text = message.content,
                    color = if (isMine) Color.White else Color.Black,
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

fun formatChatTimestamp(timestamp: Long): String {
    val date = Date(timestamp)
    val sdf = SimpleDateFormat("HH:mm dd 'THG' M", Locale.getDefault())
    return sdf.format(date).uppercase()
}

fun shouldShowTimeHeader(currentMsg: Message, prevMsg: Message?): Boolean {
    if (prevMsg == null) return true // Tin nhắn đầu tiên luôn hiện
    val diff = currentMsg.timestamp - prevMsg.timestamp
    return diff > 30 * 60 * 1000 // 30 phút tính bằng mili giây
}

fun formatTime(timestamp: Long): String {
    val sdf = SimpleDateFormat("H:mm a", Locale.getDefault())
    return sdf.format(Date(timestamp))
}