package com.example.seekho_assignment.repository.internal

import com.google.gson.annotations.SerializedName

data class ApiResponse<T>(
    @SerializedName("pagination")
    val pagination: Pagination?,

    @SerializedName("data")
    val item: T?
)
