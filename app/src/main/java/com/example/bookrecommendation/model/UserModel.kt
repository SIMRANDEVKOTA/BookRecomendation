package com.example.bookrecommendation.model

data class UserModel(
    val userId: String = "",
    val email: String = "",
    val password: String = "",
    val dob: String = "",
    val favoriteGenre: String = ""
)
