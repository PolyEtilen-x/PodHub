package com.example.podhub.viewmodels

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import com.example.podhub.models.PodcastResponseData

class LibraryViewModel : ViewModel() {

    private val _podcasts = MutableStateFlow<List<PodcastResponseData>>(emptyList())
    val podcasts: StateFlow<List<PodcastResponseData>> = _podcasts

    fun loadInitialPodcasts(data: List<PodcastResponseData>) {
        _podcasts.value = data
    }

    fun removePodcast(podcast: PodcastResponseData) {
        _podcasts.value = _podcasts.value.filterNot { it.trackId == podcast.trackId }
    }

}

