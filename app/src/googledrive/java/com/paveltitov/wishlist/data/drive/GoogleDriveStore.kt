package com.paveltitov.wishlist.data.drive

import android.app.Activity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.gson.GsonFactory
import com.google.api.services.drive.Drive
import com.google.api.services.drive.DriveScopes
import com.paveltitov.wishlist.R
import com.paveltitov.wishlist.core.DataStorageCoroutines
import com.paveltitov.wishlist.core.entities.Person
import com.paveltitov.wishlist.core.entities.Wish
import java.lang.ref.WeakReference
import java.util.*

class GoogleDriveStore(private val activityWeakReference: WeakReference<Activity>) :
    DataStorageCoroutines {

    private val googleDrive: Drive by lazy {
        initDrive()
    }

    private lateinit var wishList: List<Wish>
    private lateinit var friendsAndWishes: TreeMap<Person, List<Wish>>

    override suspend fun register(
        login: String,
        password: String
    ): DataStorageCoroutines.Response<Unit> {
        throw IllegalStateException("Authentication for Google Drive implemented with GoogleSignIn")
    }

    override suspend fun login(
        login: String,
        password: String
    ): DataStorageCoroutines.Response<Unit> {
        throw IllegalStateException("Authentication for Google Drive implemented with GoogleSignIn")
    }

    override suspend fun getMe(): DataStorageCoroutines.Response<Person> = DataStorageCoroutines.Success(ME)

    override suspend fun getFriends(): DataStorageCoroutines.Response<List<Person>> {
        TODO("Not yet implemented")
    }

    override suspend fun makeFriend(login: String): DataStorageCoroutines.Response<Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun getWishlist(person: Person): DataStorageCoroutines.Response<List<Wish>> {
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
        return DataStorageCoroutines.Success(wishlist)
    }

    override suspend fun makeWish(wish: Wish): DataStorageCoroutines.Response<Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun deleteWish(wish: Wish): DataStorageCoroutines.Response<Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun promiseWish(wish: Wish): DataStorageCoroutines.Response<Unit> {
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

    companion object {
        private const val TAG = "GoogleDriveStore"
        private val ME = Person("")
    }
}