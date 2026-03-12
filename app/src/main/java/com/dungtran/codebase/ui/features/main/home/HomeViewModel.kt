package com.dungtran.codebase.ui.features.main.home

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.dungtran.codebase.ui.navigation.Screen
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    
    /*private val homeArgs = savedStateHandle.toRoute<Screen.Home>()

    val userId = homeArgs.userId
    val userName = homeArgs.userName*/
    
    init {
        viewModelScope.launch {
//            Log.i("Atut", "HomeViewModel: $userId - $userName")
        }
    }
}

