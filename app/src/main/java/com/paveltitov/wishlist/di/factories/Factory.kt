package com.paveltitov.wishlist.di.factories

interface Factory<C> {
    fun create(): C
}