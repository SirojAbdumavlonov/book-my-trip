package com.example.book_my_trip.data.model

/**
 * Data class representing a travel destination in the application
 */
data class Destination(
    val id: String,
    val cityName: String,
    val country: String,
    val price: String,
    val imageRes: Int
)