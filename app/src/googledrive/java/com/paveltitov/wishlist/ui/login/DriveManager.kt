package com.paveltitov.wishlist.ui.login

import android.content.Context
import android.util.Log
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential
import com.google.api.services.drive.DriveScopes
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody

class DriveManager {
    fun makeSureFileExists(context: Context, accountName: String) {
        GoogleAccountCredential.usingOAuth2(context, listOf(DriveScopes.DRIVE_FILE))
            .apply {
                selectedAccountName = accountName
            }

        val okHttpClient = OkHttpClient()
        val request = Request.Builder()
            .url("https://www.googleapis.com/upload/drive/v3/files?uploadType=media")
            .post("test".toRequestBody("text/plain".toMediaType()))
            .build()
        okHttpClient.newCall(request).execute().use { response ->
            Log.d("okDrive", "${response.code} ${response.message}" )
        }
    }
}