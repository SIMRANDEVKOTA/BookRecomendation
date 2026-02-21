package com.example.bookrecommendation.model

import java.util.UUID

enum class BookStatus { READING, SAVED, FINISHED }

data class LibraryBook(
    val id: String = UUID.randomUUID().toString(),
    val title: String = "",
    val author: String = "",
    val imageRes: Int = 0,
    val totalPages: Int = 0,
    val currentPage: Int = 0,
    val rating: Int = 0,
    val review: String = "",
    val status: BookStatus = BookStatus.SAVED,
    val genre: String = "General"
)
