package com.example.book_my_trip.ui.screens

class DummyData {
    val destinations = listOf<Destination>()
}

data class Destination(
    val id: Int,
    val cityName: String,
    val country: String,
    val price: String,
    val imageRes: Int
)

