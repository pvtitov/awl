package com.paveltitov.wishlist.ui.login

import android.content.Context
import android.util.Log
import androidx.fragment.app.Fragment
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException

class Authenticator {

    private lateinit var signInClient: GoogleSignInClient

    fun signIn(fragment: Fragment) {
        fragment.context?.apply {
            checkClientInitialized()
            fragment.startActivityForResult(signInClient.signInIntent, REQUEST_CODE)
        }
    }

    fun doIfAllowed(fragment: Fragment, requestCode: Int, action: () -> Unit) {
        when (requestCode) {
            REQUEST_CODE -> {
                try {
                    action()
                } catch (e: ApiException) {
                    Log.d(TAG, "${e.statusCode} ${e.localizedMessage}")
                } catch (e: UserRecoverableAuthIOException) {
                    fragment.startActivityForResult(e.intent, REQUEST_CODE)
                }
            }
        }
    }

    fun signOut() {
        signInClient.signOut()
    }

    private fun Context.checkClientInitialized() {
        if (!this@Authenticator::signInClient.isInitialized) {
            signInClient = GoogleSignIn.getClient(
                this,
                GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestEmail()
                    .build()
            )
        }
    }

    companion object {
        const val TAG = "Authenticator"
        const val REQUEST_CODE = 112233
    }
}

