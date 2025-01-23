package com.example.seekho_assignment.repository

import com.example.seekho_assignment.model.Anime
import com.example.seekho_assignment.network.CustomError
import com.example.seekho_assignment.network.Response
import com.example.seekho_assignment.network.RetrofitClient
import com.example.seekho_assignment.repository.internal.ApiResponse

object Anime {
    private val animeInterface: AnimeInterface by lazy {
        RetrofitClient.instance.create(AnimeInterface::class.java)
    }

    internal suspend fun getTopAnime(): Response<ApiResponse<List<Anime>>> {
        try {
            val apiResponse = animeInterface.getTopAnime()
            if (apiResponse.isSuccessful) {
                apiResponse.body()?.let {
                    if (it.item == null) {
                        return Response(error = CustomError(
                            "EMPTY_DATA",
                            "Empty data returned"
                        ))
                    } else {
                        return Response(data = it)
                    }
                }
            }
            return Response(error = CustomError(
                "UNKNOWN_ERROR",
                "Something went wrong. Please try again."
            ))
        } catch (ex: Exception) {
            return Response(error = CustomError("UNKNOWN_ERROR", ex.message.toString()))
        }
    }

    internal suspend fun getAnimeDetails(animeId: Long): Response<Anime> {
        try {
            val apiResponse = animeInterface.getAnimeDetails(animeId = animeId.toString())
            if (apiResponse.isSuccessful) {
                apiResponse.body()?.let {
                    if (it.item == null) {
                        return Response(error = CustomError(
                            "EMPTY_DATA",
                            "Empty data returned"
                        ))
                    } else {
                        return Response(data = it.item)
                    }
                }
            }
            return Response(error = CustomError(
                "UNKNOWN_ERROR",
                "Something went wrong. Please try again."
            ))
        } catch (ex: Exception) {
            return Response(error = CustomError("UNKNOWN_ERROR", ex.message.toString()))
        }
    }
}