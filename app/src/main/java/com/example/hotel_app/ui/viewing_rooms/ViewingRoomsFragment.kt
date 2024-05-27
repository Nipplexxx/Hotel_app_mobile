package com.example.hotel_app.ui.viewing_rooms

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
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
        ViewModelProvider(this).get(ViewingRoomsViewModel::class.java)
        _binding = FragmentViewingRoomsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Доступ к базе данных Firebase в реальном времени
        val database = Firebase.database
        val roomsRef = database.getReference("Rooms")

        roomsRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val roomList = StringBuilder()
                for (roomSnapshot in snapshot.children) {
                    val roomType = roomSnapshot.child("roomType").getValue(String::class.java) ?: ""
                    val pricePerNight = roomSnapshot.child("pricePerNight").getValue(String::class.java) ?: ""
                    val numberOfBeds = roomSnapshot.child("numberOfBeds").getValue(String::class.java) ?: ""
                    val availability = roomSnapshot.child("availability").getValue(String::class.java) ?: ""
                    val description = roomSnapshot.child("description").getValue(String::class.java) ?: ""
                    if (availability == "freedom") {
                        roomList.append("Room Type: $roomType\n")
                        roomList.append("Price Per Night: $pricePerNight\n")
                        roomList.append("Number of Beds: $numberOfBeds\n")
                        roomList.append("Availability: $availability\n")
                        roomList.append("Description: $description\n\n")
                    }
                }

                // Отображение сведений о комнате в текстовом виде
                val roomDetailsTextView: TextView = binding.textViewRoomDetails
                roomDetailsTextView.text = roomList.toString()
            }

            override fun onCancelled(error: DatabaseError) {
                // Устранять любые ошибки, возникающие при извлечении данных
            }
        })

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}