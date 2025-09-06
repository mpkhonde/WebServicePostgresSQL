package com.example.webservicepostgressql.controller

data class CreateUserRequest(
    val username: String,
    val password: String,
    val enabled: Boolean
)
