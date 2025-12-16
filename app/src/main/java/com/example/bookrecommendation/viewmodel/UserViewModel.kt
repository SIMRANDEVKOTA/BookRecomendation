package com.example.bookrecommendation.viewmodel

import androidx.lifecycle.ViewModel
import com.example.bookrecommendation.model.UserModel
import com.example.bookrecommendation.repository.UserRepo

class UserViewModel(private val repo: UserRepo) : ViewModel() {

    fun register(
        user: UserModel,
        callback: (Boolean, String) -> Unit
    ) {
        repo.register(user, callback)
    }

    fun login(
        email: String,
        password: String,
        callback: (Boolean, String) -> Unit
    ) {
        repo.login(email, password, callback)
    }

    fun getCurrentUser(
        callback: (UserModel?) -> Unit
    ) {
        repo.getCurrentUser(callback)
    }

    fun logout(
        callback: (Boolean, String) -> Unit
    ) {
        repo.logout(callback)
    }
}
