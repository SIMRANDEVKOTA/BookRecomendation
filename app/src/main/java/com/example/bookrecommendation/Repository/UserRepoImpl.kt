package com.example.bookrecommendation.repository

import com.example.bookrecommendation.model.UserModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class UserRepoImpl : UserRepo {

    private val auth = FirebaseAuth.getInstance()
    private val database = FirebaseDatabase.getInstance().getReference("users")

    override fun register(user: UserModel, callback: (Boolean, String) -> Unit) {
        if (user.email.isEmpty() || user.password.isEmpty()) {
            callback(false, "Email and password cannot be empty")
            return
        }

        auth.createUserWithEmailAndPassword(user.email, user.password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // After successful authentication, save the rest of the user data
                    val firebaseUser = auth.currentUser
                    firebaseUser?.let {
                        database.child(it.uid).setValue(user)
                            .addOnSuccessListener {
                                callback(true, "Registration successful")
                            }
                            .addOnFailureListener { e ->
                                callback(false, "Failed to save user data: ${e.message}")
                            }
                    }
                } else {
                    callback(false, "Registration failed: ${task.exception?.message}")
                }
            }
    }

    override fun login(email: String, password: String, callback: (Boolean, String) -> Unit) {
        if (email.isEmpty() || password.isEmpty()) {
            callback(false, "Email and password cannot be empty")
            return
        }

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    callback(true, "Login successful")
                } else {
                    callback(false, "Invalid email or password")
                }
            }
    }

    override fun getCurrentUser(callback: (UserModel?) -> Unit) {
        val firebaseUser = auth.currentUser
        if (firebaseUser == null) {
            callback(null)
            return
        }
        // You would typically fetch from the database here, but for now we can just return auth details
        database.child(firebaseUser.uid).get().addOnSuccessListener { snapshot ->
            val user = snapshot.getValue(UserModel::class.java)
            callback(user)
        }.addOnFailureListener {
            callback(null)
        }
    }

    override fun logout(callback: (Boolean, String) -> Unit) {
        auth.signOut()
        callback(true, "Logged out successfully")
    }
}
