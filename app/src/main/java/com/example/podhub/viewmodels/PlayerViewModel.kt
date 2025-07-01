package com.example.podhub.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.podhub.models.PodcastResponseData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.common.MediaItem
import kotlin.random.Random

class PlayerViewModel(application: Application) : AndroidViewModel(application) {

    private val context = application.applicationContext
    val exoPlayer: ExoPlayer = ExoPlayer.Builder(context).build()

    private val _currentPodcast = MutableStateFlow<PodcastResponseData?>(null)
    val currentPodcast: StateFlow<PodcastResponseData?> = _currentPodcast

    private val _isPlaying = MutableStateFlow(false)
    val isPlaying: StateFlow<Boolean> = _isPlaying

    private val _currentEpisodeIndex = MutableStateFlow(0)
    val currentEpisodeIndex: StateFlow<Int> = _currentEpisodeIndex

    private val _repeatMode = MutableStateFlow(false)
    val repeatMode: StateFlow<Boolean> = _repeatMode

    private val _shuffleMode = MutableStateFlow(false)
    val shuffleMode: StateFlow<Boolean> = _shuffleMode

    fun setPodcast(podcast: PodcastResponseData) {
        _currentPodcast.value = podcast
        _currentEpisodeIndex.value = 0
        playEpisodeAt(0)
    }

    fun togglePlayPause() {
        if (exoPlayer.isPlaying) {
            exoPlayer.pause()
            _isPlaying.value = false
        } else {
            exoPlayer.play()
            _isPlaying.value = true
        }
    }

    fun nextEpisode() {
        val podcast = _currentPodcast.value ?: return
        val episodes = podcast.episodes

        val nextIndex = if (_shuffleMode.value) {
            episodes?.let { Random.nextInt(it.size) }
        } else {
            episodes?.let { (_currentEpisodeIndex.value + 1).coerceAtMost(it.lastIndex) }
        }

        _currentEpisodeIndex.value = nextIndex!!
        playEpisodeAt(nextIndex)
    }

    fun previousEpisode() {
        val prevIndex = (_currentEpisodeIndex.value - 1).coerceAtLeast(0)
        _currentEpisodeIndex.value = prevIndex
        playEpisodeAt(prevIndex)
    }

    fun toggleRepeat() {
        _repeatMode.value = !_repeatMode.value
        exoPlayer.repeatMode = if (_repeatMode.value) {
            ExoPlayer.REPEAT_MODE_ONE
        } else {
            ExoPlayer.REPEAT_MODE_OFF
        }
    }

    fun toggleShuffle() {
        _shuffleMode.value = !_shuffleMode.value
    }

    private fun playEpisodeAt(index: Int) {
        val podcast = _currentPodcast.value ?: return
        val episodes = podcast.episodes
        episodes?.indices?.let {
            if (index in it  ) {
                val episode = episodes?.get(index)
                val mediaItem = MediaItem.fromUri(episode?.audioUrl.toString())
                exoPlayer.setMediaItem(mediaItem)
                exoPlayer.prepare()
                exoPlayer.play()
                _isPlaying.value = true

                println("Playing episode: ${episode?.title}")

            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        exoPlayer.release()
    }
}
