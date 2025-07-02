package com.example.podhub.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.podhub.RequestModel.HistoryRequest
import com.example.podhub.Retrofit.RetrofitInstance
import com.example.podhub.models.PodcastResponseData
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class HistoryViewModel(

) : ViewModel() {

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _podcastIds = MutableStateFlow<List<PodcastResponseData>>(emptyList())
    val podcastIds: StateFlow<List<PodcastResponseData>> = _podcastIds.asStateFlow()

    private val _recentPlayed = MutableStateFlow<List<Int>>(emptyList())
    val recentPlayed: StateFlow<List<Int>> = _recentPlayed.asStateFlow()

    private val _episodeIndex = MutableStateFlow<List<Int>>(emptyList())
    val episodeIndex: StateFlow<List<Int>> = _episodeIndex.asStateFlow()
    private val _selectedIndex = MutableStateFlow<Int?>(null)
    val selectedIndex: StateFlow<Int?> = _selectedIndex.asStateFlow()
    private val _duration = MutableStateFlow<Long?>(null)
    val duration: StateFlow<Long?> = _duration.asStateFlow()


    fun loadHistory(uuid: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = RetrofitInstance.historyService.getHistory(uuid)
                if (response.isSuccessful) {
                    response.body()?.let { history ->
                        _podcastIds.value = history.podCasts
                        _recentPlayed.value = history.recentPlayed
                        _episodeIndex.value = history.episodeIndex
                    }
                } else {

                }
            } catch (e: Exception) {
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun postHistory(uuid: String, request: HistoryRequest) {
        Log.d("history post", request.toString())
        viewModelScope.launch {
            _isLoading.value = true
            try {
                RetrofitInstance.historyService.postHistory(uuid, request)

                loadHistory(uuid)
            } catch (e: Exception) {

            } finally {
                _isLoading.value = false
            }
        }
    }
    fun setSelectedIndex(index: Int) {
        _selectedIndex.value = index
    }

    // Get episode index by selected index
    fun getEpisodeIndex(): Int? {
        return _selectedIndex.value?.let { index ->
            _episodeIndex.value.getOrNull(index)
        }
    }

    // Get recent played by selected index
    fun getRecentPlayed(): Int? {
        return _selectedIndex.value?.let { index ->
            _recentPlayed.value.getOrNull(index)
        }
    }
    fun setRecentPlayed(duration : Long){
        _duration.value = duration
    }
    fun getRecentPlay(): Long?{
        return _duration.value
    }
    fun getlastIndex():Int?{
        return _selectedIndex.value
    }
    fun clear(){
        _selectedIndex.value = null
        _duration.value = null
    }
}
