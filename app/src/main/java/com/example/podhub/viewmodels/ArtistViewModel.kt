package com.example.podhub.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.podhub.Retrofit.RetrofitInstance
import com.example.podhub.models.Artist
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ArtistViewModel : ViewModel() {

    private val _artists = MutableStateFlow<List<Artist>>(emptyList())
    val artists: StateFlow<List<Artist>> get() = _artists

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> get() = _isLoading

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
}
