package com.example.book_my_trip.ui;

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow

import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AuthViewModel : ViewModel() {

    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
            val authState: StateFlow<AuthState> = _authState

    fun signIn(email: String, password: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading

            // In a real app, you would call your repository to authenticate the user
            // For now, we'll simulate a network delay and successful authentication
            try {
                // Simulate network delay
                kotlinx.coroutines.delay(1000)

                // Simple validation (in a real app, this would be more robust)
                if (email.isBlank() || !email.contains("@")) {
                    _authState.value = AuthState.Error("Please enter a valid email address")
                    return@launch
                }

                if (password.length < 6) {
                    _authState.value = AuthState.Error("Password must be at least 6 characters long")
                    return@launch
                }

                // Simulate successful authentication
                _authState.value = AuthState.Success
            } catch (e: Exception) {
                _authState.value = AuthState.Error("Authentication failed: ${e.message}")
            }
        }
    }

    fun signUp(name: String, email: String, password: String, confirmPassword: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading

            // In a real app, you would call your repository to register the user
            // For now, we'll simulate a network delay and successful registration
            try {
                // Simulate network delay
                kotlinx.coroutines.delay(1000)

                // Simple validation (in a real app, this would be more robust)
                if (name.isBlank()) {
                    _authState.value = AuthState.Error("Please enter your name")
                    return@launch
                }

                if (email.isBlank() || !email.contains("@")) {
                    _authState.value = AuthState.Error("Please enter a valid email address")
                    return@launch
                }

                if (password.length < 6) {
                    _authState.value = AuthState.Error("Password must be at least 6 characters long")
                    return@launch
                }

                if (password != confirmPassword) {
                    _authState.value = AuthState.Error("Passwords don't match")
                    return@launch
                }

                // Simulate successful registration
                _authState.value = AuthState.Success
            } catch (e: Exception) {
                _authState.value = AuthState.Error("Registration failed: ${e.message}")
            }
        }
    }

    fun resetState() {
        _authState.value = AuthState.Idle
    }
}

sealed class AuthState {
    object Idle : AuthState()
    object Loading : AuthState()
    object Success : AuthState()
    data class Error(val message: String) : AuthState()
}