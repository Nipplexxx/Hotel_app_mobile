package com.example.hotel_app.ui.bookings

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.hotel_app.R
import com.example.hotel_app.databinding.FragmentBookingsBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.android.material.textfield.TextInputEditText
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.concurrent.TimeUnit

data class Booking(
    val guestId: String,
    val roomId: String,
    val bookingStartDate: String,
    val bookingEndDate: String,
    val totalPrice: Double,
    val isPaid: Boolean
)

class BookingsFragment : Fragment() {
    private var _binding: FragmentBookingsBinding? = null
    private val binding get() = _binding!!
    private lateinit var etBookingStartDate: TextInputEditText
    private lateinit var etBookingEndDate: TextInputEditText
    private lateinit var tvTotalPrice: TextView
    private val roomPrices = mutableMapOf<String, Double>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBookingsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Получаем данные пользователя из arguments
        val userId = arguments?.getString("userId") ?: "Неизвестно"
        val userEmail = arguments?.getString("userEmail") ?: "Гость"

        // Устанавливаем ID и Email пользователя в UI
        val etGuestID: TextView = binding.etGuestID
        etGuestID.text = userId

        val spinnerRoom: Spinner = binding.spinnerRoom
        etBookingStartDate = binding.etBookingStartDate
        etBookingEndDate = binding.etBookingEndDate
        tvTotalPrice = binding.tvTotalPrice
        val btnBookRoom: Button = binding.btnBookRoom

        val database = Firebase.database
        val roomsRef = database.getReference("Rooms")
        roomsRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val roomNames = mutableListOf<String>()
                for (roomSnapshot in snapshot.children) {
                    val roomName = roomSnapshot.child("roomType").getValue(String::class.java)
                    val pricePerNightString = roomSnapshot.child("pricePerNight").getValue(String::class.java)
                    val pricePerNight = pricePerNightString?.toDoubleOrNull() ?: 0.0
                    roomName?.let {
                        roomNames.add(it)
                        roomPrices[it] = pricePerNight
                    }
                }
                val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, roomNames)
                spinnerRoom.adapter = adapter
                spinnerRoom.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                        val selectedRoom = roomNames[position]
                        val pricePerNight = roomPrices[selectedRoom] ?: 0.0
                        val startDate = etBookingStartDate.text.toString()
                        val endDate = etBookingEndDate.text.toString()
                        if (startDate.isNotEmpty() && endDate.isNotEmpty()) {
                            val totalPrice = calculateTotalPrice(startDate, endDate, pricePerNight)
                            tvTotalPrice.text = "Total Price: $totalPrice"
                        }
                    }

                    override fun onNothingSelected(parent: AdapterView<*>?) {}
                }
            }

            override fun onCancelled(error: DatabaseError) {}
        })

        etBookingStartDate.setOnClickListener {
            showDatePickerDialog(etBookingStartDate)
        }

        etBookingEndDate.setOnClickListener {
            showDatePickerDialog(etBookingEndDate)
        }

        btnBookRoom.setOnClickListener {
            val guestId = etGuestID.text.toString()
            val roomId = spinnerRoom.selectedItem.toString()
            val bookingStartDate = etBookingStartDate.text.toString()
            val bookingEndDate = etBookingEndDate.text.toString()
            val totalPrice = calculateTotalPrice(bookingStartDate, bookingEndDate, roomPrices[roomId] ?: 0.0)
            val isPaid = false
            val bookingsRef = database.getReference("Bookings")
            val bookingId = bookingsRef.push().key
            val newBooking = Booking(guestId, roomId, bookingStartDate, bookingEndDate, totalPrice, isPaid)
            bookingId?.let {
                bookingsRef.child(it).setValue(newBooking)
            }

            Toast.makeText(requireContext(), "Booking successful!", Toast.LENGTH_SHORT).show()

            findNavController().navigate(R.id.nav_home)
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun calculateTotalPrice(startDate: String, endDate: String, pricePerNight: Double): Double {
        try {
            val sdf = SimpleDateFormat("dd/MM/yyyy")
            val date1: Date = sdf.parse(startDate)
            val date2: Date = sdf.parse(endDate)
            val diffInMillies = date2.time - date1.time
            val diffInDays = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS).toInt()
            return pricePerNight * diffInDays
        } catch (e: Exception) {
            e.printStackTrace()
            return 0.0
        }
    }

    private fun showDatePickerDialog(textInputEditText: TextInputEditText) {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        val datePickerDialog = DatePickerDialog(requireContext(), { _, selectedYear, selectedMonth, selectedDay ->
            val selectedDate = "$selectedDay/${selectedMonth + 1}/$selectedYear"
            textInputEditText.setText(selectedDate)
            val startDate = etBookingStartDate.text.toString()
            val endDate = etBookingEndDate.text.toString()
            val selectedRoom = binding.spinnerRoom.selectedItem.toString()
            val pricePerNight = roomPrices[selectedRoom] ?: 0.0
            if (startDate.isNotEmpty() && endDate.isNotEmpty()) {
                val totalPrice = calculateTotalPrice(startDate, endDate, pricePerNight)
                tvTotalPrice.text = "Total Price: $totalPrice"
            }
        }, year, month, day)
        datePickerDialog.show()
    }
}