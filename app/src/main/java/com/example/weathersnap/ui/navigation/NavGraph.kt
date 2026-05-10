package com.example.weathersnap.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.weathersnap.ui.camera.CameraScreen
import com.example.weathersnap.ui.report.CreateReportScreen
import com.example.weathersnap.ui.reports.SavedReportsScreen
import com.example.weathersnap.ui.weather.WeatherScreen

@Composable
fun NavGraph(navController: NavHostController) {
    NavHost(navController = navController, startDestination = "weather") {
        composable("weather") {
            WeatherScreen(
                onCreateReportClick = { city, temp, condition, humidity, wind, pressure ->
                    navController.navigate("create_report/$city/${temp.toFloat()}/$condition/$humidity/${wind.toFloat()}/${pressure.toFloat()}")
                },
                onReportsClick = {
                    navController.navigate("saved_reports")
                }
            )
        }
        
        composable(
            route = "create_report/{city}/{temp}/{condition}/{humidity}/{wind}/{pressure}",
            arguments = listOf(
                navArgument("city") { type = NavType.StringType },
                navArgument("temp") { type = NavType.FloatType },
                navArgument("condition") { type = NavType.StringType },
                navArgument("humidity") { type = NavType.IntType },
                navArgument("wind") { type = NavType.FloatType },
                navArgument("pressure") { type = NavType.FloatType }
            )
        ) { backStackEntry ->
            val city = backStackEntry.arguments?.getString("city") ?: ""
            val temp = backStackEntry.arguments?.getFloat("temp")?.toDouble() ?: 0.0
            val condition = backStackEntry.arguments?.getString("condition") ?: ""
            val humidity = backStackEntry.arguments?.getInt("humidity") ?: 0
            val wind = backStackEntry.arguments?.getFloat("wind")?.toDouble() ?: 0.0
            val pressure = backStackEntry.arguments?.getFloat("pressure")?.toDouble() ?: 0.0
            
            val savedStateHandle = backStackEntry.savedStateHandle
            val imagePath = savedStateHandle.get<String>("imagePath")
            val origSize = savedStateHandle.get<Long>("originalSizeKb") ?: 0L
            val compSize = savedStateHandle.get<Long>("compressedSizeKb") ?: 0L
            
            CreateReportScreen(
                city = city,
                temp = temp,
                condition = condition,
                humidity = humidity,
                wind = wind,
                pressure = pressure,
                imagePath = imagePath,
                originalSizeKb = origSize,
                compressedSizeKb = compSize,
                onNavigateBack = { navController.popBackStack() },
                onNavigateToCamera = { navController.navigate("camera") },
                onNavigateToSavedReports = {
                    navController.navigate("saved_reports") {
                        popUpTo("weather")
                    }
                }
            )
        }
        
        composable("camera") {
            CameraScreen(
                onCaptureComplete = { path, orig, comp ->
                    navController.previousBackStackEntry?.savedStateHandle?.set("imagePath", path)
                    navController.previousBackStackEntry?.savedStateHandle?.set("originalSizeKb", orig)
                    navController.previousBackStackEntry?.savedStateHandle?.set("compressedSizeKb", comp)
                    navController.popBackStack()
                },
                onClose = { navController.popBackStack() }
            )
        }
        
        composable("saved_reports") {
            SavedReportsScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}
