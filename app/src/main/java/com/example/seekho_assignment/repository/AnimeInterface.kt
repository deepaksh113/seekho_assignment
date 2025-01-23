package com.example.seekho_assignment.repository

import com.example.seekho_assignment.model.Anime
import com.example.seekho_assignment.repository.internal.ApiResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface AnimeInterface {
    @GET("top/anime")
    suspend fun getTopAnime(): Response<ApiResponse<List<Anime>>>

    @GET("anime/{anime_id}")
    suspend fun getAnimeDetails(
        @Path("anime_id") animeId: String
    ): Response<ApiResponse<Anime>>
}