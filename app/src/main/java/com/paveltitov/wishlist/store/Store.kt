package com.paveltitov.wishlist.store

import com.paveltitov.wishlist.data.Person
import com.paveltitov.wishlist.data.Wish

interface Store {

    fun register(login: String,
                 password: String,
                 onSuccess: () -> Unit,
                 onError: (message: String) -> Unit
    )

    fun login(login: String,
              password: String,
              onSuccess: () -> Unit,
              onError: (message: String) -> Unit)

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

    object Factory {
        val singleInstance = StoreStub()
    }
}