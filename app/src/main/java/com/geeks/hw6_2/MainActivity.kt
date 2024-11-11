package com.geeks.hw6_2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.navigation.compose.*
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.geeks.hw6_2.ui.theme.OnboardingTheme

sealed class OnboardingRoute(val route: String) {
    object Welcome : OnboardingRoute("welcome")
    object Registration : OnboardingRoute("registration")
    object Confirmation : OnboardingRoute("confirmation/{userName}") {
        fun createRoute(userName: String) = "confirmation/$userName"
    }
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = OnboardingRoute.Welcome.route) {
                    composable(OnboardingRoute.Welcome.route) {
                        WelcomeScreen(navController)
                    }
                    composable(OnboardingRoute.Registration.route) {
                        RegistrationScreen(navController)
                    }
                    composable(
                        OnboardingRoute.Confirmation.route,
                        arguments = listOf(navArgument("userName") { type = NavType.StringType })
                    ) { backStackEntry ->
                        val userName = backStackEntry.arguments?.getString("userName") ?: "Unknown"
                        ConfirmationScreen(userName)
                    }
                }
            }
        }
    }
}

@Composable
fun WelcomeScreen(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Welcome to our App!", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = {
            navController.navigate(OnboardingRoute.Registration.route)
        }) {
            Text("Start Registration")
        }
    }
}

@Composable
fun RegistrationScreen(navController: NavController) {
    var userName by remember { mutableStateOf(TextFieldValue("")) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Enter your name:", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            value = userName,
            onValueChange = { userName = it },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Name") }
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                if (userName.text.isNotEmpty()) {
                    navController.navigate(OnboardingRoute.Confirmation.createRoute(userName.text))
                }
            },
            enabled = userName.text.isNotEmpty()
        ) {
            Text("Confirm Registration")
        }
    }
}

@Composable
fun ConfirmationScreen(userName: String) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Hello, $userName!", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(8.dp))
        Text("Your registration is complete.", style = MaterialTheme.typography.bodyLarge)
    }
}
