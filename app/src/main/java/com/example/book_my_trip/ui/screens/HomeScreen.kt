package com.example.book_my_trip.ui.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.book_my_trip.R
import com.example.book_my_trip.Screen
import com.example.book_my_trip.data.model.Destination
import com.example.book_my_trip.ui.components.*
import com.example.book_my_trip.ui.theme.Primary
import com.example.book_my_trip.ui.theme.TextSecondary
import com.example.book_my_trip.viewmodel.FlightViewModel
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController,
    flightViewModel: FlightViewModel = viewModel(factory = FlightViewModel.Factory),
    modifier: Modifier = Modifier
) {
    var searchText by remember { mutableStateOf("") }
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    // Get sample flights from the ViewModel instead of hardcoding them
    val popularFlights by flightViewModel.popularFlights.collectAsState()

    // Initialize search results if empty
    LaunchedEffect(Unit) {
    }

    // Sample destinations data
    val popularDestinations = remember {
        listOf(
            Destination(
                id = "DEST-01",
                cityName = "Paris",
                country = "France",
                price = "$350",
                imageRes = R.drawable.france
            ),
            Destination(
                id = "DEST-02",
                cityName = "Tokyo",
                country = "Japan",
                price = "$730",
                imageRes = R.drawable.tokyo
            ),
            Destination(
                id = "DEST-03",
                cityName = "Dubai",
                country = "UAE",
                price = "$450",
                imageRes = R.drawable.uae
            ),
            Destination(
                id = "DEST-04",
                cityName = "New York",
                country = "USA",
                price = "$520",
                imageRes = R.drawable.newyork
            )
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "Hello, Sirojiddin",
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Where are you flying to today?",
                            style = MaterialTheme.typography.bodyMedium,
                            color = TextSecondary
                        )
                    }
                },
                actions = {
                    // Profile icon
                    IconButton(onClick = {
                        scope.launch {
                            snackbarHostState.showSnackbar("Profile clicked")
                        }
                    }) {
                        Image(
                            painter = painterResource(id = R.drawable.my_photo),
                            contentDescription = "Profile",
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape),
                            contentScale = ContentScale.Crop
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
        // Main content area - using Box to ensure full scrollability
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // LazyColumn for scrollable content
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Search bar
                item {
                    TiniSearchBar(
                        value = searchText,
                        onValueChange = { searchText = it },
                        placeholder = "Search destinations, flights...",
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                navController.navigate(Screen.Search.route)
                            }
                    )
                }

                // Featured banner
                item {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(180.dp),
                        shape = RoundedCornerShape(16.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                    ) {
                        Box(modifier = Modifier.fillMaxSize()) {
                            // Banner image
                            Image(
                                painter = painterResource(id = R.drawable.flights),
                                contentDescription = "Special offer",
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )

                            // Gradient overlay
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(
                                        androidx.compose.ui.graphics.Brush.horizontalGradient(
                                            colors = listOf(
                                                Color.Black.copy(alpha = 0.7f),
                                                Color.Transparent
                                            )
                                        )
                                    )
                            )

                            // Promo content
                            Column(
                                modifier = Modifier
                                    .align(Alignment.CenterStart)
                                    .padding(24.dp)
                            ) {
                                Text(
                                    text = "20% OFF",
                                    style = MaterialTheme.typography.displayMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )

                                Text(
                                    text = "on international flights",
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = Color.White
                                )

                                Spacer(modifier = Modifier.height(16.dp))

                                Button(
                                    onClick = {
                                        scope.launch {
                                            snackbarHostState.showSnackbar("Promo offer clicked")
                                        }
                                    },
                                    shape = RoundedCornerShape(8.dp),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = Color.White,
                                        contentColor = Primary
                                    )
                                ) {
                                    Text(
                                        text = "Book Now",
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    }
                }

                // Popular destinations
                item {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        SectionTitle(
                            title = "Popular Destinations",
                            actionText = "View All",
                            onActionClick = {
                                scope.launch {
                                    snackbarHostState.showSnackbar("View all destinations clicked")
                                }
                            }
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        LazyRow(
                            horizontalArrangement = Arrangement.spacedBy(16.dp),
                            contentPadding = PaddingValues(end = 8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            items(popularDestinations) { destination ->
                                DestinationCard(
                                    cityName = destination.cityName,
                                    country = destination.country,
                                    price = destination.price,
                                    imageRes = destination.imageRes,
                                    onClick = {
                                        // Navigate to destination details
                                        scope.launch {
                                            snackbarHostState.showSnackbar("Destination selected: ${destination.cityName}")
                                        }
                                    }
                                )
                            }
                        }
                    }
                }

                // Popular flights
                item {
                    SectionTitle(
                        title = "Popular Flights",
                        actionText = "View All",
                        onActionClick = {
                            navController.navigate(Screen.Search.route)
                        }
                    )
                }

                // List of flights - now using data from ViewModel
                items(popularFlights.take(2)) { flight -> // Taking only 2 for popular flights section
                    FlightCard(
                        airlineName = flight.airline ?: "Airline",
                        departureTime = flight.departureTime,
                        departureAirport = flight.departureCode,
                        arrivalTime = flight.arrivalTime,
                        arrivalAirport = flight.arrivalCode,
                        price = "$${flight.price}",
                        duration = "2:30m",
                        airlineLogoRes = flight.airlineLogoRes ?: R.drawable.default_airline_logo,
                        onClick = {
                            // Navigate to flight details
                            scope.launch {
                                snackbarHostState.showSnackbar("Flight selected: ${flight.id}")
                            }
                        },
                    )
                }

                // Add some bottom padding to ensure content isn't hidden behind bottom navigation
                item {
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }
}