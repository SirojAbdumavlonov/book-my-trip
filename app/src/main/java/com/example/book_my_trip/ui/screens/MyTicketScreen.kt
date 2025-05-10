package com.example.book_my_trip.ui.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.book_my_trip.ui.components.TicketCard
import com.example.book_my_trip.ui.components.TiniBottomNavigation
import com.example.book_my_trip.ui.theme.Primary
import com.example.book_my_trip.ui.theme.TextSecondary
import com.example.book_my_trip.viewmodel.FlightViewModel
import com.google.mlkit.vision.barcode.BarcodeScanning
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyTicketScreen(
    navController: NavController,
    flightViewModel: FlightViewModel = viewModel(factory = FlightViewModel.Factory),
    navigateBack: () -> Unit
) {
    val myTickets by flightViewModel.myTickets.collectAsState()
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
                navigationIcon = {
                    IconButton(onClick = navigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                actions = {
                    IconButton(onClick = {
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
        bottomBar = {
            TiniBottomNavigation(navController = navController)
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            if (myTickets.isEmpty()) {
                println(myTickets)
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
                    items(myTickets) { flight ->
                        TicketCard(
                            airlineName = flight.airline ?: "Airline",
                            flightNumber = flight.flightNumber,
                            departureTime = flight.departureTime,
                            departureDate = flight.departureDate,
                            departureAirport = flight.departureCode,
                            departureCity = flight.departureCity,
                            arrivalTime = flight.arrivalTime,
                            arrivalDate = flight.arrivalDate,
                            arrivalAirport = flight.arrivalCode,
                            arrivalCity = flight.arrivalCity,
                            passengerName = flight.passengerName ?: "Passenger",
                            seatNumber = flight.seatNumber ?: "TBD",
                            airlineLogoRes = flight.airlineLogoRes ?: 0,
                            onClick = {
                                // Navigate to ticket details
                                scope.launch {
                                    snackbarHostState.showSnackbar("Viewing ticket details for ${flight.id}")
                                }
                            },
                            onCancelClick = {
                                flightViewModel.removeFromMyTickets(flight.id)
                                scope.launch {
                                    snackbarHostState.showSnackbar("Ticket cancelled successfully")
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