package com.paveltitov.wishlist.ui.login

sealed class LoginStates

object Idle : LoginStates()

object Loading : LoginStates()

data class Error(val message: String): LoginStates()

data class Success(val token: String): LoginStates()

