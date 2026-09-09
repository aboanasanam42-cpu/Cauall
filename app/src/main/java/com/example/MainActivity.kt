package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.ui.screens.MathDashboardScreen
import com.example.ui.screens.MathOverviewScreen
import com.example.ui.screens.MathSearchScreen
import com.example.ui.theme.DeepSpaceBlack
import com.example.ui.theme.MathLibraryTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MathLibraryTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = DeepSpaceBlack
                ) {
                    MathAppNavigation()
                }
            }
        }
    }
}

@Composable
fun MathAppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "dashboard"
    ) {
        composable("dashboard") {
            MathDashboardScreen(
                onNavigateToOverview = { navController.navigate("overview") },
                onNavigateToSearch = { navController.navigate("search") }
            )
        }
        composable("overview") {
            MathOverviewScreen(
                onBack = { navController.popBackStack() }
            )
        }
        composable("search") {
            MathSearchScreen(
                onBack = { navController.popBackStack() }
            )
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(text = "Hello $name!", modifier = modifier)
}
