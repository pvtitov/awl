package com.paveltitov.wishlist.ui.wish

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.coroutineScope
import com.paveltitov.wishlist.R
import com.paveltitov.wishlist.core.DataStorage
import com.paveltitov.wishlist.core.entities.Wish
import com.paveltitov.wishlist.di.DI
import com.paveltitov.wishlist.ui.MainActivity
import kotlinx.coroutines.launch

class WishFragment : Fragment() {

    private lateinit var wish: Wish

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val args = arguments
        if (args != null) {
            wish = args.getSerializable(WISH) as Wish
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
        lifecycle.coroutineScope.launch {
            val dataStorage = (DI.get(DataStorage::class) as DataStorage)
            when (val response = dataStorage.getMe()) {
                is DataStorage.Success -> {
                    val me = response.data
                    (activity as? MainActivity)?.hideProgressBar()
                    if (me == wish.owner) {
                        deleteButton.visibility = View.VISIBLE
                        promiseButton.visibility = View.GONE
                        deleteButton.setOnClickListener {
                            (activity as? MainActivity)?.showProgressBar()
                            lifecycle.coroutineScope.launch {
                                when (val response = dataStorage.deleteWish(wish)) {
                                    is DataStorage.Success -> {
                                        (activity as? MainActivity)?.hideProgressBar()
                                        Toast.makeText(
                                            view.context,
                                            "Wish ${wish.description} deleted",
                                            Toast.LENGTH_LONG
                                        ).show()
                                        (activity as? MainActivity)?.startWishlist()
                                    }
                                    is DataStorage.Failure -> {
                                        (activity as? MainActivity)?.hideProgressBar()
                                        Toast.makeText(
                                            view.context,
                                            response.message,
                                            Toast.LENGTH_LONG
                                        ).show()
                                    }
                                }
                            }
                        }
                    } else {
                        deleteButton.visibility = View.GONE
                        promiseButton.visibility = View.VISIBLE
                        promiseButton.setOnClickListener {
                            (activity as? MainActivity)?.showProgressBar()
                            lifecycle.coroutineScope.launch {
                                when (val response = dataStorage.promiseWish(wish)) {
                                    is DataStorage.Success -> {
                                        (activity as? MainActivity)?.hideProgressBar()
                                        Toast.makeText(
                                            view.context,
                                            "Wish ${wish.description} promised to ${wish.owner.login}",
                                            Toast.LENGTH_LONG
                                        ).show()
                                        (activity as? MainActivity)?.startWishlist()
                                    }
                                    is DataStorage.Failure -> {
                                        (activity as? MainActivity)?.hideProgressBar()
                                        Toast.makeText(
                                            view.context,
                                            response.message,
                                            Toast.LENGTH_LONG
                                        ).show()
                                    }
                                }
                            }
                        }
                    }
                }
                is DataStorage.Failure -> {
                    (activity as? MainActivity)?.hideProgressBar()
                    Toast.makeText(view.context, response.message, Toast.LENGTH_LONG).show()
                }
            }
        }
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