package com.example.notes.models

import java.io.Serializable

data class GetDetails(
    val bajarildi: Boolean,
    val batafsil: String,
    val created_at: String,
    val id: Int,
    val muddat: String,
    val sarlavha: String,
    val user: Int
): Serializable