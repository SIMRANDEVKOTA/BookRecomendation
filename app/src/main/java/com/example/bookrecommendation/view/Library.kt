package com.example.bookrecommendation.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.bookrecommendation.model.BookStatus
import com.example.bookrecommendation.model.LibraryBook
import com.example.bookrecommendation.ui.theme.purple
import com.example.bookrecommendation.ui.theme.brown
import com.example.bookrecommendation.ui.theme.pink
import com.example.bookrecommendation.viewmodel.LibraryViewModel

// Fallback Cloudinary image shown when a book has no imageUrl set
private const val FALLBACK_IMAGE_URL =
    "https://res.cloudinary.com/YOUR_CLOUD_NAME/image/upload/f_auto,q_auto,w_300/books/default_cover"

private fun bookImageUrl(imageUrl: String) =
    imageUrl.ifBlank { FALLBACK_IMAGE_URL }

@Composable
fun LibraryScreen(viewModel: LibraryViewModel) {
    var selectedTab by remember { mutableIntStateOf(0) }
    val tabs = listOf("Reading", "Saved", "Finished")
    val allBooks by viewModel.books.collectAsState()
    var showAddDialog by remember { mutableStateOf(false) }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showAddDialog = true },
                containerColor = brown,
                contentColor = Color.White
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Book")
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Brush.verticalGradient(listOf(Color(0xFFF8F5FF), Color.White)))
                .padding(16.dp)
        ) {
            LibraryHeader()
            Spacer(modifier = Modifier.height(16.dp))

            TabRow(
                selectedTabIndex = selectedTab,
                containerColor = Color.Transparent,
                contentColor = purple,
                indicator = { tabPositions ->
                    if (selectedTab < tabPositions.size) {
                        TabRowDefaults.SecondaryIndicator(
                            Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                            color = pink,
                            height = 3.dp
                        )
                    }
                },
                divider = { }
            ) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTab == index,
                        onClick = { selectedTab = index },
                        text = {
                            Text(
                                title,
                                fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Normal,
                                fontSize = 16.sp
                            )
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            when (selectedTab) {
                0 -> ReadingTab(
                    books = allBooks.filter { it.status == BookStatus.READING },
                    onUpdate = { viewModel.updateBook(it) },
                    onDelete = { viewModel.deleteBook(it.id) }
                )
                1 -> SavedTab(
                    books = allBooks.filter { it.status == BookStatus.SAVED },
                    onUpdate = { viewModel.updateBook(it) },
                    onDelete = { viewModel.deleteBook(it.id) }
                )
                2 -> FinishedTab(
                    books = allBooks.filter { it.status == BookStatus.FINISHED },
                    onUpdate = { viewModel.updateBook(it) },
                    onDelete = { viewModel.deleteBook(it.id) }
                )
            }
        }
    }

    if (showAddDialog) {
        AddBookDialog(
            onDismiss = { showAddDialog = false },
            onConfirm = { newBook ->
                viewModel.addBook(newBook)
                showAddDialog = false
            }
        )
    }
}

@Composable
fun LibraryHeader() {
    Text("My Library", fontSize = 28.sp, fontWeight = FontWeight.ExtraBold, color = Color.Black)
}

@Composable
fun ReadingTab(books: List<LibraryBook>, onUpdate: (LibraryBook) -> Unit, onDelete: (LibraryBook) -> Unit) {
    LazyColumn(verticalArrangement = Arrangement.spacedBy(16.dp), contentPadding = PaddingValues(bottom = 80.dp)) {
        items(books, key = { it.id }) { book ->
            ReadingBookCard(
                book = book,
                onUpdateProgress = { newPage -> onUpdate(book.copy(currentPage = newPage)) },
                onFinish = { onUpdate(book.copy(status = BookStatus.FINISHED, currentPage = book.totalPages)) },
                onDelete = { onDelete(book) }
            )
        }
    }
}

@Composable
fun ReadingBookCard(
    book: LibraryBook,
    onUpdateProgress: (Int) -> Unit,
    onFinish: () -> Unit,
    onDelete: () -> Unit
) {
    val progress = if (book.totalPages > 0) book.currentPage.toFloat() / book.totalPages.toFloat() else 0f
    var showEditProgress by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(6.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row {
                AsyncImage(
                    model = bookImageUrl(book.imageUrl),
                    contentDescription = book.title,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .width(70.dp)
                        .height(110.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color.Gray)
                )
                Spacer(modifier = Modifier.width(16.dp))
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(book.title, fontWeight = FontWeight.Bold, fontSize = 18.sp, modifier = Modifier.weight(1f))
                        IconButton(onClick = onDelete, modifier = Modifier.size(24.dp)) {
                            Icon(Icons.Default.Delete, contentDescription = "Delete", tint = Color.Gray)
                        }
                    }
                    Text(book.author, color = Color.Gray, fontSize = 14.sp)
                    Spacer(modifier = Modifier.height(12.dp))
                    LinearProgressIndicator(
                        progress = { progress },
                        modifier = Modifier.fillMaxWidth().height(6.dp).clip(RoundedCornerShape(4.dp)),
                        color = brown,
                        trackColor = Color(0xFFF0F0F0)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("${(progress * 100).toInt()}%", color = Color.Black, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        Text("${book.currentPage} / ${book.totalPages} pages", color = Color.Gray, fontSize = 12.sp)
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            HorizontalDivider(color = Color(0xFFF0F0F0))
            Spacer(modifier = Modifier.height(12.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                OutlinedButton(onClick = { showEditProgress = true }, shape = RoundedCornerShape(8.dp), modifier = Modifier.weight(1f)) {
                    Icon(Icons.Default.Edit, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Update Progress", fontSize = 12.sp)
                }
                Spacer(modifier = Modifier.width(8.dp))
                Button(onClick = onFinish, colors = ButtonDefaults.buttonColors(containerColor = brown), shape = RoundedCornerShape(8.dp), modifier = Modifier.weight(1f)) {
                    Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Finish Book", fontSize = 12.sp)
                }
            }
        }
    }

    if (showEditProgress) {
        UpdateProgressDialog(
            current = book.currentPage,
            total = book.totalPages,
            onDismiss = { showEditProgress = false },
            onUpdate = { newPage ->
                onUpdateProgress(newPage)
                showEditProgress = false
            }
        )
    }
}

@Composable
fun SavedTab(books: List<LibraryBook>, onUpdate: (LibraryBook) -> Unit, onDelete: (LibraryBook) -> Unit) {
    val grouped = books.groupBy { it.genre }
    LazyColumn(verticalArrangement = Arrangement.spacedBy(24.dp), contentPadding = PaddingValues(bottom = 80.dp)) {
        items(grouped.keys.toList()) { genre ->
            Column {
                Text(genre, fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                Spacer(modifier = Modifier.height(12.dp))
                LazyRow(horizontalArrangement = Arrangement.spacedBy(16.dp), contentPadding = PaddingValues(end = 16.dp)) {
                    items(grouped[genre] ?: emptyList(), key = { it.id }) { book ->
                        Box(modifier = Modifier.width(300.dp)) {
                            SavedBookCard(
                                book = book,
                                onStartReading = {
                                    onUpdate(book.copy(status = BookStatus.READING, totalPages = if (book.totalPages == 0) 300 else book.totalPages))
                                },
                                onDelete = { onDelete(book) }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SavedBookCard(book: LibraryBook, onStartReading: () -> Unit, onDelete: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(6.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.Top) {
                AsyncImage(
                    model = bookImageUrl(book.imageUrl),
                    contentDescription = book.title,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .width(70.dp)
                        .height(105.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color.Gray)
                )
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text(book.title, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(book.author, fontSize = 14.sp, color = Color.Gray)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Saved in: ${book.genre}", fontSize = 12.sp, color = Color.LightGray)
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            HorizontalDivider(color = Color(0xFFF0F0F0))
            Spacer(modifier = Modifier.height(12.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                TextButton(onClick = onDelete, modifier = Modifier.height(40.dp)) {
                    Icon(Icons.Default.Delete, contentDescription = "Remove", tint = Color.Gray, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Remove", color = Color.Gray)
                }
                Button(onClick = onStartReading, colors = ButtonDefaults.buttonColors(containerColor = brown), shape = RoundedCornerShape(8.dp), modifier = Modifier.height(40.dp)) {
                    Icon(Icons.Default.PlayArrow, contentDescription = "Start Reading", modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Start Reading")
                }
            }
        }
    }
}

@Composable
fun FinishedTab(books: List<LibraryBook>, onUpdate: (LibraryBook) -> Unit, onDelete: (LibraryBook) -> Unit) {
    LazyColumn(verticalArrangement = Arrangement.spacedBy(20.dp), contentPadding = PaddingValues(bottom = 80.dp)) {
        items(books, key = { it.id }) { book ->
            FinishedBookCard(
                book = book,
                onDelete = { onDelete(book) },
                onEditReview = { updatedBook -> onUpdate(updatedBook) }
            )
        }
    }
}

@Composable
fun FinishedBookCard(book: LibraryBook, onDelete: () -> Unit, onEditReview: (LibraryBook) -> Unit) {
    var showReviewDialog by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row {
                AsyncImage(
                    model = bookImageUrl(book.imageUrl),
                    contentDescription = book.title,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .width(70.dp)
                        .height(105.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color.Gray)
                )
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(book.title, fontSize = 18.sp, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
                        IconButton(onClick = onDelete, modifier = Modifier.size(24.dp)) {
                            Icon(Icons.Default.Delete, contentDescription = "Delete", tint = Color.Gray)
                        }
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text("Finished recently", fontSize = 12.sp, color = Color.Gray)
                    Spacer(modifier = Modifier.height(8.dp))
                    Row {
                        repeat(5) { index ->
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = null,
                                tint = if (index < book.rating) Color(0xFFFFC107) else Color.LightGray,
                                modifier = Modifier
                                    .size(20.dp)
                                    .clickable { onEditReview(book.copy(rating = index + 1)) }
                            )
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFF9F9F9), RoundedCornerShape(8.dp))
                    .padding(12.dp)
            ) {
                Column {
                    Text("My Review:", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = brown)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text("\"${book.review}\"", fontStyle = FontStyle.Italic, fontSize = 14.sp, color = Color.DarkGray, lineHeight = 20.sp)
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                OutlinedButton(onClick = { showReviewDialog = true }, shape = RoundedCornerShape(8.dp), modifier = Modifier.weight(1f)) {
                    Icon(Icons.Default.Edit, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Edit Review", fontSize = 12.sp, maxLines = 1)
                }
                Spacer(modifier = Modifier.width(8.dp))
                Button(onClick = { }, colors = ButtonDefaults.buttonColors(containerColor = brown), shape = RoundedCornerShape(8.dp), modifier = Modifier.weight(1.2f)) {
                    Icon(Icons.Default.Share, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Recommend", fontSize = 12.sp, maxLines = 1)
                }
            }
        }
    }

    if (showReviewDialog) {
        ReviewDialog(
            initialReview = book.review,
            initialRating = book.rating,
            onDismiss = { showReviewDialog = false },
            onConfirm = { review, rating ->
                onEditReview(book.copy(review = review, rating = rating))
                showReviewDialog = false
            }
        )
    }
}

@Composable
fun ReviewDialog(initialReview: String, initialRating: Int, onDismiss: () -> Unit, onConfirm: (String, Int) -> Unit) {
    var review by remember { mutableStateOf(initialReview) }
    var rating by remember { mutableIntStateOf(initialRating) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Write a Review") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                    repeat(5) { index ->
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = null,
                            tint = if (index < rating) Color(0xFFFFC107) else Color.LightGray,
                            modifier = Modifier.size(32.dp).clickable { rating = index + 1 }
                        )
                    }
                }
                OutlinedTextField(value = review, onValueChange = { review = it }, label = { Text("Your Review") }, modifier = Modifier.fillMaxWidth(), minLines = 3)
            }
        },
        confirmButton = { Button(onClick = { onConfirm(review, rating) }) { Text("Save") } },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancel") } }
    )
}

@Composable
fun AddBookDialog(onDismiss: () -> Unit, onConfirm: (LibraryBook) -> Unit) {
    var title by remember { mutableStateOf("") }
    var author by remember { mutableStateOf("") }
    var genre by remember { mutableStateOf("") }
    var totalPages by remember { mutableStateOf("300") }
    var imageUrl by remember { mutableStateOf("") }  // Added: Cloudinary URL input

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add New Book to Saved") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(value = title, onValueChange = { title = it }, label = { Text("Title") })
                OutlinedTextField(value = author, onValueChange = { author = it }, label = { Text("Author") })
                OutlinedTextField(value = genre, onValueChange = { genre = it }, label = { Text("Genre") })
                OutlinedTextField(
                    value = totalPages,
                    onValueChange = { totalPages = it },
                    label = { Text("Total Pages") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
                OutlinedTextField(
                    value = imageUrl,
                    onValueChange = { imageUrl = it },
                    label = { Text("Image URL (Cloudinary)") }
                )
            }
        },
        confirmButton = {
            Button(onClick = {
                if (title.isNotBlank()) {
                    onConfirm(LibraryBook(
                        title = title,
                        author = author,
                        genre = genre,
                        imageUrl = imageUrl,
                        totalPages = totalPages.toIntOrNull() ?: 300,
                        status = BookStatus.SAVED
                    ))
                }
            }) { Text("Add") }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancel") } }
    )
}

@Composable
fun UpdateProgressDialog(current: Int, total: Int, onDismiss: () -> Unit, onUpdate: (Int) -> Unit) {
    var newPage by remember { mutableStateOf(current.toString()) }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Update Progress") },
        text = {
            Column {
                Text("Total Pages: $total", fontSize = 14.sp)
                OutlinedTextField(
                    value = newPage,
                    onValueChange = { newPage = it },
                    label = { Text("Current Page") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
            }
        },
        confirmButton = {
            Button(onClick = {
                val page = newPage.toIntOrNull() ?: current
                onUpdate(page.coerceIn(0, total))
            }) { Text("Save") }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancel") } }
    )
}