package com.unsalgasimliapplicationsnsug.yasilhesab.model

import Device
import Reading
import RoomData
import android.app.Application
import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.Duration
import kotlin.math.max
import kotlin.math.min

data class ChartPoint(val millis: Long, val value: Float)

class DashboardFullDataViewModel(app: Application) : AndroidViewModel(app) {
    private val _rooms = MutableStateFlow<List<RoomData>>(emptyList())
    val rooms: StateFlow<List<RoomData>> = _rooms

    var selectedRoomIndex by mutableStateOf(0)
    var selectedDeviceIndex by mutableStateOf(0)

    enum class TimeFilter(
        val label: String,
        val durationMinutes: Long,
        val bucketMinutes: Long,
        val movingAvgWindow: Int
    ) {
        TWELVE_HOURS("12 saat", 12 * 60, 15, 3),
        ONE_DAY("1 gün", 24 * 60, 30, 5),
        ONE_WEEK("1 həftə", 7 * 24 * 60, 120, 9)
    }
    var selectedFilter by mutableStateOf(TimeFilter.TWELVE_HOURS)
    fun selectFilter(filter: TimeFilter) { selectedFilter = filter }

    private val prefs = app.getSharedPreferences("dashboard_cache", Context.MODE_PRIVATE)
    private val gson = Gson()
    private val dateFmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
    private val globalEnd = LocalDateTime.parse("2025-07-22 00:00", dateFmt)

    private fun LocalDateTime.toMillis(): Long =
        this.atZone(java.time.ZoneId.systemDefault()).toInstant().toEpochMilli()

    private fun movingAverage(values: List<Float>, window: Int): List<Float> {
        if (values.isEmpty() || window < 2) return values
        val result = MutableList(values.size) { 0f }
        val half = window / 2
        for (i in values.indices) {
            var count = 0
            var sum = 0f
            for (j in max(0, i - half)..min(values.lastIndex, i + half)) {
                sum += values[j]
                count++
            }
            result[i] = if (count > 0) sum / count else values[i]
        }
        return result
    }

    private fun interpolateNulls(data: List<Float?>): List<Float> {
        val res = data.toMutableList()
        var lastValue: Float? = null
        var i = 0
        while (i < res.size) {
            if (res[i] == null) {
                val start = i
                while (i < res.size && res[i] == null) i++
                val end = i
                val nextValue = if (end < res.size) res[end] else null
                if (lastValue != null && nextValue != null) {
                    val step = (nextValue - lastValue) / (end - start + 1)
                    for (j in start until end) res[j] = lastValue + step * (j - start + 1)
                } else if (lastValue != null) {
                    for (j in start until end) res[j] = lastValue
                } else if (nextValue != null) {
                    for (j in start until end) res[j] = nextValue
                } else {
                    for (j in start until end) res[j] = 0f
                }
            } else {
                lastValue = res[i]
                i++
            }
        }
        return res.map { it ?: 0f }
    }

    fun allDevices(): List<Device> = _rooms.value.flatMap { it.devices }

    fun deviceChipsForSelectedRoom(): List<Device> =
        if (selectedRoomIndex == 0) allDevices()
        else _rooms.value.getOrNull(selectedRoomIndex - 1)?.devices ?: emptyList()

    fun getChartData(): List<ChartPoint> {
        val roomList = _rooms.value
        val filter = selectedFilter
        val end = globalEnd
        val start = end.minusMinutes(filter.durationMinutes)

        val readings: List<Pair<String, Float>> =
            when {
                selectedRoomIndex == 0 && selectedDeviceIndex == 0 -> {
                    roomList.flatMap { it.devices.flatMap { d -> d.readings.map { r -> r.datetime to r.value } } }
                }
                selectedRoomIndex == 0 -> {
                    val allDevs = allDevices()
                    val device = allDevs.getOrNull(selectedDeviceIndex - 1)
                    device?.readings?.map { it.datetime to it.value } ?: emptyList()
                }
                selectedDeviceIndex == 0 -> {
                    val room = roomList.getOrNull(selectedRoomIndex - 1)
                    room?.devices?.flatMap { d -> d.readings.map { r -> r.datetime to r.value } } ?: emptyList()
                }
                else -> {
                    val room = roomList.getOrNull(selectedRoomIndex - 1)
                    val device = room?.devices?.getOrNull(selectedDeviceIndex - 1)
                    device?.readings?.map { it.datetime to it.value } ?: emptyList()
                }
            }
        return bucketizeAndSmooth(readings, start, end, filter.bucketMinutes, filter.movingAvgWindow)
    }

    fun getDeviceStats(): List<Pair<String, Float>> {
        return when {
            selectedRoomIndex == 0 && selectedDeviceIndex == 0 -> {
                listOf("Hamısı" to allDevices().flatMap { it.readings }.sumOf { it.value.toDouble() }.toFloat())
            }
            selectedRoomIndex == 0 -> {
                val allDevs = allDevices()
                val device = allDevs.getOrNull(selectedDeviceIndex - 1)
                if (device != null)
                    listOf(device.name to device.readings.sumOf { it.value.toDouble() }.toFloat())
                else emptyList()
            }
            selectedDeviceIndex == 0 -> {
                val room = _rooms.value.getOrNull(selectedRoomIndex - 1)
                room?.devices?.map { it.name to it.readings.sumOf { r -> r.value.toDouble() }.toFloat() } ?: emptyList()
            }
            else -> {
                val room = _rooms.value.getOrNull(selectedRoomIndex - 1)
                val device = room?.devices?.getOrNull(selectedDeviceIndex - 1)
                if (device != null)
                    listOf(device.name to device.readings.sumOf { it.value.toDouble() }.toFloat())
                else emptyList()
            }
        }
    }

    private fun bucketizeAndSmooth(
        readings: List<Pair<String, Float>>,
        start: LocalDateTime,
        end: LocalDateTime,
        bucketMinutes: Long,
        movingAvgWindow: Int
    ): List<ChartPoint> {
        val inFmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
        val totalBuckets = ((Duration.between(start, end).toMinutes() / bucketMinutes) + 1).toInt()
        val buckets = Array(totalBuckets) { mutableListOf<Float>() }
        for ((dtStr, value) in readings) {
            val dt = try { LocalDateTime.parse(dtStr, inFmt) } catch (_: Exception) { null }
            if (dt == null || dt < start || dt > end) continue
            val bucketIdx = ((Duration.between(start, dt).toMinutes()) / bucketMinutes).toInt().coerceIn(0, totalBuckets - 1)
            buckets[bucketIdx].add(value)
        }
        val avgVals: List<Float?> = buckets.map { if (it.isNotEmpty()) it.average().toFloat() else null }
        val interpVals = interpolateNulls(avgVals)
        val smoothVals = movingAverage(interpVals, movingAvgWindow)
        return List(totalBuckets) { i ->
            val t = start.plusMinutes(i * bucketMinutes)
            ChartPoint(t.toMillis(), smoothVals[i])
        }
    }

    fun selectRoom(idx: Int) { selectedRoomIndex = idx; selectedDeviceIndex = 0 }
    fun selectDevice(idx: Int) { selectedDeviceIndex = idx }

    fun loadRooms() {
        viewModelScope.launch {
            if (_rooms.value.isNotEmpty()) return@launch
            val cache = prefs.getString("roomData", null)
            if (cache != null) {
                val type  = object : TypeToken<List<RoomData>>() {}.type
                val list: List<RoomData> = gson.fromJson(cache, type)
                if (list.isNotEmpty()) { _rooms.value = list; return@launch }
            }
            refreshRoomsFromCloud()
        }
    }
    fun forceRefreshRooms() = viewModelScope.launch { refreshRoomsFromCloud() }

    suspend fun refreshRoomsFromCloud() {
        val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return
        val db = FirebaseFirestore.getInstance()
        val roomList = mutableListOf<RoomData>()
        val roomsSnap = db.collection("users").document(uid).collection("rooms").get().await()
        for (roomDoc in roomsSnap.documents) {
            val roomId = roomDoc.id
            val devicesSnap = roomDoc.reference.collection("devices").get().await()
            val devices = devicesSnap.documents.map { devDoc ->
                val readingsSnap = devDoc.reference.collection("usage").get().await()
                val readings = readingsSnap.documents.map { r ->
                    Reading(
                        datetime = r.getString("datetime") ?: "",
                        value = r.getDouble("value")?.toFloat() ?: 0f
                    )
                }
                Device(
                    id = devDoc.id,
                    name = devDoc.getString("name") ?: devDoc.id,
                    type = devDoc.getString("type") ?: "",
                    powerWatts = devDoc.getLong("powerWatts")?.toInt(),
                    hoursPerDay = devDoc.getDouble("hoursPerDay")?.toFloat(),
                    brand = devDoc.getString("brand") ?: "",
                    model = devDoc.getString("model") ?: "",
                    roomId = devDoc.getString("roomId") ?: roomId,
                    readings = readings
                )
            }
            roomList += RoomData(
                id = roomId,
                name = roomDoc.getString("name") ?: roomId,
                type = roomDoc.getString("type") ?: "",
                size = roomDoc.getLong("size")?.toInt() ?: 0,
                devices = devices
            )
        }
        _rooms.value = roomList
        prefs.edit().putString("roomData", gson.toJson(roomList)).apply()
    }

    fun logout() {
        FirebaseAuth.getInstance().signOut()
    }

    fun saveRoom(room: RoomData) {
        viewModelScope.launch {
            val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return@launch
            val db = FirebaseFirestore.getInstance()
            val roomRef = db.collection("users").document(uid).collection("rooms").document(room.id)
            val roomData = mapOf(
                "name" to room.name,
                "type" to room.type,
                "size" to room.size
            )
            roomRef.set(roomData).await()
            refreshRoomsFromCloud()
        }
    }

    fun deleteRoom(room: RoomData) {
        viewModelScope.launch {
            val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return@launch
            val db = FirebaseFirestore.getInstance()
            db.collection("users").document(uid).collection("rooms").document(room.id)
                .delete().await()
            refreshRoomsFromCloud()
        }
    }

    fun saveDevice(roomId: String, device: Device) {
        viewModelScope.launch {
            val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return@launch
            val db = FirebaseFirestore.getInstance()
            val deviceRef = db.collection("users")
                .document(uid)
                .collection("rooms")
                .document(roomId)
                .collection("devices")
                .document(device.id)
            val deviceData = mapOf(
                "name" to device.name,
                "type" to device.type,
                "powerWatts" to device.powerWatts,
                "hoursPerDay" to device.hoursPerDay,
                "brand" to device.brand,
                "model" to device.model,
                "roomId" to roomId
            )
            deviceRef.set(deviceData).await()
            refreshRoomsFromCloud()
        }
    }

    fun deleteDevice(roomId: String, device: Device) {
        viewModelScope.launch {
            val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return@launch
            val db = FirebaseFirestore.getInstance()
            db.collection("users")
                .document(uid)
                .collection("rooms")
                .document(roomId)
                .collection("devices")
                .document(device.id)
                .delete().await()
            refreshRoomsFromCloud()
        }
    }
    fun editRoom(room: RoomData) = saveRoom(room)
    fun editDevice(roomId: String, device: Device) = saveDevice(roomId, device)

}

