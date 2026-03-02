package com.example.bookrecommendation.view

import android.app.Activity
import android.content.Intent
import android.widget.Toast
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.bookrecommendation.R
import com.example.bookrecommendation.model.BookStatus
import com.example.bookrecommendation.model.LibraryBook
import com.example.bookrecommendation.ui.theme.brown
import com.example.bookrecommendation.ui.theme.purple
import com.example.bookrecommendation.viewmodel.LibraryViewModel
import com.example.bookrecommendation.viewmodel.UserViewModel
import com.google.firebase.auth.FirebaseAuth

@Composable
fun ProfileScreen(
    userViewModel: UserViewModel? = null,
    libraryViewModel: LibraryViewModel? = null,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val activity = context as? Activity
    var userName by remember { mutableStateOf("Simran Devkota") }
    var showEditDialog by remember { mutableStateOf(false) }

    // Fetch real books from LibraryViewModel
    val allBooks by libraryViewModel?.books?.collectAsState(initial = emptyList()) ?: remember { mutableStateOf(emptyList<LibraryBook>()) }
    val finishedBooks = allBooks.filter { it.status == BookStatus.FINISHED }

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
            ProfileHeader(
                name = userName,
                onEditClick = { showEditDialog = true },
                onLogoutClick = {
                    if (userViewModel != null) {
                        userViewModel.logout { success, message ->
                            if (success) {
                                val intent = Intent(context, LoginActivity::class.java)
                                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                context.startActivity(intent)
                                activity?.finish()
                            } else {
                                Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                            }
                        }
                    } else {
                        // Fallback for logic testing
                        val intent = Intent(context, LoginActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        context.startActivity(intent)
                        activity?.finish()
                    }
                },
                onDeleteAccountClick = {
                    val user = FirebaseAuth.getInstance().currentUser
                    user?.delete()?.addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(context, "Account Deleted", Toast.LENGTH_SHORT).show()
                            val intent = Intent(context, RegisterActivity::class.java)
                            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            context.startActivity(intent)
                            activity?.finish()
                        } else {
                            Toast.makeText(context, "Error: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            )
        }
        item {
            HorizontalDivider(color = Color.LightGray.copy(alpha = 0.5f))
            Text(
                "My Reviews",
                modifier = Modifier.padding(16.dp),
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
        }

        if (finishedBooks.isEmpty()) {
            item {
                Box(modifier = Modifier.fillMaxWidth().padding(40.dp), contentAlignment = Alignment.Center) {
                    Text(
                        "No reviews written by user",
                        color = Color.Gray,
                        textAlign = TextAlign.Center
                    )
                }
            }
        } else {
            items(finishedBooks) { book ->
                BookPostCardFromLibrary(book = book, userName = userName)
            }
        }
    }

    if (showEditDialog) {
        EditNameDialog(
            initialName = userName,
            onDismiss = { showEditDialog = false },
            onConfirm = { newName ->
                userName = newName
                showEditDialog = false
            }
        )
    }
}

@Composable
fun ProfileHeader(
    name: String,
    onEditClick: () -> Unit,
    onLogoutClick: () -> Unit,
    onDeleteAccountClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Image(
            painter = painterResource(id = R.drawable.come),
            contentDescription = "Profile Picture",
            contentScale = ContentScale.Crop,
            modifier = Modifier.size(90.dp).clip(CircleShape).background(Color.LightGray)
        )

        Text(name, fontWeight = FontWeight.Bold, fontSize = 20.sp)
        Text("@simranreads", color = Color.Gray, fontSize = 14.sp)

        Spacer(modifier = Modifier.height(4.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            ProfileStat("124", "Followers")
            ProfileStat("86", "Following")
            ProfileStat("18", "Recommended")
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Actions Row with Round Buttons
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(
                onClick = onEditClick,
                shape = RoundedCornerShape(20.dp),
                colors = ButtonDefaults.buttonColors(containerColor = brown),
                modifier = Modifier.padding(horizontal = 4.dp)
            ) {
                Icon(Icons.Outlined.Edit, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("Edit", fontSize = 12.sp)
            }

            Button(
                onClick = onLogoutClick,
                shape = RoundedCornerShape(20.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Gray),
                modifier = Modifier.padding(horizontal = 4.dp)
            ) {
                Icon(Icons.Outlined.Logout, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("Logout", fontSize = 12.sp)
            }

            Button(
                onClick = onDeleteAccountClick,
                shape = RoundedCornerShape(20.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFDC075E)),
                modifier = Modifier.padding(horizontal = 4.dp)
            ) {
                Icon(Icons.Outlined.Delete, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("Delete", fontSize = 12.sp)
            }
        }
    }
}

@Composable
fun EditNameDialog(initialName: String, onDismiss: () -> Unit, onConfirm: (String) -> Unit) {
    var name by remember { mutableStateOf(initialName) }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Edit Name") },
        text = {
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Name") },
                singleLine = true
            )
        },
        confirmButton = {
            Button(onClick = { onConfirm(name) }) { Text("Save") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}

@Composable
fun ProfileStat(count: String, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(count, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = purple)
        Text(label, color = Color.Gray, fontSize = 13.sp)
    }
}

@Composable
fun BookPostCardFromLibrary(book: LibraryBook, userName: String) {
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
                    Text(userName, fontWeight = FontWeight.Bold)
                    Text("@simranreads · Finished", fontSize = 12.sp, color = Color.Gray)
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Row(verticalAlignment = Alignment.Top) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(if (book.imageUrl.isNotBlank()) book.imageUrl else R.drawable.heart)
                    .crossfade(true)
                    .build(),
                contentDescription = book.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier.width(80.dp).height(120.dp).clip(RoundedCornerShape(8.dp))
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(book.title, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Row {
                    repeat(5) { index ->
                        Icon(
                            imageVector = Icons.Outlined.Star,
                            contentDescription = null,
                            tint = if (index < book.rating) Color(0xFFFFC107) else Color.LightGray,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            "My Review:",
            fontWeight = FontWeight.SemiBold,
            fontSize = 14.sp,
            color = Color.Black
        )
        Text(
            text = if (book.review.isNotBlank()) "\"${book.review}\"" else "No written review, just rated.",
            fontSize = 14.sp,
            color = Color.DarkGray,
            lineHeight = 20.sp,
            fontStyle = if (book.review.isNotBlank()) androidx.compose.ui.text.font.FontStyle.Italic else androidx.compose.ui.text.font.FontStyle.Normal
        )

        Spacer(modifier = Modifier.height(16.dp))
        HorizontalDivider(color = Color.LightGray.copy(alpha = 0.5f))
    }
}

@Preview(showBackground = true)
@Composable
fun ProfileScreenPreview() {
    ProfileScreen()
}
