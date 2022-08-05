package com.paveltitov.wishlist

import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import com.paveltitov.wishlist.data.Wish
import com.paveltitov.wishlist.fragments.*

class MainActivity : AppCompatActivity() {

    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

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
}