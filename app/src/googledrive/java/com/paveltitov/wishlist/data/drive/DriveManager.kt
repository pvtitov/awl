package com.paveltitov.wishlist.data.drive

import android.app.Activity
import android.content.Context
import android.os.Handler
import android.os.Looper
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.gson.GsonFactory
import com.google.api.services.drive.Drive
import com.google.api.services.drive.DriveScopes
import com.google.api.services.drive.model.File
import com.paveltitov.wishlist.R
import kotlinx.coroutines.coroutineScope

class DriveManager {
    fun makeSureFileExists(activity: Activity) {

        val googleSignInAccount = GoogleSignIn.getLastSignedInAccount(activity)
        val googleAccountCredential =
            GoogleAccountCredential.usingOAuth2(activity, listOf(DriveScopes.DRIVE_FILE))
        if (googleSignInAccount != null) {
            googleAccountCredential.selectedAccount = googleSignInAccount.account
        }

        val googleDrive = Drive.Builder(
            NetHttpTransport(),
            GsonFactory.getDefaultInstance(),
            googleAccountCredential
        )
            .setApplicationName(activity.getString(R.string.app_name))
            .build()

        val file = File()
        file.name = "awl.json"
        val handler = Handler(Looper.getMainLooper())
        Thread {
            try {
                googleDrive.files().create(file).execute()
            } catch (e: UserRecoverableAuthIOException) {
                handler.post { activity.startActivityForResult(e.intent, 123) }
            }
        }.start()
    }
}