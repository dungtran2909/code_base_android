package com.dungtran.codebase.data.local.prefs

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "code_base_prefs")

@Singleton
class DataStoreManager @Inject constructor(
    @param:ApplicationContext private val context: Context
) {
    private object PreferencesKeys {
        val ACCESS_TOKEN = stringPreferencesKey("access_token")
        val IS_LOGGED_IN = booleanPreferencesKey("is_logged_in")
        val IS_FIRST_TIME_WELCOME = booleanPreferencesKey("is_first_time_welcome")
        val EMAIL = stringPreferencesKey("email")
        val PASSWORD = stringPreferencesKey("password")
        val IS_REMEMBER = booleanPreferencesKey("is_remember")
    }

    /* --- ACCESS TOKEN & LOGIN STATUS --- */
    val accessToken: Flow<String?> = context.dataStore.data.map { it[PreferencesKeys.ACCESS_TOKEN] }
    val isLoggedIn: Flow<Boolean> = context.dataStore.data.map { it[PreferencesKeys.IS_LOGGED_IN] ?: false }

    suspend fun saveAccessToken(token: String) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.ACCESS_TOKEN] = token
            preferences[PreferencesKeys.IS_LOGGED_IN] = true
        }
    }

    /* --- WELCOME SCREEN STATUS --- */
    val isFirstTimeLaunchWelcome: Flow<Boolean> = context.dataStore.data.map {
        it[PreferencesKeys.IS_FIRST_TIME_WELCOME] ?: true
    }

    suspend fun setFirstTimeLaunchWelcome(isFirstTime: Boolean) {
        context.dataStore.edit { it[PreferencesKeys.IS_FIRST_TIME_WELCOME] = isFirstTime }
    }

    val savedEmail: Flow<String> = context.dataStore.data.map { it[PreferencesKeys.EMAIL] ?: "" }
    val savedPassword: Flow<String> = context.dataStore.data.map { it[PreferencesKeys.PASSWORD] ?: "" }
    val isRemembered: Flow<Boolean> = context.dataStore.data.map { it[PreferencesKeys.IS_REMEMBER] ?: false }

    suspend fun saveCredentials(email: String, password: String, isRemember: Boolean) {
        context.dataStore.edit { preferences ->
            if (isRemember) {
                preferences[PreferencesKeys.EMAIL] = email
                preferences[PreferencesKeys.PASSWORD] = password
                preferences[PreferencesKeys.IS_REMEMBER] = true
            } else {
                preferences.remove(PreferencesKeys.EMAIL)
                preferences.remove(PreferencesKeys.PASSWORD)
                preferences.remove(PreferencesKeys.IS_REMEMBER)
            }
        }
    }

    /* --- CLEAR DATA --- */
    suspend fun clearAll() {
        context.dataStore.edit { it.clear() }
    }
}