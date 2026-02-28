package com.example.bookrecommendation

import com.example.bookrecommendation.model.LibraryBook
import com.example.bookrecommendation.model.BookStatus
import com.example.bookrecommendation.repository.LibraryRepo
import com.example.bookrecommendation.viewmodel.LibraryViewModel
import org.junit.Assert.assertTrue
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.doAnswer
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify

class LibraryViewModelTest {

    @Test
    fun addBook_success_test() {
        val repo = mock<LibraryRepo>()
        val viewModel = LibraryViewModel(repo)
        val book = LibraryBook(title = "Test Book", author = "Author", status = BookStatus.SAVED)

        doAnswer { invocation ->
            val callback = invocation.getArgument<(Boolean, String) -> Unit>(1)
            callback(true, "Book added successfully")
            null
        }.`when`(repo).addBook(any(), any())

        viewModel.addBook(book)

        verify(repo).addBook(any(), any())
    }

    @Test
    fun updateBook_success_test() {
        val repo = mock<LibraryRepo>()
        val viewModel = LibraryViewModel(repo)
        val book = LibraryBook(title = "Updated Book", author = "Author", status = BookStatus.READING)

        doAnswer { invocation ->
            val callback = invocation.getArgument<(Boolean, String) -> Unit>(1)
            callback(true, "Book updated successfully")
            null
        }.`when`(repo).updateBook(any(), any())

        viewModel.updateBook(book)

        verify(repo).updateBook(any(), any())
    }

    @Test
    fun deleteBook_success_test() {
        val repo = mock<LibraryRepo>()
        val viewModel = LibraryViewModel(repo)
        val bookId = "test-id"

        doAnswer { invocation ->
            val callback = invocation.getArgument<(Boolean, String) -> Unit>(1)
            callback(true, "Book deleted successfully")
            null
        }.`when`(repo).deleteBook(any(), any())

        viewModel.deleteBook(bookId)

        verify(repo).deleteBook(any(), any())
    }
}
