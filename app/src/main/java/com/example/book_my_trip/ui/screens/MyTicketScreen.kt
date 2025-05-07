package com.example.book_my_trip.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.book_my_trip.R
import com.example.book_my_trip.data.model.Ticket
import com.example.book_my_trip.ui.components.TicketCard
import com.example.book_my_trip.ui.theme.Primary
import com.example.book_my_trip.ui.theme.TextSecondary
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyTicketScreen(
    navController: NavController,
    modifier: Modifier = Modifier
) {
    val tickets = remember {
        listOf(
            Ticket(
                id = "TK-123456",
                airlineName = "Emirates Airlines",
                flightNumber = "EA-2342",
                departureTime = "10:40",
                departureDate = "May 15, 2025",
                departureAirport = "JFK",
                departureCity = "New York",
                arrivalTime = "14:20",
                arrivalDate = "May 15, 2025",
                arrivalAirport = "LHR",
                arrivalCity = "London",
                passengerName = "John Doe",
                seatNumber = "21A",
                airlineLogoRes = R.drawable.ic_launcher_background
            ),
            Ticket(
                id = "TK-789012",
                airlineName = "Singapore Airlines",
                flightNumber = "SA-4521",
                departureTime = "21:30",
                departureDate = "June 2, 2025",
                departureAirport = "SIN",
                departureCity = "Singapore",
                arrivalTime = "05:15",
                arrivalDate = "June 3, 2025",
                arrivalAirport = "SYD",
                arrivalCity = "Sydney",
                passengerName = "John Doe",
                seatNumber = "14C",
                airlineLogoRes = R.drawable.ic_launcher_background
            )
        )
    }

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "My Tickets",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold
                    )
                },
                actions = {
                    IconButton(onClick = {
                        // Launch barcode scanner using ML Kit
                        scope.launch {
                            snackbarHostState.showSnackbar("QR Code scanner would launch here")
                        }
                    }) {
                        Icon(
                            imageVector = Icons.Default.QrCode,
                            contentDescription = "Scan QR Code",
                            tint = Primary
                        )
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            if (tickets.isEmpty()) {
                NoTicketsView(
                    modifier = Modifier.align(Alignment.Center)
                )
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    contentPadding = PaddingValues(vertical = 16.dp)
                ) {
                    items(tickets) { ticket ->
                        TicketCard(
                            airlineName = ticket.airlineName,
                            flightNumber = ticket.flightNumber,
                            departureTime = ticket.departureTime,
                            departureDate = ticket.departureDate,
                            departureAirport = ticket.departureAirport,
                            departureCity = ticket.departureCity,
                            arrivalTime = ticket.arrivalTime,
                            arrivalDate = ticket.arrivalDate,
                            arrivalAirport = ticket.arrivalAirport,
                            arrivalCity = ticket.arrivalCity,
                            passengerName = ticket.passengerName,
                            seatNumber = ticket.seatNumber,
                            airlineLogoRes = ticket.airlineLogoRes,
                            onClick = {
                                // Navigate to ticket details
                                scope.launch {
                                    snackbarHostState.showSnackbar("Viewing ticket details for ${ticket.id}")
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun NoTicketsView(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Default.QrCode,
            contentDescription = null,
            modifier = Modifier
                .size(80.dp)
                .padding(bottom = 16.dp),
            tint = TextSecondary
        )

        Text(
            text = "No Tickets Found",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Book a flight to see your tickets here",
            style = MaterialTheme.typography.bodyMedium,
            color = TextSecondary
        )
    }
}

// ML Kit barcode scanning implementation
class TicketBarcodeScanner {
    private val barcodeScanner = BarcodeScanning.getClient()

    // Function to process image and extract ticket data
    // Note: This is a placeholder. In a real app, you would integrate with the camera
    fun processQrCode(
        onSuccess: (String) -> Unit,
        onError: (Exception) -> Unit
    ) {
        // This function would use ML Kit's BarcodeScanning API
        // to detect QR codes or barcodes from camera input
        // and extract ticket information

        // In a real implementation, you would:
        // 1. Capture an image from the camera
        // 2. Convert it to an InputImage
        // 3. Process it with barcodeScanner.process(inputImage)
        // 4. Handle the result in a success listener
    }
}