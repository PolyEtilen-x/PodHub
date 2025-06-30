package com.example.podhub.storage

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.podcastDataStore by preferencesDataStore("selected_podcasts")

object SelectedPodcastStore {
    private val SELECTED_KEY = stringSetPreferencesKey("selected_podcasts")

    fun getSelectedPodcasts(context: Context): Flow<Set<String>> {
        return context.podcastDataStore.data.map { it[SELECTED_KEY] ?: emptySet() }
    }

    suspend fun saveSelectedPodcasts(context: Context, podcasts: Set<String>) {
        context.podcastDataStore.edit { prefs ->
            prefs[SELECTED_KEY] = podcasts
        }
    }
}
