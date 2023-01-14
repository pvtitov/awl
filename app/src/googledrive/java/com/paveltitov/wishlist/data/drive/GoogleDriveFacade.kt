package com.paveltitov.wishlist.data.drive

import android.app.Activity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential
import com.google.api.client.googleapis.json.GoogleJsonResponseException
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.gson.GsonFactory
import com.google.api.services.drive.Drive
import com.google.api.services.drive.DriveScopes
import com.paveltitov.wishlist.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import java.lang.ref.WeakReference

class GoogleDriveFacade<T>(
    private val file: GoogleDriveFile,
    activityWeakReference: WeakReference<Activity>
) {

    private val googleDrive: Drive by lazy {
        initDrive(
            activityWeakReference.get()
                ?: throw IllegalStateException("Activity should not be null")
        )
    }

    private fun initDrive(activity: Activity): Drive {
        val googleSignInAccount = GoogleSignIn.getLastSignedInAccount(activity)
        val googleAccountCredential =
            GoogleAccountCredential.usingOAuth2(activity, listOf(DriveScopes.DRIVE_FILE))
        if (googleSignInAccount != null) {
            googleAccountCredential.selectedAccount = googleSignInAccount.account
        }

        return Drive.Builder(
            NetHttpTransport(),
            GsonFactory.getDefaultInstance(),
            googleAccountCredential
        )
            .setApplicationName(activity.getString(R.string.app_name))
            .build()
    }

    suspend fun findFile(): Result<T> {
        return withContext(Dispatchers.IO) {
            try {
                TODO()
            } catch (e: GoogleJsonResponseException) {
                Error(e)
            }
        }
    }

    suspend fun createFile(): Result<T> {
        return withContext(Dispatchers.IO) {
            try {
                TODO()
            } catch (e: GoogleJsonResponseException) {
                Error(e)
            }
        }
    }

    suspend fun downloadFile(): Result<T> {
        return withContext(Dispatchers.IO) {
            try {
                val outputStream = ByteArrayOutputStream()
                googleDrive.files().get(file.id).executeMediaAndDownloadTo(outputStream)
                TODO()
            } catch (e: GoogleJsonResponseException) {
                Error(e)
            }
        }
    }

    suspend fun uploadFile(data: T): Result<T> {
        return withContext(Dispatchers.IO) {
            try {
                TODO()
            } catch (e: GoogleJsonResponseException) {
                Error(e)
            }
        }
    }
}

class GoogleDriveFile(val id: String)

sealed interface Result<T>
class Success<T>(val data: T) : Result<T>
class Error<T>(val e: Exception) : Result<T>