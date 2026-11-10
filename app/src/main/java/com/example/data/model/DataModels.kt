package com.example.data.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.IgnoreExtraProperties
import com.google.firebase.firestore.ServerTimestamp

@IgnoreExtraProperties
data class User(
    @DocumentId val id: String = "",
    val userId: String = "",
    val displayName: String = "",
    val email: String = "",
    val photoUrl: String = "",
    val loyaltyPoints: Long = 100L,
    val tier: String = "BRONZE",
    @ServerTimestamp val createdAt: Timestamp? = null,
    @ServerTimestamp val updatedAt: Timestamp? = null
)

@IgnoreExtraProperties
data class Reservation(
    @DocumentId val id: String = "",
    val userId: String = "",
    val guestName: String = "",
    val guestPhone: String = "",
    val partySize: Long = 2L,
    val seatingArea: String = "Patio Terrace",
    val date: String = "",
    val timeSlot: String = "",
    val specialRequests: String = "",
    val status: String = "CONFIRMED",
    @ServerTimestamp val createdAt: Timestamp? = null
)

@IgnoreExtraProperties
data class Order(
    @DocumentId val id: String = "",
    val userId: String = "",
    val itemsSummary: String = "",
    val itemCount: Long = 0L,
    val totalAmount: Double = 0.0,
    val orderType: String = "DINE_IN",
    val paymentMethod: String = "CARD",
    val paymentStatus: String = "PAID",
    val orderStatus: String = "CONFIRMED",
    val pointsEarned: Long = 0L,
    @ServerTimestamp val createdAt: Timestamp? = null
)

@IgnoreExtraProperties
data class Reward(
    @DocumentId val id: String = "",
    val userId: String = "",
    val rewardTitle: String = "",
    val pointsCost: Long = 0L,
    val voucherCode: String = "",
    val isUsed: Boolean = false,
    @ServerTimestamp val createdAt: Timestamp? = null
)

@IgnoreExtraProperties
data class Review(
    @DocumentId val id: String = "",
    val userId: String = "",
    val authorName: String = "",
    val rating: Long = 5L,
    val foodRating: Long = 5L,
    val serviceRating: Long = 5L,
    val ambianceRating: Long = 5L,
    val comment: String = "",
    @ServerTimestamp val createdAt: Timestamp? = null
)

data class MenuItem(
    val id: String,
    val name: String,
    val description: String,
    val category: String,
    val halfPrice: Double? = null,
    val fullPrice: Double,
    val isVeg: Boolean = true,
    val isSignature: Boolean = false,
    val spicyLevel: Int = 0, // 0 = mild, 1 = medium, 2 = hot
    val preparationTimeMin: Int = 15
)

data class CartItem(
    val item: MenuItem,
    val isHalfPortion: Boolean = false,
    val quantity: Int = 1,
    val specialInstructions: String = ""
) {
    val unitPrice: Double
        get() = if (isHalfPortion && item.halfPrice != null) item.halfPrice else item.fullPrice

    val totalPrice: Double
        get() = unitPrice * quantity
}

data class SpecialOffer(
    val id: String,
    val title: String,
    val description: String,
    val badge: String,
    val promoCode: String,
    val expiryDate: String
)
