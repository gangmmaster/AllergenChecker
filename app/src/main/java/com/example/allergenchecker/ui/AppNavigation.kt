package com.example.allergenchecker.ui

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.allergenchecker.ui.diet.DietAnalysisScreen

@Composable
fun AppNavigation() {
    val nav = rememberNavController()

    NavHost(navController = nav, startDestination = "home") {
        composable("home") {
            HomeScreen(
                onProductClick = { id -> nav.navigate("detail/$id") },
                onProfileClick = { nav.navigate("profile") },
                onScanClick = { nav.navigate("scanner") },
                onScanTextClick = { nav.navigate("scanner_text") },
                onDietClick = { nav.navigate("diet") }
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

        composable("scanner") {
            ScannerScreen(
                isTextMode = false,
                onBack = { nav.popBackStack() },
                onScanned = { barcode ->
                    nav.popBackStack()
                    nav.navigate("add?barcode=$barcode&isText=false")
                }
            )
        }

        composable("scanner_text") {
            ScannerScreen(
                isTextMode = true,
                onBack = { nav.popBackStack() },
                onScanned = { text ->
                    nav.popBackStack()
                    nav.navigate("add?barcode=0&isText=true&text=$text")
                }
            )
        }

        composable(
            route = "add?barcode={barcode}&isText={isText}&text={text}",
            arguments = listOf(
                navArgument("barcode") { type = NavType.StringType; defaultValue = "" },
                navArgument("isText") { type = NavType.BoolType; defaultValue = false },
                navArgument("text") { type = NavType.StringType; defaultValue = "" }
            )
        ) { entry ->
            val barcode = entry.arguments?.getString("barcode") ?: ""
            val isText = entry.arguments?.getBoolean("isText") ?: false
            val text = entry.arguments?.getString("text") ?: ""

            AddProductScreen(
                initialBarcode = if (isText) "" else barcode,
                initialText = if (isText) text else "",
                onBack = { nav.popBackStack() },
                onProductFound = { id ->
                    nav.popBackStack()
                    nav.navigate("detail/$id")
                }
            )
        }

        composable("diet") {
            DietAnalysisScreen()
        }
    }
}