package com.example.ui.reviews

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.RateReview
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import com.example.data.model.Review
import com.example.ui.viewmodel.ReviewSubmitState
import com.example.ui.viewmodel.ReviewsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReviewScreen(
    viewModel: ReviewsViewModel,
    currentUserDisplayName: String,
    modifier: Modifier = Modifier
) {
    val reviews by viewModel.reviews.collectAsState()
    val submitState by viewModel.submitState.collectAsState()

    var showWriteSheet by remember { mutableStateOf(false) }

    // Form inputs
    var rating by remember { mutableLongStateOf(5L) }
    var foodRating by remember { mutableLongStateOf(5L) }
    var serviceRating by remember { mutableLongStateOf(5L) }
    var ambianceRating by remember { mutableLongStateOf(5L) }
    var comment by remember { mutableStateOf("") }
    var authorName by remember { mutableStateOf(currentUserDisplayName) }
    var validationError by remember { mutableStateOf<String?>(null) }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp)
        ) {
            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = "GUEST FEEDBACK",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 2.sp,
                    fontFamily = FontFamily.Serif
                ),
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = "Share your dining impression • Earn +25 loyalty points",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Rating Summary Banner
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "4.9",
                                style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.ExtraBold),
                                color = MaterialTheme.colorScheme.primary
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Row {
                                repeat(5) {
                                    Icon(
                                        imageVector = Icons.Default.Star,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.secondary,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                            }
                        }
                        Text(
                            text = "Based on verified dine-in and online guests",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    Button(
                        onClick = { showWriteSheet = true },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = MaterialTheme.colorScheme.onPrimary
                        ),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.testTag("write_review_button")
                    ) {
                        Text("Add Review")
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Reviews List
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(reviews) { review ->
                    ReviewCard(review = review)
                }

                if (reviews.isEmpty()) {
                    item {
                        // Sample initial review cards
                        ReviewCard(
                            review = Review(
                                authorName = "Devanshi K.",
                                rating = 5,
                                foodRating = 5,
                                serviceRating = 5,
                                ambianceRating = 5,
                                comment = "The Almond Blueberry Latte is an absolute revelation! Smooth and fragrant. Paired it with Paneer Tikka. Exceptional service on the patio."
                            )
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        ReviewCard(
                            review = Review(
                                authorName = "Rahul Verma",
                                rating = 5,
                                foodRating = 5,
                                serviceRating = 5,
                                ambianceRating = 4,
                                comment = "Dehati Chicken with Tandoori Butter Roti took me straight back to authentic rustic flavors. Love the new loyalty points program!"
                            )
                        )
                    }
                }
            }
        }

        // Write Review Bottom Sheet
        if (showWriteSheet) {
            ModalBottomSheet(
                onDismissRequest = { showWriteSheet = false },
                sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp, vertical = 12.dp)
                ) {
                    Text(
                        text = "Leave Guest Review",
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "Earn +25 Bonus Loyalty Points upon submission",
                        style = MaterialTheme.typography.labelMedium.copy(color = MaterialTheme.colorScheme.primary)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    RatingSelectorRow(label = "Overall Rating", rating = rating, onSelect = { rating = it })
                    Spacer(modifier = Modifier.height(8.dp))
                    RatingSelectorRow(label = "Food & Coffee Taste", rating = foodRating, onSelect = { foodRating = it })
                    Spacer(modifier = Modifier.height(8.dp))
                    RatingSelectorRow(label = "Hospitality & Service", rating = serviceRating, onSelect = { serviceRating = it })
                    Spacer(modifier = Modifier.height(8.dp))
                    RatingSelectorRow(label = "Ambiance & Seating", rating = ambianceRating, onSelect = { ambianceRating = it })

                    Spacer(modifier = Modifier.height(14.dp))

                    OutlinedTextField(
                        value = authorName,
                        onValueChange = { authorName = it },
                        label = { Text("Your Name") },
                        singleLine = true,
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = comment,
                        onValueChange = { comment = it },
                        label = { Text("Your comments, favorite dishes & suggestions") },
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.fillMaxWidth()
                    )

                    if (validationError != null) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = validationError!!,
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    Button(
                        onClick = {
                            if (authorName.isBlank()) {
                                validationError = "Please enter your name."
                            } else if (comment.isBlank()) {
                                validationError = "Please write a brief comment."
                            } else {
                                validationError = null
                                viewModel.submitFeedback(
                                    authorName = authorName,
                                    rating = rating,
                                    foodRating = foodRating,
                                    serviceRating = serviceRating,
                                    ambianceRating = ambianceRating,
                                    comment = comment
                                )
                                showWriteSheet = false
                            }
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = MaterialTheme.colorScheme.onPrimary
                        ),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .testTag("submit_review_action_button")
                    ) {
                        Text("Submit Review & Claim 25 Pts", style = MaterialTheme.typography.titleMedium)
                    }

                    Spacer(modifier = Modifier.height(30.dp))
                }
            }
        }

        // Submit Status Notice
        when (val state = submitState) {
            is ReviewSubmitState.Submitting -> {
                AlertDialog(
                    onDismissRequest = {},
                    confirmButton = {},
                    text = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                            Spacer(modifier = Modifier.width(16.dp))
                            Text("Publishing review...")
                        }
                    }
                )
            }
            is ReviewSubmitState.Success -> {
                AlertDialog(
                    onDismissRequest = { viewModel.resetSubmitState() },
                    title = { Text("Thank You for Your Feedback!") },
                    text = { Text("Your review was posted and +25 Loyalty Points have been added to your Velora pass.") },
                    confirmButton = {
                        Button(onClick = { viewModel.resetSubmitState() }) {
                            Text("Done")
                        }
                    }
                )
            }
            is ReviewSubmitState.Error -> {
                AlertDialog(
                    onDismissRequest = { viewModel.resetSubmitState() },
                    title = { Text("Submission Notice") },
                    text = { Text(state.message) },
                    confirmButton = {
                        Button(onClick = { viewModel.resetSubmitState() }) {
                            Text("OK")
                        }
                    }
                )
            }
            else -> {}
        }
    }
}

@Composable
private fun RatingSelectorRow(
    label: String,
    rating: Long,
    onSelect: (Long) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = label, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurface)
        Row {
            for (i in 1..5) {
                IconButton(
                    onClick = { onSelect(i.toLong()) },
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        imageVector = if (i <= rating) Icons.Default.Star else Icons.Default.StarBorder,
                        contentDescription = "Star $i",
                        tint = MaterialTheme.colorScheme.secondary,
                        modifier = Modifier.size(22.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun ReviewCard(review: Review) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = review.authorName,
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onSurface
                )
                Row {
                    repeat(review.rating.toInt().coerceIn(1, 5)) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.secondary,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = review.comment,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
