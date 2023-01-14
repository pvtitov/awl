package com.paveltitov.wishlist.ui.register

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.coroutineScope
import com.paveltitov.wishlist.R
import com.paveltitov.wishlist.core.DataStorage
import com.paveltitov.wishlist.di.DI
import com.paveltitov.wishlist.ui.MainActivity
import kotlinx.coroutines.launch

class RegisterFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_register, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val loginEditText = view.findViewById<EditText>(R.id.login_edit_text)
        val passwordEditText = view.findViewById<EditText>(R.id.password_edit_text)
        val confirmPasswordEditText = view.findViewById<EditText>(R.id.confirm_password_edit_text)
        view.findViewById<Button>(R.id.register_confirm_button)?.setOnClickListener {
            if (passwordEditText.text.toString() == confirmPasswordEditText.text.toString()) {
                (activity as? MainActivity)?.showProgressBar()

                lifecycle.coroutineScope.launch {
                    when (val result =
                        (DI.get(DataStorage::class) as DataStorage).register(
                            login = loginEditText.text.toString(),
                            password = passwordEditText.text.toString(),
                        )) {
                        is DataStorage.Success -> {
                            (activity as? MainActivity)?.hideProgressBar()
                            Toast.makeText(
                                view.context,
                                "New user ${loginEditText.text} registered",
                                Toast.LENGTH_LONG
                            ).show()
                            (activity as? MainActivity)?.startWishlist()
                        }
                        is DataStorage.Failure -> {
                            (activity as? MainActivity)?.hideProgressBar()
                            Toast.makeText(view.context, result.message, Toast.LENGTH_LONG).show()
                        }
                    }
                }
            } else {
                Toast.makeText(
                    view.context,
                    getString(R.string.register_different_passwords_message),
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }
}