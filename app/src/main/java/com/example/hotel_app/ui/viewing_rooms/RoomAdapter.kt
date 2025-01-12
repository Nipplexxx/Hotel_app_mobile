package com.example.hotel_app.ui.viewing_rooms

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.hotel_app.R
class RoomAdapter(private val roomList: List<RoomModel>) :
    RecyclerView.Adapter<RoomAdapter.RoomViewHolder>() {

    class RoomViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val roomTypeText: TextView = itemView.findViewById(R.id.textRoomType)
        val priceText: TextView = itemView.findViewById(R.id.textPrice)
        val bedsText: TextView = itemView.findViewById(R.id.textBeds)
        val availabilityText: TextView = itemView.findViewById(R.id.textAvailability)
        val descriptionText: TextView = itemView.findViewById(R.id.textDescription)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RoomViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_room_card, parent, false)
        return RoomViewHolder(view)
    }

    override fun onBindViewHolder(holder: RoomViewHolder, position: Int) {
        val room = roomList[position]
        holder.roomTypeText.text = room.roomType
        holder.priceText.text = "Price: ${room.pricePerNight}"
        holder.bedsText.text = "Beds: ${room.numberOfBeds}"
        holder.descriptionText.text = room.description

        if (room.isAvailable) {
            holder.availabilityText.text = "Status: Available"
        } else {
            holder.availabilityText.text = "Status: Booked (${room.bookingDates})"
        }
    }

    override fun getItemCount() = roomList.size
}
