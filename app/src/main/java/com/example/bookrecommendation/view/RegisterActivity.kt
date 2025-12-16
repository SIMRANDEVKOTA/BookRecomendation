package com.example.bookrecommendation.view

import android.app.Activity
import android.app.DatePickerDialog
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
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
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
import androidx.compose.ui.unit.toSize
import com.example.bookrecommendation.R
import com.example.bookrecommendation.model.UserModel
import com.example.bookrecommendation.repository.UserRepoImpl
// Ensure these point to your actual Color.kt file
import com.example.bookrecommendation.ui.theme.pink
import com.example.bookrecommendation.ui.theme.purple
import com.example.bookrecommendation.ui.theme.white
import com.example.bookrecommendation.viewmodel.UserViewModel
import java.util.Calendar

class RegisterActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Initialize ViewModel with Repository
// This is CORRECT
        val userViewModel = UserViewModel(UserRepoImpl())

        setContent {
            MaterialTheme {
                RegisterBody(userViewModel)
            }
        }
    }
}

// FIX: Made UserViewModel nullable so Preview works without crashing
@Composable
fun RegisterBody(userViewModel: UserViewModel? = null) {

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var visibility by remember { mutableStateOf(false) }
    var terms by remember { mutableStateOf(false) }
    var selectedDate by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }
    var selectedOptionText by remember { mutableStateOf("") }
    var textFieldSize by remember { mutableStateOf(Size.Zero) }

    val options = listOf("Option 1", "Option 2", "Option 3")
    val context = LocalContext.current
    val activity = context as? Activity

    val calendar = Calendar.getInstance()
    val datePicker = DatePickerDialog(
        context,
        { _, y, m, d -> selectedDate = "$y/${m + 1}/$d" },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )

    Scaffold { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding) // Respect system bars
                .background(white),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(Modifier.height(50.dp))
            Text(
                "Register",
                style = TextStyle(
                    textAlign = TextAlign.Center,
                    color = pink,
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold
                ),
                modifier = Modifier.fillMaxWidth()
            )

            Image(
                painter = painterResource(R.drawable.logo),
                contentDescription = null,
                modifier = Modifier
                    .size(100.dp)
                    .padding(10.dp)
            )

            Spacer(modifier = Modifier.height(15.dp))

            // --- EMAIL ---
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                shape = RoundedCornerShape(15.dp),
                placeholder = { Text("abc@gmail.com") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 15.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = purple,
                    unfocusedContainerColor = purple,
                    focusedBorderColor = pink,
                    unfocusedBorderColor = Color.Transparent
                )
            )

            Spacer(modifier = Modifier.height(20.dp))

            // --- PASSWORD ---
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                trailingIcon = {
                    IconButton(onClick = { visibility = !visibility }) {
                        Icon(
                            if (visibility) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                            null
                        )
                    }
                },
                visualTransformation = if (visibility) VisualTransformation.None else PasswordVisualTransformation(),
                shape = RoundedCornerShape(15.dp),
                placeholder = { Text("********") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 15.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = purple,
                    unfocusedContainerColor = purple,
                    focusedBorderColor = pink,
                    unfocusedBorderColor = Color.Transparent
                )
            )

            Spacer(modifier = Modifier.height(15.dp))

            // --- DATE PICKER ---
            OutlinedTextField(
                value = selectedDate,
                onValueChange = {},
                enabled = false,
                shape = RoundedCornerShape(15.dp),
                placeholder = { Text("dd/mm/yyyy") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 15.dp)
                    .clickable { datePicker.show() },
                colors = OutlinedTextFieldDefaults.colors(
                    disabledContainerColor = purple,
                    disabledBorderColor = Color.Transparent,
                    disabledTextColor = Color.Black,
                    disabledPlaceholderColor = Color.Gray
                )
            )

            Spacer(modifier = Modifier.height(20.dp))

            // --- DROPDOWN ---
            Box(
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 15.dp)
            ) {
                OutlinedTextField(
                    value = selectedOptionText,
                    onValueChange = {},
                    enabled = false,
                    shape = RoundedCornerShape(15.dp),
                    placeholder = { Text("Select Option") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .onGloballyPositioned { textFieldSize = it.size.toSize() }
                        .clickable { expanded = true },
                    trailingIcon = { Icon(Icons.Default.ArrowDropDown, null) },
                    colors = OutlinedTextFieldDefaults.colors(
                        disabledContainerColor = purple,
                        disabledBorderColor = Color.Transparent,
                        disabledTextColor = Color.Black,
                        disabledPlaceholderColor = Color.Gray
                    )
                )

                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    modifier = Modifier.width(with(LocalDensity.current) {
                        textFieldSize.width.toDp()
                    })
                ) {
                    options.forEach {
                        DropdownMenuItem(
                            text = { Text(it) },
                            onClick = {
                                selectedOptionText = it
                                expanded = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // --- CHECKBOX ---
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = terms,
                    onCheckedChange = { terms = it },
                    colors = CheckboxDefaults.colors(
                        checkedColor = pink,
                        checkmarkColor = white
                    )
                )
                Text("I agree to terms & conditions")
            }

            Spacer(modifier = Modifier.height(10.dp))

            // --- REGISTER BUTTON ---
            Button(
                onClick = {
                    if (!terms) {
                        Toast.makeText(context, "Accept terms", Toast.LENGTH_SHORT).show()
                        return@Button
                    }

                    // Create User Model
                    val user = UserModel(
                        userId = System.currentTimeMillis().toString(),
                        email = email,
                        password = password,
                        dob = selectedDate,
                        favoriteGenre = selectedOptionText
                    )

                    // Safe call to ViewModel (it might be null in preview)
                    if (userViewModel != null) {
                        userViewModel.register(user) { success, message ->
                            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                            if (success) activity?.finish()
                        }
                    } else {
                        // Preview mode behavior
                        Toast.makeText(context, "Preview: Register clicked", Toast.LENGTH_SHORT).show()
                    }
                },
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 10.dp),
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(containerColor = pink),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
                    .padding(horizontal = 15.dp)
            ) {
                Text("Register", color = Color.White, fontSize = 18.sp)
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                buildAnnotatedString {
                    append("Already a member? ")
                    withStyle(SpanStyle(color = pink, fontWeight = FontWeight.Bold)) {
                        append("Sign In")
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 10.dp)
                    .clickable {
                        context.startActivity(Intent(context, LoginActivity::class.java))
                        activity?.finish()
                    },
                textAlign = TextAlign.Center
            )
        }
    }
}

// --- PREVIEW ---
@Preview(showBackground = true)
@Composable
fun RegisterPreview() {
    MaterialTheme {
        // Pass null for the viewModel so the preview doesn't crash
        RegisterBody(userViewModel = null)
    }
}
