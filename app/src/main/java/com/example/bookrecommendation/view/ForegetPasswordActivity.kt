package com.example.bookrecommendation.view

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bookrecommendation.ui.theme.pink // Using your theme color
import com.example.bookrecommendation.ui.theme.purple // Using your theme color
import com.example.bookrecommendation.ui.theme.white
import com.google.firebase.auth.FirebaseAuth

class ForgotPasswordActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MaterialTheme {
                ForgotPasswordBody(onBackClick = { finish() })
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class) // Add this
@Composable
fun ForgotPasswordBody(onBackClick: () -> Unit) {
    // ... rest of your code
    var email by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) } // State for loading indicator
    val context = LocalContext.current
    val auth = FirebaseAuth.getInstance()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Reset Password",
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = pink // Themed color
                        )
                    }
                },
                actions = {
                    // Spacer to balance the title and center it correctly
                    Spacer(modifier = Modifier.width(48.dp))
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(white)
                .padding(horizontal = 16.dp) // Main horizontal padding
                .verticalScroll(rememberScrollState()), // Makes screen scrollable
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(20.dp))

            Text(
                "Enter the email address associated with your account and we'll send you a link to reset your password.",
                textAlign = TextAlign.Center,
                color = Color.Gray,
                lineHeight = 22.sp
            )

            Spacer(modifier = Modifier.height(30.dp))

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                shape = RoundedCornerShape(15.dp),
                placeholder = { Text("abc@gmail.com") },
                label = { Text("Email Address") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = purple, // Themed color
                    unfocusedContainerColor = purple,
                    focusedBorderColor = pink,
                    unfocusedBorderColor = Color.Transparent
                )
            )

            Spacer(modifier = Modifier.height(30.dp))

            Button(
                onClick = {
                    if (email.isNotEmpty()) {
                        isLoading = true // Show loading
                        auth.sendPasswordResetEmail(email)
                            .addOnCompleteListener { task ->
                                isLoading = false // Hide loading
                                if (task.isSuccessful) {
                                    Toast.makeText(
                                        context,
                                        "Reset link sent to your email",
                                        Toast.LENGTH_LONG
                                    ).show()
                                    onBackClick() // Close activity
                                } else {
                                    Toast.makeText(
                                        context,
                                        "Error: ${task.exception?.message}",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                    } else {
                        Toast.makeText(context, "Please enter your email", Toast.LENGTH_SHORT)
                            .show()
                    }
                },
                enabled = !isLoading, // Disable button while loading
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 10.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = pink, // Themed color
                    contentColor = white
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp),
                shape = RoundedCornerShape(10.dp)
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = white,
                        strokeWidth = 2.dp
                    )
                } else {
                    Text("Send Reset Link", fontSize = 18.sp)
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ForgotPasswordPreview() {
    MaterialTheme {
        ForgotPasswordBody(onBackClick = {})
    }
}
