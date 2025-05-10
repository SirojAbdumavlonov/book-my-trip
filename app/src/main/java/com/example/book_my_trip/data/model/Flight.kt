package com.example.book_my_trip.data.model

/**
 * Data class representing a flight in the application
 */
data class Flight(
//    val id: String,
//    val airlineName: String,
//    val departureTime: String,
//    val departureAirport: String,
//    val departureCity: String,
//    val arrivalTime: String,
//    val arrivalAirport: String,
//    val arrivalCity: String,
//    val price: String,
    val duration: String,
//    val airlineLogoRes: Int
    val id: String,
    val airline: String? = null,
    val flightNumber: String,
    val departureTime: String,
    val departureDate: String,
    val departureCode: String,
    val departureCity: String,
    val arrivalTime: String,
    val arrivalDate: String,
    val arrivalCode: String,
    val arrivalCity: String,
    val price: String = "0.0",
    val passengerName: String? = null,
    val seatNumber: String? = null,
    val airlineLogoRes: Int
)