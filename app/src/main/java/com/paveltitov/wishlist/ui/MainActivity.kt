package com.paveltitov.wishlist.ui

import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import com.paveltitov.wishlist.R
import com.paveltitov.wishlist.core.DataStorage
import com.paveltitov.wishlist.core.ScreenRouter
import com.paveltitov.wishlist.core.entities.Wish
import com.paveltitov.wishlist.di.DI
import com.paveltitov.wishlist.di.factories.DataStorageFactory
import com.paveltitov.wishlist.di.factories.ScreenRouterFactory
import com.paveltitov.wishlist.ui.createwish.CreateWishFragment
import com.paveltitov.wishlist.ui.login.LoginFragment
import com.paveltitov.wishlist.ui.makefriend.MakeFriendFragment
import com.paveltitov.wishlist.ui.register.RegisterFragment
import com.paveltitov.wishlist.ui.wish.WishFragment
import com.paveltitov.wishlist.ui.wishlist.WishlistFragment

class MainActivity : AppCompatActivity() {

    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        initDI()

        progressBar = findViewById(R.id.progress_bar)

        if (savedInstanceState == null) {
            startLogin()
        }
    }

    override fun onBackPressed() {
        supportFragmentManager.apply {
            when {
                backStackEntryCount == 1 -> finish()
                else -> super.onBackPressed()
            }
        }
    }

    fun startLogin() {
        supportFragmentManager
            .beginTransaction()
            .replace(
                R.id.fragment_container_frame_layout,
                LoginFragment()
            )
            .addToBackStack(null)
            .commit()
    }

    fun startRegister() {
        supportFragmentManager
            .beginTransaction()
            .replace(
                R.id.fragment_container_frame_layout,
                RegisterFragment()
            )
            .addToBackStack(null)
            .commit()
    }

    fun startWishlist() {
        supportFragmentManager.apply {
            fun createWishlistFragment() {
                beginTransaction()
                    .replace(
                        R.id.fragment_container_frame_layout,
                        WishlistFragment()
                    )
                    .addToBackStack(null)
                    .commit()
            }
            if (fragments.isNotEmpty() && fragments.first() is RegisterFragment) {
                popBackStackImmediate()
                createWishlistFragment()
            } else if (fragments.isNotEmpty() &&
                (
                        (fragments.first() is WishFragment)
                                || (fragments.first() is CreateWishFragment)
                                || (fragments.first() is MakeFriendFragment)
                        )
            ) {
                popBackStackImmediate()
            } else {
                createWishlistFragment()
            }
        }
    }

    fun startWish(wish: Wish) {
        supportFragmentManager
            .beginTransaction()
            .replace(
                R.id.fragment_container_frame_layout,
                WishFragment.newInstance(wish)
            )
            .addToBackStack(null)
            .commit()
    }

    fun startCreateWish() {
        supportFragmentManager
            .beginTransaction()
            .replace(
                R.id.fragment_container_frame_layout,
                CreateWishFragment()
            )
            .addToBackStack(null)
            .commit()
    }

    fun startMakeFriend() {
        supportFragmentManager
            .beginTransaction()
            .replace(
                R.id.fragment_container_frame_layout,
                MakeFriendFragment()
            )
            .addToBackStack(null)
            .commit()
    }

    fun showProgressBar() {
        progressBar.visibility = View.VISIBLE
    }

    fun hideProgressBar() {
        progressBar.visibility = View.GONE
    }

    private fun initDI() {
        DI.setFactory(ScreenRouter::class, ScreenRouterFactory(supportFragmentManager))
        DI.setFactory(DataStorage::class, DataStorageFactory(this))
    }
}