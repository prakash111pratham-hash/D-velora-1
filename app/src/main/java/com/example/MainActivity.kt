package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.CardGiftcard
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.RateReview
import androidx.compose.material.icons.outlined.AutoAwesome
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.CardGiftcard
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.MenuBook
import androidx.compose.material.icons.outlined.MoreHoriz
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.auth.AuthenticationViewModel
import com.example.ui.auth.SignInScreen
import com.example.ui.concierge.ConciergeScreen
import com.example.ui.home.HomeScreen
import com.example.ui.menu.MenuScreen
import com.example.ui.notifications.NotificationScreen
import com.example.ui.reservation.ReservationScreen
import com.example.ui.reviews.ReviewScreen
import com.example.ui.rewards.RewardsScreen
import com.example.ui.theme.DVeloraTheme
import com.example.ui.viewmodel.ConciergeViewModel
import com.example.ui.viewmodel.MenuViewModel
import com.example.ui.viewmodel.ReservationViewModel
import com.example.ui.viewmodel.ReviewsViewModel
import com.example.ui.viewmodel.RewardsViewModel
import com.example.util.configureAppCheck
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Step 1: Configure App Check with launch intent before any UI or Firebase operation
        configureAppCheck(this, intent)

        enableEdgeToEdge()

        setContent {
            DVeloraTheme {
                AppRootContent()
            }
        }
    }
}

enum class MainDestination(
    val label: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector
) {
    HOME("Home", Icons.Filled.Home, Icons.Outlined.Home),
    MENU("Menu", Icons.Filled.MenuBook, Icons.Outlined.MenuBook),
    RESERVE("Reserve", Icons.Filled.CalendarMonth, Icons.Outlined.CalendarMonth),
    REWARDS("Rewards", Icons.Filled.CardGiftcard, Icons.Outlined.CardGiftcard),
    CHEF_AI("Chef AI", Icons.Filled.AutoAwesome, Icons.Outlined.AutoAwesome),
    MORE("More", Icons.Filled.MoreHoriz, Icons.Outlined.MoreHoriz)
}

@Composable
fun AppRootContent(
    authViewModel: AuthenticationViewModel = viewModel()
) {
    var currentUser by remember { mutableStateOf(Firebase.auth.currentUser) }

    DisposableEffect(Unit) {
        val listener = FirebaseAuth.AuthStateListener { auth ->
            currentUser = auth.currentUser
        }
        Firebase.auth.addAuthStateListener(listener)
        onDispose {
            Firebase.auth.removeAuthStateListener(listener)
        }
    }

    if (currentUser == null) {
        SignInScreen(viewModel = authViewModel)
    } else {
        val uid = currentUser!!.uid
        key(uid) {
            MainAppScaffold(
                currentUserId = uid,
                displayName = currentUser!!.displayName ?: "Valued Guest",
                authViewModel = authViewModel
            )
        }
    }
}

@Composable
fun MainAppScaffold(
    currentUserId: String,
    displayName: String,
    authViewModel: AuthenticationViewModel,
    menuViewModel: MenuViewModel = viewModel(),
    reservationViewModel: ReservationViewModel = viewModel(),
    rewardsViewModel: RewardsViewModel = viewModel(),
    conciergeViewModel: ConciergeViewModel = viewModel(),
    reviewsViewModel: ReviewsViewModel = viewModel()
) {
    var currentDestination by remember { mutableStateOf(MainDestination.HOME) }
    var moreSubTabIndex by remember { mutableIntStateOf(0) } // 0 = Reviews, 1 = Notifications

    // Handle Android system back press to return to Home screen
    BackHandler(enabled = currentDestination != MainDestination.HOME) {
        currentDestination = MainDestination.HOME
    }

    Scaffold(
        bottomBar = {
            NavigationBar(
                modifier = Modifier
                    .navigationBarsPadding()
                    .testTag("main_navigation_bar"),
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.primary,
                tonalElevation = 6.dp
            ) {
                MainDestination.entries.forEach { destination ->
                    val isSelected = currentDestination == destination
                    NavigationBarItem(
                        selected = isSelected,
                        onClick = { currentDestination = destination },
                        icon = {
                            Icon(
                                imageVector = if (isSelected) destination.selectedIcon else destination.unselectedIcon,
                                contentDescription = destination.label
                            )
                        },
                        label = {
                            Text(
                                text = destination.label,
                                style = MaterialTheme.typography.labelSmall
                            )
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = MaterialTheme.colorScheme.onPrimary,
                            indicatorColor = MaterialTheme.colorScheme.primary,
                            selectedTextColor = MaterialTheme.colorScheme.primary,
                            unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                            unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
                        ),
                        modifier = Modifier.testTag("nav_item_${destination.name.lowercase()}")
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (currentDestination) {
                MainDestination.HOME -> {
                    HomeScreen(
                        rewardsViewModel = rewardsViewModel,
                        menuViewModel = menuViewModel,
                        onNavigateToMenu = { currentDestination = MainDestination.MENU },
                        onNavigateToReserve = { currentDestination = MainDestination.RESERVE },
                        onNavigateToRewards = { currentDestination = MainDestination.REWARDS },
                        onNavigateToConcierge = { currentDestination = MainDestination.CHEF_AI }
                    )
                }
                MainDestination.MENU -> {
                    MenuScreen(viewModel = menuViewModel)
                }
                MainDestination.RESERVE -> {
                    ReservationScreen(
                        viewModel = reservationViewModel,
                        initialGuestName = displayName
                    )
                }
                MainDestination.REWARDS -> {
                    RewardsScreen(
                        rewardsViewModel = rewardsViewModel,
                        authViewModel = authViewModel
                    )
                }
                MainDestination.CHEF_AI -> {
                    ConciergeScreen(viewModel = conciergeViewModel)
                }
                MainDestination.MORE -> {
                    androidx.compose.foundation.layout.Column(modifier = Modifier.fillMaxSize()) {
                        TabRow(
                            selectedTabIndex = moreSubTabIndex,
                            containerColor = MaterialTheme.colorScheme.surface,
                            contentColor = MaterialTheme.colorScheme.primary
                        ) {
                            Tab(
                                selected = moreSubTabIndex == 0,
                                onClick = { moreSubTabIndex = 0 },
                                icon = { Icon(imageVector = Icons.Default.RateReview, contentDescription = null) },
                                text = { Text("Guest Reviews") }
                            )
                            Tab(
                                selected = moreSubTabIndex == 1,
                                onClick = { moreSubTabIndex = 1 },
                                icon = { Icon(imageVector = Icons.Default.Notifications, contentDescription = null) },
                                text = { Text("Alerts & Offers") }
                            )
                        }

                        if (moreSubTabIndex == 0) {
                            ReviewScreen(
                                viewModel = reviewsViewModel,
                                currentUserDisplayName = displayName
                            )
                        } else {
                            NotificationScreen()
                        }
                    }
                }
            }
        }
    }
}
