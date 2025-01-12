package com.example.hotel_app.ui.viewing_rooms

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.hotel_app.R
import com.example.hotel_app.databinding.FragmentViewingRoomsBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat
import java.util.Locale

class ViewingRoomsFragment : Fragment() {
    private var _binding: FragmentViewingRoomsBinding? = null
    private val binding get() = _binding!!
    private val roomList = mutableListOf<RoomModel>()
    private val existingBookings = mutableListOf<BookingModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentViewingRoomsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val recyclerView: RecyclerView = binding.recyclerViewRooms
        recyclerView.layoutManager = LinearLayoutManager(context)
        val adapter = RoomAdapter(roomList)
        recyclerView.adapter = adapter

        val database = Firebase.database
        val roomsRef = database.getReference("Rooms")
        val bookingsRef = database.getReference("Bookings")

        bookingsRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                existingBookings.clear()
                for (bookingSnapshot in snapshot.children) {
                    val roomId = bookingSnapshot.child("roomId").getValue(String::class.java) ?: ""
                    val startDate = bookingSnapshot.child("bookingStartDate").getValue(String::class.java) ?: ""
                    val endDate = bookingSnapshot.child("bookingEndDate").getValue(String::class.java) ?: ""

                    existingBookings.add(BookingModel(roomId, startDate, endDate))
                }
                loadRooms(roomsRef, adapter)
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(requireContext(), "Ошибка загрузки данных бронирований.", Toast.LENGTH_SHORT).show()
            }
        })

        return root
    }

    private fun loadRooms(roomsRef: DatabaseReference, adapter: RoomAdapter) {
        roomsRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val availableRooms = mutableListOf<RoomModel>()
                val bookedRooms = mutableListOf<RoomModel>()

                for (roomSnapshot in snapshot.children) {
                    val roomId = roomSnapshot.key ?: ""
                    val roomType = roomSnapshot.child("roomType").getValue(String::class.java) ?: ""
                    val pricePerNight = roomSnapshot.child("pricePerNight").getValue(String::class.java) ?: ""
                    val numberOfBeds = roomSnapshot.child("numberOfBeds").getValue(String::class.java) ?: ""
                    val description = roomSnapshot.child("description").getValue(String::class.java) ?: ""

                    val booking = existingBookings.find { it.roomId == roomId }
                    if (booking == null) {
                        // Номер доступен
                        availableRooms.add(RoomModel(roomId, roomType, pricePerNight, numberOfBeds, description, true))
                    } else {
                        // Номер забронирован
                        val bookingDates = "С ${booking.startDate} по ${booking.endDate}"
                        bookedRooms.add(RoomModel(roomId, roomType, pricePerNight, numberOfBeds, description, false, bookingDates))
                    }
                }

                // Сортируем: доступные номера сверху, забронированные — по дате
                bookedRooms.sortBy { it.bookingDates }

                roomList.clear()
                roomList.addAll(availableRooms)
                roomList.addAll(bookedRooms)

                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(requireContext(), "Ошибка загрузки данных комнат.", Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

