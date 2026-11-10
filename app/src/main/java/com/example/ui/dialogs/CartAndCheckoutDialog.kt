package com.example.ui.dialogs

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Payment
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
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
import com.example.model.CartItem
import com.example.ui.theme.VeloraAmber
import com.example.ui.theme.VeloraEspresso
import com.example.ui.theme.VeloraGold
import com.example.viewmodel.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartAndCheckoutBottomSheet(
    mainViewModel: MainViewModel,
    onDismiss: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val cartItems by mainViewModel.cartItems.collectAsState()
    val cartTotalPrice by mainViewModel.cartTotalPrice.collectAsState()
    val isSubmitting by mainViewModel.isSubmittingOrder.collectAsState()

    var orderType by remember { mutableStateOf("DINE_IN_PREORDER") } // DINE_IN_PREORDER or PICKUP
    var paymentMethod by remember { mutableStateOf("UPI") } // UPI, CARD, PAY_AT_COUNTER
    var promoCode by remember { mutableStateOf("") }
    var appliedDiscountPercent by remember { mutableIntStateOf(0) }
    var promoMessage by remember { mutableStateOf<String?>(null) }

    val discountAmount = (cartTotalPrice * appliedDiscountPercent) / 100
    val finalTotal = (cartTotalPrice - discountAmount).coerceAtLeast(0)
    val pointsToEarn = (finalTotal / 10).coerceAtLeast(10)

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = MaterialTheme.colorScheme.surface
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 8.dp)
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Your Dining Order",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontFamily = FontFamily.Serif,
                            fontWeight = FontWeight.Bold
                        )
                    )
                    Text(
                        text = "${cartItems.sumOf { it.quantity }} items selected",
                        style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
                    )
                }

                IconButton(onClick = onDismiss) {
                    Icon(imageVector = Icons.Default.Close, contentDescription = "Close")
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            if (cartItems.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(40.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Your order is empty. Browse our menu to add artisanal coffee and dishes!",
                        style = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier.weight(1f, fill = false),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(cartItems) { item ->
                        CartItemRow(
                            item = item,
                            onIncrement = { mainViewModel.updateCartItemQuantity(item, 1) },
                            onDecrement = { mainViewModel.updateCartItemQuantity(item, -1) }
                        )
                    }

                    item {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Order Option",
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                        )
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 6.dp),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = if (orderType == "DINE_IN_PREORDER") VeloraEspresso else MaterialTheme.colorScheme.surfaceVariant,
                                modifier = Modifier
                                    .weight(1f)
                                    .clickable { orderType = "DINE_IN_PREORDER" }
                            ) {
                                Text(
                                    text = "Dine-In Table Order",
                                    style = MaterialTheme.typography.labelMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = if (orderType == "DINE_IN_PREORDER") Color.White else MaterialTheme.colorScheme.onSurface
                                    ),
                                    modifier = Modifier.padding(vertical = 10.dp, horizontal = 12.dp)
                                )
                            }

                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = if (orderType == "PICKUP") VeloraEspresso else MaterialTheme.colorScheme.surfaceVariant,
                                modifier = Modifier
                                    .weight(1f)
                                    .clickable { orderType = "PICKUP" }
                            ) {
                                Text(
                                    text = "Takeout / Pickup",
                                    style = MaterialTheme.typography.labelMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = if (orderType == "PICKUP") Color.White else MaterialTheme.colorScheme.onSurface
                                    ),
                                    modifier = Modifier.padding(vertical = 10.dp, horizontal = 12.dp)
                                )
                            }
                        }
                    }

                    // Payment Method Selection
                    item {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Payment Method",
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                        )
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 6.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            PaymentOptionChip(
                                icon = Icons.Default.Payment,
                                label = "UPI / GPay",
                                selected = paymentMethod == "UPI",
                                onSelect = { paymentMethod = "UPI" },
                                modifier = Modifier.weight(1f)
                            )
                            PaymentOptionChip(
                                icon = Icons.Default.CreditCard,
                                label = "Card",
                                selected = paymentMethod == "CARD",
                                onSelect = { paymentMethod = "CARD" },
                                modifier = Modifier.weight(1f)
                            )
                            PaymentOptionChip(
                                icon = Icons.Default.Payments,
                                label = "Cash",
                                selected = paymentMethod == "PAY_AT_COUNTER",
                                onSelect = { paymentMethod = "PAY_AT_COUNTER" },
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }

                    // Promo Code Input
                    item {
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            OutlinedTextField(
                                value = promoCode,
                                onValueChange = { promoCode = it.uppercase() },
                                placeholder = { Text("Promo Code (e.g. BLUEBERRY20)") },
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier.weight(1f),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = VeloraGold,
                                    unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant
                                ),
                                singleLine = true
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Button(
                                onClick = {
                                    if (promoCode == "BLUEBERRY20") {
                                        appliedDiscountPercent = 20
                                        promoMessage = "20% discount applied!"
                                    } else if (promoCode == "FAMILYFEAST") {
                                        appliedDiscountPercent = 15
                                        promoMessage = "15% feast discount applied!"
                                    } else {
                                        appliedDiscountPercent = 0
                                        promoMessage = "Invalid code. Try BLUEBERRY20"
                                    }
                                },
                                shape = RoundedCornerShape(10.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = VeloraGold)
                            ) {
                                Text("Apply")
                            }
                        }

                        if (promoMessage != null) {
                            Text(
                                text = promoMessage ?: "",
                                style = MaterialTheme.typography.labelSmall.copy(color = VeloraGold),
                                modifier = Modifier.padding(top = 4.dp)
                            )
                        }
                    }

                    // Pricing breakdown
                    item {
                        HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Subtotal", style = MaterialTheme.typography.bodyMedium)
                            Text("₹$cartTotalPrice", style = MaterialTheme.typography.bodyMedium)
                        }
                        if (discountAmount > 0) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("Promo Discount ($appliedDiscountPercent%)", style = MaterialTheme.typography.bodyMedium.copy(color = VeloraGold))
                                Text("-₹$discountAmount", style = MaterialTheme.typography.bodyMedium.copy(color = VeloraGold))
                            }
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Taxes & Service (GST)", style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant))
                            Text("Included", style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant))
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                "Total Payable",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = FontFamily.Serif
                                )
                            )
                            Text(
                                "₹$finalTotal",
                                style = MaterialTheme.typography.titleLarge.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = VeloraEspresso
                                )
                            )
                        }
                        Text(
                            text = "★ Earn $pointsToEarn loyalty points on this order",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = VeloraGold,
                                fontWeight = FontWeight.Bold
                            ),
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                Button(
                    onClick = {
                        mainViewModel.submitOrder(
                            orderType = orderType,
                            paymentMethod = paymentMethod,
                            onComplete = onDismiss
                        )
                    },
                    enabled = !isSubmitting && cartItems.isNotEmpty(),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = VeloraEspresso,
                        contentColor = Color.White
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp)
                        .testTag("checkout_submit_button")
                ) {
                    if (isSubmitting) {
                        CircularProgressIndicator(
                            color = VeloraAmber,
                            strokeWidth = 2.dp,
                            modifier = Modifier.size(24.dp)
                        )
                    } else {
                        Text(
                            text = "Confirm Order (₹$finalTotal • $paymentMethod)",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))
            }
        }
    }
}

@Composable
private fun CartItemRow(
    item: CartItem,
    onIncrement: () -> Unit,
    onDecrement: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = item.item.name,
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                )
                Text(
                    text = "${item.portion.name} • ₹${item.unitPrice} each",
                    style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
                )
                Text(
                    text = "Total: ₹${item.totalPrice}",
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = VeloraEspresso
                    )
                )
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                Surface(
                    shape = CircleShape,
                    color = MaterialTheme.colorScheme.surface,
                    modifier = Modifier
                        .size(30.dp)
                        .clickable { onDecrement() }
                        .border(1.dp, MaterialTheme.colorScheme.outlineVariant, CircleShape)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = if (item.quantity == 1) Icons.Default.Delete else Icons.Default.Remove,
                            contentDescription = "Decrease",
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }

                Text(
                    text = "${item.quantity}",
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                    modifier = Modifier.padding(horizontal = 10.dp)
                )

                Surface(
                    shape = CircleShape,
                    color = VeloraGold,
                    modifier = Modifier
                        .size(30.dp)
                        .clickable { onIncrement() }
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Increase",
                            tint = Color.White,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun PaymentOptionChip(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    selected: Boolean,
    onSelect: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(10.dp),
        color = if (selected) VeloraEspresso else MaterialTheme.colorScheme.surfaceVariant,
        modifier = modifier
            .border(
                1.dp,
                if (selected) VeloraGold else MaterialTheme.colorScheme.outlineVariant,
                RoundedCornerShape(10.dp)
            )
            .clickable { onSelect() }
    ) {
        Column(
            modifier = Modifier.padding(vertical = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = if (selected) VeloraAmber else MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall.copy(
                    fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium,
                    color = if (selected) Color.White else MaterialTheme.colorScheme.onSurface
                )
            )
        }
    }
}
