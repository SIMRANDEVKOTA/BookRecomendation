package com.example.bookrecommendation.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.LocalLibrary
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.bookrecommendation.repository.LibraryRepoImpl
import com.example.bookrecommendation.repository.UserRepoImpl
import com.example.bookrecommendation.ui.theme.black
import com.example.bookrecommendation.ui.theme.grey
import com.example.bookrecommendation.ui.theme.white
import com.example.bookrecommendation.viewmodel.LibraryViewModel
import com.example.bookrecommendation.viewmodel.UserViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class DashboardActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DashboardBody()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardBody() {
    // Initialize Repositories
    val libraryRepo = remember { LibraryRepoImpl() }
    val userRepo = remember { UserRepoImpl() }

    // Initialize ViewModels
    val libraryViewModel: LibraryViewModel = viewModel(
        factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return LibraryViewModel(libraryRepo) as T
            }
        }
    )

    val userViewModel: UserViewModel = viewModel(
        factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return UserViewModel(userRepo) as T
            }
        }
    )

    var selectedIndex by remember { mutableIntStateOf(0) }

    data class NavItem(val label: String, val icon: ImageVector)

    val navItems = listOf(
        NavItem("Home", Icons.Outlined.Home),
        NavItem("Search", Icons.Outlined.Search),
        NavItem("Library", Icons.Outlined.LocalLibrary),
        NavItem("Profile", Icons.Outlined.Person)
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Next Read") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = grey,
                    titleContentColor = white
                )
            )
        },
        bottomBar = {
            NavigationBar(
                containerColor = grey
            ) {
                navItems.forEachIndexed { index, item ->
                    NavigationBarItem(
                        selected = selectedIndex == index,
                        onClick = { selectedIndex = index },
                        icon = {
                            Icon(
                                imageVector = item.icon,
                                contentDescription = item.label
                            )
                        },
                        label = { Text(item.label) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = white,
                            selectedTextColor = white,
                            unselectedIconColor = white.copy(alpha = 0.6f),
                            unselectedTextColor = white.copy(alpha = 0.6f),
                            indicatorColor = black.copy(alpha = 0.2f)
                        )
                    )
                }
            }
        }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            when (selectedIndex) {
                0 -> Home(viewModel = libraryViewModel)
                1 -> SearchScreen()
                2 -> LibraryScreen(viewModel = libraryViewModel)
                3 -> ProfileScreen(userViewModel = userViewModel, libraryViewModel = libraryViewModel)
                else -> Home(viewModel = libraryViewModel)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DashboardPreview() {
    DashboardBody()
}
