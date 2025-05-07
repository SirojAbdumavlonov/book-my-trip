package com.example.book_my_trip.data.model

/**
 * Data class representing a flight ticket in the application
 */
data class Ticket(
    val id: String,
    val airlineName: String,
    val flightNumber: String,
    val departureTime: String,
    val departureDate: String,
    val departureAirport: String,
    val departureCity: String,
    val arrivalTime: String,
    val arrivalDate: String,
    val arrivalAirport: String,
    val arrivalCity: String,
    val passengerName: String,
    val seatNumber: String,
    val airlineLogoRes: Int
)