package com.example.bookrecommendation.view

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BookmarkAdd
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bookrecommendation.R
import com.example.bookrecommendation.model.BookStatus
import com.example.bookrecommendation.model.LibraryBook
import com.example.bookrecommendation.ui.theme.purple
import com.example.bookrecommendation.viewmodel.LibraryViewModel

// Unique name for Home screen data
data class HomeBook(
    val id: Int,
    val image: Int,
    val title: String,
    var rating: Double,
    val author: String = "Unknown Author",
    val genre: String = "Fiction"
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Home(viewModel: LibraryViewModel) {
    val context = LocalContext.current
    var selectedBook by remember { mutableStateOf<HomeBook?>(null) }
    
    val books = listOf(
        HomeBook(0, R.drawable.come, "Comeback", 4.2, "Author Name", "Self-Help"),
        HomeBook(1, R.drawable.envy, "King of Envy", 4.8, "Ana Huang", "Romance"),
        HomeBook(2, R.drawable.the, "The Striker", 4.2, "Ana Huang", "Romance"),
        HomeBook(3, R.drawable.wrath, "King of Wrath", 4.9, "Ana Huang", "Romance"),
        HomeBook(4, R.drawable.heart, "Heart Still Beats", 4.7, "Author Name", "Mystery")
    )

    val categories = listOf("Fiction", "Self-Help", "Mystery", "Fantasy")

    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
        ) {
            // Header
            item {
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedTextField(
                    value = "",
                    onValueChange = { },
                    placeholder = { Text("Search your interest...") },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search",
                            tint = Color.DarkGray
                        )
                    },
                    shape = RoundedCornerShape(50),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color(0xFFF5F5F5),
                        unfocusedContainerColor = Color(0xFFF5F5F5),
                        focusedBorderColor = purple,
                        unfocusedBorderColor = Color.Gray,
                        cursorColor = purple
                    )
                )
                Spacer(modifier = Modifier.height(16.dp))
            }

            // Banner
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    shape = RoundedCornerShape(16.dp),
                    elevation = CardDefaults.cardElevation(5.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.heart),
                        contentDescription = "Featured Book Banner",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(180.dp)
                            .background(Color.LightGray)
                    )
                }
            }

            // Categories
            item { SectionHeader(title = "Categories") }
            item {
                LazyRow(modifier = Modifier.padding(horizontal = 10.dp)) {
                    items(categories.size) { index ->
                        CategoryItem(name = categories[index])
                    }
                }
            }

            // Trending
            item { SectionHeader(title = "Trending in NextRead") }
            item {
                LazyRow(modifier = Modifier.padding(horizontal = 10.dp)) {
                    items(books) { book ->
                        BookItem(
                            book = book,
                            size = 120.dp,
                            onClick = { selectedBook = book },
                            onAddClick = {
                                viewModel.addBook(LibraryBook(
                                    title = book.title,
                                    author = book.author,
                                    genre = book.genre,
                                    imageRes = book.image,
                                    status = BookStatus.SAVED
                                ))
                                Toast.makeText(context, "${book.title} added to Saved", Toast.LENGTH_SHORT).show()
                            }
                        )
                    }
                }
            }

            // Recommended
            item { SectionHeader(title = "Recommended For You") }
            item {
                LazyRow(modifier = Modifier.padding(horizontal = 10.dp, vertical = 10.dp)) {
                    items(books.reversed()) { book ->
                        BookItem(
                            book = book,
                            size = 140.dp,
                            onClick = { selectedBook = book },
                            onAddClick = {
                                viewModel.addBook(LibraryBook(
                                    title = book.title,
                                    author = book.author,
                                    genre = book.genre,
                                    imageRes = book.image,
                                    status = BookStatus.SAVED
                                ))
                                Toast.makeText(context, "${book.title} added to Saved", Toast.LENGTH_SHORT).show()
                            }
                        )
                    }
                }
            }
        }

        // Click Dialog for Book
        selectedBook?.let { book ->
            AlertDialog(
                onDismissRequest = { selectedBook = null },
                title = { Text(book.title) },
                text = { Text("Do you want to add this book to your library?") },
                confirmButton = {
                    Button(
                        onClick = {
                            viewModel.addBook(LibraryBook(
                                title = book.title,
                                author = book.author,
                                genre = book.genre,
                                imageRes = book.image,
                                status = BookStatus.SAVED
                            ))
                            Toast.makeText(context, "${book.title} added to Saved", Toast.LENGTH_SHORT).show()
                            selectedBook = null
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = purple)
                    ) {
                        Text("Add to Library")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { selectedBook = null }) {
                        Text("Cancel")
                    }
                }
            )
        }
    }
}

// --- HELPER COMPOSABLES ---

@Composable
fun SectionHeader(title: String) {
    Text(
        text = title,
        modifier = Modifier.padding(start = 16.dp, top = 16.dp, bottom = 8.dp),
        fontWeight = FontWeight.Bold,
        fontSize = 20.sp,
        color = Color.Black
    )
}

@Composable
fun CategoryItem(name: String) {
    Card(
        modifier = Modifier.padding(end = 10.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF0E6E6)),
        shape = RoundedCornerShape(50)
    ) {
        Text(
            text = name,
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 10.dp),
            fontWeight = FontWeight.Medium,
            color = Color(0xFFDC075E)
        )
    }
}

@Composable
fun BookItem(
    book: HomeBook,
    size: Dp,
    onClick: () -> Unit,
    onAddClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .padding(end = 16.dp)
            .padding(bottom = 10.dp)
            .width(size)
            .clickable { onClick() },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Card(
            shape = RoundedCornerShape(12.dp),
            elevation = CardDefaults.cardElevation(4.dp)
        ) {
            Image(
                painter = painterResource(book.image),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(width = size, height = (size * 1.5f))
                    .background(Color.Gray)
            )
        }
        Text(
            text = book.title,
            fontWeight = FontWeight.SemiBold,
            fontSize = 14.sp,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .padding(top = 8.dp)
                .fillMaxWidth()
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = "Rating",
                    tint = Color(0xFFFFC107),
                    modifier = Modifier.size(16.dp)
                )
                Text(
                    text = "${book.rating}",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(start = 4.dp)
                )
            }

            Icon(
                imageVector = Icons.Default.BookmarkAdd,
                contentDescription = "Add to list",
                tint = purple,
                modifier = Modifier
                    .size(20.dp)
                    .clickable { onAddClick() }
            )
        }
    }
}
