package com.example.bookrecommendation.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bookrecommendation.R
import com.example.bookrecommendation.ui.theme.brown
import com.example.bookrecommendation.ui.theme.purple

// Unique names for Profile data
data class ProfileReply(
    val user: String,
    val text: String
)

data class ProfilePost(
    val bookTitle: String,
    val rating: String,
    val caption: String,
    val imageRes: Int,
    val replies: List<ProfileReply>,
    val isOwner: Boolean
)

@Composable
fun ProfileScreen(modifier: Modifier = Modifier) {

    // Dummy data
    val posts = listOf(
        ProfilePost(
            bookTitle = "Heart Still Beats",
            rating = "⭐ 4.5",
            caption = "Dark, gripping and unforgettable. One of the most emotional reads I’ve ever experienced.",
            imageRes = R.drawable.heart,
            isOwner = true,
            replies = listOf(
                ProfileReply("@jennareads", "Totally agree, this one was a masterpiece!"),
                ProfileReply("@booklover88", "Added to my TBR thanks to you!")
            )
        ),
        ProfilePost(
            bookTitle = "The Silent Patient",
            rating = "⭐ 4.8",
            caption = "A psychological thriller that keeps you hooked till the last page.",
            imageRes = R.drawable.the,
            isOwner = true,
            replies = listOf(
                ProfileReply("@mysteryfan", "The ending was so unexpected! 🤯")
            )
        )
    )

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(Color(0xFFF8F5FF), Color.White)
                )
            )
    ) {
        item {
            ProfileHeader()
        }
        item {
            HorizontalDivider(color = Color.LightGray.copy(alpha = 0.5f))
        }
        items(posts) { post ->
            BookPostCard(post = post)
        }
    }
}

@Composable
fun ProfileHeader() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Image(
            painter = painterResource(id = R.drawable.come),
            contentDescription = "Profile Picture",
            contentScale = ContentScale.Crop,
            modifier = Modifier.size(90.dp).clip(CircleShape).background(Color.LightGray)
        )

        Text("Simran Devkota", fontWeight = FontWeight.Bold, fontSize = 20.sp)
        Text("@simranreads", color = Color.Gray, fontSize = 14.sp)

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            ProfileStat("124", "Followers")
            ProfileStat("86", "Following")
            ProfileStat("18", "Recommended")
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = { },
            shape = RoundedCornerShape(20.dp),
            colors = ButtonDefaults.buttonColors(containerColor = brown)
        ) {
            Icon(Icons.Outlined.Edit, contentDescription = null, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(6.dp))
            Text("Edit Profile")
        }
    }
}

@Composable
fun ProfileStat(count: String, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(count, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = purple)
        Text(label, color = Color.Gray, fontSize = 13.sp)
    }
}

@Composable
fun BookPostCard(post: ProfilePost) {
    var showMenu by remember { mutableStateOf(false) }

    Column(modifier = Modifier.padding(16.dp)) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = painterResource(id = R.drawable.come),
                    contentDescription = "User profile picture",
                    modifier = Modifier.size(40.dp).clip(CircleShape)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text("Simran Devkota", fontWeight = FontWeight.Bold)
                    Text("@simranreads · 2h", fontSize = 12.sp, color = Color.Gray)
                }
            }

            if (post.isOwner) {
                Box {
                    IconButton(onClick = { showMenu = true }) {
                        Icon(Icons.Outlined.MoreHoriz, contentDescription = "More options")
                    }
                    DropdownMenu(
                        expanded = showMenu,
                        onDismissRequest = { showMenu = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("Edit Post") },
                            onClick = { showMenu = false },
                            leadingIcon = { Icon(Icons.Outlined.Edit, contentDescription = null) }
                        )
                        DropdownMenuItem(
                            text = { Text("Delete Post") },
                            onClick = { showMenu = false },
                            leadingIcon = { Icon(Icons.Outlined.Delete, contentDescription = null) }
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Row(verticalAlignment = Alignment.Top) {
            Image(
                painter = painterResource(id = post.imageRes),
                contentDescription = post.bookTitle,
                contentScale = ContentScale.Crop,
                modifier = Modifier.width(80.dp).height(120.dp).clip(RoundedCornerShape(8.dp))
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(post.bookTitle, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Text(post.rating, fontSize = 14.sp)
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            "Why I recommend this book:",
            fontWeight = FontWeight.SemiBold,
            fontSize = 14.sp,
            color = Color.Black
        )
        Text(
            text = post.caption,
            fontSize = 14.sp,
            color = Color.DarkGray,
            lineHeight = 20.sp
        )

        Spacer(modifier = Modifier.height(12.dp))

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround) {
            ActionButton(icon = Icons.Outlined.BookmarkBorder, text = "Save")
            ActionButton(icon = Icons.Outlined.RateReview, text = "Review")
        }

        if (post.replies.isNotEmpty()) {
            Spacer(modifier = Modifier.height(16.dp))
            HorizontalDivider(color = Color.LightGray.copy(alpha = 0.5f))
            Spacer(modifier = Modifier.height(12.dp))

            post.replies.forEach { reply ->
                ReplyItem(user = reply.user, text = reply.text)
                Spacer(modifier = Modifier.height(8.dp))
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        HorizontalDivider(color = Color.LightGray.copy(alpha = 0.5f))
    }
}

@Composable
fun ActionButton(icon: ImageVector, text: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        modifier = Modifier.clickable { }
    ) {
        Icon(imageVector = icon, contentDescription = text, tint = purple)
        Text(text, color = purple, fontSize = 13.sp)
    }
}

@Composable
fun ReplyItem(user: String, text: String) {
    Row(Modifier.fillMaxWidth()) {
        Text(user, fontWeight = FontWeight.Bold, fontSize = 13.sp, color = purple)
        Spacer(Modifier.width(6.dp))
        Text(text, fontSize = 13.sp, color = Color.DarkGray)
    }
}

@Preview(showBackground = true)
@Composable
fun ProfileScreenPreview() {
    MaterialTheme {
        ProfileScreen()
    }
}
