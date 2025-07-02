package com.example.podhub.storage

import android.content.Context
import android.util.Log
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "user_prefs")

class DataStoreManager(private val context: Context) {

    companion object {
        val UID = stringPreferencesKey("uid")
        val NAME = stringPreferencesKey("displayname")
        val EMAIL = stringPreferencesKey("email")
        val AVATAR = stringPreferencesKey("photoUrl")
    }

    suspend fun saveUser(uid: String, name: String, email: String, photoUrl: String) {
        Log.d("LoginDebug", "Saving user: $uid - $name - $email - $photoUrl")

        context.dataStore.edit { prefs ->
            prefs[UID] = uid
            prefs[NAME] = name
            prefs[EMAIL] = email
            prefs[AVATAR] = photoUrl
        }
    }

    val userData: Flow<Map<String, String>> = context.dataStore.data.map { prefs ->
        mapOf(
            "uid" to (prefs[UID] ?: ""),
            "name" to (prefs[NAME] ?: ""),
            "email" to (prefs[EMAIL] ?: ""),
            "photoUrl" to (prefs[AVATAR] ?: "")
        )
    }

    suspend fun getUid(): String {
        val preferences = context.dataStore.data.first()
        return preferences[UID] ?: ""
    }

    suspend fun clear() {
        context.dataStore.edit { it.clear() }
    }
}
