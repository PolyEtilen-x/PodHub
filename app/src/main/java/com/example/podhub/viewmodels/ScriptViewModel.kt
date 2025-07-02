package com.example.podhub.viewmodels


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.podhub.RequestModel.ScriptResponse
import com.example.podhub.Retrofit.RetrofitInstance
import com.example.podhub.Service.ScriptService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ScriptViewModel() : ViewModel() {

    private val _script = MutableStateFlow<ScriptResponse?>(null)
    val script: StateFlow<ScriptResponse?> = _script

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    fun fetchScriptByPodcastId(podcastId: Int) {
        _isLoading.value = true
        _error.value = null

        viewModelScope.launch {
            try {
                val result = RetrofitInstance.scriptService.getScriptByPodcastId(podcastId)
                _script.value = result
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }
}