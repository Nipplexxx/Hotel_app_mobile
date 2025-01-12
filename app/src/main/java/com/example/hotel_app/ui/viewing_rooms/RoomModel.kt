package com.example.hotel_app.ui.viewing_rooms

data class RoomModel(
    val roomId: String,
    val roomType: String,
    val pricePerNight: String,
    val numberOfBeds: String,
    val description: String,
    val isAvailable: Boolean, // Доступен или забронирован
    val bookingDates: String = "" // Для забронированных номеров
)

