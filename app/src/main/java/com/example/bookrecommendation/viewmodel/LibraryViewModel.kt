package com.example.bookrecommendation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookrecommendation.model.LibraryBook
import com.example.bookrecommendation.repository.LibraryRepo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class LibraryViewModel(private val repo: LibraryRepo) : ViewModel() {
    private val _books = MutableStateFlow<List<LibraryBook>>(emptyList())
    val books: StateFlow<List<LibraryBook>> = _books

    init {
        loadBooks()
    }

    private fun loadBooks() {
        repo.getAllBooks { bookList ->
            _books.value = bookList
        }
    }

    fun addBook(book: LibraryBook) {
        repo.addBook(book) { success, _ ->
            // Update handled by listener in RepoImpl
        }
    }

    fun updateBook(book: LibraryBook) {
        repo.updateBook(book) { success, _ -> }
    }

    fun deleteBook(bookId: String) {
        repo.deleteBook(bookId) { success, _ -> }
    }
}
