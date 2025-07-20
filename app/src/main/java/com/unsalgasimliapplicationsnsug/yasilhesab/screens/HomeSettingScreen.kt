package com.unsalgasimliapplicationsnsug.yasilhesab.screens

import Device
import RoomData
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Kitchen
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
    import com.unsalgasimliapplicationsnsug.yasilhesab.ui.theme.DarkBg
    import com.unsalgasimliapplicationsnsug.yasilhesab.ui.theme.DarkCard
import com.unsalgasimliapplicationsnsug.yasilhesab.ui.theme.ecoGreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeSettingScreen(
    rooms: List<RoomData>,
    onRoomSave: (RoomData) -> Unit,
    onDeviceSave: (String, Device) -> Unit, // (roomId, device)
    onRoomEdit: (RoomData) -> Unit,
    onDeviceEdit: (String, Device) -> Unit,
    onRoomDelete: (RoomData) -> Unit,
    onDeviceDelete: (String, Device) -> Unit
) {
    var showRoomDialog by remember { mutableStateOf(false) }
    var editingRoom by remember { mutableStateOf<RoomData?>(null) }
    var showDeviceDialog by remember { mutableStateOf(false) }
    var editingDevice by remember { mutableStateOf<Device?>(null) }
    var deviceRoomId by remember { mutableStateOf<String?>(null) }

    Scaffold(
        floatingActionButton = {
            Row {
                FloatingActionButton(
                    onClick = {
                        showRoomDialog = true
                        editingRoom = null
                    },
                    containerColor = ecoGreen
                ) { Icon(Icons.Default.Home, contentDescription = "Otaq əlavə et") }
                Spacer(modifier = Modifier.width(16.dp))
                FloatingActionButton(
                    onClick = {
                        showDeviceDialog = true
                        editingDevice = null
                        deviceRoomId = null
                    },
                    containerColor = ecoGreen
                ) { Icon(Icons.Default.Kitchen, contentDescription = "Qurğu əlavə et") }
            }
        },
        containerColor = DarkBg
    ) { padding ->
        Column(
            Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            Text(
                "Otaqlar və Qurğular",
                fontWeight = FontWeight.Bold,
                fontSize = 22.sp,
                color = ecoGreen,
                modifier = Modifier.padding(20.dp)
            )
            if (rooms.isEmpty()) {
                Box(
                    Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Heç bir otaq əlavə edilməyib", color = Color.Gray, fontSize = 16.sp)
                }
            } else {
                LazyColumn(Modifier.weight(1f)) {
                    items(rooms) { room ->
                        RoomCard(
                            room = room,
                            onEdit = {
                                editingRoom = room
                                showRoomDialog = true
                            },
                            onDelete = { onRoomDelete(room) },
                            onAddDevice = {
                                deviceRoomId = room.id
                                editingDevice = null
                                showDeviceDialog = true
                            },
                            onDeviceEdit = { dev ->
                                deviceRoomId = room.id
                                editingDevice = dev
                                showDeviceDialog = true
                            },
                            onDeviceDelete = { dev -> onDeviceDelete(room.id, dev) }
                        )
                    }
                }
            }
        }
    }

    // OTAQ DİALOQU
    if (showRoomDialog) {
        RoomDialog(
            initialRoom = editingRoom,
            onDismiss = { showRoomDialog = false },
            onSave = {
                onRoomSave(it)
                showRoomDialog = false
            }
        )
    }
    // QURĞU DİALOQU
    if (showDeviceDialog) {
        DeviceDialog(
            initialDevice = editingDevice,
            rooms = rooms,
            defaultRoomId = deviceRoomId,
            onDismiss = {
                showDeviceDialog = false
                deviceRoomId = null
            },
            onSave = { dev, roomId ->
                // ALWAYS set roomId on device!
                onDeviceSave(roomId, dev.copy(roomId = roomId))
                showDeviceDialog = false
                deviceRoomId = null
            }
        )
    }
}

// ---------------- OTAQ KARTI ----------------

@Composable
fun RoomCard(
    room: RoomData,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    onAddDevice: () -> Unit,
    onDeviceEdit: (Device) -> Unit,
    onDeviceDelete: (Device) -> Unit
) {
    Card(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 14.dp, vertical = 8.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(DarkCard)
    ) {
        Column(Modifier.padding(14.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(room.name, fontWeight = FontWeight.Bold, fontSize = 19.sp, color = Color.White)
                Spacer(Modifier.weight(1f))
                IconButton(onClick = onEdit) {
                    Icon(Icons.Default.Edit, contentDescription = "Redaktə et", tint = ecoGreen)
                }
                IconButton(onClick = onDelete) {
                    Icon(Icons.Default.Delete, contentDescription = "Sil", tint = Color.Red)
                }
            }
            Text("Tip: ${room.type}, Ölçü: ${room.size} m²", fontSize = 13.sp, color = Color.LightGray)
            TextButton(onClick = onAddDevice) { Text("Qurğu əlavə et", color = ecoGreen) }
            Spacer(Modifier.height(4.dp))
            if (room.devices.isEmpty()) {
                Text("Bu otaqda qurğu yoxdur", color = Color.Gray, fontSize = 12.sp)
            } else {
                room.devices.forEach { device ->
                    DeviceCard(
                        device = device,
                        onEdit = { onDeviceEdit(device) },
                        onDelete = { onDeviceDelete(device) }
                    )
                }
            }
        }
    }
}

// -------------- QURĞU KARTI ---------------

@Composable
fun DeviceCard(
    device: Device,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp, horizontal = 8.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(Color(0xFF23272E))
    ) {
        Row(
            Modifier.padding(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(device.name, fontWeight = FontWeight.Medium, color = ecoGreen)
            Spacer(Modifier.weight(1f))
            IconButton(onClick = onEdit) { Icon(Icons.Default.Edit, contentDescription = "Redaktə et", tint = ecoGreen) }
            IconButton(onClick = onDelete) { Icon(Icons.Default.Delete, contentDescription = "Sil", tint = Color.Red) }
        }
        Column(Modifier.padding(horizontal = 18.dp, vertical = 2.dp)) {
            Text(
                "Tip: ${device.type}, Güc: ${device.powerWatts ?: "?"}W, Saat/gün: ${device.hoursPerDay ?: "?"}",
                fontSize = 13.sp, color = Color.LightGray
            )
            Text(
                "Marka: ${device.brand}   Model: ${device.model}",
                fontSize = 12.sp, color = Color.Gray
            )
            // Enerji hesablama
            val power = device.powerWatts ?: 0
            val hours = device.hoursPerDay ?: 0f
            val yearlyKWh = (power * hours * 365 / 1000f)
            Text(
                "Təxmini İllik İstifadə: %.1f kWh".format(yearlyKWh),
                fontSize = 13.sp, color = Color(0xFFA1FFC1)
            )
        }
    }
}

// ------------ OTAQ DİALOQU ----------------

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RoomDialog(
    initialRoom: RoomData?,
    onDismiss: () -> Unit,
    onSave: (RoomData) -> Unit
) {
    var name by remember { mutableStateOf(initialRoom?.name ?: "") }
    var type by remember { mutableStateOf(initialRoom?.type ?: "") }
    var size by remember { mutableStateOf(initialRoom?.size?.toString() ?: "") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (initialRoom == null) "Yeni Otaq" else "Otağı Redaktə Et") },
        text = {
            Column {
                OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Otaq adı") })
                OutlinedTextField(value = type, onValueChange = { type = it }, label = { Text("Tip") })
                OutlinedTextField(
                    value = size,
                    onValueChange = { size = it.filter { ch -> ch.isDigit() } },
                    label = { Text("Ölçü (m²)") }
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (name.isNotBlank() && type.isNotBlank() && size.isNotBlank()) {
                        onSave(
                            initialRoom?.copy(
                                name = name,
                                type = type,
                                size = size.toInt()
                            ) ?: RoomData(
                                id = System.currentTimeMillis().toString(),
                                name = name,
                                type = type,
                                size = size.toInt(),
                                devices = listOf()
                            )
                        )
                    }
                }
            ) { Text("Yadda saxla") }
        },
        dismissButton = { OutlinedButton(onClick = onDismiss) { Text("Bağla") } }
    )
}

// ------------ QURĞU DİALOQU ----------------

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeviceDialog(
    initialDevice: Device?,
    rooms: List<RoomData>,
    defaultRoomId: String?,
    onDismiss: () -> Unit,
    onSave: (Device, String) -> Unit // (device, roomId)
) {
    var name by remember { mutableStateOf(initialDevice?.name ?: "") }
    var type by remember { mutableStateOf(initialDevice?.type ?: "") }
    var power by remember { mutableStateOf(initialDevice?.powerWatts?.toString() ?: "") }
    var hours by remember { mutableStateOf(initialDevice?.hoursPerDay?.toString() ?: "") }
    var roomId by remember { mutableStateOf(initialDevice?.roomId ?: defaultRoomId ?: rooms.firstOrNull()?.id ?: "") }
    var brand by remember { mutableStateOf(initialDevice?.brand ?: "") }
    var model by remember { mutableStateOf(initialDevice?.model ?: "") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (initialDevice == null) "Yeni Qurğu" else "Qurğunu Redaktə Et") },
        text = {
            Column {
                OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Ad") })
                OutlinedTextField(value = type, onValueChange = { type = it }, label = { Text("Tip") })
                OutlinedTextField(value = power, onValueChange = { power = it.filter { ch -> ch.isDigit() } }, label = { Text("Güc (Watt)") })
                OutlinedTextField(value = hours, onValueChange = { hours = it.filter { ch -> ch.isDigit() || ch == '.' } }, label = { Text("Orta saat/gün") })
                RoomDropdownMenu(
                    rooms = rooms,
                    selectedRoomId = roomId,
                    onSelected = { roomId = it }
                )
                OutlinedTextField(value = brand, onValueChange = { brand = it }, label = { Text("Marka") })
                OutlinedTextField(value = model, onValueChange = { model = it }, label = { Text("Model") })
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (name.isNotBlank() && type.isNotBlank() && power.isNotBlank() && hours.isNotBlank() && roomId.isNotBlank()) {
                        onSave(
                            initialDevice?.copy(
                                name = name,
                                type = type,
                                powerWatts = power.toInt(),
                                hoursPerDay = hours.toFloat(),
                                brand = brand,
                                model = model,
                                roomId = roomId    // <- always set!
                            ) ?: Device(
                                id = System.currentTimeMillis().toString(),
                                name = name,
                                type = type,
                                powerWatts = power.toInt(),
                                hoursPerDay = hours.toFloat(),
                                brand = brand,
                                model = model,
                                roomId = roomId    // <- always set!
                            ),
                            roomId
                        )
                    }
                }
            ) { Text("Yadda saxla") }
        },
        dismissButton = { OutlinedButton(onClick = onDismiss) { Text("Bağla") } }
    )
}

@Composable
fun RoomDropdownMenu(
    rooms: List<RoomData>,
    selectedRoomId: String,
    onSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    val selectedRoomName = rooms.find { it.id == selectedRoomId }?.name ?: "Otaq seç"
    Column {
        OutlinedButton(onClick = { expanded = true }) { Text(selectedRoomName) }
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            rooms.forEach { room ->
                DropdownMenuItem(
                    text = { Text(room.name) },
                    onClick = {
                        onSelected(room.id)
                        expanded = false
                    }
                )
            }
        }
    }
}
