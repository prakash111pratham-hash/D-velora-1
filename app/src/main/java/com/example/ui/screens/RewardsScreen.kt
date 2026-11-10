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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CardGiftcard
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.MenuDataSource
import com.example.model.CatalogReward
import com.example.model.LoyaltyTier
import com.example.model.RewardVoucher
import com.example.ui.theme.VeloraAmber
import com.example.ui.theme.VeloraEspresso
import com.example.ui.theme.VeloraGold
import com.example.viewmodel.MainViewModel

@Composable
fun RewardsScreen(
    mainViewModel: MainViewModel,
    onOpenQrPass: () -> Unit
) {
    val userProfile by mainViewModel.userProfile.collectAsState()
    val vouchers by mainViewModel.vouchers.collectAsState()
    val redemptionMessage by mainViewModel.redemptionMessage.collectAsState()
    val points = userProfile?.loyaltyPoints ?: 100L
    val tier = userProfile?.tierEnum ?: LoyaltyTier.BRONZE

    val nextTier = when (tier) {
        LoyaltyTier.BRONZE -> LoyaltyTier.SILVER
        LoyaltyTier.SILVER -> LoyaltyTier.GOLD
        LoyaltyTier.GOLD -> LoyaltyTier.PLATINUM
        LoyaltyTier.PLATINUM -> null
    }

    val progress = if (nextTier != null) {
        (points.toFloat() / nextTier.requiredPoints.toFloat()).coerceIn(0f, 1f)
    } else 1f

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(start = 20.dp, end = 20.dp, top = 16.dp, bottom = 100.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Header
        item {
            Column {
                Text(
                    text = "Loyalty Club",
                    style = MaterialTheme.typography.headlineLarge.copy(
                        fontFamily = FontFamily.Serif,
                        fontWeight = FontWeight.Bold
                    )
                )
                Text(
                    text = "Dine, earn points & unlock handcrafted treats",
                    style = MaterialTheme.typography.bodySmall.copy(color = VeloraGold)
                )
            }
        }

        // Redemption message feedback
        if (redemptionMessage != null) {
            item {
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = VeloraGold.copy(alpha = 0.2f),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = redemptionMessage ?: "",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontWeight = FontWeight.SemiBold,
                                color = VeloraEspresso
                            ),
                            modifier = Modifier.weight(1f)
                        )
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = null,
                            tint = VeloraEspresso,
                            modifier = Modifier.clickable { mainViewModel.dismissRedemptionMessage() }
                        )
                    }
                }
            }
        }

        // VIP Member Digital Pass Card
        item {
            Card(
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = VeloraEspresso),
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.5.dp, VeloraGold, RoundedCornerShape(24.dp))
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.linearGradient(
                                colors = listOf(
                                    VeloraEspresso,
                                    Color(0xFF4E2B18)
                                )
                            )
                        )
                        .padding(22.dp)
                ) {
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = "D VELORA CLUB",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        color = VeloraAmber,
                                        fontWeight = FontWeight.Bold,
                                        letterSpacing = 2.sp
                                    )
                                )
                                Text(
                                    text = userProfile?.displayName ?: "Valued Patron",
                                    style = MaterialTheme.typography.titleLarge.copy(
                                        color = Color.White,
                                        fontWeight = FontWeight.Bold,
                                        fontFamily = FontFamily.Serif
                                    )
                                )
                            }

                            Surface(
                                shape = CircleShape,
                                color = VeloraGold,
                                modifier = Modifier
                                    .size(46.dp)
                                    .clickable { onOpenQrPass() }
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(
                                        imageVector = Icons.Default.QrCode,
                                        contentDescription = "QR Pass",
                                        tint = VeloraEspresso,
                                        modifier = Modifier.size(26.dp)
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(20.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.Bottom
                        ) {
                            Column {
                                Text(
                                    text = "$points",
                                    style = MaterialTheme.typography.displayMedium.copy(
                                        color = VeloraAmber,
                                        fontWeight = FontWeight.Bold,
                                        fontFamily = FontFamily.Serif
                                    )
                                )
                                Text(
                                    text = "Available Reward Points",
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        color = Color.White.copy(alpha = 0.8f)
                                    )
                                )
                            }

                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = VeloraGold.copy(alpha = 0.25f),
                                modifier = Modifier.border(1.dp, VeloraGold, RoundedCornerShape(8.dp))
                            ) {
                                Text(
                                    text = tier.displayName,
                                    style = MaterialTheme.typography.labelMedium.copy(
                                        color = VeloraAmber,
                                        fontWeight = FontWeight.Bold
                                    ),
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        LinearProgressIndicator(
                            progress = { progress },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(8.dp)
                                .clip(CircleShape),
                            color = VeloraGold,
                            trackColor = Color.White.copy(alpha = 0.2f)
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = if (nextTier != null) {
                                "${nextTier.requiredPoints - points} points to unlock ${nextTier.displayName}"
                            } else {
                                "Congratulations! You have reached our highest Platinum VIP tier."
                            },
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = Color.White.copy(alpha = 0.75f)
                            )
                        )
                    }
                }
            }
        }

        // Active Vouchers Section
        if (vouchers.isNotEmpty()) {
            item {
                Column {
                    Text(
                        text = "My Active Vouchers (${vouchers.size})",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Serif
                        )
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    vouchers.forEach { voucher ->
                        VoucherItemCard(voucher = voucher)
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }
        }

        // Tier Benefits List
        item {
            Text(
                text = "Membership Tiers & Perks",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Serif
                )
            )
        }

        item {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                LoyaltyTier.values().forEach { t ->
                    val isCurrent = tier == t
                    Card(
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (isCurrent) VeloraAmber.copy(alpha = 0.12f) else MaterialTheme.colorScheme.surface
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(
                                if (isCurrent) 1.5.dp else 1.dp,
                                if (isCurrent) VeloraGold else MaterialTheme.colorScheme.outlineVariant,
                                RoundedCornerShape(14.dp)
                            )
                    ) {
                        Row(
                            modifier = Modifier.padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = null,
                                tint = if (isCurrent) VeloraGold else MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = t.displayName,
                                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                                    )
                                    if (isCurrent) {
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Surface(
                                            shape = RoundedCornerShape(4.dp),
                                            color = VeloraGold
                                        ) {
                                            Text(
                                                text = "ACTIVE",
                                                style = MaterialTheme.typography.labelSmall.copy(
                                                    color = Color.White,
                                                    fontWeight = FontWeight.Bold,
                                                    fontSize = 9.sp
                                                ),
                                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                            )
                                        }
                                    }
                                }
                                Text(
                                    text = "${t.requiredPoints} points required • ${t.perkDescription}",
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                )
                            }
                        }
                    }
                }
            }
        }

        // Rewards Catalog
        item {
            Text(
                text = "Redeem Rewards Catalog",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Serif
                )
            )
        }

        items(MenuDataSource.catalogRewards) { reward ->
            CatalogRewardCard(
                reward = reward,
                userPoints = points,
                onRedeem = {
                    mainViewModel.redeemCatalogReward(reward.title, reward.pointsCost.toLong())
                }
            )
        }
    }
}

@Composable
private fun CatalogRewardCard(
    reward: CatalogReward,
    userPoints: Long,
    onRedeem: () -> Unit
) {
    val canRedeem = userPoints >= reward.pointsCost

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(16.dp)),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(46.dp)
                    .background(VeloraGold.copy(alpha = 0.15f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.CardGiftcard,
                    contentDescription = null,
                    tint = VeloraEspresso,
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column(modifier = Modifier.weight(1f)) {
                Surface(
                    shape = RoundedCornerShape(4.dp),
                    color = VeloraAmber.copy(alpha = 0.15f)
                ) {
                    Text(
                        text = reward.badge,
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = VeloraEspresso,
                            fontWeight = FontWeight.Bold,
                            fontSize = 10.sp
                        ),
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = reward.title,
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                )
                Text(
                    text = reward.description,
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                )
            }

            Spacer(modifier = Modifier.width(10.dp))

            Button(
                onClick = onRedeem,
                enabled = canRedeem,
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = VeloraGold,
                    disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant
                ),
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                modifier = Modifier.testTag("redeem_${reward.id}")
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "${reward.pointsCost} Pts",
                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
                    )
                    Text(
                        text = if (canRedeem) "Claim" else "Locked",
                        style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp)
                    )
                }
            }
        }
    }
}

@Composable
private fun VoucherItemCard(voucher: RewardVoucher) {
    var copied by remember { mutableStateOf(false) }

    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, VeloraGold.copy(alpha = 0.6f), RoundedCornerShape(14.dp))
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = voucher.title,
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Voucher: ${voucher.voucherCode}",
                    style = MaterialTheme.typography.labelMedium.copy(
                        color = VeloraEspresso,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )
                )
                Text(
                    text = "Present this code to your waiter or at the checkout counter",
                    style = MaterialTheme.typography.labelSmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
                )
            }

            Surface(
                shape = RoundedCornerShape(8.dp),
                color = VeloraGold.copy(alpha = 0.15f),
                modifier = Modifier
                    .clickable { copied = true }
                    .padding(4.dp)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = if (copied) Icons.Default.Check else Icons.Default.ContentCopy,
                        contentDescription = "Copy",
                        tint = VeloraEspresso,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = if (copied) "Ready" else "Copy",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = VeloraEspresso,
                            fontWeight = FontWeight.Bold
                        )
                    )
                }
            }
        }
    }
}
