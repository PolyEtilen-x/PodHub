package com.example.podhub.viewmodels

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.podhub.models.UserModel


class UserViewModel : ViewModel() {
    var currentUser by mutableStateOf<UserModel?>(null)
}
