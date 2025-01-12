package com.example.hotel_app.ui.viewing_rooms

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.hotel_app.databinding.FragmentViewingRoomsBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class ViewingRoomsFragment : Fragment() {
    private var _binding: FragmentViewingRoomsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Удален вызов ViewModelProvider
        _binding = FragmentViewingRoomsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Настройка RecyclerView
        val recyclerView: RecyclerView = binding.recyclerViewRooms
        recyclerView.layoutManager = LinearLayoutManager(context)

        // Настройка базы данных Firebase
        val database = Firebase.database
        val roomsRef = database.getReference("Rooms")
        val roomList = mutableListOf<RoomModel>()

        val adapter = RoomAdapter(roomList)
        recyclerView.adapter = adapter

        roomsRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                roomList.clear()
                for (roomSnapshot in snapshot.children) {
                    val roomType = roomSnapshot.child("roomType").getValue(String::class.java) ?: ""
                    val pricePerNight = roomSnapshot.child("pricePerNight").getValue(String::class.java) ?: ""
                    val numberOfBeds = roomSnapshot.child("numberOfBeds").getValue(String::class.java) ?: ""
                    val availability = roomSnapshot.child("availability").getValue(String::class.java) ?: ""
                    val description = roomSnapshot.child("description").getValue(String::class.java) ?: ""

                    if (availability.lowercase() in listOf("свободная", "свободна", "freedom")) {
                        roomList.add(RoomModel(roomType, pricePerNight, numberOfBeds, availability, description))
                    }
                }
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {}
        })

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}