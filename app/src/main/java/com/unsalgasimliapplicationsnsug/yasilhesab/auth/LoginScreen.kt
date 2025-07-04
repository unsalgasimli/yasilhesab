package com.unsalgasimliapplicationsnsug.yasilhesab.ui.auth

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.MailOutline
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.*
import androidx.compose.ui.unit.*
import com.google.firebase.auth.FirebaseAuth
import com.unsalgasimliapplicationsnsug.yasilhesab.R

@Composable
fun LoginScreen(
    onNavigateToRegister: () -> Unit,
    onLoginSuccess: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }
    var errorMsg by remember { mutableStateOf<String?>(null) }
    var emailError by remember { mutableStateOf<String?>(null) }
    var passwordError by remember { mutableStateOf<String?>(null) }
    val isEmailValid = android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    val isPasswordValid = password.length >= 6

    val keyboardController = LocalSoftwareKeyboardController.current
    val imeBottom = WindowInsets.ime.getBottom(LocalDensity.current)
    val cardYOffset by animateDpAsState(
        targetValue = if (imeBottom > 0) (-130).dp else 0.dp,
        animationSpec = tween(durationMillis = 430, easing = FastOutSlowInEasing),
        label = "cardYOffset"
    )

    val colors = MaterialTheme.colorScheme

    // Dynamic border color: lighter in light, lighter alpha in dark
    val isDark = colors.background.luminance() < 0.5f
    val cardBorderColor = if (isDark) Color(0x22FFFFFF) else Color(0x14000000)

    Box(
        Modifier
            .fillMaxSize()
            .background(colors.background)
    ) {
        // Card (centered, anim offset)
        Box(
            Modifier
                .align(Alignment.Center)
                .offset(y = cardYOffset)
                .padding(horizontal = 26.dp)
        ) {
            Card(
                Modifier
                    .fillMaxWidth()
                    .heightIn(min = 330.dp, max = 400.dp)
                    .border(1.dp, cardBorderColor, RoundedCornerShape(30.dp))
                    .graphicsLayer {
                        shadowElevation = 28f
                        shape = RoundedCornerShape(30.dp)
                        clip = true
                    },
                shape = RoundedCornerShape(30.dp),
                colors = CardDefaults.cardColors(containerColor = colors.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
            ) {
                Column(
                    Modifier.padding(horizontal = 24.dp, vertical = 22.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_leaf_calculator),
                        contentDescription = null,
                        tint = Color.Unspecified, // original logo color
                        modifier = Modifier.size(62.dp).padding(bottom = 6.dp)
                    )
                    Text(
                        "Xoş gəldin!",
                        color = colors.onSurface,
                        fontSize = 23.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 5.dp)
                    )
                    Text(
                        "Ev enerjinə qənaət et",
                        color = colors.primary,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium
                    )
                    Spacer(Modifier.height(12.dp))
                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it; emailError = null },
                        label = { Text("Email", color = colors.primary, fontWeight = FontWeight.SemiBold) },
                        placeholder = { Text("your@email.com", color = colors.primary.copy(alpha = 0.22f)) },
                        leadingIcon = { Icon(Icons.Filled.MailOutline, null, tint = colors.primary) },
                        isError = emailError != null,
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color.Transparent, RoundedCornerShape(12.dp)),
                        shape = RoundedCornerShape(12.dp),
                        textStyle = LocalTextStyle.current.copy(color = colors.onSurface, fontSize = 15.sp),
                        keyboardOptions = KeyboardOptions.Default.copy(
                            keyboardType = KeyboardType.Email, imeAction = ImeAction.Next
                        ),
                        keyboardActions = KeyboardActions(onNext = { keyboardController?.show() }),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = colors.primary,
                            unfocusedBorderColor = colors.primary.copy(alpha = 0.13f),
                            errorBorderColor = colors.error,
                            focusedTextColor = colors.onSurface,
                            unfocusedTextColor = colors.onSurface,
                            cursorColor = colors.primary
                        )
                    )
                    if (emailError != null) {
                        Text(
                            emailError!!, color = colors.error, fontSize = 12.sp,
                            modifier = Modifier.padding(start = 6.dp, top = 2.dp)
                        )
                    }
                    Spacer(Modifier.height(10.dp))
                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it; passwordError = null },
                        label = { Text("Şifrə", color = colors.primary, fontWeight = FontWeight.SemiBold) },
                        placeholder = { Text("••••••••", color = colors.primary.copy(alpha = 0.22f)) },
                        leadingIcon = { Icon(Icons.Filled.Lock, null, tint = colors.primary) },
                        trailingIcon = {
                            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                Icon(
                                    imageVector = if (passwordVisible) Icons.Filled.VisibilityOff else Icons.Filled.Visibility,
                                    contentDescription = null,
                                    tint = colors.primary
                                )
                            }
                        },
                        isError = passwordError != null,
                        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color.Transparent, RoundedCornerShape(12.dp)),
                        shape = RoundedCornerShape(12.dp),
                        textStyle = LocalTextStyle.current.copy(color = colors.onSurface, fontSize = 15.sp),
                        keyboardOptions = KeyboardOptions.Default.copy(
                            keyboardType = KeyboardType.Password, imeAction = ImeAction.Done
                        ),
                        keyboardActions = KeyboardActions(onDone = { keyboardController?.hide() }),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = colors.primary,
                            unfocusedBorderColor = colors.primary.copy(alpha = 0.13f),
                            errorBorderColor = colors.error,
                            focusedTextColor = colors.onSurface,
                            unfocusedTextColor = colors.onSurface,
                            cursorColor = colors.primary
                        )
                    )
                    if (passwordError != null) {
                        Text(
                            passwordError!!, color = colors.error, fontSize = 12.sp,
                            modifier = Modifier.padding(start = 6.dp, top = 2.dp)
                        )
                    }
                    Spacer(Modifier.height(14.dp))
                    AnimatedVisibility(
                        visible = errorMsg != null,
                        enter = fadeIn() + slideInVertically(),
                        exit = fadeOut()
                    ) {
                        errorMsg?.let {
                            Text(
                                it,
                                color = colors.error,
                                fontWeight = FontWeight.Medium,
                                fontSize = 13.sp,
                                modifier = Modifier.padding(vertical = 5.dp)
                            )
                        }
                    }
                    Button(
                        onClick = {
                            var valid = true
                            if (email.isBlank()) { emailError = "Email boş ola bilməz"; valid = false }
                            else if (!isEmailValid) { emailError = "Email düzgün deyil"; valid = false }
                            if (password.isBlank()) { passwordError = "Şifrə boş ola bilməz"; valid = false }
                            else if (!isPasswordValid) { passwordError = "Min. 6 simvol"; valid = false }
                            if (!valid) return@Button
                            loginUser(
                                email, password,
                                setLoading = { isLoading = it },
                                onError = { errorMsg = it },
                                onSuccess = onLoginSuccess
                            )
                        },
                        enabled = !isLoading,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(46.dp)
                            .padding(top = 4.dp),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = colors.primary,
                            contentColor = colors.onPrimary,
                            disabledContainerColor = colors.primary.copy(alpha = 0.3f)
                        )
                    ) {
                        if (isLoading) {
                            CircularProgressIndicator(
                                Modifier.size(20.dp),
                                strokeWidth = 2.dp,
                                color = colors.onPrimary
                            )
                        } else {
                            Text("Daxil ol", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                        }
                    }
                    Spacer(Modifier.height(10.dp))
                    TextButton(
                        onClick = { /* TODO: Şifrəni unutdun? */ },
                        modifier = Modifier.align(Alignment.End)
                    ) {
                        Text(
                            "Şifrəni unutdun?", color = colors.primary,
                            fontSize = 13.sp, fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        }

        Box(Modifier.align(Alignment.BottomCenter).padding(bottom = 36.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    "Hesabın yoxdur? ",
                    color = colors.onBackground.copy(alpha = 0.72f),
                    fontSize = 15.sp
                )
                TextButton(
                    onClick = onNavigateToRegister,
                    colors = ButtonDefaults.textButtonColors(contentColor = colors.primary),
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Text("Qeydiyyatdan keç", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                }
            }
        }
    }
}

private fun loginUser(
    email: String,
    password: String,
    setLoading: (Boolean) -> Unit,
    onError: (String?) -> Unit,
    onSuccess: () -> Unit
) {
    setLoading(true); onError(null)
    FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
        .addOnCompleteListener { t ->
            setLoading(false)
            if (t.isSuccessful) onSuccess() else onError(t.exception?.localizedMessage ?: "Unknown error")
        }
}
