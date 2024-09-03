package com.example.project_android_trisa.Model

data class LoginRequest(
    val username: String,
    val password: String,
    val strategy: String,
)