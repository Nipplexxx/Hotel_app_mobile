package com.example.hotel_app.ui.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.hotel_app.R
import com.example.hotel_app.databinding.ItemBookingBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

data class Booking(
    val guestId: String = "",
    val roomId: String = "",
    val bookingStartDate: String = "",
    val bookingEndDate: String = "",
    val totalPrice: Double = 0.0,
    val isPaid: Boolean = false
)

class HistoryFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var noHistoryText: TextView
    private val bookings = mutableListOf<Booking>()
    private val database = Firebase.database

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_history, container, false)
        recyclerView = view.findViewById(R.id.recycler_view_history)
        noHistoryText = view.findViewById(R.id.tv_no_history)

        // Получаем ID пользователя из аргументов
        val userId = arguments?.getString("userId") ?: "Неизвестно"

        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = BookingAdapter(bookings)

        loadBookingHistory(userId)
        return view
    }

    private fun loadBookingHistory(userId: String) {
        val bookingsRef = database.getReference("Bookings")
        bookingsRef.orderByChild("guestId").equalTo(userId)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    bookings.clear()
                    for (bookingSnapshot in snapshot.children) {
                        val booking = bookingSnapshot.getValue(Booking::class.java)
                        if (booking != null) {
                            bookings.add(booking)
                        }
                    }
                    if (bookings.isEmpty()) {
                        noHistoryText.visibility = View.VISIBLE
                        recyclerView.visibility = View.GONE
                    } else {
                        noHistoryText.visibility = View.GONE
                        recyclerView.visibility = View.VISIBLE
                        recyclerView.adapter?.notifyDataSetChanged()
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    noHistoryText.visibility = View.VISIBLE
                    recyclerView.visibility = View.GONE
                }
            })
    }
}
