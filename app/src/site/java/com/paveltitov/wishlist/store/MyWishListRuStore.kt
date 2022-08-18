package com.paveltitov.wishlist.store

import android.os.Handler
import android.os.Looper
import com.paveltitov.wishlist.data.Person
import com.paveltitov.wishlist.data.Wish

class MyWishListRuStore(private val httpClient: HttpClient) : Store {
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
        val mainHandler = Handler(Looper.getMainLooper())
        Thread {
            val response = httpClient.login("pavel_titov", "just4E")
            when (response) {
                is HttpClient.Response.Success -> response.response
                is HttpClient.Response.Error -> response.message
            }.let { raw ->
                val results = mutableListOf<String>()
                raw
                    .split("<table class='Virgin'><tr><td><h5><a href=")
                    .drop(0)
                    .forEach { it
                        .substringAfter("\">")
                        .substringBefore("</a>")
                    }

                mainHandler.post {
                    onError(results.toString())
                }
            }
        }.start()
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