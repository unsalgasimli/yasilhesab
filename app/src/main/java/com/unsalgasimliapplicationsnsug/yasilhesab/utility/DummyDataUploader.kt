package com.unsalgasimliapplicationsnsug.yasilhesab.utility

import Device
import RoomData
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.*
import kotlinx.coroutines.tasks.await
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.random.Random

fun DummyDataUploader() {
//    val user = FirebaseAuth.getInstance().currentUser ?: return println("Heç bir istifadəçi login olmayıb!")
//    val uid = user.uid
//    val db = FirebaseFirestore.getInstance()
//    val fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
//    val start = LocalDateTime.parse("2025-07-15 00:00", fmt)
//    val numDays = 7
//    val usageIntervalsPerDay = 24 // per hour
//
//    // Demo rooms and devices (roomId device-də verilir!)
//    val rooms = listOf(
//        RoomData(
//            id = "room1",
//            name = "Zal",
//            type = "Living Room",
//            size = 24,
//            devices = listOf(
//                Device(id = "dev1", name = "Televizor", type = "TV", powerWatts = 100, hoursPerDay = 5f, brand = "LG", model = "55NANO", roomId = "room1"),
//                Device(id = "dev2", name = "Konsol", type = "Console", powerWatts = 80, hoursPerDay = 2f, brand = "Sony", model = "PS5", roomId = "room1"),
//                Device(id = "dev3", name = "Lampa", type = "Light", powerWatts = 15, hoursPerDay = 6f, roomId = "room1")
//            )
//        ),
//        RoomData(
//            id = "room2",
//            name = "Mətbəx",
//            type = "Kitchen",
//            size = 15,
//            devices = listOf(
//                Device(id = "dev4", name = "Soyuducu", type = "Fridge", powerWatts = 120, hoursPerDay = 24f, brand = "Samsung", model = "X900", roomId = "room2"),
//                Device(id = "dev5", name = "Qabyuyan", type = "Dishwasher", powerWatts = 1500, hoursPerDay = 1f, brand = "Bosch", model = "B-24", roomId = "room2"),
//                Device(id = "dev6", name = "Mikrodalğa", type = "Microwave", powerWatts = 800, hoursPerDay = 0.5f, brand = "Sharp", model = "R299", roomId = "room2")
//            )
//        ),
//        RoomData(
//            id = "room3",
//            name = "Yataq otağı",
//            type = "Bedroom",
//            size = 18,
//            devices = listOf(
//                Device(id = "dev7", name = "Kondisioner", type = "AC", powerWatts = 1500, hoursPerDay = 3f, brand = "Mitsubishi", model = "MZ-SF", roomId = "room3"),
//                Device(id = "dev8", name = "Lampa", type = "Light", powerWatts = 12, hoursPerDay = 8f, roomId = "room3"),
//                Device(id = "dev9", name = "Laptop", type = "Laptop", powerWatts = 60, hoursPerDay = 4f, brand = "Apple", model = "MacBook Air", roomId = "room3")
//            )
//        ),
//        RoomData(
//            id = "room4",
//            name = "Uşaq otağı",
//            type = "Child Room",
//            size = 13,
//            devices = listOf(
//                Device(id = "dev10", name = "Komputer", type = "PC", powerWatts = 300, hoursPerDay = 2f, brand = "Asus", model = "TUF-Gaming", roomId = "room4"),
//                Device(id = "dev11", name = "PlayStation", type = "Console", powerWatts = 90, hoursPerDay = 1f, brand = "Sony", model = "PS4", roomId = "room4"),
//                Device(id = "dev12", name = "Masa lampası", type = "Table Lamp", powerWatts = 10, hoursPerDay = 5f, roomId = "room4")
//            )
//        )
//    )
//
//    CoroutineScope(Dispatchers.IO).launch {
//        // User doc info
//        db.collection("users")
//            .document(uid)
//            .set(
//                mapOf(
//                    "displayName" to (user.displayName ?: "Demo User"),
//                    "email" to user.email,
//                    "createdAt" to Timestamp.now()
//                ),
//                com.google.firebase.firestore.SetOptions.merge()
//            ).await()
//
//        // --- Main Usage Calculation Lists ---
//        val generalUsageList = MutableList(numDays * usageIntervalsPerDay) { 0f }
//        val roomUsageMap = mutableMapOf<String, MutableList<Float>>() // roomId -> hourly list
//
//        // --- Upload Rooms, Devices, Device Usages, Aggregate Room/General Usage ---
//        for (room in rooms) {
//            val roomDoc = db.collection("users")
//                .document(uid)
//                .collection("rooms")
//                .document(room.id)
//
//            roomDoc.set(
//                mapOf(
//                    "name" to room.name,
//                    "type" to room.type,
//                    "size" to room.size
//                ),
//                com.google.firebase.firestore.SetOptions.merge()
//            ).await()
//
//            // Hər otağa usage listi (init 0)
//            roomUsageMap[room.id] = MutableList(numDays * usageIntervalsPerDay) { 0f }
//
//            for (device in room.devices) {
//                val deviceDoc = roomDoc
//                    .collection("devices")
//                    .document(device.id)
//
//                deviceDoc.set(
//                    mapOf(
//                        "name" to device.name,
//                        "type" to device.type,
//                        "powerWatts" to (device.powerWatts ?: 0),
//                        "hoursPerDay" to (device.hoursPerDay ?: 0f),
//                        "roomId" to device.roomId,
//                        "brand" to device.brand,
//                        "model" to device.model
//                    ),
//                    com.google.firebase.firestore.SetOptions.merge()
//                ).await()
//
//                // Device usage və toplanma
//                val usageColl = deviceDoc.collection("usage")
//                for (day in 0 until numDays) {
//                    for (hour in 0 until usageIntervalsPerDay) {
//                        val idx = day * usageIntervalsPerDay + hour
//                        val dt = start.plusDays(day.toLong()).plusHours(hour.toLong())
//                        val isActive = when (device.type.lowercase()) {
//                            "fridge" -> true
//                            "ac", "kondisioner" -> hour in 13..22
//                            "tv", "televizor" -> hour in 17..23
//                            "microwave" -> hour in listOf(8,13,19)
//                            "dishwasher", "qabyuyan" -> hour == 21
//                            "laptop", "pc", "komputer" -> hour in 15..20
//                            "light", "masa lampası", "table lamp", "lampa" -> hour in 19..23 || hour in 6..7
//                            "console", "konsol", "playstation" -> hour in 19..23
//                            else -> Random.nextBoolean()
//                        }
//                        val basePower = device.powerWatts ?: 0
//                        val hpDay = device.hoursPerDay ?: 0f
//                        val perHour = if (isActive) {
//                            val shouldRun = hpDay > 0f && (Random.nextFloat() < hpDay / 24f)
//                            if (shouldRun || basePower > 500) {
//                                val efficiency = 0.7f + Random.nextFloat() * 0.3f
//                                (basePower.toFloat() / 1000f) * efficiency
//                            } else {
//                                0.01f + Random.nextFloat() * 0.02f
//                            }
//                        } else {
//                            0.01f + Random.nextFloat() * 0.01f
//                        }
//                        val usageVal = String.format("%.3f", perHour).toFloat()
//                        val timeStr = dt.format(fmt)
//                        usageColl.add(mapOf("datetime" to timeStr, "value" to usageVal)).await()
//
//                        // OTAQ və ÜMUMİ usage-lara topla
//                        roomUsageMap[room.id]?.set(idx, roomUsageMap[room.id]?.get(idx)?.plus(usageVal) ?: usageVal)
//                        generalUsageList[idx] += usageVal
//                    }
//                }
//            }
//
//            // Room usage (aggregate)
//            val roomUsageColl = roomDoc.collection("usage")
//            for (idx in roomUsageMap[room.id]!!.indices) {
//                val timeStr = start.plusHours(idx.toLong()).format(fmt)
//                val usageVal = String.format("%.3f", roomUsageMap[room.id]!![idx]).toFloat()
//                roomUsageColl.add(mapOf("datetime" to timeStr, "value" to usageVal)).await()
//            }
//        }
//
//        // General/Ev usage (aggregate)
//        val generalUsageColl = db.collection("users")
//            .document(uid)
//            .collection("generalUsage")
//        for (idx in generalUsageList.indices) {
//            val timeStr = start.plusHours(idx.toLong()).format(fmt)
//            val usageVal = String.format("%.3f", generalUsageList[idx]).toFloat()
//            generalUsageColl.add(mapOf("datetime" to timeStr, "value" to usageVal)).await()
//        }
//        println("Qurğu, Otaq və Ümumi usage-lər uğurla əlavə olundu!")
//    }
}
