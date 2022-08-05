package com.paveltitov.wishlist.data

data class Wish(
    val description: String,
    val owner: Person,
    val isPromised: Boolean
)
