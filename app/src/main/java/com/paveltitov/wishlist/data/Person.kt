package com.paveltitov.wishlist.data

data class Person(
    val login: String,
    val wishlist: List<Wish>,
    val promiselist: List<Wish>
)