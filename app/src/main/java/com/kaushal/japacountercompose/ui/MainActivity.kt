package com.kaushal.japacountercompose.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocal
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.kaushal.japacountercompose.ui.composables.AddNewJapaScreen
import com.kaushal.japacountercompose.ui.composables.JapaListScreen
import com.kaushal.japacountercompose.ui.composables.WelcomeScreen
import com.kaushal.japacountercompose.ui.theme.JapaCounterComposeTheme

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

    //get current back stack entry
    val backStackEntry by navController.currentBackStackEntryAsState()

    //get the name of the current screen
    val currentScreen = JapaAppScreens.valueOf(
        backStackEntry?.destination?.route ?: JapaAppScreens.welcome.name
    )
    
    NavHost(
        navController = navController,
        startDestination = JapaAppScreens.welcome.name,
        modifier = Modifier.fillMaxSize())
    {
        // define the composable screens within the NavHost

        composable(route = JapaAppScreens.welcome.name) {
            WelcomeScreen(navController = navController) // pass the nav controller
        }

        composable(route = JapaAppScreens.japaList.name) {
            JapaListScreen(navController = navController)
        }

        composable(route = JapaAppScreens.addJapa.name) {
            AddNewJapaScreen(navController = navController)
        }

        composable(route = JapaAppScreens.japDetails.name) {

        }
    }
}
