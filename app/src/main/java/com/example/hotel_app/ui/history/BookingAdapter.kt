package com.example.hotel_app.ui.history

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.hotel_app.R

class BookingAdapter(private val bookings: List<Booking>) :
    RecyclerView.Adapter<BookingAdapter.BookingViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookingViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_booking, parent, false)
        return BookingViewHolder(view)
    }

    override fun onBindViewHolder(holder: BookingViewHolder, position: Int) {
        val booking = bookings[position]
        holder.roomType.text = "Room: ${booking.roomId}"
        holder.bookingDates.text = "Dates: ${booking.bookingStartDate} - ${booking.bookingEndDate}"
        holder.totalPrice.text = "Total Price: ${booking.totalPrice} ₽"
        holder.status.text = if (booking.isPaid) "Status: Paid" else "Status: Not Paid"
    }

    override fun getItemCount() = bookings.size

    class BookingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val roomType: TextView = itemView.findViewById(R.id.tv_room_type)
        val bookingDates: TextView = itemView.findViewById(R.id.tv_booking_dates)
        val totalPrice: TextView = itemView.findViewById(R.id.tv_total_price)
        val status: TextView = itemView.findViewById(R.id.tv_status)
    }
}


