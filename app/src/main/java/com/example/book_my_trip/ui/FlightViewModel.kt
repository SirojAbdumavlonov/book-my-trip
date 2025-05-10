package com.example.book_my_trip.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.book_my_trip.R
import com.example.book_my_trip.data.model.Flight
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class FlightViewModel : ViewModel() {
    // Available search results
    private val _searchResults = MutableStateFlow<List<Flight>>(
        listOf(
            Flight(
                id = "FL-123",
                airlineName = "Emirates Airlines",
                departureTime = "10:40",
                departureAirport = "JFK",
                departureCity = "New York",
                arrivalTime = "14:20",
                arrivalAirport = "LHR",
                arrivalCity = "London",
                price = "$440",
                duration = "7h 40m",
                airlineLogoRes = R.drawable.london
            ),
            Flight(
                id = "FL-456",
                airlineName = "British Airways",
                departureTime = "13:15",
                departureAirport = "JFK",
                departureCity = "New York",
                arrivalTime = "17:30",
                arrivalAirport = "LHR",
                arrivalCity = "London",
                price = "$510",
                duration = "8h 15m",
                airlineLogoRes = R.drawable.london
            ),
            Flight(
                id = "FL-789",
                airlineName = "Virgin Atlantic",
                departureTime = "19:50",
                departureAirport = "JFK",
                departureCity = "New York",
                arrivalTime = "23:55",
                arrivalAirport = "LHR",
                arrivalCity = "London",
                price = "$390",
                duration = "8h 05m",
                airlineLogoRes = R.drawable.london
            )
        )
    )
    val searchResults: StateFlow<List<Flight>> = _searchResults.asStateFlow()

    // My tickets
    private val _myTickets = MutableStateFlow<List<Flight>>(emptyList())
    val myTickets: StateFlow<List<Flight>> = _myTickets.asStateFlow()

    // Add a flight to My Tickets
    fun addToMyTickets(flightId: String) {
        val flight = _searchResults.value.find { it.id == flightId }
        flight?.let {
            val currentList = _myTickets.value.toMutableList()
            if (!currentList.contains(it)) {
                currentList.add(it)
                _myTickets.value = currentList
            }
        }
    }

    // Remove a flight from My Tickets
    fun removeFromMyTickets(flightId: String) {
        val currentList = _myTickets.value.toMutableList()
        currentList.removeIf { it.id == flightId }
        _myTickets.value = currentList
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                FlightViewModel()
            }
        }
    }
}