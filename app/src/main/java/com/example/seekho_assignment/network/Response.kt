package com.example.seekho_assignment.network

data class Response<T>(
    val data: T? = null,
    val error: CustomError? = null
)