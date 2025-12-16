package com.example.bookrecommendation.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bookrecommendation.R
import com.example.bookrecommendation.ui.theme.purple

// Unique name for Search data
data class SearchBook(val image: Int, val title: String)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(modifier: Modifier = Modifier) {

    var searchText by remember { mutableStateOf("") }

    val recentSearches = listOf("King of Envy", "Self-Help", "Ana Huang")
    val popularGenres = listOf("Romance", "Fantasy", "Mystery", "Self-Help", "Thriller", "Sci-Fi")

    // Suggested books
    val suggestedBooks = listOf(
        SearchBook(R.drawable.come, "Comeback"),
        SearchBook(R.drawable.envy, "King of Envy"),
        SearchBook(R.drawable.wrath, "King of Wrath"),
        SearchBook(R.drawable.the, "The Striker")
    )

    // Main Container
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {

        // Title
        Text(
            text = "Search",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Search Bar
        OutlinedTextField(
            value = searchText,
            onValueChange = { searchText = it },
            placeholder = { Text("Search books, authors, genres") },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Outlined.Search,
                    contentDescription = "Search"
                )
            },
            shape = RoundedCornerShape(50),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = Color(0xFF756E6E),
                unfocusedContainerColor = Color(0xFFF5F5F5),
                focusedBorderColor = Color.Transparent,
                unfocusedBorderColor = Color.Gray,
                cursorColor = purple
            )
        )

        Spacer(modifier = Modifier.height(24.dp))

        // --- 1. RECENT SEARCHES ---
        Text(
            text = "Recent searches",
            fontWeight = FontWeight.SemiBold,
            fontSize = 18.sp
        )

        Spacer(modifier = Modifier.height(12.dp))

        LazyRow {
            items(recentSearches.size) { index ->
                SearchChip(text = recentSearches[index])
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // --- 2. SUGGESTED FOR YOU (With Images) ---
        Text(
            text = "Suggested for you",
            fontWeight = FontWeight.SemiBold,
            fontSize = 18.sp
        )

        Spacer(modifier = Modifier.height(12.dp))

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(suggestedBooks.size) { index ->
                SuggestedBookCard(
                    imageRes = suggestedBooks[index].image,
                    title = suggestedBooks[index].title
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // --- 3. POPULAR SEARCHES ---
        Text(
            text = "Popular searches in Next Read",
            fontWeight = FontWeight.SemiBold,
            fontSize = 18.sp
        )

        Spacer(modifier = Modifier.height(12.dp))

        popularGenres.forEach { genre ->
            Column {
                Text(
                    text = genre,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { searchText = genre }
                        .padding(vertical = 12.dp),
                    fontSize = 16.sp
                )
                HorizontalDivider(color = Color.LightGray.copy(alpha = 0.5f))
            }
        }
    }
}

@Composable
fun SuggestedBookCard(imageRes: Int, title: String) {
    Column(
        modifier = Modifier
            .width(100.dp)
            .clickable { },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Book Image Card
        Card(
            modifier = Modifier
                .height(150.dp)
                .fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Image(
                painter = painterResource(id = imageRes),
                contentDescription = title,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Book Title (Below image)
        Text(
            text = title,
            fontWeight = FontWeight.Medium,
            fontSize = 14.sp,
            color = Color.Black,
            maxLines = 1,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
fun SearchChip(text: String) {
    Card(
        modifier = Modifier
            .padding(end = 10.dp)
            .clickable { },
        shape = RoundedCornerShape(50),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF3E5F5))
    ) {

        Text(
            text = text,
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 10.dp),
            fontWeight = FontWeight.Medium,
            color = Color(0xFFDC075E)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun SearchPreview() {
    SearchScreen()
}
