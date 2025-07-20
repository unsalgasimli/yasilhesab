package com.unsalgasimliapplicationsnsug.yasilhesab.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.unsalgasimliapplicationsnsug.yasilhesab.R
import com.unsalgasimliapplicationsnsug.yasilhesab.ui.theme.EcoGreen
import com.unsalgasimliapplicationsnsug.yasilhesab.ui.theme.EcoBg

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EcoTopBar(
    title: String,
    onMenuClick: () -> Unit,
    onProfileClick: () -> Unit,
    userName: String? = null // Optional, if you want to show it
) {
    TopAppBar(
        title = {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
                color = EcoGreen
            )
        },
        navigationIcon = {
            IconButton(onClick = onMenuClick) {
                Icon(
                    imageVector = Icons.Default.Menu,
                    contentDescription = stringResource(R.string.menu),
                    tint = EcoGreen
                )
            }
        },
        actions = {
            // Show user name or only the icon
            if (!userName.isNullOrBlank()) {
                Text(
                    text = userName,
                    color = EcoGreen,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(end = 8.dp)
                )
            }
            IconButton(onClick = onProfileClick) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = stringResource(R.string.profile),
                    tint = EcoGreen
                )
            }
        },
        colors = TopAppBarDefaults.mediumTopAppBarColors(containerColor = EcoBg)
    )
}
