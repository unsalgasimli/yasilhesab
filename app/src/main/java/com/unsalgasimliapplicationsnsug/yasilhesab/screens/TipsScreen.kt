package com.unsalgasimliapplicationsnsug.yasilhesab.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.unsalgasimliapplicationsnsug.yasilhesab.ui.theme.EcoGreen

private val AllTips = listOf(
    "Otaq və qurğu əlavə edib enerji istifadənizi izləyin.",
    "Qurğu statistikasına baxıb qənaət üçün təkliflərə əməl edin.",
    "Enerji sərfiyyatınızı azaltmaq üçün həftəlik hesabatı yoxlayın.",
    "Daha çox məsləhət üçün tətbiqdə qalın!"
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TipsScreen(
    tips: List<String> = AllTips,
    onBack: (() -> Unit)? = null
) {
    Scaffold(
        topBar = {
            TopAppBar(  // <-- YENİLƏNDİ
                title = { Text("Məsləhətlər", color = EcoGreen) },
                navigationIcon = {
                    if (onBack != null) {
                        IconButton(onClick = onBack) {
                            Icon(
                                imageVector = Icons.Default.ArrowBack,
                                contentDescription = "Geri",
                                tint = EcoGreen
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.mediumTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        }
    ) { padding ->
        Column(
            Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(16.dp)
        ) {
            tips.forEach { tip ->
                Card(
                    Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp),
                    colors = CardDefaults.cardColors(containerColor = EcoGreen.copy(alpha = 0.07f))
                ) {
                    Text("💡  $tip", Modifier.padding(16.dp))
                }
            }
        }
    }
}
