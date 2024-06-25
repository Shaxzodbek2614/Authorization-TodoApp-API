package com.example.notes.models

data class GetUserDetails(
    val date_joined: String,
    val email: String,
    val familiya: Any,
    val id: Int,
    val ism: Any,
    val password: String,
    val username: String
)