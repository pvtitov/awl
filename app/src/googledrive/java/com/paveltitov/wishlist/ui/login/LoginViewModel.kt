package com.paveltitov.wishlist.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.paveltitov.wishlist.BuildConfig
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {
    private val _uiState: MutableStateFlow<LoginStates> = MutableStateFlow(Idle)
    val uiState: StateFlow<LoginStates> = _uiState.asStateFlow()

    fun login() {
        viewModelScope.launch { _uiState.emit(Loading) }
    }

    fun onError(message: String) {
        viewModelScope.launch { _uiState.emit(Error(message)) }
    }

    fun onSuccess(token: String) {
        viewModelScope.launch { _uiState.emit(Success(token)) }
    }

    fun buildRequest(): BeginSignInRequest {
        return BeginSignInRequest.builder()
            .setGoogleIdTokenRequestOptions(
                BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                    .setSupported(true)
                    // Your server's client ID, not your Android client ID.
                    .setServerClientId(BuildConfig.SERVER_CLIENT_ID)
                    .setFilterByAuthorizedAccounts(false)
                    .build()
            )
            .build()
    }
}