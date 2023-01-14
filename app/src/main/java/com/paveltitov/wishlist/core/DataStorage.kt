package com.paveltitov.wishlist.core

import com.paveltitov.wishlist.core.entities.Person
import com.paveltitov.wishlist.core.entities.Wish

interface DataStorage {

    suspend fun register(
        login: String,
        password: String
    ): Response<Unit>

    suspend fun login(
        login: String,
        password: String
    ): Response<Unit>

    suspend fun getMe(): Response<Person>

    suspend fun getFriends(): Response<List<Person>>

    suspend fun makeFriend(login: String): Response<Unit>

    suspend fun getWishlist(person: Person): Response<List<Wish>>

    suspend fun makeWish(wish: Wish): Response<Unit>

    suspend fun deleteWish(wish: Wish): Response<Unit>

    suspend fun promiseWish(wish: Wish): Response<Unit>

    sealed interface Response<T>
    class Success<T>(val data: T) : Response<T>
    class Failure<T>(val message: String) : Response<T>
}