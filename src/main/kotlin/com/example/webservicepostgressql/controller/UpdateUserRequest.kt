package com.example.webservicepostgressql.controller

data class UpdateUserRequest(
    val username: String?,
    val password: String?,
    val enabled: Boolean?
)
