package com.dungtran.codebase.ui.features.welcome

import androidx.lifecycle.ViewModel
import com.dungtran.codebase.data.local.prefs.PreferenceManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class WelcomeViewModel @Inject constructor(
    private val preferenceManager: PreferenceManager
) : ViewModel() {
    fun completeWelcome() {
        preferenceManager.setFirstTimeLaunchWelcome(false)
    }
}