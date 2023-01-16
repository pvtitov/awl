package com.paveltitov.wishlist.data.drive

import android.app.Activity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential
import com.google.api.client.googleapis.json.GoogleJsonResponseException
import com.google.api.client.http.ByteArrayContent
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.gson.GsonFactory
import com.google.api.services.drive.Drive
import com.google.api.services.drive.DriveScopes
import com.google.api.services.drive.model.File
import com.google.api.services.drive.model.FileList
import com.google.gson.Gson
import com.paveltitov.wishlist.R
import com.paveltitov.wishlist.core.entities.Wish
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import java.lang.ref.WeakReference
import java.nio.charset.Charset


class GoogleDriveFacade(
    activityWeakReference: WeakReference<Activity>
) {

    private val gson: Gson by lazy { Gson() }
    private var file: AwlFile? = null

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

    private suspend fun findFile(fileName: String): Result<AwlFile> {
        return withContext(Dispatchers.IO) {
            try {
                var firstFoundFile: File? = null
                var pageToken: String? = null
                do {
                    val result: FileList = googleDrive.files().list()
                        .setQ("name='$fileName'")
                        .setSpaces("appDataFolder")
                        .setFields("nextPageToken, items(id, title)")
                        .setPageToken(pageToken)
                        .execute()
                    if (result.files.isNullOrEmpty().not()) {
                        firstFoundFile = result.files.first()
                    }
                    pageToken = result.nextPageToken
                } while (pageToken != null)

                return@withContext firstFoundFile
                    ?.let {
                        val file = AwlFile(it.id, it.name)
                        this@GoogleDriveFacade.file = file
                        Success(file)
                    }
                    ?: throw IllegalStateException("File $fileName not found")

            } catch (e: GoogleJsonResponseException) {
                Failure(e)
            } catch (e: IllegalStateException) {
                Failure(e)
            }
        }
    }

    suspend fun createFile(): Result<AwlFile> {
        return withContext(Dispatchers.IO) {
            try {
                val resultFile: File = googleDrive.files()
                    .create(File().apply { name = FILE_NAME })
                    .execute()
                val file = AwlFile(resultFile.id, resultFile.name)
                this@GoogleDriveFacade.file = file
                Success(file)
            } catch (e: GoogleJsonResponseException) {
                Failure(e)
            }
        }
    }

    suspend fun downloadFile(fileId: String): Result<Storage> {
        return withContext(Dispatchers.IO) {
            try {
                val outputStream = ByteArrayOutputStream()
                googleDrive.files().get(fileId).executeMediaAndDownloadTo(outputStream)
                val resultString = outputStream.toString("UTF-8")
                val jsonRootObject: Storage = gson.fromJson(resultString, Storage::class.java)
                Success(jsonRootObject)
            } catch (e: GoogleJsonResponseException) {
                Failure(e)
            }
        }
    }

    suspend fun uploadFile(storage: Storage): Result<Storage> {
        return withContext(Dispatchers.IO) {
            try {
                val file = this@GoogleDriveFacade.file
                    ?: throw IllegalStateException("File $FILE_NAME was not found or created")

                val data: ByteArray = gson.toJson(storage).toByteArray(Charset.forName("UTF-8"))

                googleDrive.files()
                    .create(
                        File().apply { name = file.name },
                        ByteArrayContent("application/json", data)
                    )
                    .execute()
                Success(storage)
            } catch (e: GoogleJsonResponseException) {
                Failure(e)
            } catch (e: IllegalStateException) {
                Failure(e)
            }
        }
    }
}

sealed interface Result<T>
data class Success<T>(val data: T) : Result<T>
data class Failure<T>(val e: Exception) : Result<T>

data class Storage(val wishlistByLogin: Map<String, List<Wish>>)

data class AwlFile(val id: String, val name: String)

private const val FILE_NAME = "awl.json"