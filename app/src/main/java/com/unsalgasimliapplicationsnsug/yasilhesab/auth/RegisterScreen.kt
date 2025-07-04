//package com.unsalgasimliapplicationsnsug.yasilhesab.ui.auth
//
//import android.os.Build
//import androidx.annotation.RequiresApi
//import androidx.compose.animation.*
//import androidx.compose.foundation.BorderStroke
//import androidx.compose.foundation.background
//import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.foundation.text.KeyboardActions
//import androidx.compose.foundation.text.KeyboardOptions
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.Lock
//import androidx.compose.material.icons.filled.MailOutline
//import androidx.compose.material.icons.filled.Person
//import androidx.compose.material.icons.filled.Visibility
//import androidx.compose.material.icons.filled.VisibilityOff
//import androidx.compose.material3.*
//import androidx.compose.runtime.*
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.draw.shadow
//import androidx.compose.ui.focus.FocusRequester
//import androidx.compose.ui.focus.onFocusChanged
//import androidx.compose.ui.graphics.Brush
//import androidx.compose.ui.platform.LocalContext
//import androidx.compose.ui.text.input.KeyboardType
//import androidx.compose.ui.text.input.PasswordVisualTransformation
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.text.input.VisualTransformation
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.platform.LocalSoftwareKeyboardController
//import androidx.compose.ui.text.input.ImeAction
//import androidx.compose.ui.unit.sp
//import com.google.firebase.auth.FirebaseAuth
//import com.google.firebase.firestore.FirebaseFirestore
//import com.unsalgasimliapplicationsnsug.yasilhesab.ui.theme.*
//
//@RequiresApi(Build.VERSION_CODES.S)
//@OptIn(ExperimentalAnimationApi::class)
//@Composable
//fun RegisterScreen(
//    onRegisterSuccess: () -> Unit,
//    onBack: () -> Unit
//) {
//    val context = LocalContext.current
//    var name by remember { mutableStateOf("") }
//    var email by remember { mutableStateOf("") }
//    var password by remember { mutableStateOf("") }
//    var isLoading by remember { mutableStateOf(false) }
//    var errorMsg by remember { mutableStateOf<String?>(null) }
//    var passwordVisible by remember { mutableStateOf(false) }
//
//    val nameError = name.isBlank()
//    val emailError = !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
//    val passwordError = password.length < 6
//    val bgGradient = Brush.verticalGradient(listOf(DarkGradientStart.copy(alpha = 0.95f), DarkGradientEnd.copy(alpha = 0.95f)))
//    val buttonGradient = Brush.horizontalGradient(listOf(DarkGradientStart, DarkGradientEnd))
//    val errorColor = ErrorColor
//    val fieldTextColor = Black
//
//
//    val passwordFocus = remember { FocusRequester() }
//    var nameFieldFocus by remember { mutableStateOf(false) }
//    var emailFieldFocus by remember { mutableStateOf(false) }
//    var passwordFieldFocus by remember { mutableStateOf(false) }
//    val keyboardController = LocalSoftwareKeyboardController.current
//
//    fun register() {
//        if (nameError || emailError || passwordError) {
//            errorMsg = "Please fill all fields correctly."
//            return
//        }
//        isLoading = true
//        errorMsg = null
//        FirebaseAuth.getInstance()
//            .createUserWithEmailAndPassword(email, password)
//            .addOnSuccessListener { authResult ->
//                val user = authResult.user ?: return@addOnSuccessListener
//                val userMap = hashMapOf(
//                    "uid" to user.uid,
//                    "displayName" to name,
//                    "email" to email,
//                    "pairedUid" to null,
//                    "requestsSent" to listOf<String>(),
//                    "requestsReceived" to listOf<String>()
//                )
//                FirebaseFirestore.getInstance()
//                    .collection("users")
//                    .document(user.uid)
//                    .set(userMap)
//                    .addOnSuccessListener {
//                        isLoading = false
//                        onRegisterSuccess()
//                    }
//                    .addOnFailureListener {
//                        isLoading = false
//                        errorMsg = "Error saving user: ${it.message}"
//                    }
//            }
//            .addOnFailureListener {
//                isLoading = false
//                errorMsg = "Registration failed: ${it.message}"
//            }
//    }
//
//
//    Box(
//        Modifier
//            .fillMaxSize()
//            .background(bgGradient)
//            .imePadding()
//    ) {
//        Box(
//            Modifier
//                .align(Alignment.Center)
//                .padding(horizontal = 16.dp)
//        ) {
//            Card(
//                Modifier
//                    .fillMaxWidth()
//                    .wrapContentHeight()
//                    .shadow(12.dp, RoundedCornerShape(24.dp)),
//                shape = RoundedCornerShape(24.dp),
//                colors = CardDefaults.cardColors(containerColor = Color.Transparent)
//            ) {
//                Column(
//                    Modifier
//                        .background(White, RoundedCornerShape(24.dp))
//                        .padding(horizontal = 24.dp, vertical = 30.dp),
//                    horizontalAlignment = Alignment.CenterHorizontally
//                ) {
//                    Text(
//                        "Ugurlar",
//                        fontSize = 36.sp,
//                        fontWeight = FontWeight.Black,
//                        color = Black,
//                        modifier = Modifier.padding(vertical = 6.dp)
//                    )
//
//                    Spacer(Modifier.height(18.dp))
//
//                    OutlinedTextField(
//                        value = name,
//                        onValueChange = {
//                            name = it
//                        },
//                        label = {
//                            Text(
//                                "Name",
//                                color = if (nameFieldFocus) RedPrimary else Black,
//                                fontWeight = FontWeight.Bold
//                            )
//                        },
//                        placeholder = { Text("Salam Aleykum", color = RedPrimary.copy(alpha = 0.28f)) },
//                        leadingIcon = {
//                            Icon(Icons.Filled.Person, null, tint = if (nameFieldFocus) RedPrimary else Black
//                            )
//                        },
//                        singleLine = true,
//                        isError = false,
//                        shape = RoundedCornerShape(12.dp),
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .onFocusChanged { nameFieldFocus = it.isFocused },
//                        textStyle = LocalTextStyle.current.copy(fontSize = 16.sp, color = fieldTextColor),
//                        keyboardOptions = KeyboardOptions(
//                            keyboardType = KeyboardType.Email,
//                            imeAction = ImeAction.Next
//                        ),
//                        keyboardActions = KeyboardActions(onNext = { passwordFocus.requestFocus() }),
//                        colors = OutlinedTextFieldDefaults.colors(
//                            focusedBorderColor =  RedPrimary,
//                            unfocusedBorderColor = Black,
//                            errorBorderColor = errorColor,
//                            focusedTextColor = RedPrimary,
//                            unfocusedTextColor = Black,
//                            cursorColor = RedPrimary
//                        )
//                    )
//
//
//                    Spacer(Modifier.height(10.dp))
//
//                    OutlinedTextField(
//                        value = email,
//                        onValueChange = {
//                            email = it
//                        },
//                        label = {
//                            Text(
//                                "E-Mail",
//                                color = if (emailFieldFocus) RedPrimary else Black,
//                                fontWeight = FontWeight.Bold
//                            )
//                        },
//                        placeholder = { Text("Aleykum Salam", color = RedPrimary.copy(alpha = 0.28f)) },
//                        leadingIcon = {
//                            Icon(Icons.Filled.MailOutline, null, tint = if (emailFieldFocus) RedPrimary else Black
//                            )
//                        },
//                        singleLine = true,
//                        isError = false,
//                        shape = RoundedCornerShape(12.dp),
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .onFocusChanged { emailFieldFocus = it.isFocused },
//                        textStyle = LocalTextStyle.current.copy(fontSize = 16.sp, color = fieldTextColor),
//                        keyboardOptions = KeyboardOptions(
//                            keyboardType = KeyboardType.Email,
//                            imeAction = ImeAction.Next
//                        ),
//                        keyboardActions = KeyboardActions(onNext = { passwordFocus.requestFocus() }),
//                        colors = OutlinedTextFieldDefaults.colors(
//                            focusedBorderColor =  RedPrimary,
//                            unfocusedBorderColor = Black,
//                            errorBorderColor = errorColor,
//                            focusedTextColor = RedPrimary,
//                            unfocusedTextColor = Black,
//                            cursorColor = RedPrimary
//                        )
//                    )
//
//                    Spacer(Modifier.height(10.dp))
//
//                    OutlinedTextField(
//                        value = password,
//                        onValueChange = { password = it },
//                        label = {
//                            Text(
//                                "Password",
//                                color = if (passwordFieldFocus) RedPrimary else Black,
//                                fontWeight = FontWeight.Bold
//                            )
//                        },
//                        isError = false,
//                        shape = RoundedCornerShape(12.dp),
//                        singleLine = true,
//                        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
//                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
//
//                        leadingIcon = {
//                            Icon(Icons.Filled.Lock, null, tint = if (passwordFieldFocus) RedPrimary else Black)
//                        },
//                        trailingIcon = {
//                            val (icon, desc) = if (passwordVisible) Icons.Filled.VisibilityOff to "Hide"
//                            else Icons.Filled.Visibility to "Show"
//                            IconButton({ passwordVisible = !passwordVisible }) {
//                                Icon(icon, desc, tint = if (passwordFieldFocus) RedPrimary else Black)
//                            }
//                        },
//                        modifier = Modifier.fillMaxWidth()
//                            .onFocusChanged { passwordFieldFocus = it.isFocused },
//                        textStyle = MaterialTheme.typography.bodyLarge,
//                        colors = OutlinedTextFieldDefaults.colors(
//                            focusedBorderColor = RedPrimary,
//                            unfocusedBorderColor = Black,
//                            cursorColor = RedPrimary,
//                            focusedTextColor = RedPrimary,
//                            unfocusedTextColor = Black
//                        )
//                    )
//
//
//
//                    AnimatedVisibility(
//                        visible = errorMsg != null,
//                        enter = fadeIn() + slideInVertically(),
//                        exit = fadeOut()
//                    ) {
//                        Text(
//                            errorMsg ?: "",
//                            color = errorColor,
//                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
//                            modifier = Modifier.padding(vertical = 6.dp)
//                        )
//                    }
//                    Spacer(Modifier.height(16.dp))
//
//                    Button(
//                        onClick = { register() },
//                        enabled = !isLoading,
//                        modifier = Modifier
//                            .width(180.dp)
//                            .height(60.dp)
//                            .shadow(8.dp, RoundedCornerShape(14.dp)),
//                        shape = RoundedCornerShape(14.dp),
//                        contentPadding = PaddingValues(0.dp),
//                        colors = ButtonDefaults.buttonColors(
//                            containerColor = Color.Transparent,
//                            contentColor = Color.White,
//                            disabledContainerColor = RedPrimary.copy(alpha = 0.18f),
//                            disabledContentColor = Color.White
//                        )
//                    ) {
//                        Box(
//                            Modifier
//                                .background(buttonGradient, RoundedCornerShape(14.dp))
//                                .fillMaxSize(),
//                            contentAlignment = Alignment.Center
//                        ) {
//                            if (isLoading) {
//                                CircularProgressIndicator(
//                                    Modifier.size(20.dp),
//                                    strokeWidth = 2.dp,
//                                    color = Color.White
//                                )
//                            } else {
//                                Text("SIGN UP", style = MaterialTheme.typography.labelLarge)
//                            }
//                        }
//                    }
//                }
//            }
//        }
//
//        Box(
//            Modifier
//                .align(Alignment.Center)
//                .offset(y = 260.dp)
//                .widthIn(max = 350.dp)
//        ) {
//            Row(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(top = 8.dp),
//                verticalAlignment = Alignment.CenterVertically,
//                horizontalArrangement = Arrangement.Center
//            ) {
//                Text(
//                    "Already have an account? ",
//                    color = White,
//                    style = MaterialTheme.typography.bodyMedium
//                )
//                TextButton(
//                    onClick = onBack,
//                    colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.onPrimary),
//                    contentPadding = PaddingValues(0.dp)
//                ) {
//                    Text(
//                        "Sign in",
//                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
//                        color = White
//                    )
//                }
//            }
//        }
//    }
//}
