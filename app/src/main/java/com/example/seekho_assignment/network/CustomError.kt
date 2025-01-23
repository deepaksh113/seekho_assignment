package com.example.seekho_assignment.network

import com.google.gson.annotations.SerializedName

data class CustomError(
    @SerializedName("code") val code: String,

    @SerializedName("description") val description: String,
)
