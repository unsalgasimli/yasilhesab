package com.unsalgasimliapplicationsnsug.yasilhesab.screens

import SettingsViewModel
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter

@Composable
fun SettingsScreen(
    vm: SettingsViewModel = androidx.lifecycle.viewmodel.compose.viewModel(),
    onSignOut: () -> Unit = {}
) {
    val user by vm.user.collectAsState()
    var editMode by remember { mutableStateOf(false) }
    var showAvatarDialog by remember { mutableStateOf(false) }

    // Fields (temp state for edit mode)
    var name by remember { mutableStateOf(user.name) }
    var nickname by remember { mutableStateOf(user.nickname) }
    var email by remember { mutableStateOf(user.email) }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    val accent = Color(0xFF2CB67D)
    val bg = Color(0xFF181A20)
    val cardBg = Color(0xFF23272E)

    Column(
        Modifier
            .fillMaxSize()
            .background(bg)
            .padding(18.dp)
    ) {
        // Header
        Row(
            Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Account Settings", fontSize = 25.sp, color = accent, modifier = Modifier.weight(1f))
            IconButton(onClick = { editMode = !editMode }) {
                Icon(Icons.Default.Edit, contentDescription = "Edit", tint = accent)
            }
        }
        Spacer(Modifier.height(12.dp))

        // Avatar (clickable)
        Box(
            Modifier.align(Alignment.CenterHorizontally)
        ) {
            val painter = rememberAsyncImagePainter(
                user.avatarUrl ?: "https://api.dicebear.com/7.x/miniavs/svg?seed=${user.nickname.ifBlank { "anon" }}"
            )
            Image(
                painter = painter,
                contentDescription = "Avatar",
                modifier = Modifier
                    .size(90.dp)
                    .clip(CircleShape)
                    .shadow(7.dp, CircleShape)
                    .background(cardBg)
                    .clickable(enabled = editMode) { showAvatarDialog = true }
            )
            if (editMode) {
                Box(
                    Modifier
                        .align(Alignment.BottomEnd)
                        .size(32.dp)
                        .background(accent, CircleShape)
                        .clickable { showAvatarDialog = true },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.Edit, contentDescription = "Change Avatar", tint = Color.White, modifier = Modifier.size(17.dp))
                }
            }
        }
        Spacer(Modifier.height(16.dp))

        // Editable Form
        Card(
            shape = RoundedCornerShape(17.dp),
            colors = CardDefaults.cardColors(containerColor = cardBg),
            modifier = Modifier.fillMaxWidth().shadow(8.dp, RoundedCornerShape(17.dp))
        ) {
            Column(Modifier.padding(18.dp)) {
                SettingsField("Full Name", name, onValueChange = { name = it }, enabled = editMode)
                Spacer(Modifier.height(10.dp))
                SettingsField("Nickname", nickname, onValueChange = { nickname = it }, enabled = editMode)
                Spacer(Modifier.height(10.dp))
                SettingsField("Email", email, onValueChange = { email = it }, enabled = editMode, singleLine = true)
                Spacer(Modifier.height(10.dp))
                // Password change section
                Text("Password", fontWeight = FontWeight.SemiBold, color = Color.Gray, fontSize = 13.sp)
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("Change password") },
                    enabled = editMode,
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        val icon = if (passwordVisible) painterResource(android.R.drawable.ic_menu_view)
                        else painterResource(android.R.drawable.ic_secure)
                        IconButton(onClick = { passwordVisible = !passwordVisible }) {
                            Icon(icon, contentDescription = "Toggle password visibility")
                        }
                    },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = accent,
                        unfocusedBorderColor = Color.DarkGray
                    )
                )
            }
        }
        Spacer(Modifier.height(28.dp))

        // Save/Cancel
        if (editMode) {
            Row(
                Modifier.align(Alignment.End),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedButton(onClick = {
                    // Reset fields
                    name = user.name
                    nickname = user.nickname
                    email = user.email
                    password = ""
                    editMode = false
                }) { Text("Cancel") }
                Button(
                    onClick = {
                        val updatedUser = user.copy(
                            name = name,
                            nickname = nickname,
                            email = email
                            // password is not stored in user model directly for security
                        )
                        vm.updateUser(updatedUser)
                        // If password is set, update password here with your logic!
                        editMode = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = accent)
                ) { Text("Save") }
            }
        }

        Spacer(Modifier.height(30.dp))

        // Sign Out
        Button(
            onClick = { vm.signOut(); onSignOut() },
            modifier = Modifier.align(Alignment.CenterHorizontally),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
        ) {
            Icon(Icons.Default.ExitToApp, contentDescription = null, tint = Color.White, modifier = Modifier.size(19.dp))
            Spacer(Modifier.width(8.dp))
            Text("Sign Out", color = Color.White)
        }
    }

    // Avatar Change Dialog (template)
    if (showAvatarDialog) {
        AlertDialog(
            onDismissRequest = { showAvatarDialog = false },
            title = { Text("Change Avatar") },
            text = { Text("Avatar uploading/changing not implemented in this template. You can add file/image picker here.") },
            confirmButton = {
                Button(onClick = { showAvatarDialog = false }) { Text("OK") }
            }
        )
    }
}

@Composable
fun SettingsField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    enabled: Boolean = false,
    singleLine: Boolean = false
) {
    Text(label, fontWeight = FontWeight.SemiBold, color = Color.Gray, fontSize = 13.sp)
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier.fillMaxWidth(),
        enabled = enabled,
        singleLine = singleLine,
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Color(0xFF2CB67D),
            unfocusedBorderColor = Color.DarkGray
        )
    )
}
