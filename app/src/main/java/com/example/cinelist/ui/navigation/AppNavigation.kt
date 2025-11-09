package com.example.cinelist.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.cinelist.ui.detail.DetailScreen
import com.example.cinelist.ui.home.HomeScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "home"
    ) {
        composable("home") {
            HomeScreen(
                onTitleClick = { titleId ->
                    navController.navigate("detail/$titleId")
                }
            )
        }

        composable(
            "detail/{titleId}",
            arguments = listOf(
                navArgument("titleId") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val titleId = backStackEntry.arguments?.getInt("titleId") ?: return@composable
            DetailScreen(
                titleId = titleId,
                onBackClick = { navController.popBackStack() }
            )
        }
    }
}
