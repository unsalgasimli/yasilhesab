package com.unsalgasimliapplicationsnsug.yasilhesab.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.unsalgasimliapplicationsnsug.yasilhesab.ui.theme.EcoGreen
import com.unsalgasimliapplicationsnsug.yasilhesab.ui.theme.EcoCardBg
import com.unsalgasimliapplicationsnsug.yasilhesab.ui.theme.EcoBorder
import com.unsalgasimliapplicationsnsug.yasilhesab.ui.theme.EcoTextPassive

@Composable
fun StatCard(
    value: String,
    unit: String,
    icon: ImageVector,
    accent: Color = EcoGreen,
    cardBg: Color = EcoCardBg,
    border: Color = EcoBorder,
    isSelected: Boolean = false,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        border = BorderStroke(if (isSelected) 2.dp else 1.dp, border),
        modifier = modifier
            .height(98.dp)
            .shadow(
                if (isSelected) 14.dp else 7.dp,
                spotColor = accent.copy(alpha = if (isSelected) 0.4f else 0.2f)
            )
    ) {
        Box(
            Modifier
                .background(
                    if (isSelected) accent.copy(alpha = 0.09f) else cardBg,
                    RoundedCornerShape(18.dp)
                )
                .fillMaxSize()
        ) {
            Column(
                Modifier.align(Alignment.Center),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(
                    icon,
                    contentDescription = null,
                    modifier = Modifier.size(28.dp),
                    tint = accent // Use EcoTextNormal if you want it always white
                )
                Spacer(Modifier.height(5.dp))
                Text(
                    value,
                    color = accent,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )
                Text(
                    unit,
                    fontSize = 13.sp,
                    color = EcoTextPassive
                )
            }
        }
    }
}
