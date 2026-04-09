package com.dungtran.codebase.data.local.prefs

import android.content.Context
import android.content.SharedPreferences
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton
import androidx.core.content.edit

@Singleton
class PreferenceManager @Inject constructor(
    @ApplicationContext context: Context
) {
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("code_base_prefs", Context.MODE_PRIVATE)

    companion object {
        private const val KEY_ACCESS_TOKEN = "access_token"
        private const val KEY_IS_LOGGED_IN = "is_logged_in"
        private const val KEY_IS_FIRST_TIME_WELCOME = "is_first_time_welcome"

        private const val KEY_EMAIL = "email"
        private const val KEY_PASSWORD = "password"
        private const val KEY_IS_REMEMBER = "is_remember"
    }

    fun saveAccessToken(token: String) {
        sharedPreferences.edit { putString(KEY_ACCESS_TOKEN, token) }
        sharedPreferences.edit { putBoolean(KEY_IS_LOGGED_IN, true) }
    }

    fun getAccessToken(): String? {
        return sharedPreferences.getString(KEY_ACCESS_TOKEN, null)
    }

    fun isLoggedIn(): Boolean {
        return sharedPreferences.getBoolean(KEY_IS_LOGGED_IN, false)
    }

    fun isFirstTimeLaunchWelcome(): Boolean {
        return sharedPreferences.getBoolean(KEY_IS_FIRST_TIME_WELCOME, true)
    }
    
    fun setFirstTimeLaunchWelcome(isFirstTime: Boolean) {
        sharedPreferences.edit { putBoolean(KEY_IS_FIRST_TIME_WELCOME, isFirstTime) }
    }

    fun clearData() {
        sharedPreferences.edit { clear() }
    }

    fun saveCredentials(email: String, password: String, isRemember: Boolean) {
        sharedPreferences.edit().apply {
            if (isRemember) {
                putString(KEY_EMAIL, email)
                putString(KEY_PASSWORD, password)
                putBoolean(KEY_IS_REMEMBER, true)
            } else {
                clearCredentials()
            }
            apply()
        }
    }

    fun getSavedEmail(): String = sharedPreferences.getString(KEY_EMAIL, "") ?: ""
    fun getSavedPassword(): String = sharedPreferences.getString(KEY_PASSWORD, "") ?: ""
    fun isRemembered(): Boolean = sharedPreferences.getBoolean(KEY_IS_REMEMBER, false)
    
    fun clearCredentials() {
        sharedPreferences.edit {
            remove(KEY_EMAIL)
            remove(KEY_PASSWORD)
            remove(KEY_IS_REMEMBER)
        }
    }
    
}