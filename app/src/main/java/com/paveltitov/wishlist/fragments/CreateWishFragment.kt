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
import com.paveltitov.wishlist.data.Wish
import com.paveltitov.wishlist.store.Store
import com.paveltitov.wishlist.store.StoreFactory

class CreateWishFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_create_wish, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val titleEditText = view.findViewById<EditText>(R.id.create_wish_title_edit_text)
        view.findViewById<Button>(R.id.create_wish_confirm_button)?.setOnClickListener {
            (activity as? MainActivity)?.showProgressBar()
            StoreFactory.store.getMe(
                { me ->
                    val wish = Wish(titleEditText.text.toString(), me)
                    StoreFactory.store.makeWish(
                        wish,
                        {
                            (activity as? MainActivity)?.hideProgressBar()
                            Toast.makeText(
                                view.context,
                                "Wish was made: ${wish.description}",
                                Toast.LENGTH_SHORT
                            ).show()
                            (activity as MainActivity).startWishlist()
                        },
                        { message ->
                            (activity as? MainActivity)?.hideProgressBar()
                            Toast.makeText(view.context, message, Toast.LENGTH_LONG).show()
                        }
                    )
                },
                { message ->
                    (activity as? MainActivity)?.hideProgressBar()
                    Toast.makeText(view.context, message, Toast.LENGTH_LONG).show()
                }
            )
        }
    }
}