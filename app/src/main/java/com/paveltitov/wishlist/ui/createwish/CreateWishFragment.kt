package com.paveltitov.wishlist.ui.createwish

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.paveltitov.wishlist.R
import com.paveltitov.wishlist.core.DataStorage
import com.paveltitov.wishlist.core.entities.Wish
import com.paveltitov.wishlist.di.DI
import com.paveltitov.wishlist.ui.MainActivity

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
            (DI.get(DataStorage::class) as DataStorage).getMe(
                { me ->
                    val wish = Wish(titleEditText.text.toString(), me)
                    (DI.get(DataStorage::class) as DataStorage).makeWish(
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