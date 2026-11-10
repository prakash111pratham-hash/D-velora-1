package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.DietaryType
import com.example.model.MenuCategory
import com.example.model.MenuItem
import com.example.model.PortionSelection
import com.example.ui.theme.NonVegRed
import com.example.ui.theme.VegGreen
import com.example.ui.theme.VeloraAmber
import com.example.ui.theme.VeloraEspresso
import com.example.ui.theme.VeloraGold
import com.example.viewmodel.MainViewModel

@Composable
fun MenuScreen(
    mainViewModel: MainViewModel,
    onOpenCart: () -> Unit
) {
    val searchQuery by mainViewModel.searchQuery.collectAsState()
    val selectedCategory by mainViewModel.selectedCategory.collectAsState()
    val selectedDietary by mainViewModel.selectedDietaryFilter.collectAsState()
    val menuItems by mainViewModel.filteredMenuItems.collectAsState()
    val cartTotalCount by mainViewModel.cartTotalCount.collectAsState()
    val cartTotalPrice by mainViewModel.cartTotalPrice.collectAsState()

    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = if (cartTotalCount > 0) 140.dp else 90.dp)
        ) {
            // Header
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 12.dp)
                ) {
                    Text(
                        text = "Digital Menu",
                        style = MaterialTheme.typography.headlineLarge.copy(
                            fontFamily = FontFamily.Serif,
                            fontWeight = FontWeight.Bold
                        )
                    )
                    Text(
                        text = "Taste • Quality • Hygiene • Fresh Food Fresh Life",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = VeloraGold,
                            fontWeight = FontWeight.SemiBold
                        )
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    // Search input
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { mainViewModel.setSearchQuery(it) },
                        placeholder = { Text("Search dishes, rolls, curries, lattes...") },
                        leadingIcon = {
                            Icon(imageVector = Icons.Default.Search, contentDescription = "Search", tint = VeloraGold)
                        },
                        trailingIcon = {
                            if (searchQuery.isNotEmpty()) {
                                IconButton(onClick = { mainViewModel.setSearchQuery("") }) {
                                    Icon(imageVector = Icons.Default.Clear, contentDescription = "Clear")
                                }
                            }
                        },
                        shape = RoundedCornerShape(16.dp),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = VeloraGold,
                            unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant,
                            focusedContainerColor = MaterialTheme.colorScheme.surface,
                            unfocusedContainerColor = MaterialTheme.colorScheme.surface
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("menu_search_field")
                    )
                }
            }

            // Dietary Filter Chips
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 4.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    FilterChip(
                        selected = selectedDietary == null,
                        onClick = { mainViewModel.selectDietaryFilter(null) },
                        label = { Text("All") },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = VeloraEspresso,
                            selectedLabelColor = Color.White
                        )
                    )
                    FilterChip(
                        selected = selectedDietary == DietaryType.VEG,
                        onClick = { mainViewModel.selectDietaryFilter(DietaryType.VEG) },
                        label = { Text("Veg Only") },
                        leadingIcon = {
                            Box(
                                modifier = Modifier
                                    .size(10.dp)
                                    .background(VegGreen, CircleShape)
                            )
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = VegGreen,
                            selectedLabelColor = Color.White
                        )
                    )
                    FilterChip(
                        selected = selectedDietary == DietaryType.NON_VEG,
                        onClick = { mainViewModel.selectDietaryFilter(DietaryType.NON_VEG) },
                        label = { Text("Non-Veg") },
                        leadingIcon = {
                            Box(
                                modifier = Modifier
                                    .size(10.dp)
                                    .background(NonVegRed, CircleShape)
                            )
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = NonVegRed,
                            selectedLabelColor = Color.White
                        )
                    )
                    FilterChip(
                        selected = selectedDietary == DietaryType.CHEF_SPECIAL,
                        onClick = { mainViewModel.selectDietaryFilter(DietaryType.CHEF_SPECIAL) },
                        label = { Text("★ Signatures") },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = VeloraGold,
                            selectedLabelColor = Color.White
                        )
                    )
                }
            }

            // Categories horizontal bar
            item {
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 20.dp, vertical = 6.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(MenuCategory.values()) { category ->
                        val isSelected = selectedCategory == category
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = if (isSelected) VeloraEspresso else MaterialTheme.colorScheme.surface,
                            modifier = Modifier
                                .border(
                                    1.dp,
                                    if (isSelected) VeloraGold else MaterialTheme.colorScheme.outlineVariant,
                                    RoundedCornerShape(12.dp)
                                )
                                .clickable { mainViewModel.selectCategory(category) }
                        ) {
                            Text(
                                text = category.displayName,
                                style = MaterialTheme.typography.labelMedium.copy(
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                    color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface
                                ),
                                modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp)
                            )
                        }
                    }
                }
            }

            // Items count info
            item {
                Text(
                    text = "${menuItems.size} dishes found",
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    ),
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp)
                )
            }

            // Dish Items List
            items(menuItems) { dish ->
                DishItemRow(
                    dish = dish,
                    onAddToCart = { portion ->
                        mainViewModel.addToCart(dish, portion)
                    }
                )
            }
        }

        // Floating Cart Summary Bar
        AnimatedVisibility(
            visible = cartTotalCount > 0,
            enter = fadeIn(),
            exit = fadeOut(),
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 78.dp, start = 16.dp, end = 16.dp)
        ) {
            Surface(
                shape = RoundedCornerShape(20.dp),
                color = VeloraEspresso,
                tonalElevation = 8.dp,
                shadowElevation = 10.dp,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onOpenCart() }
                    .border(1.5.dp, VeloraGold, RoundedCornerShape(20.dp))
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 18.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(
                            shape = CircleShape,
                            color = VeloraAmber,
                            modifier = Modifier.size(36.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Default.ShoppingBag,
                                    contentDescription = "Cart",
                                    tint = VeloraEspresso,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Column {
                            Text(
                                text = "$cartTotalCount items in Order",
                                style = MaterialTheme.typography.titleSmall.copy(
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold
                                )
                            )
                            Text(
                                text = "Total: ₹$cartTotalPrice",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = VeloraAmber,
                                    fontWeight = FontWeight.Bold
                                )
                            )
                        }
                    }

                    Button(
                        onClick = onOpenCart,
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = VeloraGold,
                            contentColor = Color.White
                        ),
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 6.dp),
                        modifier = Modifier.testTag("view_order_cart_button")
                    ) {
                        Text(
                            text = "View Order",
                            style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun DishItemRow(
    dish: MenuItem,
    onAddToCart: (PortionSelection) -> Unit
) {
    var selectedPortion by remember {
        mutableStateOf(if (dish.halfPrice != null && dish.fullPrice != null) PortionSelection.HALF else PortionSelection.SINGLE)
    }

    Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 7.dp)
            .border(1.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(18.dp)),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        // Veg / Non-veg dot
                        val dotColor = when (dish.dietaryType) {
                            DietaryType.VEG -> VegGreen
                            DietaryType.NON_VEG -> NonVegRed
                            DietaryType.CHEF_SPECIAL -> VeloraGold
                        }
                        Box(
                            modifier = Modifier
                                .size(14.dp)
                                .border(1.5.dp, dotColor, RoundedCornerShape(4.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(6.dp)
                                    .background(dotColor, CircleShape)
                            )
                        }

                        Spacer(modifier = Modifier.width(8.dp))

                        if (dish.badge != null) {
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = VeloraAmber.copy(alpha = 0.15f)
                            ) {
                                Text(
                                    text = dish.badge,
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        color = VeloraEspresso,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 10.sp
                                    ),
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = dish.name,
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    Text(
                        text = dish.description,
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        ),
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(start = 8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = null,
                        tint = VeloraAmber,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(3.dp))
                    Text(
                        text = "${dish.rating}",
                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Portion toggles & Pricing
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // If half/full available
                if (dish.halfPrice != null && dish.fullPrice != null) {
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = if (selectedPortion == PortionSelection.HALF) VeloraEspresso else MaterialTheme.colorScheme.surfaceVariant,
                            modifier = Modifier
                                .clickable { selectedPortion = PortionSelection.HALF }
                        ) {
                            Text(
                                text = "Half (₹${dish.halfPrice})",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = if (selectedPortion == PortionSelection.HALF) Color.White else MaterialTheme.colorScheme.onSurface
                                ),
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                            )
                        }

                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = if (selectedPortion == PortionSelection.FULL) VeloraEspresso else MaterialTheme.colorScheme.surfaceVariant,
                            modifier = Modifier
                                .clickable { selectedPortion = PortionSelection.FULL }
                        ) {
                            Text(
                                text = "Full (₹${dish.fullPrice})",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = if (selectedPortion == PortionSelection.FULL) Color.White else MaterialTheme.colorScheme.onSurface
                                ),
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                            )
                        }
                    }
                } else {
                    Text(
                        text = dish.getDisplayPrice(),
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = VeloraEspresso
                        )
                    )
                }

                Button(
                    onClick = { onAddToCart(selectedPortion) },
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = VeloraGold,
                        contentColor = Color.White
                    ),
                    contentPadding = PaddingValues(horizontal = 14.dp, vertical = 6.dp),
                    modifier = Modifier.testTag("add_to_cart_${dish.id}")
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Add", style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold))
                }
            }
        }
    }
}
