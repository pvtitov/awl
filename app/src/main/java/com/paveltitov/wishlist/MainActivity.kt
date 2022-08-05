package com.paveltitov.wishlist

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.paveltitov.wishlist.fragments.*

private const val LOGIN_FRAGMENT_TAG = "LOGIN_FRAGMENT_TAG"

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            startLogin()
        }
    }

    override fun onBackPressed() {
        if (isLoginFragment()) {
            finish()
        } else {
            super.onBackPressed()
        }
    }

    fun startLogin() {
        supportFragmentManager.apply {
                popAllBackStack()
                beginTransaction()
                    .replace(
                        R.id.fragment_container_frame_layout,
                        LoginFragment(),
                        LOGIN_FRAGMENT_TAG
                    )
                    .addToBackStack(LoginFragment::class.simpleName)
                    .commit()
            }
    }

    fun startRegister() {
        supportFragmentManager.apply {
            popToLogin()
            beginTransaction()
                .replace(
                    R.id.fragment_container_frame_layout,
                    RegisterFragment()
                )
                .addToBackStack(RegisterFragment::class.simpleName)
                .commit()
        }
    }

    fun startWishlist() {
        supportFragmentManager.apply {
                beginTransaction()
                    .replace(
                        R.id.fragment_container_frame_layout,
                        WishlistFragment(),
                        LOGIN_FRAGMENT_TAG
                    )
                    .addToBackStack(WishlistFragment::class.simpleName)
                    .commit()
            }
    }

    fun startWish() {
        supportFragmentManager.apply {
                beginTransaction()
                    .replace(
                        R.id.fragment_container_frame_layout,
                        WishFragment(),
                        LOGIN_FRAGMENT_TAG
                    )
                    .addToBackStack(WishFragment::class.simpleName)
                    .commit()
            }
    }

    fun startCreateWish() {
        supportFragmentManager.apply {
                beginTransaction()
                    .replace(
                        R.id.fragment_container_frame_layout,
                        CreateWishFragment(),
                        LOGIN_FRAGMENT_TAG
                    )
                    .addToBackStack(CreateWishFragment::class.simpleName)
                    .commit()
            }
    }

    private fun FragmentManager.popAllBackStack(){
        for (i in 0 until backStackEntryCount) {
            popBackStack()
        }
    }

    private fun FragmentManager.popToLogin() {
        for (i in 0 until backStackEntryCount - 1) {
            popBackStack()
        }
        if (!isLoginFragment()) {
            throw IllegalStateException("LoginFragment should always be at bottom of fragments stack")
        }
    }

    private fun isLoginFragment(): Boolean =
        with(supportFragmentManager) {
            backStackEntryCount == 1 && findFragmentByTag(LOGIN_FRAGMENT_TAG) != null
        }
}