package com.paveltitov.wishlist.ui.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.coroutineScope
import com.google.android.gms.common.SignInButton
import com.paveltitov.wishlist.R
import com.paveltitov.wishlist.core.Authenticator
import com.paveltitov.wishlist.core.DataStorage
import com.paveltitov.wishlist.di.DI
import com.paveltitov.wishlist.di.factories.AuthenticatorFactory
import kotlinx.coroutines.launch


class LoginFragment : Fragment() {

    private lateinit var auth: Authenticator
    private lateinit var button: SignInButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DI.setFactory(Authenticator::class, AuthenticatorFactory(this))
        auth = DI.get(Authenticator::class) as Authenticator
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        button = view.findViewById(R.id.login_sign_in_button)
        button.setOnClickListener { auth.signIn() }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            Authenticator.REQUEST_CODE -> {
                activity?.let {
                    auth.onSuccessfulResponse {
                        val storage = DI.get(DataStorage::class) as DataStorage
                        lifecycle.coroutineScope.launch {
                            when (val response = storage.getMe()) {
                                is DataStorage.Success ->
                                    storage.getWishlist(response.data)
                                is DataStorage.Failure ->
                                    proccessError(response.message)
                            }
                        }
                    }
                }
            }
        }
    }

    private fun proccessError(message: String) {
        Log.d(TAG, message)
    }

    companion object {
        private const val TAG = "LoginFragment"
    }
}