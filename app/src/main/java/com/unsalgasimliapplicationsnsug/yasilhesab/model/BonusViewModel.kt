import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class BonusViewModel : ViewModel() {
    private val _userPoints = MutableStateFlow(340)
    val userPoints: StateFlow<Int> = _userPoints

    // Mock: How user earned points
    private val _history = MutableStateFlow(
        listOf(
            BonusHistoryItem("1", "2025-07-09", "Logged in today", 10),
            BonusHistoryItem("2", "2025-07-09", "Saved your first device", 30),
            BonusHistoryItem("3", "2025-07-08", "Checked statistics", 20),
            BonusHistoryItem("4", "2025-07-07", "3 days login streak", 40),
            BonusHistoryItem("5", "2025-07-07", "Invited a friend", 100)
        )
    )
    val history: StateFlow<List<BonusHistoryItem>> = _history

    // Mock: What can be bought
    private val _rewards = MutableStateFlow(
        listOf(
            RewardItem("r1", "1-month Premium", "Ad-free and bonus stats", 300, "🌟"),
            RewardItem("r2", "Eco Shopping Discount", "5% off on partner stores", 200, "🛒"),
            RewardItem("r3", "Special Avatar", "Unique profile icon", 150, "😎"),
            RewardItem("r4", "Green Badge", "Eco-friendly user badge", 80, "🍃")
        )
    )
    val rewards: StateFlow<List<RewardItem>> = _rewards
}
