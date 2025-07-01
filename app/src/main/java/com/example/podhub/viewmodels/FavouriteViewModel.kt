package com.example.podhub.viewmodels

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.podhub.RequestModel.FavouriteRequest
import com.example.podhub.Retrofit.RetrofitInstance
import com.example.podhub.data.PodcastResponse
import com.example.podhub.models.PodcastResponseData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class FavouriteViewModel : ViewModel() {

    private val _suggestedPodcasts = MutableStateFlow<List<PodcastResponseData>>(emptyList())
    val suggestedPodcasts: StateFlow<List<PodcastResponseData>> = _suggestedPodcasts

    private val _isFavourite = MutableStateFlow(false)
    val isFavourite: StateFlow<Boolean> = _isFavourite

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    fun collectFavourite(uuid: String, artists: List<Long>, categories: List<Long>) {
        viewModelScope.launch {
            try {
                val response = RetrofitInstance.favouriteService.collectFavourite(
                    uuid, FavouriteRequest(artists, categories)
                )
                if (response.isSuccessful) {
                    Log.d("FavouriteViewModel", "Success: ${response.body()}")
                } else {
                    Log.e("FavouriteViewModel", "Failed: ${response.code()}")
                }
            } catch (e: Exception) {
                Log.e("FavouriteViewModel", "Exception: ${e.message}")
            }
        }
    }


    fun loadSuggestedPodcasts(uuid: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = RetrofitInstance.favouriteService.getSuggestedPodcasts(uuid)
                if (response.isSuccessful) {
                    _suggestedPodcasts.value = response.body().orEmpty()
                } else {
                    Log.e("FavouriteViewModel", "Suggestion failed: ${response.code()}")
                }
            } catch (e: Exception) {
                Log.e("FavouriteViewModel", "Error: ${e.message}")
            } finally {
                _isLoading.value = false
            }
        }
    }


    fun checkIfPodcastFavourited(uuid: String, trackId: Long) {
        viewModelScope.launch {
            try {
                val response = RetrofitInstance.favouriteService.isPodcastFavourited(uuid, trackId)
                if (response.isSuccessful) {
                    _isFavourite.value = response.body() ?: false
                }
            } catch (e: Exception) {
                Log.e("FavouriteViewModel", "Check fav error: ${e.message}")
            }
        }
    }


    fun toggleFavourite(uuid: String, trackId: Long) {
        viewModelScope.launch {
            try {
                val response = RetrofitInstance.favouriteService.togglePodcastFavourite(uuid, trackId)
                if (response.isSuccessful) {
                    _isFavourite.value = !_isFavourite.value
                }
            } catch (e: Exception) {
                Log.e("FavouriteViewModel", "Toggle fav error: ${e.message}")
            }
        }
    }
}
