import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun BonusesScreen(
    vm: BonusViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    val userPoints by vm.userPoints.collectAsState()
    val history by vm.history.collectAsState()
    val rewards by vm.rewards.collectAsState()

    val accent = Color(0xFF2CB67D)
    val cardBg = Color(0xFF23272E)
    val bg = Color(0xFF181A20)

    Column(
        Modifier
            .fillMaxSize()
            .background(bg)
            .padding(14.dp)
    ) {
        // HEADER
        Text(
            "Bonuses & Rewards",
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold,
            color = accent
        )
        Spacer(Modifier.height(4.dp))
        Text("Earn points by using the app and unlock rewards!", color = Color.Gray, fontSize = 15.sp)
        Spacer(Modifier.height(16.dp))

        // USER POINTS CARD
        Card(
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = cardBg),
            modifier = Modifier
                .fillMaxWidth()
                .shadow(9.dp, RoundedCornerShape(18.dp))
        ) {
            Column(
                Modifier.padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Your Points", fontWeight = FontWeight.Medium, color = accent, fontSize = 17.sp)
                Spacer(Modifier.height(6.dp))
                Text(
                    "$userPoints",
                    fontSize = 42.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }

        Spacer(Modifier.height(22.dp))

        // HOW TO EARN POINTS (Show only as ideas, can use a LazyRow for more style)
        Text("How to Earn Points", fontWeight = FontWeight.SemiBold, color = accent, fontSize = 18.sp)
        Spacer(Modifier.height(5.dp))
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            EarnMethodCard("📅", "Daily Login", "+10 pts")
            EarnMethodCard("🔄", "Streaks", "+30 pts")
            EarnMethodCard("💡", "Save Device", "+20 pts")
            EarnMethodCard("🤝", "Invite Friend", "+100 pts")
        }

        Spacer(Modifier.height(20.dp))

        // EARNED HISTORY
        Text("Points History", fontWeight = FontWeight.SemiBold, color = accent, fontSize = 18.sp)
        Spacer(Modifier.height(6.dp))
        Card(
            shape = RoundedCornerShape(13.dp),
            colors = CardDefaults.cardColors(containerColor = cardBg),
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = 140.dp)
        ) {
            LazyColumn(
                Modifier.padding(vertical = 4.dp),
                contentPadding = PaddingValues(horizontal = 10.dp)
            ) {
                items(history) { item ->
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(item.date, color = Color.Gray, fontSize = 13.sp, modifier = Modifier.width(78.dp))
                        Column(Modifier.weight(1f)) {
                            Text(item.description, color = Color.White, fontSize = 15.sp)
                        }
                        Text("+${item.points}", color = accent, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    }
                    Divider(color = Color(0xFF1A1A1A))
                }
            }
        }

        Spacer(Modifier.height(24.dp))

        // REWARDS CATALOG
        Text("Redeem Rewards", fontWeight = FontWeight.SemiBold, color = accent, fontSize = 18.sp)
        Spacer(Modifier.height(7.dp))
        LazyColumn(
            Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(bottom = 20.dp)
        ) {
            items(rewards) { reward ->
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = cardBg),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 7.dp)
                        .shadow(6.dp, RoundedCornerShape(16.dp))
                ) {
                    Row(
                        Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(reward.icon, fontSize = 28.sp, modifier = Modifier.padding(end = 12.dp))
                        Column(Modifier.weight(1f)) {
                            Text(reward.name, fontWeight = FontWeight.Bold, color = Color.White, fontSize = 17.sp)
                            Text(reward.description, color = Color.Gray, fontSize = 14.sp)
                        }
                        Button(
                            onClick = { /* TODO: Add redeem logic */ },
                            enabled = userPoints >= reward.pointsRequired,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (userPoints >= reward.pointsRequired) accent else Color.Gray
                            )
                        ) {
                            Text("${reward.pointsRequired} pts")
                        }
                    }
                }
            }
        }
    }
}

// EarnMethodCard helper
@Composable
fun EarnMethodCard(icon: String, title: String, points: String) {
    Card(
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF24282F)),
        modifier = Modifier
            .padding(2.dp)
            .width(82.dp)
            .height(64.dp)
    ) {
        Column(
            Modifier
                .padding(7.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(icon, fontSize = 22.sp)
            Spacer(Modifier.height(2.dp))
            Text(title, color = Color.White, fontWeight = FontWeight.SemiBold, fontSize = 13.sp, maxLines = 1)
            Text(points, color = Color(0xFF23C882), fontSize = 12.sp, fontWeight = FontWeight.Bold)
        }
    }
}
