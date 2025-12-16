package com.example.bookrecommendation.repository

import com.example.bookrecommendation.model.UserModel

interface UserRepo {

    fun register(
        user: UserModel,
        callback: (Boolean, String) -> Unit
    )

    fun login(
        email: String,
        password: String,
        callback: (Boolean, String) -> Unit
    )

    fun getCurrentUser(
        callback: (UserModel?) -> Unit
    )

    fun logout(
        callback: (Boolean, String) -> Unit
    )
}
