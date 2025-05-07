package com.example.book_my_trip

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.book_my_trip.ui.screens.HomeScreen
import com.example.book_my_trip.ui.screens.MyTicketScreen
import com.example.book_my_trip.ui.screens.SearchScreen
import com.example.book_my_trip.ui.theme.TiniFlightBookingTheme

class MainActivity : ComponentActivity() {
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

@Composable
fun TiniApp() {
    val navController = rememberNavController()

    TiniNavHost(navController = navController)
}

@Composable
fun TiniNavHost(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {
        composable(Screen.Home.route) {
            HomeScreen(navController = navController)
        }
        composable(Screen.Search.route) {
            SearchScreen(navController = navController)
        }
        composable(Screen.MyTicket.route) {
            MyTicketScreen(navController = navController)
        }
    }
}

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Search : Screen("search")
    object MyTicket : Screen("my_ticket")
}