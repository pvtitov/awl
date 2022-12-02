package com.paveltitov.wishlist.core

import com.paveltitov.wishlist.core.entities.Person
import com.paveltitov.wishlist.core.entities.Wish

interface DataStorage {

    fun register(
        login: String,
        password: String,
        onSuccess: () -> Unit,
        onError: (message: String) -> Unit
    )

    fun login(
        login: String,
        password: String,
        onSuccess: () -> Unit,
        onError: (message: String) -> Unit
    )

    fun getMe(
        onSuccess: (me: Person) -> Unit,
        onError: (message: String) -> Unit
    )

    fun getFriends(
        onSuccess: (friends: List<Person>) -> Unit,
        onError: (message: String) -> Unit
    )

    fun makeFriend(
        login: String,
        onSuccess: () -> Unit,
        onError: (message: String) -> Unit
    )

    fun getWishlist(
        person: Person,
        onSuccess: (wishlist: List<Wish>) -> Unit,
        onError: (message: String) -> Unit
    )

    fun makeWish(
        wish: Wish,
        onSuccess: () -> Unit,
        onError: (message: String) -> Unit
    )

    fun deleteWish(
        wish: Wish,
        onSuccess: () -> Unit,
        onError: (message: String) -> Unit
    )

    fun promiseWish(
        wish: Wish,
        onSuccess: () -> Unit,
        onError: (message: String) -> Unit
    )
}