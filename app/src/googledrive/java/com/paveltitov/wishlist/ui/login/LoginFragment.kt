package com.paveltitov.wishlist.ui.login

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
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.auth.api.identity.BeginSignInResult
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.gms.common.api.ApiException
import com.paveltitov.wishlist.R
import com.paveltitov.wishlist.data.drive.DriveManager
import kotlinx.coroutines.launch


class LoginFragment : Fragment() {

    private lateinit var oneTapClient: SignInClient
    private lateinit var viewModel: LoginViewModel
    private lateinit var progressBar: ProgressBar
    private lateinit var textView: TextView

    override fun onAttach(context: Context) {
        super.onAttach(context)

        viewModel = ViewModelProvider(this)[LoginViewModel::class.java]
        oneTapClient = Identity.getSignInClient(requireActivity())
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
        subscribeOnUiStates()
        textView.apply {
            isFocusable = true
            isClickable = true
            setOnClickListener {
                viewModel.login()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            SIGN_IN_REQUEST_CODE -> {
                try {
                    viewModel.onSuccess(
                        oneTapClient.getSignInCredentialFromIntent(data).id
                    )
                } catch (e: ApiException) {
                    viewModel.onError("${e.statusCode} ${e.localizedMessage}")
                }
            }
        }
    }

    private fun View.findViews() {
        progressBar = findViewById(R.id.login_progress_bar)
        textView = findViewById(R.id.login_text_view)
    }

    private fun subscribeOnUiStates() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiState.collect { state ->
                when (state) {
                    Idle -> {
                        progressBar.visibility = View.GONE
                    }
                    Loading -> {
                        progressBar.visibility = View.VISIBLE
                        activity?.also { activityNonNull ->
                            oneTapClient.beginSignIn(viewModel.buildRequest())
                                .addOnSuccessListener(activityNonNull) { result ->
                                    activityNonNull.startOneTapUI(result)
                                }
                                .addOnFailureListener(activityNonNull) { e ->
                                    viewModel.onError(e.localizedMessage ?: "Failed to open One Tap UI")
                                }
                        }
                    }
                    is Error -> {
                        progressBar.visibility = View.GONE
                        textView.text = context?.getString(R.string.login_authentication_failed)
                        Log.d(TAG, state.message)
                    }
                    is Success -> {
                        progressBar.visibility = View.GONE
                        context?.let { c ->
                            Toast.makeText(c, "Ура! Вы вошли.", Toast.LENGTH_SHORT).show()
                            c.makeSureUserHasWishlistOnGoogleDrive(state.token)
                        }
                    }
                }
            }
        }
    }

    private fun FragmentActivity.startOneTapUI(result: BeginSignInResult) {
        try {
            startIntentSenderForResult(
                result.pendingIntent.intentSender, SIGN_IN_REQUEST_CODE,
                null, 0, 0, 0
            )
        } catch (e: IntentSender.SendIntentException) {
            Log.e(TAG, "Couldn't start One Tap UI: ${e.localizedMessage}")
        }
    }

    private fun Context.makeSureUserHasWishlistOnGoogleDrive(token: String) {
        // TODO refactor the following, start wishllist fragment with idToken put into arguments
        DriveManager().makeSureFileExists(this, token)
    }

    companion object {
        const val SIGN_IN_REQUEST_CODE = 4422
        const val TAG = "LoginFragment"
    }
}