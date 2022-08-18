package com.paveltitov.wishlist.store

import android.util.Log
import com.paveltitov.wishlist.data.Person
import com.paveltitov.wishlist.data.Wish
import java.io.*
import java.net.HttpURLConnection
import java.net.URL
import java.nio.charset.Charset

class MyWishListRuStore : Store {
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
        Thread {
            val httpURLConnection =
                URL("http://www.mywishlist.ru/").openConnection() as HttpURLConnection
            try {
                val inputStream: InputStream = BufferedInputStream(httpURLConnection.inputStream)
                val stringBuilder = StringBuilder()
                BufferedReader(
                    InputStreamReader(inputStream, Charset.forName("UTF-8"))
                ).use { reader ->
                    var ch = 0
                    while (reader.read().also { ch = it } != -1) {
                        stringBuilder.append(ch.toChar())
                    }
                    Log.d("happy", stringBuilder.toString())
                }
            } finally {
                httpURLConnection.disconnect()
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