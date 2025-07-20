data class Device(
    val id: String = "",
    val name: String = "",
    val type: String = "",
    val powerWatts: Int? = null,
    val hoursPerDay: Float? = null,
    val brand: String = "",
    val model: String = "",
    val roomId: String = "",             // <--- ROOM ID BURADA!
    val readings: List<Reading> = emptyList()
)

data class RoomData(
    val id: String = "",
    val name: String = "",
    val type: String = "",
    val size: Int = 0,
    val devices: List<Device> = emptyList()
)

data class Reading(
    val datetime: String = "",
    val value: Float = 0f
)
data class BonusHistoryItem(
    val id: String,
    val date: String,        // e.g. "2025-07-10"
    val description: String, // "Used app 3 days in a row"
    val points: Int
)

data class RewardItem(
    val id: String,
    val name: String,
    val description: String,
    val pointsRequired: Int,
    val icon: String
)
data class UserProfileUi(
    val avatarUrl: String? = null,
    val name: String = "",
    val nickname: String = "",
    val email: String = "",
    val password: String = "" // Only for input, don't show in UI for security
)
