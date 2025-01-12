package com.example.hotel_app.ui.admin_rooms

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.hotel_app.databinding.FragmentAdminRoomsBinding
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class AdminRoomsFragment : Fragment() {
    private var _binding: FragmentAdminRoomsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAdminRoomsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val etRoomType: EditText = binding.etRoomType
        val etPricePerNight: EditText = binding.etPricePerNight
        val etNumberOfBeds: EditText = binding.etNumberOfBeds
        val etAvailability: EditText = binding.etAvailability
        val etDescription: EditText = binding.etDescription
        val btnAddRooms: Button = binding.btnAddRooms

        btnAddRooms.setOnClickListener {
            val roomType = etRoomType.text.toString()
            val pricePerNight = etPricePerNight.text.toString()
            val numberOfBeds = etNumberOfBeds.text.toString()
            val availability = etAvailability.text.toString()
            val description = etDescription.text.toString()

            if (isAdded) { // проверяем, прикреплен ли фрагмент
                val database = Firebase.database
                val roomsRef = database.getReference("Rooms")
                val room = hashMapOf(
                    "roomType" to roomType,
                    "pricePerNight" to pricePerNight,
                    "numberOfBeds" to numberOfBeds,
                    "availability" to availability,
                    "description" to description
                )

                roomsRef.push().setValue(room).addOnSuccessListener {
                    // Сообщение об успешном добавлении
                    Toast.makeText(context, "Номер добавлен!", Toast.LENGTH_SHORT).show()
                }.addOnFailureListener {
                    // Сообщение об ошибке
                    Toast.makeText(context, "Ошибка при добавлении номера!", Toast.LENGTH_SHORT).show()
                }

                // Очищает поля
                etRoomType.text.clear()
                etPricePerNight.text.clear()
                etNumberOfBeds.text.clear()
                etAvailability.text.clear()
                etDescription.text.clear()
            } else {
                // Если фрагмент не привязан
                Toast.makeText(context, "Ошибка: фрагмент не привязан к контексту.", Toast.LENGTH_SHORT).show()
            }
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}