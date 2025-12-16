package com.example.bookrecommendation.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bookrecommendation.R
import com.example.bookrecommendation.ui.theme.purple
import com.example.bookrecommendation.ui.theme.brown
import com.example.bookrecommendation.ui.theme.pink

// Unique names for Library data
data class LibraryBook(
    val title: String,
    val author: String,
    val imageRes: Int,
    val totalPages: Int = 0,
    val currentPage: Int = 0,
    val rating: Int = 0,
    val review: String = ""
)

data class LibraryGenreSection(val genre: String, val books: List<LibraryBook>)

@Composable
fun LibraryScreen(modifier: Modifier = Modifier) {

    var selectedTab by remember { mutableIntStateOf(0) }
    val tabs = listOf("Reading", "Saved", "Finished")

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(Color(0xFFF8F5FF), Color.White)
                )
            )
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
            0 -> ReadingTab()
            1 -> SavedTab()
            2 -> FinishedTab()
        }
    }
}

@Composable
fun LibraryHeader() {
    Column {
        Text("My Library", fontSize = 28.sp, fontWeight = FontWeight.ExtraBold, color = Color.Black)

    }
}

@Composable
fun ReadingTab() {
    val readingBooks = remember {
        mutableStateListOf(
            LibraryBook("King of Wrath", "Ana Huang", R.drawable.wrath, 400, 145),
            LibraryBook("Comeback", "Author Name", R.drawable.come, 350, 50)
        )
    }

    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(bottom = 20.dp)
    ) {
        items(readingBooks) { book ->
            var currentPage by remember { mutableIntStateOf(book.currentPage) }
            ReadingBookCard(
                book = book.copy(currentPage = currentPage),
                onProgressUpdate = { newPage -> currentPage = newPage },
                onFinish = { currentPage = book.totalPages }
            )
        }
    }
}

@Composable
fun ReadingBookCard(book: LibraryBook, onProgressUpdate: (Int) -> Unit, onFinish: () -> Unit) {
    val progress = book.currentPage.toFloat() / book.totalPages.toFloat()

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(6.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row {
                Image(
                    painter = painterResource(id = book.imageRes),
                    contentDescription = book.title,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.width(70.dp).height(110.dp).clip(RoundedCornerShape(8.dp)).background(Color.Gray)
                )
                Spacer(modifier = Modifier.width(16.dp))
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(book.title, fontWeight = FontWeight.Bold, fontSize = 18.sp)
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
                        Text(text = "${(progress * 100).toInt()}%", color = Color.Black, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        Text(text = "${book.currentPage} / ${book.totalPages} pages", color = Color.Gray, fontSize = 12.sp)
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            HorizontalDivider(color = Color(0xFFF0F0F0))
            Spacer(modifier = Modifier.height(12.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                OutlinedButton(onClick = { }, shape = RoundedCornerShape(8.dp), modifier = Modifier.weight(1f)) {
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
}

@Composable
fun SavedTab() {
    val savedLibrary = listOf(
        LibraryGenreSection("Romance", listOf(
            LibraryBook(title = "King of Envy", author = "Ana Huang", imageRes = R.drawable.envy),
            LibraryBook(title = "King of Wrath", author = "Ana Huang", imageRes = R.drawable.wrath)
        )),
        LibraryGenreSection("Self-Help", listOf(
            LibraryBook(title = "Atomic Habits", author = "James Clear", imageRes = R.drawable.come),
            LibraryBook(title = "Deep Work", author = "Cal Newport", imageRes = R.drawable.heart)
        ))
    )

    LazyColumn(verticalArrangement = Arrangement.spacedBy(24.dp), contentPadding = PaddingValues(bottom = 20.dp)) {
        items(savedLibrary) { section ->
            Column {
                Text(section.genre, fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                Spacer(modifier = Modifier.height(12.dp))
                LazyRow(horizontalArrangement = Arrangement.spacedBy(16.dp), contentPadding = PaddingValues(end = 16.dp)) {
                    items(section.books) { book ->
                        Box(modifier = Modifier.width(300.dp)) { SavedBookCard(book = book) }
                    }
                }
            }
        }
    }
}

@Composable
fun SavedBookCard(book: LibraryBook) {
    Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp), elevation = CardDefaults.cardElevation(6.dp), colors = CardDefaults.cardColors(containerColor = Color.White)) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.Top) {
                Image(painter = painterResource(id = book.imageRes), contentDescription = null, contentScale = ContentScale.Crop, modifier = Modifier.width(70.dp).height(105.dp).clip(RoundedCornerShape(8.dp)).background(Color.Gray))
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text(book.title, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(book.author, fontSize = 14.sp, color = Color.Gray)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Saved on: Dec 12, 2024", fontSize = 12.sp, color = Color.LightGray)
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            HorizontalDivider(color = Color(0xFFF0F0F0))
            Spacer(modifier = Modifier.height(12.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                TextButton(onClick = { }, modifier = Modifier.height(40.dp)) {
                    Icon(Icons.Default.Delete, contentDescription = "Remove", tint = Color.Gray, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Remove", color = Color.Gray)
                }
                Button(onClick = { }, colors = ButtonDefaults.buttonColors(containerColor = brown), shape = RoundedCornerShape(8.dp), modifier = Modifier.height(40.dp)) {
                    Icon(Icons.Default.PlayArrow, contentDescription = "Start Reading", modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Start Reading")
                }
            }
        }
    }
}

@Composable
fun FinishedTab() {
    LazyColumn(verticalArrangement = Arrangement.spacedBy(20.dp), contentPadding = PaddingValues(bottom = 20.dp)) {
        items(2) {
            FinishedBookCard(book = LibraryBook(title = "Heart Still Beats", author = "Author Name", imageRes = R.drawable.heart, rating = 5, review = "Dark, gripping and unforgettable. I couldn't put it down."))
        }
    }
}

@Composable
fun FinishedBookCard(book: LibraryBook) {
    Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp), elevation = CardDefaults.cardElevation(4.dp), colors = CardDefaults.cardColors(containerColor = Color.White)) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row {
                Image(painter = painterResource(id = book.imageRes), contentDescription = null, contentScale = ContentScale.Crop, modifier = Modifier.width(70.dp).height(105.dp).clip(RoundedCornerShape(8.dp)).background(Color.Gray))
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text(book.title, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text("Finished on: Dec 10, 2024", fontSize = 12.sp, color = Color.Gray)
                    Spacer(modifier = Modifier.height(8.dp))
                    Row { repeat(5) { index -> Icon(imageVector = Icons.Default.Star, contentDescription = null, tint = if (index < book.rating) Color(0xFFFFC107) else Color.LightGray, modifier = Modifier.size(20.dp)) } }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Box(modifier = Modifier.fillMaxWidth().background(Color(0xFFF9F9F9), RoundedCornerShape(8.dp)).padding(12.dp)) {
                Column {
                    Text("My Review:", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = brown)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(text = "\"${book.review}\"", fontStyle = FontStyle.Italic, fontSize = 14.sp, color = Color.DarkGray, lineHeight = 20.sp)
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                OutlinedButton(onClick = { }, shape = RoundedCornerShape(8.dp), modifier = Modifier.weight(1f)) {
                    Icon(Icons.Default.Edit, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Edit", fontSize = 12.sp, maxLines = 1)
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
}

@Preview(showBackground = true)
@Composable
fun LibraryPreview() {
    LibraryScreen()
}
