package com.paveltitov.wishlist.core

import com.paveltitov.wishlist.core.entities.Wish

interface ScreenRouter {

    fun startLoginScreen()

    fun startRegisterScreen()

    fun startWishlistScreen()

    fun startWishScreen(wish: Wish)

    fun startCreateWishScreen()

    fun startMakeFriendScreen()
}