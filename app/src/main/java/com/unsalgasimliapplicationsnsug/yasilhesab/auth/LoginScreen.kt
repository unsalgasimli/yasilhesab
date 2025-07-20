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
import androidx.compose.ui.draw.*
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.platform.*
import androidx.compose.ui.text.input.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.*
import android.graphics.RenderEffect
import android.graphics.Shader
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.ui.focus.*
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
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
        targetValue = if (imeBottom > 0) (-110).dp else 0.dp,
        animationSpec = tween(durationMillis = 430, easing = FastOutSlowInEasing),
        label = "cardYOffset"
    )

    // Stringləri başda saxla
    val welcomeStr = stringResource(R.string.welcome)
    val subtitleStr = stringResource(R.string.subtitle)
    val emailLabelStr = stringResource(R.string.email_label)
    val emailPlaceholderStr = stringResource(R.string.email_placeholder)
    val passwordLabelStr = stringResource(R.string.password_label)
    val passwordPlaceholderStr = stringResource(R.string.password_placeholder)
    val loginStr = stringResource(R.string.login)
    val forgotPasswordStr = stringResource(R.string.forgot_password)
    val noAccountStr = stringResource(R.string.no_account)
    val registerStr = stringResource(R.string.register)
    val emailEmptyStr = stringResource(R.string.email_empty)
    val emailInvalidStr = stringResource(R.string.email_invalid)
    val passwordEmptyStr = stringResource(R.string.password_empty)
    val passwordShortStr = stringResource(R.string.password_short)

    // Theme-dən rəngləri götür
    val colors = MaterialTheme.colorScheme
    val ecoGreen = colors.primary
    val ecoGreenLight = colors.primary.copy(alpha = 0.25f)
    val ecoError = colors.error
    val ecoOnBg = colors.onSurface
    val cardBorder = if (colors.background.luminance() > 0.5f) Color(0x11000000) else Color(0x22FFFFFF)
    val cardBg = colors.surface

    Box(
        Modifier
            .fillMaxSize()
            .background(colors.background)
    ) {
        // Card (always center, but anim offset)
        Box(
            Modifier
                .align(Alignment.Center)
                .offset(y = cardYOffset)
                .padding(horizontal = 26.dp)
        ) {
            Card(
                Modifier
                    .fillMaxWidth()
                    .heightIn(min = 340.dp, max = 420.dp)
                    .border(1.dp, cardBorder, RoundedCornerShape(30.dp))
                    .graphicsLayer {
                        shadowElevation = 28f
                        shape = RoundedCornerShape(30.dp)
                        clip = true
                    },
                shape = RoundedCornerShape(30.dp),
                colors = CardDefaults.cardColors(containerColor = cardBg),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
            ) {
                Column(
                    Modifier
                        .padding(horizontal = 24.dp, vertical = 22.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // LOGO (böyük, dəyişməz)
                    Icon(
                        painter = painterResource(id = R.drawable.ic_leaf_calculator),
                        contentDescription = null,
                        tint = Color.Unspecified,
                        modifier = Modifier
                            .size(96.dp)
                            .padding(bottom = 8.dp)
                    )
                    Text(
                        welcomeStr,
                        color = ecoOnBg,
                        fontSize = 23.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 5.dp)
                    )
                    Text(
                        subtitleStr,
                        color = ecoGreen,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium
                    )
                    Spacer(Modifier.height(12.dp))
                    // Email
                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it; emailError = null },
                        label = { Text(emailLabelStr, color = ecoGreen, fontWeight = FontWeight.SemiBold) },
                        placeholder = { Text(emailPlaceholderStr, color = ecoGreenLight) },
                        leadingIcon = { Icon(Icons.Filled.MailOutline, null, tint = ecoGreen) },
                        isError = emailError != null,
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color.Transparent, RoundedCornerShape(12.dp)),
                        shape = RoundedCornerShape(12.dp),
                        textStyle = LocalTextStyle.current.copy(color = ecoOnBg, fontSize = 15.sp),
                        keyboardOptions = KeyboardOptions.Default.copy(
                            keyboardType = KeyboardType.Email, imeAction = ImeAction.Next
                        ),
                        keyboardActions = KeyboardActions(
                            onNext = { keyboardController?.show() }
                        ),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = ecoGreen,
                            unfocusedBorderColor = ecoGreenLight,
                            errorBorderColor = ecoError,
                            focusedTextColor = ecoOnBg,
                            unfocusedTextColor = ecoOnBg,
                            cursorColor = ecoGreen
                        )
                    )
                    if (emailError != null) {
                        Text(emailError!!, color = ecoError, fontSize = 12.sp, modifier = Modifier.padding(start = 6.dp, top = 2.dp))
                    }
                    Spacer(Modifier.height(10.dp))
                    // Password
                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it; passwordError = null },
                        label = { Text(passwordLabelStr, color = ecoGreen, fontWeight = FontWeight.SemiBold) },
                        placeholder = { Text(passwordPlaceholderStr, color = ecoGreenLight) },
                        leadingIcon = { Icon(Icons.Filled.Lock, null, tint = ecoGreen) },
                        trailingIcon = {
                            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                Icon(
                                    imageVector = if (passwordVisible) Icons.Filled.VisibilityOff else Icons.Filled.Visibility,
                                    contentDescription = null,
                                    tint = ecoGreen
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
                        textStyle = LocalTextStyle.current.copy(color = ecoOnBg, fontSize = 15.sp),
                        keyboardOptions = KeyboardOptions.Default.copy(
                            keyboardType = KeyboardType.Password, imeAction = ImeAction.Done
                        ),
                        keyboardActions = KeyboardActions(
                            onDone = { keyboardController?.hide() }
                        ),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = ecoGreen,
                            unfocusedBorderColor = ecoGreenLight,
                            errorBorderColor = ecoError,
                            focusedTextColor = ecoOnBg,
                            unfocusedTextColor = ecoOnBg,
                            cursorColor = ecoGreen
                        )
                    )
                    if (passwordError != null) {
                        Text(passwordError!!, color = ecoError, fontSize = 12.sp, modifier = Modifier.padding(start = 6.dp, top = 2.dp))
                    }
                    Spacer(Modifier.height(14.dp))
                    // Error Message
                    AnimatedVisibility(
                        visible = errorMsg != null,
                        enter = fadeIn() + slideInVertically(),
                        exit = fadeOut()
                    ) {
                        errorMsg?.let {
                            Text(
                                it,
                                color = ecoError,
                                fontWeight = FontWeight.Medium,
                                fontSize = 13.sp,
                                modifier = Modifier.padding(vertical = 5.dp)
                            )
                        }
                    }
                    Button(
                        onClick = {
                            var valid = true
                            if (email.isBlank()) { emailError = emailEmptyStr; valid = false }
                            else if (!isEmailValid) { emailError = emailInvalidStr; valid = false }
                            if (password.isBlank()) { passwordError = passwordEmptyStr; valid = false }
                            else if (!isPasswordValid) { passwordError = passwordShortStr; valid = false }
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
                            containerColor = ecoGreen,
                            contentColor = Color.White,
                            disabledContainerColor = ecoGreen.copy(alpha = 0.3f)
                        )
                    ) {
                        if (isLoading) {
                            CircularProgressIndicator(
                                Modifier.size(20.dp),
                                strokeWidth = 2.dp,
                                color = Color.White
                            )
                        } else {
                            Text(loginStr, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                        }
                    }
                    Spacer(Modifier.height(10.dp))
                    // Forgot password
                    TextButton(
                        onClick = { /* TODO: Şifrəni unutdun? */ },
                        modifier = Modifier.align(Alignment.End)
                    ) {
                        Text(forgotPasswordStr, color = ecoGreen, fontSize = 13.sp, fontWeight = FontWeight.Medium)
                    }
                }
            }
        }

        // Qeydiyyat (aşağıda)
        Box(Modifier.align(Alignment.BottomCenter).padding(bottom = 36.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    noAccountStr,
                    color = ecoOnBg.copy(alpha = 0.72f),
                    fontSize = 15.sp
                )
                TextButton(
                    onClick = onNavigateToRegister,
                    colors = ButtonDefaults.textButtonColors(contentColor = ecoGreen),
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Text(registerStr, fontWeight = FontWeight.Bold, fontSize = 15.sp)
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
