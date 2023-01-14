package com.paveltitov.wishlist.ui.createwish

import android.os.Bundle
import android.util.Log
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
import com.paveltitov.wishlist.core.entities.Wish
import com.paveltitov.wishlist.di.DI
import com.paveltitov.wishlist.ui.MainActivity
import com.paveltitov.wishlist.ui.UIException
import kotlinx.coroutines.launch

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

            lifecycle.coroutineScope.launch {
                try {
                    val me = when (val result =
                        (DI.get(DataStorage::class) as DataStorage).getMe()) {
                        is DataStorage.Success -> result.data
                        is DataStorage.Failure -> throw UIException(result.message)
                    }

                    val wish = Wish(titleEditText.text.toString(), me)

                    when (val result =
                        (DI.get(DataStorage::class) as DataStorage).makeWish(
                            wish
                        )) {
                        is DataStorage.Success -> {
                            (activity as? MainActivity)?.hideProgressBar()
                            Toast.makeText(
                                view.context,
                                "Wish was made: ${wish.description}",
                                Toast.LENGTH_SHORT
                            ).show()
                            (activity as MainActivity).startWishlist()
                        }
                        is DataStorage.Failure -> throw UIException(result.message)
                    }
                } catch (e: UIException) {
                    (activity as? MainActivity)?.hideProgressBar()
                    Toast.makeText(view.context, e.message, Toast.LENGTH_LONG).show()
                    Log.d(TAG, e.message ?: "UIException")
                }
            }
        }
    }

    companion object {
        private const val TAG = "CreateWishFragment"
    }
}