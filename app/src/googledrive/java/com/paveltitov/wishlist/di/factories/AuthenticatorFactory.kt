package com.paveltitov.wishlist.di.factories

import androidx.fragment.app.Fragment
import com.paveltitov.wishlist.core.Authenticator
import com.paveltitov.wishlist.ui.login.AuthenticatorImpl
import java.lang.ref.WeakReference

class AuthenticatorFactory(fragment: Fragment) : Factory<Authenticator> {

    private val fragmentWeakReference = WeakReference(fragment)

    override fun create(): Authenticator {
        return AuthenticatorImpl(fragmentWeakReference)
    }
}