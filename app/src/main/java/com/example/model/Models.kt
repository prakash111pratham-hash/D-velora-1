package com.example.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.IgnoreExtraProperties
import com.google.firebase.firestore.ServerTimestamp

enum class MenuCategory(val displayName: String, val iconName: String) {
    SIGNATURE("House Signatures", "star"),
    STARTER("Starters", "local_dining"),
    FAST_FOOD("Fast Food & Rolls", "fastfood"),
    INDIAN_VEG("Indian Veg", "eco"),
    INDIAN_NON_VEG("Indian Non-Veg", "restaurant"),
    BIRYANI_RICE("Biryani & Rice", "rice_bowl"),
    ROTI_DAL("Breads & Dal", "bakery_dining"),
    DESSERT_CAFE("Cafe & Desserts", "coffee")
}

enum class DietaryType {
    VEG, NON_VEG, CHEF_SPECIAL
}

data class MenuItem(
    val id: String,
    val name: String,
    val description: String,
    val category: MenuCategory,
    val dietaryType: DietaryType,
    val singlePrice: Int? = null,
    val halfPrice: Int? = null,
    val fullPrice: Int? = null,
    val rating: Double = 4.8,
    val isAvailable: Boolean = true,
    val badge: String? = null
) {
    fun getDisplayPrice(): String {
        return when {
            halfPrice != null && fullPrice != null -> "Half: ₹$halfPrice • Full: ₹$fullPrice"
            singlePrice != null -> "₹$singlePrice"
            else -> "Market Price"
        }
    }
}

enum class PortionSelection {
    SINGLE, HALF, FULL
}

data class CartItem(
    val item: MenuItem,
    val portion: PortionSelection = PortionSelection.SINGLE,
    val quantity: Int = 1,
    val specialNote: String = ""
) {
    val unitPrice: Int
        get() = when (portion) {
            PortionSelection.HALF -> item.halfPrice ?: item.singlePrice ?: 0
            PortionSelection.FULL -> item.fullPrice ?: item.singlePrice ?: 0
            PortionSelection.SINGLE -> item.singlePrice ?: item.fullPrice ?: item.halfPrice ?: 0
        }

    val totalPrice: Int
        get() = unitPrice * quantity
}

enum class LoyaltyTier(val displayName: String, val requiredPoints: Int, val perkDescription: String) {
    BRONZE("Bronze Guest", 0, "5% order discount & birthday treat"),
    SILVER("Silver Connoisseur", 300, "10% order discount + free monthly specialty coffee"),
    GOLD("Gold VIP", 800, "15% off + priority seating + complimentary chef dessert"),
    PLATINUM("Platinum Patron", 1500, "20% off all dining + chef table private access")
}

@IgnoreExtraProperties
data class UserProfile(
    @DocumentId val id: String = "",
    val userId: String = "",
    val displayName: String = "",
    val email: String = "",
    val photoUrl: String? = null,
    val loyaltyPoints: Long = 0L,
    val tier: String = "BRONZE",
    @ServerTimestamp val createdAt: Timestamp? = null,
    @ServerTimestamp val updatedAt: Timestamp? = null
) {
    val tierEnum: LoyaltyTier
        get() = try {
            LoyaltyTier.valueOf(tier)
        } catch (_: Exception) {
            LoyaltyTier.BRONZE
        }
}

@IgnoreExtraProperties
data class Reservation(
    @DocumentId val id: String = "",
    val userId: String = "",
    val guestName: String = "",
    val guestPhone: String = "",
    val partySize: Long = 2L,
    val seatingArea: String = "Indoor Dining",
    val date: String = "",
    val timeSlot: String = "",
    val specialRequests: String? = null,
    val status: String = "CONFIRMED",
    @ServerTimestamp val createdAt: Timestamp? = null
)

@IgnoreExtraProperties
data class Order(
    @DocumentId val id: String = "",
    val userId: String = "",
    val itemSummary: String = "",
    val totalAmount: Long = 0L,
    val orderType: String = "DINE_IN_PREORDER",
    val paymentMethod: String = "UPI",
    val status: String = "PLACED",
    @ServerTimestamp val createdAt: Timestamp? = null
)

@IgnoreExtraProperties
data class RewardVoucher(
    @DocumentId val id: String = "",
    val userId: String = "",
    val title: String = "",
    val voucherCode: String = "",
    val pointsSpent: Long = 0L,
    val status: String = "ACTIVE",
    @ServerTimestamp val createdAt: Timestamp? = null
)

data class CatalogReward(
    val id: String,
    val title: String,
    val description: String,
    val pointsCost: Int,
    val category: String,
    val badge: String
)

@IgnoreExtraProperties
data class Review(
    @DocumentId val id: String = "",
    val userId: String = "",
    val authorName: String = "",
    val rating: Long = 5L,
    val foodRating: Long = 5L,
    val serviceRating: Long = 5L,
    val comment: String = "",
    @ServerTimestamp val createdAt: Timestamp? = null
)

data class SpecialOffer(
    val id: String,
    val title: String,
    val description: String,
    val promoCode: String,
    val discountPercent: Int,
    val validUntil: String
)
