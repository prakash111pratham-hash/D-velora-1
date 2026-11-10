package com.example.data

import android.content.Context
import com.example.R
import com.example.model.LoyaltyTier
import com.example.model.Order
import com.example.model.Reservation
import com.example.model.Review
import com.example.model.RewardVoucher
import com.example.model.UserProfile
import com.example.util.OperationType
import com.example.util.handleFirestoreError
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.snapshots
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import java.util.UUID

class FirestoreRepository(private val db: FirebaseFirestore) {

    constructor(context: Context) : this(
        FirebaseFirestore.getInstance(
            context.applicationContext.getString(R.string.firestore_database_id)
        )
    )

    private val auth = Firebase.auth

    private fun requireUserId(): String {
        return auth.currentUser?.uid
            ?: throw IllegalStateException("User must be signed in with Google before accessing Firestore.")
    }

    // 1. User Profile & Loyalty
    fun observeUserProfile(userId: String): Flow<UserProfile?> {
        val docRef = db.collection("users").document(userId)
        return docRef.snapshots().map { snapshot ->
            if (snapshot.exists()) {
                snapshot.toObject(UserProfile::class.java)
            } else {
                null
            }
        }.catch { e ->
            handleFirestoreError(e as Exception, OperationType.GET, docRef.path)
            emit(null)
        }
    }

    suspend fun createInitialProfileIfMissing(userId: String, displayName: String, email: String, photoUrl: String?) {
        val docRef = db.collection("users").document(userId)
        try {
            val existing = docRef.get().await()
            if (!existing.exists()) {
                val payload = mutableMapOf<String, Any>(
                    "userId" to userId,
                    "displayName" to displayName.ifBlank { "Valued Guest" },
                    "email" to email,
                    "loyaltyPoints" to 100L, // 100 Welcome bonus points!
                    "tier" to LoyaltyTier.BRONZE.name,
                    "createdAt" to FieldValue.serverTimestamp(),
                    "updatedAt" to FieldValue.serverTimestamp()
                )
                if (!photoUrl.isNullOrBlank()) {
                    payload["photoUrl"] = photoUrl
                }
                docRef.set(payload).await()
            }
        } catch (e: Exception) {
            handleFirestoreError(e, OperationType.CREATE, docRef.path)
        }
    }

    suspend fun updateLoyaltyPoints(userId: String, pointsDelta: Long) {
        val docRef = db.collection("users").document(userId)
        try {
            val snapshot = docRef.get().await()
            val currentPoints = snapshot.getLong("loyaltyPoints") ?: 0L
            val newPoints = (currentPoints + pointsDelta).coerceAtLeast(0L)

            val newTier = when {
                newPoints >= LoyaltyTier.PLATINUM.requiredPoints -> LoyaltyTier.PLATINUM.name
                newPoints >= LoyaltyTier.GOLD.requiredPoints -> LoyaltyTier.GOLD.name
                newPoints >= LoyaltyTier.SILVER.requiredPoints -> LoyaltyTier.SILVER.name
                else -> LoyaltyTier.BRONZE.name
            }

            docRef.update(
                mapOf(
                    "loyaltyPoints" to newPoints,
                    "tier" to newTier,
                    "updatedAt" to FieldValue.serverTimestamp()
                )
            ).await()
        } catch (e: Exception) {
            handleFirestoreError(e, OperationType.UPDATE, docRef.path)
        }
    }

    // 2. Table Reservations
    fun observeReservations(userId: String): Flow<List<Reservation>> {
        val collectionRef = db.collection("users").document(userId).collection("reservations")
        return collectionRef.orderBy("createdAt", Query.Direction.DESCENDING)
            .snapshots()
            .map { it.toObjects(Reservation::class.java) }
            .catch { e ->
                handleFirestoreError(e as Exception, OperationType.LIST, collectionRef.path)
                emit(emptyList())
            }
    }

    suspend fun createReservation(
        guestName: String,
        guestPhone: String,
        partySize: Long,
        seatingArea: String,
        date: String,
        timeSlot: String,
        specialRequests: String?
    ): Boolean {
        val uid = requireUserId()
        val docRef = db.collection("users").document(uid).collection("reservations").document()
        val payload = mutableMapOf<String, Any>(
            "id" to docRef.id,
            "userId" to uid,
            "guestName" to guestName,
            "guestPhone" to guestPhone,
            "partySize" to partySize,
            "seatingArea" to seatingArea,
            "date" to date,
            "timeSlot" to timeSlot,
            "status" to "CONFIRMED",
            "createdAt" to FieldValue.serverTimestamp()
        )
        if (!specialRequests.isNullOrBlank()) {
            payload["specialRequests"] = specialRequests
        }

        return try {
            docRef.set(payload).await()
            // Earn 50 bonus reservation loyalty points
            updateLoyaltyPoints(uid, 50L)
            true
        } catch (e: Exception) {
            handleFirestoreError(e, OperationType.CREATE, docRef.path)
            false
        }
    }

    suspend fun cancelReservation(reservationId: String): Boolean {
        val uid = requireUserId()
        val docRef = db.collection("users").document(uid).collection("reservations").document(reservationId)
        return try {
            docRef.update("status", "CANCELLED").await()
            true
        } catch (e: Exception) {
            handleFirestoreError(e, OperationType.UPDATE, docRef.path)
            false
        }
    }

    // 3. Digital Pre-Orders / Takeout
    fun observeOrders(userId: String): Flow<List<Order>> {
        val collectionRef = db.collection("users").document(userId).collection("orders")
        return collectionRef.orderBy("createdAt", Query.Direction.DESCENDING)
            .snapshots()
            .map { it.toObjects(Order::class.java) }
            .catch { e ->
                handleFirestoreError(e as Exception, OperationType.LIST, collectionRef.path)
                emit(emptyList())
            }
    }

    suspend fun placeOrder(
        itemSummary: String,
        totalAmount: Long,
        orderType: String,
        paymentMethod: String
    ): Boolean {
        val uid = requireUserId()
        val docRef = db.collection("users").document(uid).collection("orders").document()
        val payload = mapOf(
            "id" to docRef.id,
            "userId" to uid,
            "itemSummary" to itemSummary,
            "totalAmount" to totalAmount,
            "orderType" to orderType,
            "paymentMethod" to paymentMethod,
            "status" to "PLACED",
            "createdAt" to FieldValue.serverTimestamp()
        )

        return try {
            docRef.set(payload).await()
            // Earn 10 points per ₹100 spent (minimum 10 points)
            val pointsEarned = ((totalAmount / 10L).coerceAtLeast(10L))
            updateLoyaltyPoints(uid, pointsEarned)
            true
        } catch (e: Exception) {
            handleFirestoreError(e, OperationType.CREATE, docRef.path)
            false
        }
    }

    // 4. Rewards Redemptions
    fun observeVouchers(userId: String): Flow<List<RewardVoucher>> {
        val collectionRef = db.collection("users").document(userId).collection("rewards")
        return collectionRef.orderBy("createdAt", Query.Direction.DESCENDING)
            .snapshots()
            .map { it.toObjects(RewardVoucher::class.java) }
            .catch { e ->
                handleFirestoreError(e as Exception, OperationType.LIST, collectionRef.path)
                emit(emptyList())
            }
    }

    suspend fun redeemReward(title: String, pointsCost: Long): Boolean {
        val uid = requireUserId()
        val userDoc = db.collection("users").document(uid).get().await()
        val currentPoints = userDoc.getLong("loyaltyPoints") ?: 0L
        if (currentPoints < pointsCost) {
            return false
        }

        val voucherRef = db.collection("users").document(uid).collection("rewards").document()
        val voucherCode = "DV-" + UUID.randomUUID().toString().take(6).uppercase()

        val payload = mapOf(
            "id" to voucherRef.id,
            "userId" to uid,
            "title" to title,
            "voucherCode" to voucherCode,
            "pointsSpent" to pointsCost,
            "status" to "ACTIVE",
            "createdAt" to FieldValue.serverTimestamp()
        )

        return try {
            voucherRef.set(payload).await()
            updateLoyaltyPoints(uid, -pointsCost)
            true
        } catch (e: Exception) {
            handleFirestoreError(e, OperationType.CREATE, voucherRef.path)
            false
        }
    }

    // 5. Community Reviews & Guest Feedback
    fun observeReviews(): Flow<List<Review>> {
        val collectionRef = db.collection("reviews")
        return collectionRef.orderBy("createdAt", Query.Direction.DESCENDING)
            .snapshots()
            .map { it.toObjects(Review::class.java) }
            .catch { e ->
                handleFirestoreError(e as Exception, OperationType.LIST, collectionRef.path)
                emit(emptyList())
            }
    }

    suspend fun submitReview(
        authorName: String,
        rating: Long,
        foodRating: Long,
        serviceRating: Long,
        comment: String
    ): Boolean {
        val uid = requireUserId()
        val docRef = db.collection("reviews").document()
        val payload = mapOf(
            "id" to docRef.id,
            "userId" to uid,
            "authorName" to authorName,
            "rating" to rating,
            "foodRating" to foodRating,
            "serviceRating" to serviceRating,
            "comment" to comment,
            "createdAt" to FieldValue.serverTimestamp()
        )

        return try {
            docRef.set(payload).await()
            // Reward patron with 25 points for submitting constructive feedback
            updateLoyaltyPoints(uid, 25L)
            true
        } catch (e: Exception) {
            handleFirestoreError(e, OperationType.CREATE, docRef.path)
            false
        }
    }
}
