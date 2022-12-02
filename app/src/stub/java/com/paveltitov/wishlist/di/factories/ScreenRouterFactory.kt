package com.paveltitov.wishlist.di.factories

import androidx.fragment.app.FragmentManager
import com.paveltitov.wishlist.core.ScreenRouter
import com.paveltitov.wishlist.ui.ScreenRouterImpl

class ScreenRouterFactory(private val fragmentManager: FragmentManager) : Factory<ScreenRouter> {
    override fun create(): ScreenRouter {
        return ScreenRouterImpl(fragmentManager)
    }
}