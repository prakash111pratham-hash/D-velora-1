package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CardGiftcard
import androidx.compose.material.icons.filled.EventSeat
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.RestaurantMenu
import androidx.compose.material.icons.filled.SupportAgent
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.dialogs.CartAndCheckoutBottomSheet
import com.example.ui.dialogs.MemberQrDialog
import com.example.ui.dialogs.NotificationDialog
import com.example.ui.theme.VeloraAmber
import com.example.ui.theme.VeloraEspresso
import com.example.ui.theme.VeloraGold
import com.example.viewmodel.AppTab
import com.example.viewmodel.MainViewModel

@Composable
fun MainAppScreen(mainViewModel: MainViewModel) {
    val selectedTab by mainViewModel.selectedTab.collectAsState()
    val userProfile by mainViewModel.userProfile.collectAsState()
    val orderSuccessMessage by mainViewModel.orderSuccessMessage.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    var showCartSheet by remember { mutableStateOf(false) }
    var showNotificationsDialog by remember { mutableStateOf(false) }
    var showMemberQrDialog by remember { mutableStateOf(false) }

    LaunchedEffect(orderSuccessMessage) {
        if (orderSuccessMessage != null) {
            snackbarHostState.showSnackbar(orderSuccessMessage!!)
            mainViewModel.dismissOrderSuccess()
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        bottomBar = {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.surface,
                tonalElevation = 8.dp,
                modifier = Modifier
                    .border(0.5.dp, MaterialTheme.colorScheme.outlineVariant)
                    .windowInsetsPadding(WindowInsets.navigationBars)
                    .testTag("bottom_nav_bar")
            ) {
                NavigationBarItem(
                    selected = selectedTab == AppTab.HOME,
                    onClick = { mainViewModel.selectTab(AppTab.HOME) },
                    icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
                    label = { Text("Home", fontSize = 11.sp, fontWeight = if (selectedTab == AppTab.HOME) FontWeight.Bold else FontWeight.Normal) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color.White,
                        selectedTextColor = VeloraEspresso,
                        indicatorColor = VeloraEspresso,
                        unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                )

                NavigationBarItem(
                    selected = selectedTab == AppTab.MENU,
                    onClick = { mainViewModel.selectTab(AppTab.MENU) },
                    icon = { Icon(Icons.Default.RestaurantMenu, contentDescription = "Menu") },
                    label = { Text("Menu", fontSize = 11.sp, fontWeight = if (selectedTab == AppTab.MENU) FontWeight.Bold else FontWeight.Normal) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color.White,
                        selectedTextColor = VeloraEspresso,
                        indicatorColor = VeloraEspresso,
                        unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                )

                NavigationBarItem(
                    selected = selectedTab == AppTab.RESERVE,
                    onClick = { mainViewModel.selectTab(AppTab.RESERVE) },
                    icon = { Icon(Icons.Default.EventSeat, contentDescription = "Reserve") },
                    label = { Text("Book", fontSize = 11.sp, fontWeight = if (selectedTab == AppTab.RESERVE) FontWeight.Bold else FontWeight.Normal) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color.White,
                        selectedTextColor = VeloraEspresso,
                        indicatorColor = VeloraEspresso,
                        unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                )

                NavigationBarItem(
                    selected = selectedTab == AppTab.REWARDS,
                    onClick = { mainViewModel.selectTab(AppTab.REWARDS) },
                    icon = { Icon(Icons.Default.CardGiftcard, contentDescription = "Rewards") },
                    label = { Text("Rewards", fontSize = 11.sp, fontWeight = if (selectedTab == AppTab.REWARDS) FontWeight.Bold else FontWeight.Normal) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color.White,
                        selectedTextColor = VeloraEspresso,
                        indicatorColor = VeloraEspresso,
                        unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                )

                NavigationBarItem(
                    selected = selectedTab == AppTab.COMMUNITY,
                    onClick = { mainViewModel.selectTab(AppTab.COMMUNITY) },
                    icon = { Icon(Icons.Default.SupportAgent, contentDescription = "Concierge") },
                    label = { Text("Concierge", fontSize = 11.sp, fontWeight = if (selectedTab == AppTab.COMMUNITY) FontWeight.Bold else FontWeight.Normal) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color.White,
                        selectedTextColor = VeloraEspresso,
                        indicatorColor = VeloraEspresso,
                        unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (selectedTab) {
                AppTab.HOME -> HomeScreen(
                    mainViewModel = mainViewModel,
                    onOpenNotifications = { showNotificationsDialog = true },
                    onOpenQrPass = { showMemberQrDialog = true }
                )
                AppTab.MENU -> MenuScreen(
                    mainViewModel = mainViewModel,
                    onOpenCart = { showCartSheet = true }
                )
                AppTab.RESERVE -> ReservationScreen(
                    mainViewModel = mainViewModel
                )
                AppTab.REWARDS -> RewardsScreen(
                    mainViewModel = mainViewModel,
                    onOpenQrPass = { showMemberQrDialog = true }
                )
                AppTab.COMMUNITY -> CommunityScreen(
                    mainViewModel = mainViewModel
                )
            }
        }
    }

    // Cart Bottom Sheet
    if (showCartSheet) {
        CartAndCheckoutBottomSheet(
            mainViewModel = mainViewModel,
            onDismiss = { showCartSheet = false }
        )
    }

    // Notifications Dialog
    if (showNotificationsDialog) {
        NotificationDialog(
            offers = mainViewModel.offers,
            onDismiss = { showNotificationsDialog = false }
        )
    }

    // Member QR Pass Dialog
    if (showMemberQrDialog) {
        MemberQrDialog(
            userProfile = userProfile,
            onDismiss = { showMemberQrDialog = false }
        )
    }
}
