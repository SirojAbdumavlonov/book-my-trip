package com.example.book_my_trip.viewmodel

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.book_my_trip.R
import com.example.book_my_trip.data.model.Flight
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.time.LocalDate
import java.time.format.DateTimeFormatter

/**
 * ViewModel for managing flight data across different screens
 */
class FlightViewModel : ViewModel() {
    // Available search results
    @RequiresApi(Build.VERSION_CODES.O)
    private val _searchResults = MutableStateFlow<List<Flight>>(
        listOf(
            Flight(
                id = "FL-123",
                airline = "Emirates Airlines",
                departureTime = "10:40",
                departureCode = "JFK",
                departureCity = "New York",
                arrivalTime = "14:20",
                arrivalCode = "LHR",
                arrivalCity = "London",
                price = "$440",
                duration = "7h 40m",
                airlineLogoRes = R.drawable.london,
                flightNumber = generateRandomString(6),
                departureDate = getCurrentDateAsString(),
                arrivalDate = getCurrentDateAsString(),
                passengerName = "Bob",
                seatNumber = generateRandomString(3)
            ),
            Flight(
                id = "FL-456",
                airline = "British Airways",
                departureTime = "13:15",
                departureCode = "JFK",
                departureCity = "New York",
                arrivalTime = "17:30",
                arrivalCode = "LHR",
                arrivalCity = "London",
                price = "$510",
                duration = "8h 15m",
                airlineLogoRes = R.drawable.london,
                flightNumber = generateRandomString(6),
                departureDate = getCurrentDateAsString(),
                arrivalDate = getCurrentDateAsString(),
                passengerName = "Mirshod",
                seatNumber = generateRandomString(3)
            ),
            Flight(
                id = "FL-789",
                airline = "Virgin Atlantic",
                departureTime = "19:50",
                departureCode = "JFK",
                departureCity = "New York",
                arrivalTime = "23:55",
                arrivalCode = "LHR",
                arrivalCity = "London",
                price = "$390",
                duration = "8h 05m",
                airlineLogoRes = R.drawable.london,
                flightNumber = generateRandomString(6),
                departureDate = getCurrentDateAsString(),
                arrivalDate = getCurrentDateAsString(),
                passengerName = "Zoir",
                seatNumber = generateRandomString(3)
            )
        )
    )
    @RequiresApi(Build.VERSION_CODES.O)
    val searchResults: StateFlow<List<Flight>> = _searchResults.asStateFlow()
    @RequiresApi(Build.VERSION_CODES.O)
    val popularFlights: StateFlow<List<Flight>> = _searchResults.asStateFlow()

    // My tickets - Changed from empty list to include a test ticket for debugging
    private val _myTickets = MutableStateFlow<List<Flight>>(mutableListOf())
    val myTickets: StateFlow<List<Flight>> = _myTickets.asStateFlow()

    // Add a flight to My Tickets - Fixed with RequiresApi annotation
    @RequiresApi(Build.VERSION_CODES.O)
    fun addToMyTickets(flightId: String) {
        // Log for debugging
        println("Adding flight with ID: $flightId to my tickets")

        // First check if flight already exists in tickets
        val existingTicket = _myTickets.value.find { it.id == flightId }
        if (existingTicket != null) {
            println("Flight already exists in tickets")
            return
        }

        // Find the flight in search results
        val flight = _searchResults.value.find { it.id == flightId }
        flight?.let {
            println("Found flight: ${it.airline} from ${it.departureCity} to ${it.arrivalCity}")
            val currentList = _myTickets.value.toMutableList()
            currentList.add(it)
            _myTickets.value = currentList
            println("Added flight. Current tickets count: ${_myTickets.value.size}")

        } ?: println("Flight with ID $flightId not found in search results")
    }

    // Remove a flight from My Tickets
    fun removeFromMyTickets(flightId: String) {
        val currentList = _myTickets.value.toMutableList()
        currentList.removeIf { it.id == flightId }
        _myTickets.value = currentList
    }

    // Update search results based on search criteria
    @RequiresApi(Build.VERSION_CODES.O)
    fun updateSearchResults(fromCity: String, toCity: String) {
        // In a real app, this would make an API call or query a database
        // For this example, we'll just simulate a filter
        _searchResults.value = _searchResults.value.filter {
            it.departureCity.equals(fromCity, ignoreCase = true) &&
                    it.arrivalCity.equals(toCity, ignoreCase = true)
        }
    }

    // Reset search results to original state
    @RequiresApi(Build.VERSION_CODES.O)
    fun resetSearchResults() {
        _searchResults.value = listOf(
            Flight(
                id = "FL-123",
                airline = "Emirates Airlines",
                departureTime = "10:40",
                departureCode = "JFK",
                departureCity = "New York",
                arrivalTime = "14:20",
                arrivalCode = "LHR",
                arrivalCity = "London",
                price = "$440",
                duration = "7h 40m",
                airlineLogoRes = R.drawable.london,
                flightNumber = generateRandomString(6),
                departureDate = getCurrentDateAsString(),
                arrivalDate = getCurrentDateAsString(),
                passengerName = "Sirojiddin",
                seatNumber = generateRandomString(3)
            ),
            Flight(
                id = "FL-456",
                airline = "British Airways",
                departureTime = "13:15",
                departureCode = "JFK",
                departureCity = "New York",
                arrivalTime = "17:30",
                arrivalCode = "LHR",
                arrivalCity = "London",
                price = "$510",
                duration = "8h 15m",
                airlineLogoRes = R.drawable.london,
                flightNumber = generateRandomString(6),
                departureDate = getCurrentDateAsString(),
                arrivalDate = getCurrentDateAsString(),
                passengerName = "Abdurakhmon",
                seatNumber = generateRandomString(3)
            ),
            Flight(
                id = "FL-789",
                airline = "Virgin Atlantic",
                departureTime = "19:50",
                departureCode = "JFK",
                departureCity = "New York",
                arrivalTime = "23:55",
                arrivalCode = "LHR",
                arrivalCity = "London",
                price = "$390",
                duration = "8h 05m",
                airlineLogoRes = R.drawable.london,
                flightNumber = generateRandomString(6),
                departureDate = getCurrentDateAsString(),
                arrivalDate = getCurrentDateAsString(),
                passengerName = "Azim",
                seatNumber = generateRandomString(3)
            )
        )
    }

    companion object {
        // Added ViewModelFactory to ensure consistent instance across screens
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                FlightViewModel()
            }
        }
    }

    fun generateRandomString(length: Int): String {
        val allowedChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789"
        return (1..length)
            .map { allowedChars.random() }
            .joinToString("")
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getCurrentDateAsString(): String {
        val currentDate = LocalDate.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd") // or "dd/MM/yyyy", etc.
        return currentDate.format(formatter)
    }
}