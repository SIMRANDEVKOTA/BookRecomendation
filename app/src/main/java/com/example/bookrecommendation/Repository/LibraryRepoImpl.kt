package com.example.bookrecommendation.repository

import com.example.bookrecommendation.model.LibraryBook
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class LibraryRepoImpl : LibraryRepo {
    private val auth = FirebaseAuth.getInstance()
    private val database = FirebaseDatabase.getInstance().getReference("library")

    override fun getAllBooks(callback: (List<LibraryBook>) -> Unit) {
        val userId = auth.currentUser?.uid
        if (userId == null) {
            callback(emptyList())
            return
        }

        database.child(userId).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val books = mutableListOf<LibraryBook>()
                for (child in snapshot.children) {
                    val book = child.getValue(LibraryBook::class.java)
                    if (book != null) {
                        books.add(book)
                    }
                }
                callback(books)
            }

            override fun onCancelled(error: DatabaseError) {
                callback(emptyList())
            }
        })
    }

    override fun addBook(book: LibraryBook, callback: (Boolean, String) -> Unit) {
        val userId = auth.currentUser?.uid
        if (userId == null) {
            callback(false, "User not logged in")
            return
        }

        database.child(userId).child(book.id).setValue(book)
            .addOnSuccessListener { callback(true, "Book added successfully") }
            .addOnFailureListener { e -> callback(false, e.message ?: "Failed to add book") }
    }

    override fun updateBook(book: LibraryBook, callback: (Boolean, String) -> Unit) {
        val userId = auth.currentUser?.uid
        if (userId == null) {
            callback(false, "User not logged in")
            return
        }

        database.child(userId).child(book.id).setValue(book)
            .addOnSuccessListener { callback(true, "Book updated successfully") }
            .addOnFailureListener { e -> callback(false, e.message ?: "Failed to update book") }
    }

    override fun deleteBook(bookId: String, callback: (Boolean, String) -> Unit) {
        val userId = auth.currentUser?.uid
        if (userId == null) {
            callback(false, "User not logged in")
            return
        }

        database.child(userId).child(bookId).removeValue()
            .addOnSuccessListener { callback(true, "Book deleted successfully") }
            .addOnFailureListener { e -> callback(false, e.message ?: "Failed to delete book") }
    }
}
