package com.paveltitov.wishlist.data.drive

import android.app.Activity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.gson.GsonFactory
import com.google.api.services.drive.Drive
import com.google.api.services.drive.DriveScopes
import com.paveltitov.wishlist.R
import com.paveltitov.wishlist.core.DataStorage
import com.paveltitov.wishlist.core.entities.Person
import com.paveltitov.wishlist.core.entities.Wish
import java.lang.ref.WeakReference
import java.util.*

class GoogleDriveStore(private val activityWeakReference: WeakReference<Activity>) : DataStorage {

    private val googleDrive: Drive by lazy {
        initDrive()
    }

    private lateinit var wishList: List<Wish>
    private lateinit var friendsAndWishes: TreeMap<Person, List<Wish>>

    override fun register(
        login: String,
        password: String,
        onSuccess: () -> Unit,
        onError: (message: String) -> Unit
    ) {
        throw IllegalStateException("Authentication for Google Drive implemented with GoogleSignIn")
    }

    override fun login(
        login: String,
        password: String,
        onSuccess: () -> Unit,
        onError: (message: String) -> Unit
    ) {
        throw IllegalStateException("Authentication for Google Drive implemented with GoogleSignIn")
    }

    override fun getMe(onSuccess: (me: Person) -> Unit, onError: (message: String) -> Unit) {
        onSuccess(ME)
    }

    override fun getFriends(
        onSuccess: (friends: List<Person>) -> Unit,
        onError: (message: String) -> Unit
    ) {

    }

    override fun makeFriend(
        login: String,
        onSuccess: () -> Unit,
        onError: (message: String) -> Unit
    ) {

    }

    override fun getWishlist(
        person: Person,
        onSuccess: (wishlist: List<Wish>) -> Unit,
        onError: (message: String) -> Unit
    ) {
        onSuccess(
            when (person) {
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
        )
    }

    private fun loadWishList(person: Person): List<Wish> {
        googleDrive // TODO remove
        return when (person) {
            ME -> { TODO("Not yet implemented") }
            else -> { TODO("Not yet implemented") }
        }
    }

    override fun makeWish(wish: Wish, onSuccess: () -> Unit, onError: (message: String) -> Unit) {

    }

    override fun deleteWish(wish: Wish, onSuccess: () -> Unit, onError: (message: String) -> Unit) {

    }

    override fun promiseWish(
        wish: Wish,
        onSuccess: () -> Unit,
        onError: (message: String) -> Unit
    ) {

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

    companion object {
        private const val TAG = "GoogleDriveStore"
        private val ME = Person("")
    }
}