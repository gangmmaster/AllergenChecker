package com.example.allergenchecker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.allergenchecker.ui.AppNavigation
import com.example.allergenchecker.ui.theme.AllergenCheckerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AllergenCheckerTheme {
                AppNavigation()
            }
        }
    }
}