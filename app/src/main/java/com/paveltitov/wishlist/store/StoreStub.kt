package com.paveltitov.wishlist.store

import com.paveltitov.wishlist.data.Person
import com.paveltitov.wishlist.data.Wish

class StoreStub : Store {

    private val personMe = Person("test")
    private val testPassword = "123"

    private val myWishTicketsToMetallica = Wish("Tickets to Metallica", personMe)

    private val friendJohn = Person("john77")

    private val wishBasketball = Wish("Basketball", friendJohn)
    private val wishTicketsToMetallica = Wish("Tickets to Metallica", friendJohn)

    private val friendJessica = Person("honey-bunny")

    private val wishLipstick = Wish("Lipstick", friendJessica)
    private val wishPurse = Wish("Purse", friendJessica)
    private val wishBookMenFromMars = Wish("Book Men from Mars", friendJessica, friendJohn)

    override fun register(
        login: String,
        password: String,
        onSuccess: () -> Unit,
        onError: (message: String) -> Unit
    ) {
        if (login != personMe.login) {
            onSuccess()
        } else {
            onError("Login $login is occupied")
        }
    }

    override fun login(
        login: String,
        password: String,
        onSuccess: () -> Unit,
        onError: (message: String) -> Unit
    ) {
        if (login == personMe.login && password == testPassword) {
            onSuccess()
        } else {
            onError("Wrong login or password")
        }
    }

    override fun getMe(onSuccess: (me: Person) -> Unit, onError: (message: String) -> Unit) {
        onSuccess(personMe)
    }

    override fun getFriends(
        onSuccess: (friends: List<Person>) -> Unit,
        onError: (message: String) -> Unit
    ) {
        onSuccess(listOf(friendJohn, friendJessica))
    }

    override fun makeFriend(
        login: String,
        onSuccess: () -> Unit,
        onError: (message: String) -> Unit
    ) {
        onError("Can't add $login")
    }

    override fun getWishlist(
        person: Person,
        onSuccess: (wishlist: List<Wish>) -> Unit,
        onError: (message: String) -> Unit
    ) {
        when (person) {
            personMe -> onSuccess(listOf(myWishTicketsToMetallica))
            friendJohn -> onSuccess(listOf(wishBasketball, wishTicketsToMetallica))
            friendJessica -> onSuccess(listOf(wishLipstick, wishPurse, wishBookMenFromMars))
            else -> onError("No such person as ${person.login}")
        }
    }

    override fun makeWish(wish: Wish, onSuccess: () -> Unit, onError: (message: String) -> Unit) {
        onSuccess()
    }

    override fun deleteWish(wish: Wish, onSuccess: () -> Unit, onError: (message: String) -> Unit) {
        onSuccess()
    }

    override fun promiseWish(
        wish: Wish,
        onSuccess: () -> Unit,
        onError: (message: String) -> Unit
    ) {
        onSuccess()
    }


}