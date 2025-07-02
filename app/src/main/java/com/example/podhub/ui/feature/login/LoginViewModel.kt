package com.example.podhub.ui.feature.login

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.podhub.RequestModel.Login
import com.example.podhub.Retrofit.RetrofitInstance
import com.example.podhub.storage.DataStoreManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class LoginViewModel(private val dataStoreManager: DataStoreManager) : ViewModel() {

    private val _loginMessage = MutableStateFlow<Boolean>(false)
    val loginMessage: StateFlow<Boolean?> get() = _loginMessage

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> get() = _isLoading

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> get() = _errorMessage

    fun login(uuid: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
           Log.d("HongTonLog",uuid)

            try {
                Log.d("login123",Login(uuid).toString())
                val response = RetrofitInstance.userService.loginUser(LoginRequest = Login(uuid))
                if (response.isSuccessful) {
                    Log.d("cahayj",loginMessage.toString())
                    _loginMessage.value = response.body()?.message!!
                    Log.d("login",loginMessage.toString())
                } else {
                    _errorMessage.value = response.errorBody()?.string() ?: "Unknown error"
                    Log.d("login",loginMessage.toString())
                }
            } catch (e: Exception) {
                _errorMessage.value = e.message ?: "Exception occurred"
            } finally {
                _isLoading.value = false
            }
        }
    }
}
