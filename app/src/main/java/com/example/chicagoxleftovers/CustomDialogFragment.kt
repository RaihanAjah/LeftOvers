package com.example.chicagoxleftovers

import android.os.Bundle
import android.text.Layout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentContainer
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.fragment_custom_dialog.*
import kotlinx.android.synthetic.main.fragment_custom_dialog.view.*
import kotlinx.android.synthetic.main.ulasan_item.*
import kotlinx.android.synthetic.main.ulasan_item.rbRating
import java.util.zip.Inflater



class CustomDialogFragment: DialogFragment() {

    override fun onCreateView(

        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {
        val database = FirebaseDatabase.getInstance().getReference()
        val user = FirebaseAuth.getInstance().currentUser
        val id_rating = database.push().key
        var rootView: View = inflater.inflate(R.layout.fragment_custom_dialog, container, false)
        val id_toko = activity?.intent?.extras?.getString("EXTRA_id")
        var ratings = 0f

//        rootView.rbRating.numStars = 2
//        rootView.rbRating.stepSize = .5f

        rootView.rbRating.setOnRatingBarChangeListener { ratingBar, rating, fromUser ->

            Toast.makeText(requireActivity(), "Rating, $rating", Toast.LENGTH_SHORT).show()
            ratings = rating


        }

        rootView.btBatal.setOnClickListener {
            dismiss()
        }

        rootView.btTambahUlasan.setOnClickListener {

            val ulasan = etUlasan.text.toString()
            if (user != null) {
                if (id_rating != null) {
                    database.child("rating").child(id_rating).setValue(
                        id_toko?.let { it1 -> Rating(id_rating, it1, user.uid, ratings, ulasan) }
                    )
                }
            }
        }
        return rootView
    }
}
