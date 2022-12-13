package com.paveltitov.wishlist.ui.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.paveltitov.wishlist.R
import com.paveltitov.wishlist.data.drive.DriveManager


class LoginFragment : Fragment() {

    private lateinit var progressBar: ProgressBar
    private lateinit var button: SignInButton
    private lateinit var signInClient: GoogleSignInClient

    override fun onAttach(context: Context) {
        super.onAttach(context)

        signInClient = GoogleSignIn.getClient(
            context,
            GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build()
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViews()
        button.setOnClickListener {
            startActivityForResult(signInClient.signInIntent, SIGN_IN_REQUEST_CODE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            SIGN_IN_REQUEST_CODE, 123 -> {
                try {
                    activity?.let { act ->
                        DriveManager().makeSureFileExists(act)
                    }
                } catch (e: ApiException) {
                    Log.d(TAG, "${e.statusCode} ${e.localizedMessage}")
                }
            }
        }
    }

    private fun View.findViews() {
        progressBar = findViewById(R.id.login_progress_bar)
        button = findViewById(R.id.login_sign_in_button)
    }

    companion object {
        const val SIGN_IN_REQUEST_CODE = 4422
        const val TAG = "LoginFragment"
    }
}