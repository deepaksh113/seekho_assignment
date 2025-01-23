package com.example.seekho_assignment.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.seekho_assignment.network.Response
import com.example.seekho_assignment.repository.Anime
import com.example.seekho_assignment.repository.internal.ApiResponse
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import com.example.seekho_assignment.model.Anime as AnimeModel

class AnimeViewModel: ViewModel() {

    var animeList by mutableStateOf<List<AnimeModel>?>(null)
        private set

    var animeDetails by mutableStateOf<AnimeModel?>(null)

    var isLoadingForHomeScreen by mutableStateOf(false)
        private set

    var isLoadingForAnimeScreen by mutableStateOf(false)
        private set

    fun getTopAnimeList() {
        viewModelScope.launch {
            isLoadingForHomeScreen = true
            try {
                val result = getTopAnimeFromNetwork()
                Log.d("Deepak", "getTopAnimeList: ${Gson().toJson(result)}")
                animeList = result.data?.item
            } catch (ex: Exception) {
                Log.d("Deepak", "getTopAnimeList: ${ex.message}")
                animeList = emptyList()
            } finally {
                isLoadingForHomeScreen = false
            }
        }
    }

    fun getAnimeDetails(animeId: Long) {
        viewModelScope.launch {
            isLoadingForAnimeScreen = true
            try {
                val result = getAnimeDetailsFromNetwork(animeId)
                animeDetails = result.data
            } catch (ex: Exception) {
                animeDetails = null
            } finally {
                isLoadingForAnimeScreen = false
            }
        }
    }

    private suspend fun getTopAnimeFromNetwork(): Response<ApiResponse<List<AnimeModel>>> {
        return withContext(Dispatchers.IO) {
            Anime.getTopAnime()
        }
    }

    private suspend fun getAnimeDetailsFromNetwork(animeId: Long): Response<AnimeModel> {
        return withContext(Dispatchers.IO) {
            Anime.getAnimeDetails(animeId)
        }
    }
}