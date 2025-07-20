package com.unsalgasimliapplicationsnsug.yasilhesab.compenents

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.unsalgasimliapplicationsnsug.yasilhesab.ui.theme.EcoBorder
import com.unsalgasimliapplicationsnsug.yasilhesab.ui.theme.EcoCardBg
import com.unsalgasimliapplicationsnsug.yasilhesab.ui.theme.EcoGreen
import com.unsalgasimliapplicationsnsug.yasilhesab.ui.theme.EcoTextNormal

@Composable
fun ChipItem(
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
    leadingIcon: String? = null
) {
    // Animations for border and color
    val borderAnim by animateDpAsState(targetValue = if (selected) 3.dp else 1.dp, label = "")
    val colorAnim by animateColorAsState(targetValue = if (selected) EcoGreen else EcoBorder, label = "")

    Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent
        ),
        border = BorderStroke(borderAnim, colorAnim),
        modifier = Modifier
            .height(48.dp)
            .defaultMinSize(minWidth = 105.dp)
            .shadow(
                if (selected) 8.dp else 2.dp,
                RoundedCornerShape(18.dp),
                ambientColor = EcoGreen.copy(alpha = 0.22f),
                spotColor = EcoGreen.copy(alpha = 0.13f)
            )
            .clip(RoundedCornerShape(18.dp))
            .clickable { onClick() }
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    if (selected)
                        EcoGreen.copy(alpha = 0.10f)
                    else
                        EcoCardBg.copy(alpha = 0.65f),
                    shape = RoundedCornerShape(18.dp)
                )
                .padding(horizontal = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            if (!leadingIcon.isNullOrEmpty()) {
                Text(
                    text = leadingIcon,
                    fontSize = 18.sp,
                    modifier = Modifier.padding(end = 5.dp)
                )
            }
            Text(
                text = text,
                color = if (selected) EcoGreen else EcoTextNormal,
                fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Medium,
                fontSize = 16.sp,
                maxLines = 1
            )
        }
    }
}
