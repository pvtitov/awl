package com.paveltitov.wishlist.ui.makefriend

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

class MakeFriendFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_make_friend, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val titleEditText = view.findViewById<EditText>(R.id.make_friend_title_edit_text)
        view.findViewById<Button>(R.id.make_friend_confirm_button)?.setOnClickListener {
            val login = titleEditText.text.toString()
            (activity as? MainActivity)?.showProgressBar()
            lifecycle.coroutineScope.launch {
                when (val result =
                    (DI.get(DataStorage::class) as DataStorage).makeFriend(
                        login
                    )) {
                    is DataStorage.Success -> {
                        (activity as? MainActivity)?.hideProgressBar()
                        Toast.makeText(
                            view.context,
                            "Friend $login added",
                            Toast.LENGTH_SHORT
                        ).show()
                        (activity as MainActivity).startWishlist()
                    }
                    is DataStorage.Failure -> {
                        (activity as? MainActivity)?.hideProgressBar()
                        Toast.makeText(view.context, result.message, Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }
}