package com.paveltitov.wishlist.store

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential
import com.google.api.services.drive.Drive
import java.util.*

class GoogleDriveFacade {

    private val drive: Drive by lazy {
        TODO("Not implemented")
    }

    fun syncData(timeout: Long = 5000L, onResult: (Boolean) -> Unit) {

    }
}