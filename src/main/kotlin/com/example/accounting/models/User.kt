package com.example.accounting.models

data class User (
    val id: Long? = null,
    val login: String,
    val password: String,
    val admin: Boolean
)