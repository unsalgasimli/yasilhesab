package com.unsalgasimliapplicationsnsug.yasilhesab

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.unsalgasimliapplicationsnsug.yasilhesab.ui.theme.YasilhesabTheme
import com.unsalgasimliapplicationsnsug.yasilhesab.ui.auth.LoginScreen

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            YasilhesabTheme(darkTheme = true) { // Dəyişə bilərsən
                Scaffold(modifier = Modifier) {
                    LoginScreen(
                        onNavigateToRegister = { /* TODO */ },
                        onLoginSuccess = { /* TODO */ }
                    )
                }
            }
        }
    }
}
