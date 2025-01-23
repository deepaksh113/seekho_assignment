package com.example.seekho_assignment.model

import com.google.gson.annotations.SerializedName

data class Anime(
    @SerializedName("mal_id")
    val id: Long,

    @SerializedName("title")
    val title: String,

    @SerializedName("images")
    val images: Images,

    @SerializedName("trailer")
    val trailer: Trailer?,

    @SerializedName("rating")
    val rating: String,

    @SerializedName("score")
    val score: Float,

    @SerializedName("scored_by")
    val scoredBy: Long,

    @SerializedName("synopsis")
    val synopsis: String,

    @SerializedName("background")
    val background: String,

    @SerializedName("episodes")
    val episodes: Int,

    @SerializedName("genres")
    val genres: List<GenreOrProducer>,

    @SerializedName("producers")
    val producers: List<GenreOrProducer>
)

data class Images(
    @SerializedName("jpg")
    val jpg: Jpg
)

data class Jpg(
    @SerializedName("image_url")
    val imageUrl: String,

    @SerializedName("large_image_url")
    val largeImageUrl: String
)

data class Trailer(
    @SerializedName("url")
    val url: String,

    @SerializedName("embed_url")
    val embedUrl: String
)

data class GenreOrProducer(
    @SerializedName("mal_id")
    val id: Long,

    @SerializedName("type")
    val type: String,

    @SerializedName("name")
    val name: String,

    @SerializedName("url")
    val url: String
)
