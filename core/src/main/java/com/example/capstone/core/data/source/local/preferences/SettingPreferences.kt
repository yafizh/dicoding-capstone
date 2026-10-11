package com.example.capstone.core.data.source.local.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

val Context.settingDataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class SettingPreferences(private val dataStore: DataStore<Preferences>) {

    fun isDarkModeActive(): Flow<Boolean> = dataStore.data
        .catch { throwable ->
            if (throwable is IOException) emit(emptyPreferences()) else throw throwable
        }
        .map { preferences -> preferences[DARK_MODE_KEY] ?: false }

    suspend fun setDarkModeActive(isActive: Boolean) {
        dataStore.edit { preferences ->
            preferences[DARK_MODE_KEY] = isActive
        }
    }

    private companion object {
        val DARK_MODE_KEY = booleanPreferencesKey("dark_mode")
    }
}
