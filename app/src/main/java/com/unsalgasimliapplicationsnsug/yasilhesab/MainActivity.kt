package com.unsalgasimliapplicationsnsug.yasilhesab

import BonusesScreen
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseAuth
import com.unsalgasimliapplicationsnsug.yasilhesab.model.DashboardFullDataViewModel
import com.unsalgasimliapplicationsnsug.yasilhesab.screens.DashboardScreen
import com.unsalgasimliapplicationsnsug.yasilhesab.screens.HomeSettingScreen
import com.unsalgasimliapplicationsnsug.yasilhesab.screens.SettingsScreen
import com.unsalgasimliapplicationsnsug.yasilhesab.screens.TipsScreen
import com.unsalgasimliapplicationsnsug.yasilhesab.ui.auth.LoginScreen
import com.unsalgasimliapplicationsnsug.yasilhesab.ui.auth.RegisterScreen
import com.unsalgasimliapplicationsnsug.yasilhesab.ui.theme.EcoTheme

@OptIn(ExperimentalMaterial3Api::class)
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            EcoTheme(darkTheme = true) {
                AppNavGraph()
            }
        }
    }
}

@Composable
fun AppNavGraph() {
    val navController = rememberNavController()
    // Check login state at startup
    val startDestination = if (FirebaseAuth.getInstance().currentUser == null) "login" else "dashboard"

    // Share the same DashboardFullDataViewModel instance if you want room/device data synced across screens:
    val dashboardVm: DashboardFullDataViewModel = viewModel()

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        // LOGIN
        composable("login") {
            LoginScreen(
                onNavigateToRegister = { navController.navigate("register") },
                onLoginSuccess = {
                    navController.navigate("dashboard") {
                        popUpTo("login") { inclusive = true }
                    }
                }
            )
        }
        // REGISTER
        composable("register") {
            RegisterScreen(
                onRegisterSuccess = {
                    navController.navigate("dashboard") {
                        popUpTo("register") { inclusive = true }
                    }
                },
                onBack = { navController.popBackStack() }
            )
        }
        // DASHBOARD
        composable("dashboard") {
            DashboardScreen(
                userName = FirebaseAuth.getInstance().currentUser?.email ?: "",
                navController = navController,
                vm = dashboardVm // Use shared VM
            )
        }
        // HOME SETTING
        composable("homeSetting") {
            val rooms by dashboardVm.rooms.collectAsState()
            HomeSettingScreen(
                rooms = rooms,
                onRoomSave = { dashboardVm.saveRoom(it) },
                onDeviceSave = { roomId, device -> dashboardVm.saveDevice(roomId, device) },
                onRoomEdit = { dashboardVm.editRoom(it) },
                onDeviceEdit = { roomId, device -> dashboardVm.editDevice(roomId, device) },
                onRoomDelete = { dashboardVm.deleteRoom(it) },
                onDeviceDelete = { roomId, device -> dashboardVm.deleteDevice(roomId, device) }
            )
        }
        composable("tips") {
            TipsScreen(
                onBack = { navController.popBackStack() }
            )
        }
        // BONUSES
        composable("bonuses") {
            BonusesScreen()
        }
        // SETTINGS
        composable("settings") {
            SettingsScreen(
                onSignOut = {
                    FirebaseAuth.getInstance().signOut()
                    navController.navigate("login") {
                        popUpTo(0) { inclusive = true }
                        launchSingleTop = true
                    }
                }
            )
        }
    }
}
