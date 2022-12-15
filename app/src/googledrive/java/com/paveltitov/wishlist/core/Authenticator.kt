package com.paveltitov.wishlist.core

interface Authenticator {

    fun signIn()

    fun onSuccessfulResponse(action: () -> Unit)

    fun signOut()

    companion object {
        const val REQUEST_CODE = 112233
    }
}