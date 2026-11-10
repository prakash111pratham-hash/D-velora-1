package com.example.ui.auth

import android.app.Activity
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.model.User
import com.example.data.repository.AuthRepository
import com.example.data.repository.RestaurantRepository
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed interface AuthUiState {
    data object Idle : AuthUiState
    data object Loading : AuthUiState
    data class Authenticated(val user: FirebaseUser) : AuthUiState
    data class Error(val message: String) : AuthUiState
}

class AuthenticationViewModel(application: Application) : AndroidViewModel(application) {
    private val authRepository = AuthRepository(application)
    private val restaurantRepository = RestaurantRepository(application)

    private val _uiState = MutableStateFlow<AuthUiState>(AuthUiState.Idle)
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    val currentUser: StateFlow<FirebaseUser?> = authRepository.currentUser

    init {
        viewModelScope.launch {
            authRepository.currentUser.collect { user ->
                if (user != null) {
                    _uiState.value = AuthUiState.Authenticated(user)
                    // Ensure user document exists in Firestore
                    restaurantRepository.syncUserProfile(user)
                } else {
                    _uiState.value = AuthUiState.Idle
                }
            }
        }
    }

    fun attemptSilentSignIn() {
        if (_uiState.value is AuthUiState.Authenticated) return
        authRepository.attemptAutoSignIn(
            scope = viewModelScope,
            onSuccess = {
                // AuthStateListener updates currentUser
            },
            onFailure = {
                // Silent check completed; remain at Idle
            }
        )
    }

    fun signInWithGoogle(activity: Activity) {
        _uiState.value = AuthUiState.Loading
        authRepository.signInWithGoogle(
            activity = activity,
            scope = viewModelScope,
            onSuccess = {
                // State is updated automatically by authStateListener
            },
            onError = { errorMsg ->
                _uiState.value = AuthUiState.Error(errorMsg)
            },
            onCancelled = {
                _uiState.value = AuthUiState.Idle
            }
        )
    }

    fun signOut() {
        _uiState.value = AuthUiState.Loading
        authRepository.signOut(viewModelScope) {
            _uiState.value = AuthUiState.Idle
        }
    }

    fun clearError() {
        if (_uiState.value is AuthUiState.Error) {
            _uiState.value = AuthUiState.Idle
        }
    }
}
