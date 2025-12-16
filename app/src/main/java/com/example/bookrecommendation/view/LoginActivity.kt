package com.example.bookrecommendation.view

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bookrecommendation.R
import com.example.bookrecommendation.repository.UserRepoImpl
import com.example.bookrecommendation.ui.theme.orange
import com.example.bookrecommendation.ui.theme.pink
import com.example.bookrecommendation.ui.theme.purple
import com.example.bookrecommendation.ui.theme.white
import com.example.bookrecommendation.viewmodel.UserViewModel

class LoginActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val userViewModel = UserViewModel(UserRepoImpl())

        setContent {
            MaterialTheme {
                LoginBody(userViewModel)
            }
        }
    }
}

@Composable
fun LoginBody(userViewModel: UserViewModel? = null) {

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var visibility by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val activity = context as? Activity

    Scaffold { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(Color.White),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(50.dp))
            Text(
                "Welcome To Next Read",
                style = TextStyle(
                    textAlign = TextAlign.Center,
                    color = pink,
                    fontSize = 30.sp,
                    fontWeight = FontWeight.ExtraBold
                ),
                modifier = Modifier.fillMaxWidth()
            )

            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "App Logo",
                modifier = Modifier
                    .size(100.dp)
                    .padding(vertical = 10.dp)
            )

            Text(
                "This is NextRead Where Top Reads Meet Trusted Reviews ",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 20.dp, horizontal = 30.dp),
                style = TextStyle(
                    textAlign = TextAlign.Center,
                    color = Color.Black.copy(alpha = 0.6f)
                )
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 15.dp)
            ) {
                SocialMediaCard(
                    Modifier.height(60.dp).weight(1f),
                    R.drawable.face,
                    "Facebook"
                )
                Spacer(modifier = Modifier.width(20.dp))
                SocialMediaCard(
                    Modifier.height(60.dp).weight(1f),
                    R.drawable.gmail,
                    "Gmail"
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 40.dp, horizontal = 15.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                HorizontalDivider(modifier = Modifier.weight(1f))
                Text("OR", modifier = Modifier.padding(horizontal = 20.dp))
                HorizontalDivider(modifier = Modifier.weight(1f))
            }

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                shape = RoundedCornerShape(15.dp),
                placeholder = { Text("abc@gmail.com") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                modifier = Modifier.fillMaxWidth().padding(horizontal = 15.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = orange,
                    unfocusedContainerColor = orange,
                    focusedBorderColor = pink,
                    unfocusedBorderColor = Color.Transparent
                )
            )

            Spacer(modifier = Modifier.height(20.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                trailingIcon = {
                    val iconImage = if (visibility) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                    IconButton(onClick = { visibility = !visibility }) {
                        Icon(imageVector = iconImage, contentDescription = null)
                    }
                },
                visualTransformation = if (visibility) VisualTransformation.None else PasswordVisualTransformation(),
                shape = RoundedCornerShape(15.dp),
                placeholder = { Text("********") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                modifier = Modifier.fillMaxWidth().padding(horizontal = 15.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = purple,
                    unfocusedContainerColor = orange,
                    focusedBorderColor = pink,
                    unfocusedBorderColor = Color.Transparent
                )
            )

            // --- FIXED SECTION START ---
            Text(
                "Forget password?",
                style = TextStyle(textAlign = TextAlign.End),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 15.dp, horizontal = 15.dp)
                    .clickable {
                        val intent = Intent(context, ForgotPasswordActivity::class.java)
                        context.startActivity(intent)
                    }
            )
            // --- FIXED SECTION END ---

            Button(
                onClick = {
                    if (email.isEmpty() || password.isEmpty()) {
                        Toast.makeText(context, "Please fill all fields", Toast.LENGTH_SHORT).show()
                    } else {
                        if (userViewModel != null) {
                            userViewModel.login(email, password) { success, message ->
                                Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                                if (success) {
                                    val intent = Intent(context, DashboardActivity::class.java)
                                    context.startActivity(intent)
                                    activity?.finish()
                                }
                            }
                        } else {
                            Toast.makeText(context, "Preview Login Clicked", Toast.LENGTH_SHORT).show()
                        }
                    }
                },
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 10.dp),
                colors = ButtonDefaults.buttonColors(containerColor = pink, contentColor = white),
                modifier = Modifier.fillMaxWidth().height(60.dp).padding(horizontal = 15.dp),
                shape = RoundedCornerShape(10.dp)
            ) {
                Text("Log In", fontSize = 18.sp)
            }

            Spacer(Modifier.weight(1f))

            Text(
                buildAnnotatedString {
                    append("Don't have an account? ")
                    withStyle(SpanStyle(color = pink, fontWeight = FontWeight.Bold)) {
                        append("Sign up")
                    }
                },
                modifier = Modifier
                    .padding(bottom = 32.dp)
                    .clickable {
                        val intent = Intent(context, RegisterActivity::class.java)
                        context.startActivity(intent)
                    }
            )
        }
    }
}

@Composable
fun SocialMediaCard(modifier: Modifier, image: Int, label: String) {
    Card(modifier = modifier) {
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(image),
                contentDescription = null,
                modifier = Modifier.size(30.dp)
            )
            Spacer(modifier = Modifier.width(20.dp))
            Text(label)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewLogin() {
    MaterialTheme {
        LoginBody(userViewModel = null)
    }
}
