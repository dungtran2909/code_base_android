package com.dungtran.codebase.ui.features.main.chat

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dungtran.codebase.R
import com.dungtran.codebase.domain.model.Chat
import com.dungtran.codebase.domain.model.User
import com.dungtran.codebase.ui.common.UserAvatarView

@Composable
fun ChatRoute(
    modifier: Modifier = Modifier,
    viewModel: ChatViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    ChatScreen(
        modifier = modifier,
        uiState = uiState,
        gotoPrivateChat = viewModel::gotoPrivateChat
    )
}

@Composable
fun ChatScreen(
    modifier: Modifier = Modifier,
    uiState: ChatUiState,
    gotoPrivateChat: (String) -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxHeight()
            .fillMaxWidth()
    ) {
        Image(
            painter = painterResource(id = R.drawable.background_screen_type_three),
            contentDescription = null,
            modifier = Modifier.fillMaxSize().padding(top = 40.dp),
            contentScale = ContentScale.Crop
        )
        Row(
            modifier = modifier
                .fillMaxWidth()
                .height(40.dp)
                .padding(horizontal = 16.dp)
                .background(color = MaterialTheme.colorScheme.surface.copy(alpha = 0.8f))
        ) {
            Text(
                modifier = Modifier.fillMaxSize(),
                text = "messenger",
                style = MaterialTheme.typography.headlineLarge,
            )
        }
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp)
                .padding(top = 40.dp)
            /*.verticalScroll(rememberScrollState())*/,
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(uiState.users) { user ->
                    HeaderItem(
                        user = user, 
                        gotoPrivateChat = gotoPrivateChat
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(uiState.chats) { chat ->
                    val partnerId = chat.members.firstOrNull { it != uiState.myUid }
                    val partnerInfo = uiState.users.find { it.uid == partnerId }
                    ChatItem(
                        displayName = partnerInfo?.displayName ?: "Người dùng",
                        lastMessage = chat.lastMessage,
                        photoUrl = partnerInfo?.photoUrl ?: "",
                        onClick = { }
                    )
                }
            }
        }
    }
}

@Composable
fun HeaderItem(user: User, gotoPrivateChat: (String) -> Unit) {
    Box(
        contentAlignment = Alignment.TopCenter,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .clickable { gotoPrivateChat(user.uid) }
                .padding(bottom = 8.dp, top = 30.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            UserAvatarView(
                imageUrl = user.photoUrl,
                size = 60.dp,
                borderWidth = 0.dp
            )
            Text(
                modifier = Modifier.padding(top = 4.dp),
                text = user.displayName.split(" ").first(),
                style = MaterialTheme.typography.labelSmall,
                color = Color.Gray
            )
        }
        
        if (user.isMe || user.thinking.isNotEmpty()) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .width(70.dp)
                    .height(40.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_bubble_chat),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.FillBounds
                )

                Text(
                    modifier = Modifier
                        .padding(bottom = 4.dp)
                        .padding(horizontal = 4.dp),
                    text = when {
                        user.isMe && user.thinking.isEmpty() -> "Chia sẻ\nghi chú..."
                        else -> user.thinking
                    },
                    textAlign = TextAlign.Center,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontSize = 8.sp,
                        lineHeight = 10.sp,
                        fontWeight = FontWeight.Light
                    ),
                    color = Color.White
                )
            }   
        }
    }
}

@Composable
fun ChatItem(displayName: String, lastMessage: String, photoUrl: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(bottom = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        UserAvatarView(
            imageUrl = photoUrl,
            size = 50.dp,
            borderWidth = 0.dp
        )

        Column(modifier = Modifier.padding(start = 12.dp)) {
            Text(
                text = displayName,
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)
            )
            Text(
                text = lastMessage,
                style = MaterialTheme.typography.labelSmall,
                color = Color.Gray
            )
        }
    }
}