package com.paveltitov.wishlist.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.paveltitov.wishlist.MainActivity
import com.paveltitov.wishlist.R
import com.paveltitov.wishlist.store.Store

class LoginFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val loginEditText = view.findViewById<EditText>(R.id.login_edit_text)
        val passwordEditText = view.findViewById<EditText>(R.id.password_edit_text)
        view.findViewById<Button>(R.id.login_confirm_button)?.setOnClickListener {
            (activity as? MainActivity)?.showProgressBar()
            Store.Factory.singleInstance.login(
                login = loginEditText.text.toString(),
                password = passwordEditText.text.toString(),
                onSuccess = {
                    (activity as? MainActivity)?.hideProgressBar()
                    (activity as? MainActivity)?.startWishlist()
                },
                onError = { message ->
                    (activity as? MainActivity)?.hideProgressBar()
                    Toast.makeText(view.context, message, Toast.LENGTH_LONG).show()
                }
            )
        }
        view.findViewById<Button>(R.id.login_register_button)?.setOnClickListener {
            (activity as? MainActivity)?.startRegister()
        }
    }
}