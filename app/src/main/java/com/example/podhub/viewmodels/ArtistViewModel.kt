package com.example.podhub.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.podhub.Retrofit.RetrofitInstance
import com.example.podhub.models.Artist
import com.example.podhub.models.PodcastResponseData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ArtistViewModel : ViewModel() {

    private val _artists = MutableStateFlow<List<Artist>>(emptyList())
    val artists: StateFlow<List<Artist>> get() = _artists

    private val _FavourtiteArtists = MutableStateFlow<List<Artist>>(emptyList())
    val FavourtiteArtists: StateFlow<List<Artist>> get() = _FavourtiteArtists
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> get() = _isLoading
    private val _podcasts = MutableStateFlow<List<PodcastResponseData>>(emptyList())
    val podcasts: StateFlow<List<PodcastResponseData>> = _podcasts

    fun fetchAllArtists() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = RetrofitInstance.artistService.getAllArtists()
                if (response.isSuccessful) {
                    _artists.value = response.body() ?: emptyList()
                } else {
                    Log.e("ArtistVM", "Failed: ${response.code()}")
                }
            } catch (e: Exception) {
                Log.e("ArtistVM", "Error: ${e.message}")
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun fetchPodCastsByArtistId(artistId : Int){
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = RetrofitInstance.artistService.getPodCastByArtist(artistId)
                if (response.isSuccessful) {
                    _podcasts.value = response.body() ?: emptyList()
                } else {
                    Log.e("Podcast by Artist", "Failed: ${response.code()}")
                }
            } catch (e: Exception) {
                Log.e("Podcast by Artist", "Exception: ${e.message}")
            } finally {
                _isLoading.value = false
            }
        }
    }
    fun fetchFavouriteArtist(uuid : String){
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = RetrofitInstance.artistService.getAllFavouriteArtist(uuid)
                if (response.isSuccessful) {
                    _FavourtiteArtists.value = response.body() ?: emptyList()
                } else {
                    Log.e("ArtistVM", "Failed: ${response.code()}")
                }
            } catch (e: Exception) {
                Log.e("ArtistVM", "Error: ${e.message}")
            } finally {
                _isLoading.value = false
            }
        }
    }
}
