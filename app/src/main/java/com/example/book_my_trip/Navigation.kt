package com.example.book_my_trip

sealed class Screen(val route: String) {
    object SignIn : Screen("sign_in")
    object SignUp : Screen("sign_up")
    object Home : Screen("home")
    object Search : Screen("search")
    object MyTicket : Screen("my_ticket")
}