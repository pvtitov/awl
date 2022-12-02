package com.paveltitov.wishlist.core.entities

import java.io.Serializable

data class Wish(
    val description: String,
    val owner: Person,
    val promisedBy: Person? = null
): Serializable