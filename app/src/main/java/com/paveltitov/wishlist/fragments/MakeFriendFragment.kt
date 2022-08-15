package com.paveltitov.wishlist.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.paveltitov.wishlist.MainActivity
import com.paveltitov.wishlist.R
import com.paveltitov.wishlist.store.StoreFactory

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
            StoreFactory.store.makeFriend(
                login,
                {
                    (activity as? MainActivity)?.hideProgressBar()
                    Toast.makeText(
                        view.context,
                        "Friend $login added",
                        Toast.LENGTH_SHORT
                    ).show()
                    (activity as MainActivity).startWishlist()
                },
                { message ->
                    (activity as? MainActivity)?.hideProgressBar()
                    Toast.makeText(view.context, message, Toast.LENGTH_LONG).show()
                }
            )
        }
    }
}