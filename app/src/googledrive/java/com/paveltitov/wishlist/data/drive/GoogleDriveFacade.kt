package com.paveltitov.wishlist.data.drive

import com.google.api.services.drive.Drive

class GoogleDriveFacade {

    private val drive: Drive by lazy {
        TODO("Not implemented")
    }

    fun syncData(timeout: Long = 5000L, onResult: (Boolean) -> Unit) {

    }
}