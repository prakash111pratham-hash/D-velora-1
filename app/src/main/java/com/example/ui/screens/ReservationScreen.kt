package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.EventSeat
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.Reservation
import com.example.ui.theme.VeloraAmber
import com.example.ui.theme.VeloraEspresso
import com.example.ui.theme.VeloraGold
import com.example.viewmodel.MainViewModel

@Composable
fun ReservationScreen(mainViewModel: MainViewModel) {
    var selectedSubTab by remember { mutableIntStateOf(0) } // 0: Book Table, 1: My Bookings
    val userProfile by mainViewModel.userProfile.collectAsState()
    val reservations by mainViewModel.reservations.collectAsState()
    val isSubmitting by mainViewModel.isSubmittingReservation.collectAsState()
    val successMessage by mainViewModel.reservationSuccessMessage.collectAsState()

    // Form fields
    var guestName by remember(userProfile) { mutableStateOf(userProfile?.displayName ?: "") }
    var guestPhone by remember { mutableStateOf("+91 98765 43210") }
    var partySize by remember { mutableLongStateOf(2L) }
    var selectedSeating by remember { mutableStateOf("Patio Cafe Terrace") }
    var selectedDate by remember { mutableStateOf("Today, Nov 10") }
    var selectedTime by remember { mutableStateOf("19:30") }
    var specialRequests by remember { mutableStateOf("") }

    val seatingOptions = listOf(
        "Patio Cafe Terrace" to "Open-air garden vibes with acoustic cafe jazz",
        "Window Booth" to "Private booth with boulevard street view",
        "Main Dining Hall" to "Spacious family dining near the live tandoor",
        "Private Suite" to "Quiet premium section for special occasions"
    )

    val dates = listOf(
        "Today, Nov 10",
        "Tomorrow, Nov 11",
        "Friday, Nov 14",
        "Saturday, Nov 15",
        "Sunday, Nov 16"
    )

    val timeSlots = listOf(
        "12:30", "13:15", "14:00",
        "19:00", "19:30", "20:15", "21:00", "21:45"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        // Screen Header
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 12.dp)
        ) {
            Text(
                text = "Table Reservations",
                style = MaterialTheme.typography.headlineLarge.copy(
                    fontFamily = FontFamily.Serif,
                    fontWeight = FontWeight.Bold
                )
            )
            Text(
                text = "Dine with us • Experience warmth and authentic hospitality",
                style = MaterialTheme.typography.bodySmall.copy(color = VeloraGold)
            )
        }

        // Sub Tabs
        TabRow(
            selectedTabIndex = selectedSubTab,
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = VeloraEspresso,
            indicator = { tabPositions ->
                TabRowDefaults.SecondaryIndicator(
                    modifier = Modifier.tabIndicatorOffset(tabPositions[selectedSubTab]),
                    color = VeloraGold,
                    height = 3.dp
                )
            }
        ) {
            Tab(
                selected = selectedSubTab == 0,
                onClick = { selectedSubTab = 0 },
                text = {
                    Text(
                        "Book a Table",
                        fontWeight = if (selectedSubTab == 0) FontWeight.Bold else FontWeight.Medium
                    )
                }
            )
            Tab(
                selected = selectedSubTab == 1,
                onClick = { selectedSubTab = 1 },
                text = {
                    Text(
                        "My Bookings (${reservations.size})",
                        fontWeight = if (selectedSubTab == 1) FontWeight.Bold else FontWeight.Medium
                    )
                }
            )
        }

        // Content
        if (selectedSubTab == 0) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(start = 20.dp, end = 20.dp, top = 16.dp, bottom = 100.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                if (successMessage != null) {
                    item {
                        Card(
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = VeloraGold.copy(alpha = 0.15f)),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.padding(14.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.CheckCircle,
                                    contentDescription = null,
                                    tint = VeloraEspresso
                                )
                                Spacer(modifier = Modifier.width(10.dp))
                                Text(
                                    text = successMessage ?: "",
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        fontWeight = FontWeight.SemiBold,
                                        color = VeloraEspresso
                                    ),
                                    modifier = Modifier.weight(1f)
                                )
                            }
                        }
                    }
                }

                // Guest Details
                item {
                    Text("1. Guest Contact", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = guestName,
                        onValueChange = { guestName = it },
                        label = { Text("Guest Name") },
                        leadingIcon = { Icon(Icons.Default.Person, contentDescription = null, tint = VeloraGold) },
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = VeloraGold,
                            unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant
                        )
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = guestPhone,
                        onValueChange = { guestPhone = it },
                        label = { Text("Phone Number") },
                        leadingIcon = { Icon(Icons.Default.Phone, contentDescription = null, tint = VeloraGold) },
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = VeloraGold,
                            unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant
                        )
                    )
                }

                // Party Size
                item {
                    Text("2. Party Size", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
                    Spacer(modifier = Modifier.height(8.dp))

                    LazyRow(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        items(listOf(1L, 2L, 4L, 6L, 8L, 12L)) { count ->
                            val isSelected = partySize == count
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = if (isSelected) VeloraEspresso else MaterialTheme.colorScheme.surface,
                                modifier = Modifier
                                    .border(
                                        1.dp,
                                        if (isSelected) VeloraGold else MaterialTheme.colorScheme.outlineVariant,
                                        RoundedCornerShape(12.dp)
                                    )
                                    .clickable { partySize = count }
                            ) {
                                Text(
                                    text = "$count ${if (count == 1L) "Guest" else "Guests"}",
                                    style = MaterialTheme.typography.labelMedium.copy(
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                        color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface
                                    ),
                                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp)
                                )
                            }
                        }
                    }
                }

                // Seating Area
                item {
                    Text("3. Seating Atmosphere", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
                    Spacer(modifier = Modifier.height(8.dp))

                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        seatingOptions.forEach { (area, desc) ->
                            val isSelected = selectedSeating == area
                            Card(
                                shape = RoundedCornerShape(14.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = if (isSelected) VeloraAmber.copy(alpha = 0.12f) else MaterialTheme.colorScheme.surface
                                ),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .border(
                                        1.5.dp,
                                        if (isSelected) VeloraGold else MaterialTheme.colorScheme.outlineVariant,
                                        RoundedCornerShape(14.dp)
                                    )
                                    .clickable { selectedSeating = area }
                            ) {
                                Row(
                                    modifier = Modifier.padding(14.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.EventSeat,
                                        contentDescription = null,
                                        tint = if (isSelected) VeloraEspresso else MaterialTheme.colorScheme.onSurfaceVariant,
                                        modifier = Modifier.size(24.dp)
                                    )
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = area,
                                            style = MaterialTheme.typography.titleSmall.copy(
                                                fontWeight = FontWeight.Bold,
                                                color = if (isSelected) VeloraEspresso else MaterialTheme.colorScheme.onSurface
                                            )
                                        )
                                        Text(
                                            text = desc,
                                            style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                // Date & Time
                item {
                    Text("4. Select Date & Time", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
                    Spacer(modifier = Modifier.height(8.dp))

                    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        items(dates) { date ->
                            val isSelected = selectedDate == date
                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = if (isSelected) VeloraEspresso else MaterialTheme.colorScheme.surface,
                                modifier = Modifier
                                    .border(
                                        1.dp,
                                        if (isSelected) VeloraGold else MaterialTheme.colorScheme.outlineVariant,
                                        RoundedCornerShape(10.dp)
                                    )
                                    .clickable { selectedDate = date }
                            ) {
                                Text(
                                    text = date,
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface
                                    ),
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        items(timeSlots) { time ->
                            val isSelected = selectedTime == time
                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = if (isSelected) VeloraGold else MaterialTheme.colorScheme.surface,
                                modifier = Modifier
                                    .border(
                                        1.dp,
                                        if (isSelected) VeloraGold else MaterialTheme.colorScheme.outlineVariant,
                                        RoundedCornerShape(10.dp)
                                    )
                                    .clickable { selectedTime = time }
                            ) {
                                Text(
                                    text = time,
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface
                                    ),
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
                                )
                            }
                        }
                    }
                }

                // Special notes
                item {
                    OutlinedTextField(
                        value = specialRequests,
                        onValueChange = { specialRequests = it },
                        label = { Text("Special Requests / Dietary Notes (Optional)") },
                        placeholder = { Text("e.g. Birthday candle, quiet corner, wheelchair accessible") },
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = VeloraGold,
                            unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant
                        )
                    )
                }

                // Submit Button
                item {
                    Button(
                        onClick = {
                            if (guestName.isNotBlank() && guestPhone.isNotBlank()) {
                                mainViewModel.bookTable(
                                    guestName = guestName,
                                    guestPhone = guestPhone,
                                    partySize = partySize,
                                    seatingArea = selectedSeating,
                                    date = selectedDate,
                                    timeSlot = selectedTime,
                                    specialRequests = specialRequests.ifBlank { null },
                                    onSuccess = {
                                        selectedSubTab = 1
                                    }
                                )
                            }
                        },
                        enabled = !isSubmitting && guestName.isNotBlank() && guestPhone.isNotBlank(),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = VeloraEspresso,
                            contentColor = Color.White
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(54.dp)
                            .testTag("confirm_booking_button"),
                        elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
                    ) {
                        if (isSubmitting) {
                            CircularProgressIndicator(color = VeloraAmber, strokeWidth = 2.dp, modifier = Modifier.size(24.dp))
                        } else {
                            Text(
                                text = "Confirm Table Reservation (+50 Pts)",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                            )
                        }
                    }
                }
            }
        } else {
            // My Bookings
            if (reservations.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Surface(
                            shape = CircleShape,
                            color = VeloraAmber.copy(alpha = 0.15f),
                            modifier = Modifier.size(72.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Default.EventSeat,
                                    contentDescription = null,
                                    tint = VeloraEspresso,
                                    modifier = Modifier.size(36.dp)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "No Reservations Yet",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily.Serif
                            )
                        )
                        Text(
                            text = "Book your table in advance and earn 50 bonus loyalty points!",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            ),
                            modifier = Modifier.padding(top = 6.dp)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = { selectedSubTab = 0 },
                            colors = ButtonDefaults.buttonColors(containerColor = VeloraGold),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text("Book Table Now")
                        }
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(20.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    items(reservations) { res ->
                        ReservationItemCard(
                            reservation = res,
                            onCancel = { mainViewModel.cancelReservation(res.id) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ReservationItemCard(
    reservation: Reservation,
    onCancel: () -> Unit
) {
    val isConfirmed = reservation.status == "CONFIRMED"

    Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(18.dp)),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = if (isConfirmed) VeloraGold.copy(alpha = 0.15f) else MaterialTheme.colorScheme.surfaceVariant
                ) {
                    Text(
                        text = reservation.status,
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = if (isConfirmed) VeloraEspresso else MaterialTheme.colorScheme.onSurfaceVariant,
                            fontWeight = FontWeight.Bold
                        ),
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }

                Text(
                    text = "${reservation.partySize} Guests",
                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "${reservation.date} • ${reservation.timeSlot}",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Serif
                )
            )

            Text(
                text = "Area: ${reservation.seatingArea}",
                style = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
            )

            Text(
                text = "Guest: ${reservation.guestName} (${reservation.guestPhone})",
                style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
            )

            if (!reservation.specialRequests.isNullOrBlank()) {
                Text(
                    text = "Note: ${reservation.specialRequests}",
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = VeloraGold,
                        fontWeight = FontWeight.Medium
                    ),
                    modifier = Modifier.padding(top = 4.dp)
                )
            }

            if (isConfirmed) {
                Spacer(modifier = Modifier.height(12.dp))
                OutlinedButton(
                    onClick = onCancel,
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.error),
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Text("Cancel Booking", style = MaterialTheme.typography.labelSmall)
                }
            }
        }
    }
}
