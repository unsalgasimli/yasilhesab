package com.unsalgasimliapplicationsnsug.yasilhesab.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Kitchen
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.unsalgasimliapplicationsnsug.yasilhesab.R
import com.unsalgasimliapplicationsnsug.yasilhesab.model.DashboardFullDataViewModel
import com.unsalgasimliapplicationsnsug.yasilhesab.ui.theme.*
import com.unsalgasimliapplicationsnsug.yasilhesab.compenents.*
import com.unsalgasimliapplicationsnsug.yasilhesab.components.EcoTopBar
import com.unsalgasimliapplicationsnsug.yasilhesab.components.StatCard
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    userName: String,
    navController: NavController,
    vm: DashboardFullDataViewModel = viewModel()
) {
    LaunchedEffect(Unit) {
        vm.loadRooms()
    }
    EcoTheme {
        val drawerState = rememberDrawerState(DrawerValue.Closed)
        val scope = rememberCoroutineScope()
        val rooms by vm.rooms.collectAsState()
        val selectedRoomIdx = vm.selectedRoomIndex
        val selectedDeviceIdx = vm.selectedDeviceIndex
        val chartData = vm.getChartData()
        val filters = DashboardFullDataViewModel.TimeFilter.values()
        val selectedTabIdx = filters.indexOf(vm.selectedFilter)
        val snackbarHostState = remember { SnackbarHostState() }

        ModalNavigationDrawer(
            drawerState = drawerState,
            drawerContent = {
                EcoDrawer(
                    selected = "Dashboard", // or use a variable if you have dynamic navigation
                    onDestinationClicked = { route ->
                        scope.launch { drawerState.close() }
                        when(route) {
                            "Dashboard" -> {}
                            "HomeSetting" -> navController.navigate("homeSetting")
                            "Bonuses" -> navController.navigate("bonuses")
                            "UserSettings" -> navController.navigate("settings")
                        }
                    }
                )
            },
            gesturesEnabled = false
        )    {
            Scaffold(
                topBar = {
                    EcoTopBar(
                        title = stringResource(R.string.dashboard_title),
                        onMenuClick = { scope.launch { drawerState.open() } },
                        onProfileClick = { /* TODO: Go to profile */ },
                        userName = userName
                    )
                },
                snackbarHost = { SnackbarHost(snackbarHostState) },
                containerColor = EcoBg
            ) { padding ->
                Box(modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                    ) {
                        // OTAQLAR SEÇİMİ (Rooms)
                        val roomChips = listOf(stringResource(R.string.all_rooms)) + rooms.map { it.name }
                        LazyRow(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 18.dp, bottom = 8.dp, start = 10.dp, end = 10.dp),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            itemsIndexed(roomChips) { idx, name ->
                                val isSelected = idx == selectedRoomIdx
                                ChipItem(
                                    text = name,
                                    selected = isSelected,
                                    onClick = { vm.selectRoom(idx) }
                                )
                            }
                        }

                        // QURĞU SEÇİMİ (Devices)
                        val selectedRoom = rooms.getOrNull(selectedRoomIdx - 1)
                        val deviceList = selectedRoom?.devices ?: emptyList()
                        val deviceChips = listOf(stringResource(R.string.all_devices)) + deviceList.map { it.name }
                        if (selectedRoomIdx > 0 && deviceList.isNotEmpty()) {
                            LazyRow(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 4.dp, bottom = 12.dp, start = 10.dp, end = 10.dp),
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                itemsIndexed(deviceChips) { idx, name ->
                                    val isSelected = idx == selectedDeviceIdx
                                    ChipItem(
                                        text = name,
                                        selected = isSelected,
                                        onClick = { vm.selectDevice(idx) },
                                        leadingIcon = if (idx > 0) getDeviceIconEmoji(deviceList[idx - 1].type) else null
                                    )
                                }
                            }
                        }

                        // STATİSTİKA (Stat Cards)
                        Row(
                            Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 12.dp, vertical = 8.dp),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            if (selectedRoomIdx == 0) {
                                val totalUsage = rooms.flatMap { it.devices }.flatMap { it.readings }.sumOf { it.value.toDouble() }
                                StatCard(
                                    value = "%.1f".format(totalUsage),
                                    unit = "kWh",
                                    icon = Icons.Default.Home,
                                    accent = EcoGreen,
                                    cardBg = EcoGreen.copy(alpha = 0.13f),
                                    border = EcoGreen,
                                    isSelected = true
                                )
                            } else if (selectedDeviceIdx == 0) {
                                deviceList.take(3).forEach { device ->
                                    val total = device.readings.sumOf { it.value.toDouble() }.toFloat()
                                    StatCard(
                                        value = "%.1f".format(total),
                                        unit = "kWh",
                                        icon = Icons.Default.Kitchen, // TODO: Replace with per-device icon if you want
                                        accent = EcoGreen,
                                        cardBg = EcoCardBg,
                                        border = EcoBorder,
                                        isSelected = false
                                    )
                                }
                            } else {
                                val device = deviceList.getOrNull(selectedDeviceIdx - 1)
                                if (device != null) {
                                    val total = device.readings.sumOf { it.value.toDouble() }.toFloat()
                                    StatCard(
                                        value = "%.1f".format(total),
                                        unit = "kWh",
                                        icon = Icons.Default.Kitchen,
                                        accent = EcoGreen,
                                        cardBg = EcoGreen.copy(alpha = 0.13f),
                                        border = EcoGreen,
                                        isSelected = true
                                    )
                                }
                            }
                        }

                        Divider(
                            Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            color = EcoBorder,
                            thickness = 1.dp
                        )
                        Spacer(Modifier.height(48.dp)) // Make space for bottom chart card
                    }

                    // ALT QRAFİK (Bottom chart, always visible at bottom)
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(Alignment.BottomCenter)
                            .padding(bottom = 18.dp)
                            .height(240.dp),
                        colors = CardDefaults.cardColors(containerColor = EcoCardBg),
                        shape = RectangleShape,
                        elevation = CardDefaults.cardElevation(0.dp)
                    ) {
                        EnergyLineChartSection(
                            chartData = chartData,
                            filters = filters,
                            selectedFilterIdx = selectedTabIdx,
                            onFilterSelected = { vm.selectFilter(it) }
                        )
                    }
                }
            }
        }
    }
}

// You should define these somewhere central in your codebase
fun getDeviceIconEmoji(type: String): String = when (type.lowercase()) {
    "tv", "televizor" -> "\uD83D\uDCFA"
    "ac", "kondisioner" -> "\uD83C\uDF21️"
    "fridge", "soyuducu" -> "\uD83E\uDDCA"
    "microwave" -> "\uD83D\uDD2B"
    "dishwasher", "qabyuyan" -> "\uD83E\uDDFE"
    "kombi", "boiler", "hitter" -> "\uD83D\uDD25"
    "kettle", "çaydan" -> "\uD83E\uDED6"
    "blender" -> "\uD83E\uDD5E"
    "light", "lampa", "table lamp", "masa lampası" -> "\uD83D\uDCA1"
    "console", "konsol", "playstation" -> "\uD83C\uDFAE"
    "pc", "komputer", "laptop" -> "\uD83D\uDCBB"
    "printer" -> "\uD83D\uDDA8️"
    "audio", "speaker", "musiqi qutusu" -> "\uD83C\uDFB5"
    else -> "\uD83D\uDEE0️"
}
