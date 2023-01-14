package com.paveltitov.wishlist.data.drive

import android.app.Activity
import android.util.Log
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential
import com.google.api.client.googleapis.json.GoogleJsonResponseException
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.gson.GsonFactory
import com.google.api.services.drive.Drive
import com.google.api.services.drive.DriveScopes
import com.paveltitov.wishlist.R
import com.paveltitov.wishlist.core.DataStorage
import com.paveltitov.wishlist.core.entities.Person
import com.paveltitov.wishlist.core.entities.Wish
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import java.lang.ref.WeakReference
import java.util.*

class GoogleDriveStore(private val activityWeakReference: WeakReference<Activity>) :
    DataStorage {

    private val googleDrive: Drive by lazy {
        initDrive()
    }

    private lateinit var wishList: List<Wish>
    private lateinit var friendsAndWishes: TreeMap<Person, List<Wish>>

    override suspend fun register(
        login: String,
        password: String
    ): DataStorage.Response<Unit> {
        throw IllegalStateException("Authentication for Google Drive implemented with GoogleSignIn")
    }

    override suspend fun login(
        login: String,
        password: String
    ): DataStorage.Response<Unit> {
        throw IllegalStateException("Authentication for Google Drive implemented with GoogleSignIn")
    }

    override suspend fun getMe(): DataStorage.Response<Person> =
        DataStorage.Success(ME)

    override suspend fun getFriends(): DataStorage.Response<List<Person>> {
        TODO("Not yet implemented")
    }

    override suspend fun makeFriend(login: String): DataStorage.Response<Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun getWishlist(person: Person): DataStorage.Response<List<Wish>> {
        val wishlist = when (person) {
            ME -> {
                if (::wishList.isInitialized) {
                    wishList
                } else {
                    loadWishList(ME)
                }
            }
            else -> {
                if (::friendsAndWishes.isInitialized) {
                    friendsAndWishes[person] ?: emptyList()
                } else {
                    loadWishList(person)
                }
            }
        }
        return DataStorage.Success(wishlist)
    }

    override suspend fun makeWish(wish: Wish): DataStorage.Response<Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun deleteWish(wish: Wish): DataStorage.Response<Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun promiseWish(wish: Wish): DataStorage.Response<Unit> {
        TODO("Not yet implemented")
    }

    private fun initDrive(): Drive {
        val activity = activityWeakReference.get()
            ?: throw IllegalStateException("Activity should not be null")
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

    private fun loadWishList(person: Person): List<Wish> {
        googleDrive // TODO remove
        return when (person) {
            ME -> {
                TODO("Not yet implemented")
            }
            else -> {
                TODO("Not yet implemented")
            }
        }
    }

    private suspend fun download(file: AwlFile) {
        withContext(Dispatchers.IO) {
            try {
                val outputStream = ByteArrayOutputStream()
                googleDrive.files().get(file.id).executeMediaAndDownloadTo(outputStream)
            } catch (e: GoogleJsonResponseException) {
                Log.d(TAG, e.message ?: "GoogleJsonResponseException while downloading file")
            }
        }
    }

    private fun upload(file: AwlFile) {

    }

    private fun findFile(): AwlFile {
        TODO()
        // найти файл
    }

    class AwlFile(val id: String)

    companion object {
        private const val TAG = "GoogleDriveStore"
        private val ME = Person("")
    }
}