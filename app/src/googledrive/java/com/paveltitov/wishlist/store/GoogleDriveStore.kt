package com.paveltitov.wishlist.store

import com.paveltitov.wishlist.data.Person
import com.paveltitov.wishlist.data.Wish

class GoogleDriveStore(private val httpClient: HttpClient) : Store {
    override fun register(
        login: String,
        password: String,
        onSuccess: () -> Unit,
        onError: (message: String) -> Unit
    ) {

    }

    override fun login(
        login: String,
        password: String,
        onSuccess: () -> Unit,
        onError: (message: String) -> Unit
    ) {

    }

    override fun getMe(onSuccess: (me: Person) -> Unit, onError: (message: String) -> Unit) {

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
}