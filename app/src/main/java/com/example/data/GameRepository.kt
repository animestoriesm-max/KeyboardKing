package com.example.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class GameRepository(private val context: Context) {

    private val HIGH_SCORE_KEY = intPreferencesKey("high_score")
    private val XP_KEY = intPreferencesKey("xp")
    private val LEVEL_KEY = intPreferencesKey("level")
    private val LANGUAGE_KEY = stringPreferencesKey("language") // "en" or "ar"
    private val SOUND_MUTED_KEY = booleanPreferencesKey("sound_muted")

    val highScoreFlow: Flow<Int> = context.dataStore.data.map { it[HIGH_SCORE_KEY] ?: 0 }
    val xpFlow: Flow<Int> = context.dataStore.data.map { it[XP_KEY] ?: 0 }
    val levelFlow: Flow<Int> = context.dataStore.data.map { it[LEVEL_KEY] ?: 1 }
    val languageFlow: Flow<String> = context.dataStore.data.map { it[LANGUAGE_KEY] ?: "en" }
    val soundMutedFlow: Flow<Boolean> = context.dataStore.data.map { it[SOUND_MUTED_KEY] ?: false }

    suspend fun saveHighScore(score: Int) {
        context.dataStore.edit { it[HIGH_SCORE_KEY] = score }
    }

    suspend fun saveXp(xp: Int) {
        context.dataStore.edit { it[XP_KEY] = xp }
    }

    suspend fun saveLevel(level: Int) {
        context.dataStore.edit { it[LEVEL_KEY] = level }
    }

    suspend fun setLanguage(lang: String) {
        context.dataStore.edit { it[LANGUAGE_KEY] = lang }
    }

    suspend fun setSoundMuted(muted: Boolean) {
        context.dataStore.edit { it[SOUND_MUTED_KEY] = muted }
    }
}
