package com.example.seekho_assignment.repository.internal

import com.google.gson.annotations.SerializedName

data class Pagination(
    @SerializedName("current_page")
    val currentPage: Int,

    @SerializedName("last_visible_page")
    val lastVisiblePage: Int,

    @SerializedName("has_next_page")
    val hasNextPage: Boolean,

    @SerializedName("items")
    val items: Items
)

data class Items(
    @SerializedName("count")
    val count: Int,

    @SerializedName("per_page")
    val perPage: Int,

    @SerializedName("total")
    val total: Long
)