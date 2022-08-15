package com.paveltitov.wishlist.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.paveltitov.wishlist.MainActivity
import com.paveltitov.wishlist.R
import com.paveltitov.wishlist.data.Wish
import com.paveltitov.wishlist.store.Store
import com.paveltitov.wishlist.store.StoreFactory

class WishFragment : Fragment() {

    private lateinit var wish: Wish

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val args = arguments
        if (args != null) {
            wish = args.get(WISH) as Wish
        } else {
            throw IllegalStateException("Should always be initialised wish wish argument.")
        }
        return inflater.inflate(R.layout.fragment_wish, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<TextView>(R.id.wish_title_text_view).also { it.text = wish.description }
        view.findViewById<TextView>(R.id.wish_owner_text_view).also { it.text = wish.owner.login }
        view.findViewById<TextView>(R.id.wish_status_text_view).also {
            it.text =
                view.context.getString(if (wish.promisedBy != null) R.string.wish_status_is_promised else R.string.wish_status_is_available)
        }

        val deleteButton = view.findViewById<Button>(R.id.wish_delete_button)
        val promiseButton = view.findViewById<Button>(R.id.wish_promise_button)

        (activity as? MainActivity)?.showProgressBar()
        StoreFactory.store.getMe(
            { me ->
                if (me == wish.owner) {
                    deleteButton.visibility = View.VISIBLE
                    promiseButton.visibility = View.GONE
                    deleteButton.setOnClickListener {
                        StoreFactory.store.deleteWish(
                            wish = wish,
                            onSuccess = {
                                (activity as? MainActivity)?.hideProgressBar()
                                Toast.makeText(
                                    view.context,
                                    "Wish ${wish.description} deleted",
                                    Toast.LENGTH_LONG
                                ).show()
                                (activity as? MainActivity)?.startWishlist()
                            },
                            onError = { message ->
                                (activity as? MainActivity)?.hideProgressBar()
                                Toast.makeText(view.context, message, Toast.LENGTH_LONG).show()
                            }
                        )
                    }
                } else {
                    deleteButton.visibility = View.GONE
                    promiseButton.visibility = View.VISIBLE
                    promiseButton.setOnClickListener {
                        StoreFactory.store.promiseWish(
                            wish = wish,
                            onSuccess = {
                                (activity as? MainActivity)?.hideProgressBar()
                                Toast.makeText(
                                    view.context,
                                    "Wish ${wish.description} promised to ${wish.owner.login}",
                                    Toast.LENGTH_LONG
                                ).show()
                                (activity as? MainActivity)?.startWishlist()
                            },
                            onError = { message ->
                                (activity as? MainActivity)?.hideProgressBar()
                                Toast.makeText(view.context, message, Toast.LENGTH_LONG).show()
                            }
                        )
                    }
                }
            },
            { message ->
                (activity as? MainActivity)?.hideProgressBar()
                Toast.makeText(view.context, message, Toast.LENGTH_LONG).show()
            }
        )
    }

    companion object {

        private const val WISH = "WISH"

        fun newInstance(wish: Wish): WishFragment {
            return WishFragment().apply {
                arguments = Bundle().apply { putSerializable(WISH, wish) }
            }
        }
    }
}