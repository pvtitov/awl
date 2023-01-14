package com.paveltitov.wishlist.ui.login

import android.content.Context
import android.util.Log
import androidx.fragment.app.Fragment
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException
import com.paveltitov.wishlist.core.Authenticator
import com.paveltitov.wishlist.core.Authenticator.Companion.REQUEST_CODE
import java.lang.ref.WeakReference

class AuthenticatorImpl(private val fragmentWeakReference: WeakReference<Fragment>) :
    Authenticator {

    private lateinit var signInClient: GoogleSignInClient

    override fun signIn() {
        fragmentWeakReference.get()?.context?.apply {
            checkClientInitialized()
            fragmentWeakReference.get()
                ?.startActivityForResult(signInClient.signInIntent, REQUEST_CODE)
        } ?: logFragmentIsNull()
    }

    override fun onSuccessfulResponse(action: () -> Unit) {
        try {
            action.invoke()
        } catch (e: ApiException) {
            Log.d(TAG, "${e.statusCode} ${e.localizedMessage}")
        } catch (e: UserRecoverableAuthIOException) {
            fragmentWeakReference.get()?.startActivityForResult(e.intent, REQUEST_CODE)
                ?: logFragmentIsNull()
        }
    }

    override fun signOut() {
        signInClient.signOut()
    }

    private fun Context.checkClientInitialized() {
        if (!this@AuthenticatorImpl::signInClient.isInitialized) {
            signInClient = GoogleSignIn.getClient(
                this,
                GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestEmail()
                    .build()
            )
        }
    }

    private fun logFragmentIsNull() {
        Log.d(TAG, "Fragment is null")
    }

    companion object {
        private const val TAG = "Authenticator"
    }
}

