package com.example.book_my_trip.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.book_my_trip.R
import com.example.book_my_trip.data.model.Flight
import com.example.book_my_trip.ui.components.*
import com.example.book_my_trip.ui.theme.*
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    navController: NavController,
    modifier: Modifier = Modifier
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    // States for search inputs
    var fromCity by remember { mutableStateOf("New York") }
    var toCity by remember { mutableStateOf("London") }
    var departureDate by remember { mutableStateOf("15 May 2025") }
    var returnDate by remember { mutableStateOf("22 May 2025") }
    var passengerCount by remember { mutableStateOf("1 Passenger") }

    // State for search results
    var showResults by remember { mutableStateOf(false) }

    // Sample search results
    val searchResults = remember {
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
                airlineLogoRes = R.drawable.ic_launcher_background
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
                airlineLogoRes = R.drawable.ic_launcher_background
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
                airlineLogoRes = R.drawable.ic_launcher_background
            )
        )
    }

    // Text recognition for passport scanning
    val textRecognizer = remember {
        TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Search Flights",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(vertical = 16.dp)
        ) {
            if (!showResults) {
                // Search form
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            // From City
                            TiniTextField(
                                value = fromCity,
                                onValueChange = { fromCity = it },
                                label = "From",
                                leadingIcon = Icons.Default.Flight
                            )

                            // To City
                            TiniTextField(
                                value = toCity,
                                onValueChange = { toCity = it },
                                label = "To",
                                leadingIcon = Icons.Default.FlightLand
                            )

                            // Dates
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(16.dp)
                            ) {
                                // Departure Date
                                TiniTextField(
                                    value = departureDate,
                                    onValueChange = { departureDate = it },
                                    label = "Departure",
                                    leadingIcon = Icons.Default.DateRange,
                                    modifier = Modifier.weight(1f)
                                )

                                // Return Date
                                TiniTextField(
                                    value = returnDate,
                                    onValueChange = { returnDate = it },
                                    label = "Return",
                                    leadingIcon = Icons.Default.DateRange,
                                    modifier = Modifier.weight(1f)
                                )
                            }

                            // Passenger Count
                            TiniTextField(
                                value = passengerCount,
                                onValueChange = { passengerCount = it },
                                label = "Passengers",
                                leadingIcon = Icons.Default.Person
                            )

                            // Search Button
                            TiniButton(
                                text = "Search Flights",
                                onClick = {
                                    showResults = true
                                }
                            )

                            // Scan Passport Button
                            OutlinedButton(
                                onClick = {
                                    // Launch ML Kit text recognition for passport scanning
                                    scope.launch {
                                        snackbarHostState.showSnackbar("Passport scanner would launch here")
                                    }
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(56.dp),
                                shape = RoundedCornerShape(16.dp),
                                colors = ButtonDefaults.outlinedButtonColors(
                                    contentColor = Primary
                                )
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.DocumentScanner,
                                        contentDescription = "Scan Passport",
                                        tint = Primary
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = "Scan Passport",
                                        style = MaterialTheme.typography.labelLarge,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                }
                            }
                        }
                    }
                }
            } else {
                // Search results
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "$fromCity to $toCity",
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.weight(1f))

                        TextButton(
                            onClick = {
                                showResults = false
                            }
                        ) {
                            Text(
                                text = "Edit",
                                style = MaterialTheme.typography.labelMedium,
                                color = Primary
                            )
                        }
                    }

                    Text(
                        text = "$departureDate · $passengerCount",
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextSecondary,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                }

                items(searchResults) { flight ->
                    FlightCard(
                        airlineName = flight.airlineName,
                        departureTime = flight.departureTime,
                        departureAirport = flight.departureAirport,
                        arrivalTime = flight.arrivalTime,
                        arrivalAirport = flight.arrivalAirport,
                        price = flight.price,
                        duration = flight.duration,
                        airlineLogoRes = flight.airlineLogoRes,
                        onClick = {
                            // Navigate to flight details
                            scope.launch {
                                snackbarHostState.showSnackbar("Selected flight: ${flight.id}")
                            }
                        }
                    )
                }

                // ML Kit implementation for text extraction from passports
                item {
                    // This would be used to process the passport data after scanning
                    // In a real app, we would:
                    // 1. Capture an image of the passport
                    // 2. Process it with textRecognizer.process(inputImage)
                    // 3. Extract relevant information like name, passport number, etc.
                    // 4. Auto-fill passenger details
                }
            }
        }
    }
}

// ML Kit text recognition implementation for passport scanning
class PassportScanner {
    private val textRecognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)

    // Function to extract passport data
    // Note: This is a placeholder. In a real app, you would integrate with the camera
    fun processPassport(
        onSuccess: (PassportData) -> Unit,
        onError: (Exception) -> Unit
    ) {
        // This function would use ML Kit's Text Recognition API
        // to extract information from passport

        // In a real implementation, you would:
        // 1. Capture an image from the camera
        // 2. Convert it to an InputImage
        // 3. Process it with textRecognizer.process(inputImage)
        // 4. Parse the text to extract passport information
    }
}

// Data class to hold extracted passport information
data class PassportData(
    val fullName: String,
    val passportNumber: String,
    val nationality: String,
    val dateOfBirth: String,
    val expiryDate: String
)