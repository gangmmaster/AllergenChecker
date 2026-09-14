package com.example.allergenchecker.ui

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument

@Composable
fun AppNavigation() {
    val nav = rememberNavController()

    NavHost(navController = nav, startDestination = "home") {
        composable("home") {
            HomeScreen(
                onProductClick = { id -> nav.navigate("detail/$id") },
                onProfileClick = { nav.navigate("profile") }
            )
        }
        composable("profile") {
            ProfileScreen(onBack = { nav.popBackStack() })
        }
        composable(
            route = "detail/{id}",
            arguments = listOf(navArgument("id") { type = NavType.IntType })
        ) { entry ->
            val id = entry.arguments?.getInt("id") ?: 0
            DetailScreen(id = id, onBack = { nav.popBackStack() })
        }
    }
}