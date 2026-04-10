package com.kaushal.japacountercompose.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.kaushal.japacountercompose.ui.composables.AddNewJapaScreen
import com.kaushal.japacountercompose.ui.composables.JapaDetailsScreen
import com.kaushal.japacountercompose.ui.composables.JapaListScreen
import com.kaushal.japacountercompose.ui.composables.WelcomeScreen
import com.kaushal.japacountercompose.ui.theme.JapaCounterComposeTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            JapaCounterComposeTheme {
                JapaApp()
            }
        }
    }
}


@Composable
fun JapaApp() {
    val navController: NavHostController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = JapaAppScreens.welcome.name,
        modifier = Modifier.fillMaxSize()
    ) {
        composable(route = JapaAppScreens.welcome.name) {
            WelcomeScreen(navController = navController)
        }

        composable(route = JapaAppScreens.japaList.name) {
            JapaListScreen(navController = navController)
        }

        composable(route = JapaAppScreens.addJapa.name) {
            AddNewJapaScreen(navController = navController)
        }

        composable(
            route = JapaAppScreens.JAPA_DETAILS_ROUTE,
            arguments = listOf(
                navArgument(JapaAppScreens.JAPA_ID_ARG) { type = NavType.IntType }
            )
        ) {
            JapaDetailsScreen(navController = navController)
        }
    }
}
