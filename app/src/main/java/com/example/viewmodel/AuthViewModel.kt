package com.example.viewmodel

import android.app.Activity
import android.content.Context
import androidx.credentials.CredentialManager
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.AuthRepository
import com.example.data.FirestoreRepository
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AuthViewModel(
    private val authRepository: AuthRepository = AuthRepository(),
    private val firestoreRepository: FirestoreRepository? = null
) : ViewModel() {

    private val _currentUser = MutableStateFlow<FirebaseUser?>(authRepository.currentUser)
    val currentUser: StateFlow<FirebaseUser?> = _currentUser.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    init {
        viewModelScope.launch {
            authRepository.observeAuthState().collect { user ->
                _currentUser.value = user
                if (user != null && firestoreRepository != null) {
                    firestoreRepository.createInitialProfileIfMissing(
                        userId = user.uid,
                        displayName = user.displayName ?: "Valued Guest",
                        email = user.email ?: "",
                        photoUrl = user.photoUrl?.toString()
                    )
                }
            }
        }
    }

    fun attemptAutoSignIn(context: Context, credentialManager: CredentialManager) {
        authRepository.attemptAutoSignIn(
            context = context,
            credentialManager = credentialManager,
            onAuthSuccess = {
                _currentUser.value = authRepository.currentUser
            },
            onUnauthenticated = {
                // Stay unauthenticated, show welcome screen
            },
            scope = viewModelScope
        )
    }

    fun signInWithGoogle(activity: Activity, credentialManager: CredentialManager) {
        _isLoading.value = true
        _errorMessage.value = null
        authRepository.onGoogleSignInClicked(
            activity = activity,
            credentialManager = credentialManager,
            onAuthSuccess = {
                _isLoading.value = false
                _currentUser.value = authRepository.currentUser
            },
            onAuthError = { errorMsg ->
                _isLoading.value = false
                _errorMessage.value = errorMsg
            },
            onAuthCancelled = {
                _isLoading.value = false
            },
            scope = viewModelScope
        )
    }

    fun signOut(credentialManager: CredentialManager) {
        _isLoading.value = true
        authRepository.signOut(
            credentialManager = credentialManager,
            scope = viewModelScope,
            onSignOutComplete = {
                _isLoading.value = false
                _currentUser.value = null
            }
        )
    }

    fun clearError() {
        _errorMessage.value = null
    }
}
