package com.example.podhub.viewmodels

import androidx.lifecycle.ViewModel
import com.example.podhub.models.Playlist

class SharedPlaylistViewModel : ViewModel() {
    var selectedPlaylist: Playlist? = null
}
