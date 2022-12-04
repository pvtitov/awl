package com.paveltitov.wishlist.data.drive

import android.content.Context
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential
import com.google.api.client.googleapis.json.GoogleJsonResponseException
import com.google.api.client.http.FileContent
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.gson.GsonFactory
import com.google.api.services.drive.Drive
import com.google.api.services.drive.DriveScopes
import com.google.api.services.drive.model.File
import com.paveltitov.wishlist.R

class DriveManager {
    fun makeSureFileExists(context: Context) {

        val googleSignInAccount = GoogleSignIn.getLastSignedInAccount(context)
        val googleAccountCredential =
            GoogleAccountCredential.usingOAuth2(context, listOf(DriveScopes.DRIVE_FILE))
        if (googleSignInAccount != null) {
            googleAccountCredential.selectedAccount = googleSignInAccount.account
        }

        val googleDrive = Drive.Builder(
            NetHttpTransport(),
            GsonFactory.getDefaultInstance(),
            googleAccountCredential
        )
            .setApplicationName(context.getString(R.string.app_name))
            .build()

        val file = File()
        file.name = "awl.json"
        googleDrive.files().create(file).execute()
    }
}