package com.example.book_my_trip.data.model

/**
 * Data class representing a flight in the application
 */
data class Flight(
    val id: String,
    val airlineName: String,
    val departureTime: String,
    val departureAirport: String,
    val departureCity: String,
    val arrivalTime: String,
    val arrivalAirport: String,
    val arrivalCity: String,
    val price: String,
    val duration: String,
    val airlineLogoRes: Int
)