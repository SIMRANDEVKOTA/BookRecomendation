package com.example.bookrecommendation.repository

import com.example.bookrecommendation.model.LibraryBook

interface LibraryRepo {
    fun getAllBooks(callback: (List<LibraryBook>) -> Unit)
    fun addBook(book: LibraryBook, callback: (Boolean, String) -> Unit)
    fun updateBook(book: LibraryBook, callback: (Boolean, String) -> Unit)
    fun deleteBook(bookId: String, callback: (Boolean, String) -> Unit)
}
