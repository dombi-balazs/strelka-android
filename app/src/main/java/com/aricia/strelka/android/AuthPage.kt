package com.aricia.strelka.android

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.aricia.strelka.android.DataBase.supabase
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.builtin.Email
import kotlinx.coroutines.launch

@Composable
fun AuthPage( navController: NavController, onAuthSuccess: () -> Unit) {
    val coroutineScope = rememberCoroutineScope()
    var emailState by remember { mutableStateOf("") }
    var passwordState by remember { mutableStateOf("") }

    Column {

        Text("Hello. :)", fontSize = 50.sp, modifier = Modifier.padding(20.dp))

        OutlinedTextField(
            value = emailState,
            onValueChange = { emailState = it },
            label = { Text("Email") },
            modifier = Modifier.padding(20.dp)
        )

        OutlinedTextField(
            value = passwordState,
            onValueChange = { passwordState = it },
            label = { Text("Password") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.padding(20.dp)
        )

        Button(
            onClick = {
                coroutineScope.launch {
                    try {
                        supabase.auth.signUpWith(Email) {
                            email = emailState
                            password = passwordState
                        }
                    } catch (e: Exception) {
                        Log.e("AuthPage", "User registration error: ${e.message}", e)
                    }
                }
            },
            modifier = Modifier.padding(20.dp)
        ) {
            Text("Sign up")
        }

        Button(onClick = {
            coroutineScope.launch {
                try {
                    supabase.auth.signInWith(Email) {
                        email = emailState
                        password = passwordState
                    }
                    onAuthSuccess()
                } catch (e: Exception) {
                    Log.e("AuthPage", "User sign in error: ${e.message}", e)
                }
            }
        }, modifier = Modifier.padding(20.dp)) {
            Text("Log in")
        }
        Button(
            onClick = { navController.navigate(Screens.MainScreen.route) },
            modifier = Modifier.padding(20.dp)
        ) { Text("Forward (for testing purpose)") }
    }
}