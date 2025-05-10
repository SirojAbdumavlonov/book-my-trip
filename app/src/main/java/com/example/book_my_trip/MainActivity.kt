package com.example.book_my_trip

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.book_my_trip.ui.screens.HomeScreen
import com.example.book_my_trip.ui.screens.MyTicketScreen
import com.example.book_my_trip.ui.screens.SearchScreen
import com.example.book_my_trip.ui.screens.auth.SignInScreen
import com.example.book_my_trip.ui.screens.auth.SignUpScreen
import com.example.book_my_trip.ui.theme.TiniFlightBookingTheme
import com.example.book_my_trip.viewmodel.FlightViewModel

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TiniFlightBookingTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    TiniApp()
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TiniApp() {
    val navController = rememberNavController()

    TiniNavHost(navController = navController)
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TiniNavHost(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Screen.SignIn.route
    ) {
        composable(Screen.SignIn.route) {
            SignInScreen(navController = navController)
        }
        composable(Screen.SignUp.route) {
            SignUpScreen(navController = navController)
        }
        composable(Screen.Home.route) {
            // Create shared FlightViewModel here
            val flightViewModel = viewModel<FlightViewModel>()
            HomeScreen(navController = navController, flightViewModel = flightViewModel)
        }
        composable(Screen.Search.route) {
            // Share the same ViewModel from Home
            val parentEntry = remember {
                navController.getBackStackEntry(Screen.Home.route)
            }
            val flightViewModel = viewModel<FlightViewModel>(parentEntry)
            SearchScreen(navController = navController, flightViewModel = flightViewModel)
        }
        composable(Screen.MyTicket.route) {
            // Share the same ViewModel from Home
            val parentEntry = remember {
                navController.getBackStackEntry(Screen.Home.route)
            }
            val flightViewModel = viewModel<FlightViewModel>(parentEntry)
            MyTicketScreen(
                navController = navController,
                flightViewModel = flightViewModel,
                navigateBack = { navController.popBackStack() }
            )
        }
    }
}


// Screen class moved to Navigation.kt