package com.unsalgasimliapplicationsnsug.yasilhesab.compenents

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.unsalgasimliapplicationsnsug.yasilhesab.ui.theme.*

data class DrawerItem(
    val title: String,
    val key: String,
    val icon: ImageVector? = null
)

private val DrawerMenuItems = listOf(
    DrawerItem("Dashboard", "Dashboard", Icons.Default.Dashboard),
    DrawerItem("Home Setting", "HomeSetting", Icons.Default.Home),
    DrawerItem("Bonuses", "Bonuses", Icons.Default.Star),
    DrawerItem("Tips", "Tips", Icons.Default.Lightbulb),  // <-- Səhifəyə yönləndirən item
    DrawerItem("Settings", "UserSettings", Icons.Default.Settings)
)

private val DefaultTips = listOf(
    "Otaq və qurğu əlavə edib enerji istifadənizi izləyin.",
    "Qurğu statistikasına baxıb qənaət üçün təkliflərə əməl edin.",
    "Enerji sərfiyyatınızı azaltmaq üçün həftəlik hesabatı yoxlayın.",
    "Daha çox məsləhət üçün tətbiqdə qalın!"
)

@Composable
fun EcoDrawer(
    selected: String,
    onDestinationClicked: (String) -> Unit,
    modifier: Modifier = Modifier,
    tips: List<String> = DefaultTips,
    version: String = "v1.0.0"
) {
    var tipIndex by remember { mutableStateOf(0) }

    ModalDrawerSheet(
        modifier = modifier
            .width(250.dp)
            .background(EcoCardBg, RoundedCornerShape(topEnd = 24.dp, bottomEnd = 24.dp)),
        drawerContainerColor = EcoCardBg,
        drawerContentColor = EcoTextNormal
    ) {
        Spacer(Modifier.height(32.dp))
        Text(
            text = "Menu",
            style = MaterialTheme.typography.titleLarge,
            color = EcoGreen,
            modifier = Modifier.padding(start = 24.dp, bottom = 18.dp)
        )

        DrawerMenuItems.forEach { item ->
            EcoDrawerMenuItem(
                text = item.title,
                selected = selected == item.key,
                onClick = { onDestinationClicked(item.key) },
                icon = item.icon
            )
        }

        Spacer(Modifier.weight(1f))

        Divider(
            color = EcoGreen.copy(alpha = 0.12f),
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 10.dp)
        )

        // Tips section (card only changes tip on tap, not navigation)
        Text(
            text = "Məsləhət",
            color = EcoGreen,
            fontWeight = FontWeight.SemiBold,
            fontSize = 15.sp,
            modifier = Modifier.padding(start = 24.dp, top = 6.dp, bottom = 2.dp)
        )

        Row(
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 2.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(EcoGreen.copy(alpha = 0.06f))
                .clickable {
                    tipIndex = (tipIndex + 1) % tips.size
                }
                .padding(horizontal = 10.dp, vertical = 8.dp)
        ) {
            Text(
                text = "💡  ${tips[tipIndex]}",
                color = EcoTextPassive,
                fontSize = 14.sp,
                lineHeight = 18.sp,
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(Modifier.height(10.dp))
        Text(
            version,
            color = EcoTextPassive,
            style = MaterialTheme.typography.labelSmall,
            modifier = Modifier.padding(start = 22.dp, bottom = 12.dp)
        )
        Spacer(Modifier.height(6.dp))
    }
}


@Composable
fun EcoDrawerMenuItem(
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
    icon: ImageVector? = null
) {
    val color = if (selected) EcoGreen else EcoTextNormal
    val bg = if (selected) EcoGreen.copy(alpha = 0.11f) else EcoCardBg
    Row(
        verticalAlignment = androidx.compose.ui.Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(bg)
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 14.dp)
    ) {
        if (icon != null) {
            Icon(
                icon,
                contentDescription = text,
                tint = color,
                modifier = Modifier.size(22.dp).padding(end = 12.dp)
            )
        }
        Text(
            text,
            color = color,
            fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium,
            fontSize = 16.sp
        )
    }
}
