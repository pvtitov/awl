package com.paveltitov.wishlist.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.paveltitov.wishlist.MainActivity
import com.paveltitov.wishlist.R
import com.paveltitov.wishlist.data.Person
import com.paveltitov.wishlist.data.Wish
import com.paveltitov.wishlist.store.Store

class WishlistFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_wish_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val wishListRecyclerView = view.findViewById<RecyclerView>(R.id.wishlist_recycler_view)
        val combinedList = mutableListOf<Person>()

        fun updateAdapter() {
            wishListRecyclerView.adapter = WishlistAdapter(combinedList) { wish ->
                (activity as MainActivity).startWish(wish)
            }
        }

        with(Store.Factory.singleInstance) {
            (activity as? MainActivity)?.showProgressBar()
            getMe(
                onSuccess = { me ->
                    (activity as? MainActivity)?.hideProgressBar()
                    combinedList.add(0, me)
                    updateAdapter()
                },
                onError = { message ->
                    (activity as? MainActivity)?.hideProgressBar()
                    Toast.makeText(view.context, message, Toast.LENGTH_LONG).show()
                }
            )
            (activity as? MainActivity)?.showProgressBar()
            getFriends(
                onSuccess = { friendList ->
                    (activity as? MainActivity)?.showProgressBar()
                    combinedList.addAll(friendList)
                    updateAdapter()
                },
                onError = { message ->
                    (activity as? MainActivity)?.showProgressBar()
                    Toast.makeText(view.context, message, Toast.LENGTH_LONG).show()
                }
            )
        }
        view.findViewById<Button>(R.id.wishlist_create_wish_button).setOnClickListener {
            (activity as MainActivity).startCreateWish()
        }
        view.findViewById<Button>(R.id.wishlist_make_friend_button).setOnClickListener {
            (activity as MainActivity).startMakeFriend()
        }
    }

    class WishlistAdapter(personList: List<Person>, val onClick: (Wish) -> Unit) :
        RecyclerView.Adapter<RecyclerView.ViewHolder>() {

        private val flatData: List<*>
        private val personTypeIndexSet: Set<Int>

        init {
            var count = 0
            flatData = mutableListOf<Any>()
            personTypeIndexSet = mutableSetOf()

            personList.forEach { person ->
                flatData.add(person)
                personTypeIndexSet.add(count++)
                Store.Factory.singleInstance.getWishlist(
                    person = person,
                    onSuccess = { wishlist ->
                        wishlist.forEach { wish ->
                            flatData.add(wish)
                            count++
                        }
                    },
                    onError = { message ->
                        TODO("Handle error")
                    }
                )
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            return when (viewType) {
                ViewTypes.PERSON.ordinal -> {
                    val view = LayoutInflater.from(parent.context)
                        .inflate(R.layout.item_person, parent, false)
                    PersonViewHolder(view)
                }
                else -> {
                    val view = LayoutInflater.from(parent.context)
                        .inflate(R.layout.item_wish, parent, false)
                    WishViewHolder(view)
                }
            }
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            when (holder) {
                is PersonViewHolder -> holder.bind(flatData[position] as Person)
                else -> (flatData[position] as Wish).let { wish ->
                    (holder as WishViewHolder).bind(wish) { onClick(wish) }
                }
            }
        }

        override fun getItemCount(): Int {
            return flatData.size
        }

        override fun getItemViewType(position: Int): Int {
            return if (personTypeIndexSet.contains(position)) {
                ViewTypes.PERSON.ordinal
            } else {
                ViewTypes.WISH.ordinal
            }
        }
    }

    class PersonViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        fun bind(person: Person) {
            itemView.findViewById<TextView>(R.id.item_person_text_view).text = person.login
        }
    }

    class WishViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        fun bind(wish: Wish, onClick: () -> Unit) {
            with(itemView.findViewById<TextView>(R.id.item_wish_text_view)) {
                text =
                    "${wish.description} - ${if (wish.promisedBy != null) "??????????????" else "????????????????"}"
                setOnClickListener { onClick() }
            }
        }
    }

    private enum class ViewTypes {
        PERSON,
        WISH
    }
}