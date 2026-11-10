package com.example.ui.dialogs

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.UserProfile
import com.example.ui.theme.VeloraAmber
import com.example.ui.theme.VeloraEspresso
import com.example.ui.theme.VeloraGold

@Composable
fun MemberQrDialog(
    userProfile: UserProfile?,
    onDismiss: () -> Unit
) {
    val tier = userProfile?.tierEnum
    val points = userProfile?.loyaltyPoints ?: 100L
    val memberId = "DV-" + (userProfile?.userId?.take(8)?.uppercase() ?: "PATRON01")

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "D Velora Member Pass",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontFamily = FontFamily.Serif,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                ),
                modifier = Modifier.fillMaxWidth()
            )
        },
        text = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = userProfile?.displayName ?: "Valued Guest",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                )
                Text(
                    text = "${tier?.displayName ?: "Bronze Member"} • $points Points",
                    style = MaterialTheme.typography.bodySmall.copy(color = VeloraGold, fontWeight = FontWeight.SemiBold)
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Simulated Stylized QR Canvas
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = Color.White,
                    modifier = Modifier
                        .size(190.dp)
                        .border(2.dp, VeloraEspresso, RoundedCornerShape(16.dp))
                        .padding(14.dp)
                ) {
                    Canvas(modifier = Modifier.fillMaxSize()) {
                        val gridSize = 11
                        val cellSize = size.width / gridSize
                        val darkColor = Color(0xFF2E1810)

                        // Corner markers (classic QR corners)
                        drawRect(darkColor, Offset(0f, 0f), Size(cellSize * 3, cellSize * 3))
                        drawRect(Color.White, Offset(cellSize, cellSize), Size(cellSize, cellSize))

                        drawRect(darkColor, Offset(size.width - cellSize * 3, 0f), Size(cellSize * 3, cellSize * 3))
                        drawRect(Color.White, Offset(size.width - cellSize * 2, cellSize), Size(cellSize, cellSize))

                        drawRect(darkColor, Offset(0f, size.height - cellSize * 3), Size(cellSize * 3, cellSize * 3))
                        drawRect(Color.White, Offset(cellSize, size.height - cellSize * 2), Size(cellSize, cellSize))

                        // Center patterns
                        for (r in 0 until gridSize) {
                            for (c in 0 until gridSize) {
                                // Skip corners
                                val inTopLeft = r < 3 && c < 3
                                val inTopRight = r < 3 && c >= gridSize - 3
                                val inBottomLeft = r >= gridSize - 3 && c < 3
                                if (!inTopLeft && !inTopRight && !inBottomLeft) {
                                    if ((r * 7 + c * 13 + (memberId.hashCode() % 17)) % 3 == 0) {
                                        drawRect(
                                            darkColor,
                                            Offset(c * cellSize + 2f, r * cellSize + 2f),
                                            Size(cellSize - 4f, cellSize - 4f)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = "Member ID: $memberId",
                    style = MaterialTheme.typography.labelMedium.copy(
                        color = VeloraEspresso,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )
                )
                Text(
                    text = "Scan at cashier for instant points earning & reward redemption",
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center
                    ),
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        },
        confirmButton = {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(containerColor = VeloraEspresso),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Close Pass")
            }
        }
    )
}
