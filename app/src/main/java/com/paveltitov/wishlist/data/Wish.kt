package com.paveltitov.wishlist.data

import java.io.Serializable

data class Wish(
    val description: String,
    val owner: Person,
    val promisedBy: Person? = null
): Serializable