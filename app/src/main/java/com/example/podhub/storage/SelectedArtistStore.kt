package com.example.podhub.storage

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.artistDataStore by preferencesDataStore(name = "selected_artists")

object SelectedArtistStore {
    private val SELECTED_KEY = stringSetPreferencesKey("selected_artists")

    fun getSelectedArtists(context: Context): Flow<Set<Int>> {
        return context.artistDataStore.data.map { prefs ->
            prefs[SELECTED_KEY]?.mapNotNull { it.toIntOrNull() }?.toSet() ?: emptySet()
        }
    }

    suspend fun saveSelectedArtists(context: Context, artists: Set<Int>) {
        context.artistDataStore.edit { prefs ->
            prefs[SELECTED_KEY] = artists.map { it.toString() }.toSet()
        }
    }
}
