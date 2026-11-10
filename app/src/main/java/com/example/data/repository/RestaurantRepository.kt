package com.example.data.repository

import android.content.Context
import com.example.R
import com.example.data.model.CartItem
import com.example.data.model.MenuItem
import com.example.data.model.Order
import com.example.data.model.Reservation
import com.example.data.model.Review
import com.example.data.model.Reward
import com.example.data.model.SpecialOffer
import com.example.data.model.User
import com.example.util.OperationType
import com.example.util.handleFirestoreError
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.snapshots
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import java.util.UUID

class RestaurantRepository(private val db: FirebaseFirestore) {

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

    // --- Master Menu Catalog ---
    fun getMenuCatalog(): List<MenuItem> {
        return listOf(
            // Cafe Signatures
            MenuItem(
                id = "sig_almond_latte",
                name = "Almond Blueberry Latte",
                description = "House Signature. Artisanal espresso with roasted almond milk, wild blueberry reduction & velvety foam. Medium-Dark roast with smooth fruity acidity.",
                category = "Cafe Signatures",
                halfPrice = null,
                fullPrice = 220.0,
                isVeg = true,
                isSignature = true,
                spicyLevel = 0,
                preparationTimeMin = 8
            ),
            MenuItem(
                id = "bev_hot_coffee",
                name = "Hot Coffee (Artisan Drip)",
                description = "Freshly ground Arabica blend with rich crema.",
                category = "Cafe Signatures",
                halfPrice = null,
                fullPrice = 40.0,
                isVeg = true,
                isSignature = false,
                spicyLevel = 0
            ),
            MenuItem(
                id = "bev_cold_coffee",
                name = "Velora Signature Cold Coffee",
                description = "Chilled slow-brew with vanilla ice cream and chocolate drizzle.",
                category = "Cafe Signatures",
                halfPrice = null,
                fullPrice = 180.0,
                isVeg = true,
                isSignature = true,
                spicyLevel = 0
            ),
            MenuItem(
                id = "bev_hot_choco_coffee",
                name = "Hot Chocolate Coffee",
                description = "Velvety Belgian dark chocolate infused with double espresso.",
                category = "Cafe Signatures",
                halfPrice = null,
                fullPrice = 80.0,
                isVeg = true,
                isSignature = false,
                spicyLevel = 0
            ),
            MenuItem(
                id = "bev_lemon_tea",
                name = "Lemon Honey Tea",
                description = "Assam CTC tea with fresh lemon juice and organic honey.",
                category = "Cafe Signatures",
                halfPrice = null,
                fullPrice = 30.0,
                isVeg = true,
                spicyLevel = 0
            ),

            // Starters
            MenuItem(
                id = "str_paneer_chilli",
                name = "Paneer Chilli",
                description = "Crispy cottage cheese cubes tossed with capsicum, onion & dark soy chili glaze.",
                category = "Starters",
                halfPrice = 90.0,
                fullPrice = 160.0,
                isVeg = true,
                spicyLevel = 2
            ),
            MenuItem(
                id = "str_paneer_tikka",
                name = "Paneer Tikka",
                description = "Charcoal-roasted cottage cheese skewers with mustard marinade and mint chutney.",
                category = "Starters",
                halfPrice = 100.0,
                fullPrice = 180.0,
                isVeg = true,
                isSignature = true,
                spicyLevel = 1
            ),
            MenuItem(
                id = "str_baby_corn_chilli",
                name = "Baby Corn Chilli",
                description = "Golden crisp baby corn tossed in spicy schezwan wok sauce.",
                category = "Starters",
                halfPrice = 100.0,
                fullPrice = 180.0,
                isVeg = true,
                spicyLevel = 2
            ),
            MenuItem(
                id = "str_mushroom_chilli",
                name = "Mushroom Chilli",
                description = "Tender button mushrooms flash fried with green chilies and spring onions.",
                category = "Starters",
                halfPrice = 90.0,
                fullPrice = 160.0,
                isVeg = true,
                spicyLevel = 2
            ),
            MenuItem(
                id = "str_chicken_chilli",
                name = "Chicken Chilli (Boneless)",
                description = "Juicy tender chicken cubes wok-fried with bell peppers and fiery Indo-Chinese glaze.",
                category = "Starters",
                halfPrice = 120.0,
                fullPrice = 220.0,
                isVeg = false,
                isSignature = true,
                spicyLevel = 2
            ),
            MenuItem(
                id = "str_chicken_65",
                name = "Chicken 65",
                description = "South-Indian spiced crispy chicken morsels with curry leaves and mustard seeds.",
                category = "Starters",
                halfPrice = 140.0,
                fullPrice = 250.0,
                isVeg = false,
                spicyLevel = 2
            ),
            MenuItem(
                id = "str_chicken_lollipop",
                name = "Chicken Lollipop",
                description = "Frenched chicken drumettes crisp-fried with fiery schezwan dipping dip.",
                category = "Starters",
                halfPrice = 130.0,
                fullPrice = 240.0,
                isVeg = false,
                spicyLevel = 2
            ),

            // Fast Food
            MenuItem(
                id = "ff_veg_chowmein",
                name = "Veg Hakka Chowmein",
                description = "Stir-fried noodles with julienned vegetables and aromatic garlic seasoning.",
                category = "Fast Food",
                halfPrice = null,
                fullPrice = 80.0,
                isVeg = true,
                spicyLevel = 1
            ),
            MenuItem(
                id = "ff_paneer_chowmein",
                name = "Paneer Chowmein",
                description = "Wok tossed noodles loaded with marinated paneer batons and veggies.",
                category = "Fast Food",
                halfPrice = null,
                fullPrice = 120.0,
                isVeg = true,
                spicyLevel = 1
            ),
            MenuItem(
                id = "ff_chicken_chowmein",
                name = "Chicken Chowmein",
                description = "Smoky egg noodles tossed with shredded chicken, scallions and soy.",
                category = "Fast Food",
                halfPrice = null,
                fullPrice = 140.0,
                isVeg = false,
                spicyLevel = 1
            ),
            MenuItem(
                id = "ff_chicken_roll",
                name = "Kolkata Style Chicken Roll",
                description = "Flaky paratha layered with spicy grilled chicken chunks, onions and zesty lime.",
                category = "Fast Food",
                halfPrice = null,
                fullPrice = 90.0,
                isVeg = false,
                isSignature = true,
                spicyLevel = 1
            ),
            MenuItem(
                id = "ff_paneer_roll",
                name = "Paneer Tikka Roll",
                description = "Griddled flatbread wrapped around tandoori paneer and pickled onion relish.",
                category = "Fast Food",
                halfPrice = null,
                fullPrice = 80.0,
                isVeg = true,
                spicyLevel = 1
            ),

            // Indian Veg
            MenuItem(
                id = "veg_paneer_butter_masala",
                name = "Paneer Butter Masala",
                description = "Silky rich tomato gravy finished with fresh butter, cream, and tender cottage cheese.",
                category = "Indian Veg",
                halfPrice = null,
                fullPrice = 160.0,
                isVeg = true,
                isSignature = true,
                spicyLevel = 1
            ),
            MenuItem(
                id = "veg_paneer_handi",
                name = "Paneer Handi",
                description = "Slow simmered in a traditional clay handi with crushed coriander and whole spices.",
                category = "Indian Veg",
                halfPrice = 100.0,
                fullPrice = 180.0,
                isVeg = true,
                spicyLevel = 2
            ),
            MenuItem(
                id = "veg_paneer_tikka_masala",
                name = "Paneer Tikka Masala",
                description = "Char-grilled tandoori paneer simmered in an aromatic spiced tomato-onion masala.",
                category = "Indian Veg",
                halfPrice = 120.0,
                fullPrice = 220.0,
                isVeg = true,
                spicyLevel = 2
            ),
            MenuItem(
                id = "veg_mushroom_masala",
                name = "Mushroom Masala",
                description = "Fresh button mushrooms cooked in a semi-dry roasted spice gravy.",
                category = "Indian Veg",
                halfPrice = null,
                fullPrice = 165.0,
                isVeg = true,
                spicyLevel = 1
            ),
            MenuItem(
                id = "veg_mix_veg",
                name = "Special Mix Veg",
                description = "Medley of seasonal farm vegetables slow-cooked in homestyle Indian masala.",
                category = "Indian Veg",
                halfPrice = null,
                fullPrice = 140.0,
                isVeg = true,
                spicyLevel = 1
            ),

            // Indian Non-Veg
            MenuItem(
                id = "nonveg_dehati_chicken",
                name = "Dehati Chicken (Chef's Special)",
                description = "Rustic village-style slow cooked chicken with crushed garlic, whole red chilies and mustard oil.",
                category = "Indian Non-Veg",
                halfPrice = 230.0,
                fullPrice = 400.0,
                isVeg = false,
                isSignature = true,
                spicyLevel = 2
            ),
            MenuItem(
                id = "nonveg_butter_chicken",
                name = "Tandoori Butter Chicken",
                description = "Smoky tandoori chicken simmered in our signature velvet tomato butter makhani sauce.",
                category = "Indian Non-Veg",
                halfPrice = 220.0,
                fullPrice = 400.0,
                isVeg = false,
                isSignature = true,
                spicyLevel = 1
            ),
            MenuItem(
                id = "nonveg_chicken_curry",
                name = "Chicken Curry (Home Style)",
                description = "Traditional family-recipe curry with fragrant whole spices and tender chicken cuts.",
                category = "Indian Non-Veg",
                halfPrice = 110.0,
                fullPrice = 190.0,
                isVeg = false,
                spicyLevel = 2
            ),
            MenuItem(
                id = "nonveg_mutton_kosha",
                name = "Mutton Kosha",
                description = "Slow browned tender mutton cooked down in dark caramelized onion and ginger gravy.",
                category = "Indian Non-Veg",
                halfPrice = null,
                fullPrice = 270.0,
                isVeg = false,
                isSignature = true,
                spicyLevel = 2
            ),

            // Biryani & Rice
            MenuItem(
                id = "bir_chicken_biryani",
                name = "Dum Chicken Biryani",
                description = "Aromatic aged basmati rice layered with succulent chicken, saffron, mint and fried onions.",
                category = "Biryani & Rice",
                halfPrice = null,
                fullPrice = 170.0,
                isVeg = false,
                isSignature = true,
                spicyLevel = 2
            ),
            MenuItem(
                id = "bir_paneer_biryani",
                name = "Royal Paneer Biryani",
                description = "Fragrant long-grain basmati rice infused with kewra water and spiced cottage cheese cubes.",
                category = "Biryani & Rice",
                halfPrice = null,
                fullPrice = 160.0,
                isVeg = true,
                spicyLevel = 1
            ),
            MenuItem(
                id = "rice_jeera_butter",
                name = "Jeera Butter Rice",
                description = "Steamed basmati rice tempered with roasted cumin seeds and farm butter.",
                category = "Biryani & Rice",
                halfPrice = 50.0,
                fullPrice = 90.0,
                isVeg = true,
                spicyLevel = 0
            ),
            MenuItem(
                id = "rice_kashmiri_pulao",
                name = "Kashmiri Pulao",
                description = "Mildly sweet saffron basmati rice garnished with pomegranate, roasted nuts, and fruits.",
                category = "Biryani & Rice",
                halfPrice = null,
                fullPrice = 140.0,
                isVeg = true,
                spicyLevel = 0
            ),

            // Roti, Naan & Dal
            MenuItem(
                id = "bread_garlic_naan",
                name = "Tandoori Garlic Naan",
                description = "Soft leavened flatbread brushed with garlic butter and fresh coriander.",
                category = "Roti & Dal",
                halfPrice = null,
                fullPrice = 45.0,
                isVeg = true,
                spicyLevel = 0
            ),
            MenuItem(
                id = "bread_butter_naan",
                name = "Butter Naan",
                description = "Classic tandoor-baked naan with generous dollop of butter.",
                category = "Roti & Dal",
                halfPrice = null,
                fullPrice = 30.0,
                isVeg = true,
                spicyLevel = 0
            ),
            MenuItem(
                id = "bread_tandoori_butter_roti",
                name = "Tandoori Butter Roti",
                description = "Crisp whole wheat bread baked in clay tandoor and brushed with ghee.",
                category = "Roti & Dal",
                halfPrice = null,
                fullPrice = 18.0,
                isVeg = true,
                spicyLevel = 0
            ),
            MenuItem(
                id = "dal_makhani",
                name = "Dal Makhani (Overnight Simmer)",
                description = "Black lentils and kidney beans slow cooked with churned butter and sweet cream.",
                category = "Roti & Dal",
                halfPrice = null,
                fullPrice = 100.0,
                isVeg = true,
                isSignature = true,
                spicyLevel = 1
            ),
            MenuItem(
                id = "dal_tarka",
                name = "Tarka Dal (Yellow Lentils)",
                description = "Tempered toor dal with garlic, cumin, tomatoes and dried red chili.",
                category = "Roti & Dal",
                halfPrice = null,
                fullPrice = 80.0,
                isVeg = true,
                spicyLevel = 1
            ),

            // Desserts
            MenuItem(
                id = "des_matka_ice_cream",
                name = "Authentic Matka Kulfi",
                description = "Slow churned traditional ice cream served in an earthen clay pot with saffron & pistachio.",
                category = "Desserts",
                halfPrice = null,
                fullPrice = 80.0,
                isVeg = true,
                isSignature = true,
                spicyLevel = 0
            ),
            MenuItem(
                id = "des_gulab_jamun",
                name = "Warm Gulab Jamun (2 Pcs)",
                description = "Soft golden milk dumplings dipped in fragrant cardamom-rose sugar syrup.",
                category = "Desserts",
                halfPrice = null,
                fullPrice = 60.0,
                isVeg = true,
                spicyLevel = 0
            )
        )
    }

    fun getOffers(): List<SpecialOffer> {
        return listOf(
            SpecialOffer(
                id = "off_almond_latte",
                title = "Signature Coffee Pairing",
                description = "Order any Biryani or Main Course and get 50% off our house signature Almond Blueberry Latte!",
                badge = "SIGNATURE SPECIAL",
                promoCode = "ALMOND50",
                expiryDate = "Valid this week"
            ),
            SpecialOffer(
                id = "off_loyalty_boost",
                title = "Double Rewards Weekend",
                description = "Earn 20 loyalty points for every ₹10 spent on online table pre-orders and dine-in booking.",
                badge = "LOYALTY BOOST",
                promoCode = "2XPOINTS",
                expiryDate = "Ends Sunday"
            ),
            SpecialOffer(
                id = "off_tandoori_night",
                title = "Tandoor & Kebab Fest",
                description = "Complimentary Garlic Naan with every Full Portion Chicken or Paneer Starter.",
                badge = "CHEF'S PICK",
                promoCode = "NAANFEST",
                expiryDate = "Daily after 6 PM"
            )
        )
    }

    // --- User Profile & Loyalty Sync ---
    suspend fun syncUserProfile(firebaseUser: FirebaseUser): User {
        val uid = firebaseUser.uid
        val docRef = db.collection("users").document(uid)
        return try {
            val snapshot = docRef.get().await()
            if (snapshot.exists()) {
                snapshot.toObject(User::class.java) ?: User(userId = uid)
            } else {
                val newUser = User(
                    userId = uid,
                    displayName = firebaseUser.displayName ?: "Valued Guest",
                    email = firebaseUser.email ?: "",
                    photoUrl = firebaseUser.photoUrl?.toString() ?: "",
                    loyaltyPoints = 150L, // Welcome bonus points!
                    tier = "BRONZE"
                )
                val payload = mapOf(
                    "userId" to uid,
                    "displayName" to newUser.displayName,
                    "email" to newUser.email,
                    "photoUrl" to newUser.photoUrl,
                    "loyaltyPoints" to newUser.loyaltyPoints,
                    "tier" to newUser.tier,
                    "createdAt" to FieldValue.serverTimestamp(),
                    "updatedAt" to FieldValue.serverTimestamp()
                )
                docRef.set(payload).await()
                newUser
            }
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            handleFirestoreError(e, OperationType.WRITE, docRef.path)
            User(userId = uid, displayName = firebaseUser.displayName ?: "Valued Guest")
        }
    }

    fun observeUser(userId: String): Flow<User?> = flow {
        val path = "users/$userId"
        emitAll(
            db.collection("users").document(userId)
                .snapshots()
                .map { snapshot ->
                    if (snapshot.exists()) snapshot.toObject(User::class.java) else null
                }
                .catch { error ->
                    val firestoreError = generateSequence(error) { it.cause }
                        .filterIsInstance<FirebaseFirestoreException>()
                        .firstOrNull()
                    if (firestoreError == null && error is CancellationException) throw error
                    val exceptionToLog = firestoreError ?: (error as? Exception)
                    if (exceptionToLog != null && auth.currentUser?.uid == userId) {
                        handleFirestoreError(exceptionToLog, OperationType.GET, path)
                    }
                    throw error
                }
        )
    }

    // --- Reservations ---
    suspend fun createReservation(reservation: Reservation): String {
        val uid = requireUserId()
        val docRef = db.collection("users").document(uid).collection("reservations").document()
        val payload = mapOf(
            "userId" to uid,
            "guestName" to reservation.guestName,
            "guestPhone" to reservation.guestPhone,
            "partySize" to reservation.partySize,
            "seatingArea" to reservation.seatingArea,
            "date" to reservation.date,
            "timeSlot" to reservation.timeSlot,
            "specialRequests" to reservation.specialRequests,
            "status" to "CONFIRMED",
            "createdAt" to FieldValue.serverTimestamp()
        )
        try {
            docRef.set(payload).await()
            // Add bonus loyalty points for booking a table!
            addLoyaltyPoints(uid, 50L)
            return docRef.id
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            handleFirestoreError(e, OperationType.CREATE, docRef.path)
            throw e
        }
    }

    fun observeReservations(userId: String): Flow<List<Reservation>> = flow {
        val path = "users/$userId/reservations"
        emitAll(
            db.collection("users").document(userId).collection("reservations")
                .snapshots()
                .map { snapshot -> snapshot.toObjects(Reservation::class.java) }
                .catch { error ->
                    val firestoreError = generateSequence(error) { it.cause }
                        .filterIsInstance<FirebaseFirestoreException>()
                        .firstOrNull()
                    if (firestoreError == null && error is CancellationException) throw error
                    val exceptionToLog = firestoreError ?: (error as? Exception)
                    if (exceptionToLog != null && auth.currentUser?.uid == userId) {
                        handleFirestoreError(exceptionToLog, OperationType.LIST, path)
                    }
                    throw error
                }
        )
    }

    // --- Orders & Checkout ---
    suspend fun placeOrder(
        cartItems: List<CartItem>,
        orderType: String,
        paymentMethod: String
    ): Order {
        val uid = requireUserId()
        val totalAmount = cartItems.sumOf { it.totalPrice }
        val itemsSummary = cartItems.joinToString(", ") { "${it.quantity}x ${it.item.name} (${if (it.isHalfPortion) "Half" else "Full"})" }
        val pointsEarned = (totalAmount / 10).toLong()

        val docRef = db.collection("users").document(uid).collection("orders").document()
        val payload = mapOf(
            "userId" to uid,
            "itemsSummary" to itemsSummary,
            "itemCount" to cartItems.sumOf { it.quantity }.toLong(),
            "totalAmount" to totalAmount,
            "orderType" to orderType,
            "paymentMethod" to paymentMethod,
            "paymentStatus" to "PAID",
            "orderStatus" to "PREPARING",
            "pointsEarned" to pointsEarned,
            "createdAt" to FieldValue.serverTimestamp()
        )
        try {
            docRef.set(payload).await()
            addLoyaltyPoints(uid, pointsEarned)
            return Order(
                id = docRef.id,
                userId = uid,
                itemsSummary = itemsSummary,
                itemCount = cartItems.sumOf { it.quantity }.toLong(),
                totalAmount = totalAmount,
                orderType = orderType,
                paymentMethod = paymentMethod,
                paymentStatus = "PAID",
                orderStatus = "PREPARING",
                pointsEarned = pointsEarned
            )
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            handleFirestoreError(e, OperationType.CREATE, docRef.path)
            throw e
        }
    }

    fun observeOrders(userId: String): Flow<List<Order>> = flow {
        val path = "users/$userId/orders"
        emitAll(
            db.collection("users").document(userId).collection("orders")
                .snapshots()
                .map { snapshot -> snapshot.toObjects(Order::class.java) }
                .catch { error ->
                    val firestoreError = generateSequence(error) { it.cause }
                        .filterIsInstance<FirebaseFirestoreException>()
                        .firstOrNull()
                    if (firestoreError == null && error is CancellationException) throw error
                    val exceptionToLog = firestoreError ?: (error as? Exception)
                    if (exceptionToLog != null && auth.currentUser?.uid == userId) {
                        handleFirestoreError(exceptionToLog, OperationType.LIST, path)
                    }
                    throw error
                }
        )
    }

    // --- Loyalty Points & Rewards ---
    private suspend fun addLoyaltyPoints(userId: String, pointsToAdd: Long) {
        val userDoc = db.collection("users").document(userId)
        try {
            val snapshot = userDoc.get().await()
            val currentPoints = snapshot.getLong("loyaltyPoints") ?: 0L
            val updatedPoints = currentPoints + pointsToAdd
            val updatedTier = when {
                updatedPoints >= 1000L -> "PLATINUM"
                updatedPoints >= 500L -> "GOLD"
                updatedPoints >= 250L -> "SILVER"
                else -> "BRONZE"
            }
            userDoc.update(
                mapOf(
                    "loyaltyPoints" to updatedPoints,
                    "tier" to updatedTier,
                    "updatedAt" to FieldValue.serverTimestamp()
                )
            ).await()
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            handleFirestoreError(e, OperationType.UPDATE, userDoc.path)
        }
    }

    suspend fun redeemReward(rewardTitle: String, pointsCost: Long): String {
        val uid = requireUserId()
        val userDoc = db.collection("users").document(uid)
        val voucherCode = "DV-" + UUID.randomUUID().toString().substring(0, 8).uppercase()
        val rewardDoc = userDoc.collection("rewards").document()

        val payload = mapOf(
            "userId" to uid,
            "rewardTitle" to rewardTitle,
            "pointsCost" to pointsCost,
            "voucherCode" to voucherCode,
            "isUsed" to false,
            "createdAt" to FieldValue.serverTimestamp()
        )
        try {
            rewardDoc.set(payload).await()
            val snapshot = userDoc.get().await()
            val currentPoints = snapshot.getLong("loyaltyPoints") ?: 0L
            val newPoints = (currentPoints - pointsCost).coerceAtLeast(0L)
            userDoc.update(
                mapOf(
                    "loyaltyPoints" to newPoints,
                    "updatedAt" to FieldValue.serverTimestamp()
                )
            ).await()
            return voucherCode
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            handleFirestoreError(e, OperationType.CREATE, rewardDoc.path)
            throw e
        }
    }

    fun observeRewards(userId: String): Flow<List<Reward>> = flow {
        val path = "users/$userId/rewards"
        emitAll(
            db.collection("users").document(userId).collection("rewards")
                .snapshots()
                .map { snapshot -> snapshot.toObjects(Reward::class.java) }
                .catch { error ->
                    val firestoreError = generateSequence(error) { it.cause }
                        .filterIsInstance<FirebaseFirestoreException>()
                        .firstOrNull()
                    if (firestoreError == null && error is CancellationException) throw error
                    val exceptionToLog = firestoreError ?: (error as? Exception)
                    if (exceptionToLog != null && auth.currentUser?.uid == userId) {
                        handleFirestoreError(exceptionToLog, OperationType.LIST, path)
                    }
                    throw error
                }
        )
    }

    // --- Reviews & Feedback ---
    suspend fun submitReview(
        authorName: String,
        rating: Long,
        foodRating: Long,
        serviceRating: Long,
        ambianceRating: Long,
        comment: String
    ) {
        val uid = requireUserId()
        val reviewDoc = db.collection("reviews").document()
        val payload = mapOf(
            "userId" to uid,
            "authorName" to authorName,
            "rating" to rating,
            "foodRating" to foodRating,
            "serviceRating" to serviceRating,
            "ambianceRating" to ambianceRating,
            "comment" to comment,
            "createdAt" to FieldValue.serverTimestamp()
        )
        try {
            reviewDoc.set(payload).await()
            // Reward patron with 25 points for submitting a verified review!
            addLoyaltyPoints(uid, 25L)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            handleFirestoreError(e, OperationType.CREATE, reviewDoc.path)
            throw e
        }
    }

    fun observeReviews(): Flow<List<Review>> = flow {
        val path = "reviews"
        emitAll(
            db.collection("reviews")
                .snapshots()
                .map { snapshot -> snapshot.toObjects(Review::class.java) }
                .catch { error ->
                    val firestoreError = generateSequence(error) { it.cause }
                        .filterIsInstance<FirebaseFirestoreException>()
                        .firstOrNull()
                    if (firestoreError == null && error is CancellationException) throw error
                    val exceptionToLog = firestoreError ?: (error as? Exception)
                    if (exceptionToLog != null) {
                        handleFirestoreError(exceptionToLog, OperationType.LIST, path)
                    }
                    throw error
                }
        )
    }
}
