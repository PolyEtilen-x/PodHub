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
class PodcastViewModel : ViewModel() {

    private val _podcasts = MutableStateFlow<List<PodcastResponseData>>(emptyList())
    val podcasts: StateFlow<List<PodcastResponseData>> = _podcasts
    private val _categoryPodcasts = MutableStateFlow<List<PodcastResponseData>>(emptyList())
    val categoryPodcasts: StateFlow<List<PodcastResponseData>> = _categoryPodcasts

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    fun fetchPodcasts(term: String = "comedy", limit: Int = 20) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = RetrofitInstance.podcastService.getPodcastsWithEpisodes(term, limit)
                if (response.isSuccessful) {
                    _podcasts.value = response.body() ?: emptyList()
                } else {
                    Log.e("PodcastVM", "Failed: ${response.code()}")
                }
            } catch (e: Exception) {
                Log.e("PodcastVM", "Exception: ${e.message}")
            } finally {
                _isLoading.value = false
            }
        }
    }
    fun fetchPodcastsByCategory(term: String , limit: Int = 10) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = RetrofitInstance.podcastService.getPodcastsWithEpisodes(term, limit)
                if (response.isSuccessful) {
                    _categoryPodcasts.value = response.body() ?: emptyList()
                } else {
                    Log.e("PodcastVM", "Failed: ${response.code()}")
                }
            } catch (e: Exception) {
                Log.e("PodcastVM", "Exception: ${e.message}")
            } finally {
                _isLoading.value = false
            }
        }
    }
}
