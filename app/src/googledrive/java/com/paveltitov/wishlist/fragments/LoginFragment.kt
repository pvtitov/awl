package com.paveltitov.wishlist.fragments

import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.gms.common.api.ApiException
import com.paveltitov.wishlist.BuildConfig.SERVER_CLIENT_ID
import com.paveltitov.wishlist.R


class LoginFragment : Fragment() {

    private lateinit var oneTapClient: SignInClient
    private lateinit var signInRequest: BeginSignInRequest

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getFromUserGoogleIdToken(view)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            SIGN_IN_REQUEST_CODE -> {
                try {
                    val credential = oneTapClient.getSignInCredentialFromIntent(data)
                    val accountName = credential.id
                    makeSureUserHasWishlistOnGoogleDrive(accountName)
                } catch (e: ApiException) {
                    Log.e(TAG, "${e.statusCode} ${e.localizedMessage}")
                }
            }
        }
    }

    private fun getFromUserGoogleIdToken(view: View) {
        val nonNullActivity = activity ?: throw IllegalStateException("Activity can not be null")

        oneTapClient = Identity.getSignInClient(nonNullActivity)
        signInRequest = BeginSignInRequest.builder()
            .setGoogleIdTokenRequestOptions(
                BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                    .setSupported(true)
                    // Your server's client ID, not your Android client ID.
                    .setServerClientId(SERVER_CLIENT_ID)
                    .build()
            )
            .build()

        oneTapClient.beginSignIn(signInRequest)
            .addOnSuccessListener(nonNullActivity) { result ->
                try {
                    startIntentSenderForResult(
                        result.pendingIntent.intentSender, SIGN_IN_REQUEST_CODE,
                        null, 0, 0, 0, null
                    )
                } catch (e: IntentSender.SendIntentException) {
                    Log.e(TAG, "Couldn't start One Tap UI: ${e.localizedMessage}")
                }
            }
            .addOnFailureListener(nonNullActivity) { e ->
                view.findViewById<ProgressBar>(R.id.login_progress_bar).visibility = View.GONE
                view.findViewById<TextView>(R.id.login_text_view).text =
                    nonNullActivity.getString(R.string.login_authentication_failed)
                e.localizedMessage?.let { Log.d(TAG, it) }
            }
    }

    private fun makeSureUserHasWishlistOnGoogleDrive(accountName: String) {
        context?.let {
            Toast.makeText(it, "Ура! Вы вошли.", Toast.LENGTH_SHORT).show()
            // TODO refactor the following, start wishllist fragment with idToken put into arguments
            DriveManager().makeSureFileExists(it, accountName)
        }
    }

    companion object {
        const val SIGN_IN_REQUEST_CODE = 4422
        const val TAG = "LoginFragment"
    }
}