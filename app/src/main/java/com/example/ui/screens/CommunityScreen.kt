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
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.RateReview
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarOutline
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import com.example.model.Review
import com.example.ui.theme.VeloraAmber
import com.example.ui.theme.VeloraEspresso
import com.example.ui.theme.VeloraGold
import com.example.viewmodel.ChatMessage
import com.example.viewmodel.MainViewModel

@Composable
fun CommunityScreen(mainViewModel: MainViewModel) {
    var selectedSubTab by remember { mutableIntStateOf(0) } // 0: Chef AI, 1: Guest Reviews
    val userProfile by mainViewModel.userProfile.collectAsState()
    val chatMessages by mainViewModel.chatMessages.collectAsState()
    val isThinking by mainViewModel.isAiThinking.collectAsState()
    val highThinkingMode by mainViewModel.highThinkingMode.collectAsState()
    val reviews by mainViewModel.reviews.collectAsState()

    var showReviewDialog by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxSize()) {
        // Header
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 12.dp)
        ) {
            Text(
                text = "Concierge & Reviews",
                style = MaterialTheme.typography.headlineLarge.copy(
                    fontFamily = FontFamily.Serif,
                    fontWeight = FontWeight.Bold
                )
            )
            Text(
                text = "Culinary sommelier AI • Guest dining experiences",
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
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.AutoAwesome,
                            contentDescription = null,
                            tint = VeloraGold,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Chef Velora AI", fontWeight = if (selectedSubTab == 0) FontWeight.Bold else FontWeight.Medium)
                    }
                }
            )
            Tab(
                selected = selectedSubTab == 1,
                onClick = { selectedSubTab = 1 },
                text = {
                    Text(
                        "Guest Reviews (${reviews.size})",
                        fontWeight = if (selectedSubTab == 1) FontWeight.Bold else FontWeight.Medium
                    )
                }
            )
        }

        if (selectedSubTab == 0) {
            ChefAiChatSection(
                chatMessages = chatMessages,
                isThinking = isThinking,
                highThinkingMode = highThinkingMode,
                onToggleThinking = { mainViewModel.toggleHighThinking(it) },
                onSendMessage = { mainViewModel.askChefVelora(it) }
            )
        } else {
            GuestReviewsSection(
                reviews = reviews,
                onOpenWriteReview = { showReviewDialog = true }
            )
        }
    }

    if (showReviewDialog) {
        WriteReviewDialog(
            authorDefaultName = userProfile?.displayName ?: "Guest Patron",
            onDismiss = { showReviewDialog = false },
            onSubmit = { author, overall, food, service, comment ->
                mainViewModel.submitReview(
                    authorName = author,
                    rating = overall,
                    foodRating = food,
                    serviceRating = service,
                    comment = comment,
                    onSuccess = { showReviewDialog = false }
                )
            }
        )
    }
}

@Composable
private fun ChefAiChatSection(
    chatMessages: List<ChatMessage>,
    isThinking: Boolean,
    highThinkingMode: Boolean,
    onToggleThinking: (Boolean) -> Unit,
    onSendMessage: (String) -> Unit
) {
    var inputQuery by remember { mutableStateOf("") }
    val listState = rememberLazyListState()

    val promptSuggestions = listOf(
        "Pair a coffee with dessert",
        "What makes the Almond Blueberry Latte special?",
        "Plan a 4-person dinner under ₹1000",
        "Tell me about parking & transit directions",
        "What is Dehati Chicken cooked with?"
    )

    LaunchedEffect(chatMessages.size, isThinking) {
        if (chatMessages.isNotEmpty()) {
            listState.animateScrollToItem(chatMessages.size - 1)
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        // High Thinking Mode Banner
        Surface(
            color = MaterialTheme.colorScheme.surfaceVariant,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.AutoAwesome,
                        contentDescription = null,
                        tint = VeloraGold,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(
                            text = if (highThinkingMode) "High Thinking Culinary Sommelier" else "Express Flash Mode",
                            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
                        )
                        Text(
                            text = if (highThinkingMode) "Gemini 4 Argon deep culinary reasoning" else "Gemini 3.8 Flash rapid responses",
                            style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        )
                    }
                }

                Switch(
                    checked = highThinkingMode,
                    onCheckedChange = onToggleThinking,
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = VeloraEspresso,
                        checkedTrackColor = VeloraGold
                    )
                )
            }
        }

        // Suggestions row
        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(promptSuggestions) { prompt ->
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = MaterialTheme.colorScheme.surface,
                    modifier = Modifier
                        .border(1.dp, VeloraGold.copy(alpha = 0.5f), RoundedCornerShape(12.dp))
                        .clickable { onSendMessage(prompt) }
                ) {
                    Text(
                        text = prompt,
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Medium),
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                    )
                }
            }
        }

        // Chat message list
        LazyColumn(
            state = listState,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(chatMessages) { msg ->
                ChatBubble(message = msg)
            }

            if (isThinking) {
                item {
                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(8.dp)) {
                        CircularProgressIndicator(
                            color = VeloraGold,
                            strokeWidth = 2.dp,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = if (highThinkingMode) "Chef Velora is deliberating pairings and flavors..." else "Chef Velora is responding...",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = VeloraGold,
                                fontWeight = FontWeight.Medium
                            )
                        )
                    }
                }
            }
        }

        // Input Field
        Surface(
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 4.dp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 76.dp)
        ) {
            Row(
                modifier = Modifier.padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = inputQuery,
                    onValueChange = { inputQuery = it },
                    placeholder = { Text("Ask Chef Velora anything...") },
                    shape = RoundedCornerShape(20.dp),
                    modifier = Modifier
                        .weight(1f)
                        .testTag("chef_ai_query_input"),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = VeloraGold,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant
                    ),
                    singleLine = true
                )

                Spacer(modifier = Modifier.width(8.dp))

                IconButton(
                    onClick = {
                        if (inputQuery.isNotBlank() && !isThinking) {
                            val q = inputQuery
                            inputQuery = ""
                            onSendMessage(q)
                        }
                    },
                    modifier = Modifier
                        .size(48.dp)
                        .background(VeloraEspresso, CircleShape)
                        .testTag("chef_ai_send_button")
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.Send,
                        contentDescription = "Send",
                        tint = VeloraAmber,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun ChatBubble(message: ChatMessage) {
    val isUser = message.sender == "user"
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (isUser) Arrangement.End else Arrangement.Start
    ) {
        if (!isUser) {
            Surface(
                shape = CircleShape,
                color = VeloraGold,
                modifier = Modifier
                    .size(32.dp)
                    .align(Alignment.Top)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.Default.AutoAwesome,
                        contentDescription = null,
                        tint = VeloraEspresso,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.width(8.dp))
        }

        Surface(
            shape = RoundedCornerShape(
                topStart = 16.dp,
                topEnd = 16.dp,
                bottomStart = if (isUser) 16.dp else 4.dp,
                bottomEnd = if (isUser) 4.dp else 16.dp
            ),
            color = if (isUser) VeloraEspresso else MaterialTheme.colorScheme.surface,
            tonalElevation = 2.dp,
            modifier = Modifier
                .widthIn(max = 290.dp)
                .border(
                    1.dp,
                    if (isUser) VeloraGold else MaterialTheme.colorScheme.outlineVariant,
                    RoundedCornerShape(16.dp)
                )
        ) {
            Text(
                text = message.message,
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = if (isUser) Color.White else MaterialTheme.colorScheme.onSurface
                ),
                modifier = Modifier.padding(14.dp)
            )
        }
    }
}

@Composable
private fun GuestReviewsSection(
    reviews: List<Review>,
    onOpenWriteReview: () -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(start = 20.dp, end = 20.dp, top = 16.dp, bottom = 100.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Summary Rating Card
        item {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(20.dp)),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "4.9",
                                    style = MaterialTheme.typography.displayMedium.copy(
                                        fontFamily = FontFamily.Serif,
                                        fontWeight = FontWeight.Bold,
                                        color = VeloraEspresso
                                    )
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Row {
                                    repeat(5) {
                                        Icon(
                                            imageVector = Icons.Default.Star,
                                            contentDescription = null,
                                            tint = VeloraAmber,
                                            modifier = Modifier.size(20.dp)
                                        )
                                    }
                                }
                            }
                            Text(
                                text = "Based on verified community dining reviews",
                                style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
                            )
                        }

                        Button(
                            onClick = onOpenWriteReview,
                            colors = ButtonDefaults.buttonColors(containerColor = VeloraGold),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.testTag("write_review_button")
                        ) {
                            Icon(imageVector = Icons.Default.RateReview, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Rate Us", style = MaterialTheme.typography.labelMedium)
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        ScorePill(label = "Food Quality", score = "5.0 ★")
                        ScorePill(label = "Hospitality", score = "4.8 ★")
                        ScorePill(label = "Ambiance", score = "4.9 ★")
                    }
                }
            }
        }

        // Reviews List
        if (reviews.isEmpty()) {
            item {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    modifier = Modifier.fillMaxWidth().padding(top = 16.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("Be the first to leave a review!", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
                        Text(
                            text = "Share your dining feedback and earn 25 bonus loyalty points.",
                            style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant),
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }
                }
            }
        } else {
            items(reviews) { rev ->
                ReviewItemCard(review = rev)
            }
        }
    }
}

@Composable
private fun ScorePill(label: String, score: String) {
    Surface(
        shape = RoundedCornerShape(10.dp),
        color = MaterialTheme.colorScheme.surfaceVariant,
        modifier = Modifier.padding(2.dp)
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = score, style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold, color = VeloraEspresso))
            Text(text = label, style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant))
        }
    }
}

@Composable
private fun ReviewItemCard(review: Review) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(16.dp))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        shape = CircleShape,
                        color = VeloraGold.copy(alpha = 0.2f),
                        modifier = Modifier.size(34.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(
                                text = review.authorName.take(1).uppercase(),
                                fontWeight = FontWeight.Bold,
                                color = VeloraEspresso
                            )
                        }
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = review.authorName,
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                    )
                }

                Row {
                    repeat(review.rating.toInt().coerceIn(1, 5)) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = null,
                            tint = VeloraAmber,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = review.comment,
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = MaterialTheme.colorScheme.onSurface
                )
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "Food: ${review.foodRating}★ • Service: ${review.serviceRating}★",
                style = MaterialTheme.typography.labelSmall.copy(color = VeloraGold)
            )
        }
    }
}

@Composable
private fun WriteReviewDialog(
    authorDefaultName: String,
    onDismiss: () -> Unit,
    onSubmit: (String, Long, Long, Long, String) -> Unit
) {
    var author by remember { mutableStateOf(authorDefaultName) }
    var overallRating by remember { mutableLongStateOf(5L) }
    var foodRating by remember { mutableLongStateOf(5L) }
    var serviceRating by remember { mutableLongStateOf(5L) }
    var comment by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Share Your Dining Experience",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontFamily = FontFamily.Serif,
                    fontWeight = FontWeight.Bold
                )
            )
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text(
                    text = "Your feedback helps our kitchen and service team excel (+25 Pts awarded)",
                    style = MaterialTheme.typography.bodySmall.copy(color = VeloraGold)
                )

                OutlinedTextField(
                    value = author,
                    onValueChange = { author = it },
                    label = { Text("Your Name") },
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.fillMaxWidth()
                )

                // Star selectors
                Text("Overall Rating:", style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold))
                StarSelector(rating = overallRating, onSelect = { overallRating = it })

                Text("Food & Taste:", style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold))
                StarSelector(rating = foodRating, onSelect = { foodRating = it })

                Text("Staff & Service:", style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold))
                StarSelector(rating = serviceRating, onSelect = { serviceRating = it })

                OutlinedTextField(
                    value = comment,
                    onValueChange = { comment = it },
                    label = { Text("Comments / Favorite dish") },
                    placeholder = { Text("The Almond Blueberry Latte was extraordinary...") },
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 3
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (comment.isNotBlank() && author.isNotBlank()) {
                        onSubmit(author, overallRating, foodRating, serviceRating, comment)
                    }
                },
                enabled = comment.isNotBlank() && author.isNotBlank(),
                colors = ButtonDefaults.buttonColors(containerColor = VeloraEspresso),
                shape = RoundedCornerShape(10.dp)
            ) {
                Text("Submit Review")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

@Composable
private fun StarSelector(rating: Long, onSelect: (Long) -> Unit) {
    Row {
        for (i in 1..5) {
            IconButton(
                onClick = { onSelect(i.toLong()) },
                modifier = Modifier.size(32.dp)
            ) {
                Icon(
                    imageVector = if (i <= rating) Icons.Default.Star else Icons.Default.StarOutline,
                    contentDescription = "$i Stars",
                    tint = VeloraAmber,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}
