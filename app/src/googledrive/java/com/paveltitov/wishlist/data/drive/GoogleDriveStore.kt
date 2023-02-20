package com.paveltitov.wishlist.data.drive

import com.paveltitov.wishlist.core.DataStorage
import com.paveltitov.wishlist.core.entities.Person
import com.paveltitov.wishlist.core.entities.Wish
import java.util.*

class GoogleDriveStore(private val googleDriveFacade: GoogleDriveFacade) :
    DataStorage {

    private lateinit var wishList: List<Wish>
    private lateinit var friendsAndWishes: TreeMap<Person, List<Wish>>
    private lateinit var personMe: Person

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
        DataStorage.Success(personMe)

    override suspend fun getFriends(): DataStorage.Response<List<Person>> {
        TODO("Not yet implemented")
    }

    override suspend fun makeFriend(login: String): DataStorage.Response<Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun getWishlist(person: Person): DataStorage.Response<List<Wish>> {
        val wishlist = when (person) {
            personMe -> {
                if (::wishList.isInitialized) {
                    wishList
                } else {
                    loadWishList(personMe)
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

    private fun loadWishList(person: Person): List<Wish> {
        // TODO remove
        return when (person) {
            personMe -> {
                TODO("Not yet implemented")
            }
            else -> {
                TODO("Not yet implemented")
            }
        }
    }
}