package com.paveltitov.wishlist.store

object StoreFactory {
    val store: Store = MyWishListRuStore(HttpClient())
}