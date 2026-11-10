package com.example.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.AiConciergeRepository
import com.example.data.FirestoreRepository
import com.example.data.MenuDataSource
import com.example.model.CartItem
import com.example.model.DietaryType
import com.example.model.MenuCategory
import com.example.model.MenuItem
import com.example.model.Order
import com.example.model.PortionSelection
import com.example.model.Reservation
import com.example.model.Review
import com.example.model.RewardVoucher
import com.example.model.SpecialOffer
import com.example.model.UserProfile
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

enum class AppTab(val title: String) {
    HOME("Home"),
    MENU("Digital Menu"),
    RESERVE("Book Table"),
    REWARDS("Rewards"),
    COMMUNITY("Concierge & Reviews")
}

data class ChatMessage(
    val sender: String, // "user" or "chef"
    val message: String,
    val isThinking: Boolean = false
)

class MainViewModel(
    private val userId: String,
    private val firestoreRepository: FirestoreRepository,
    private val aiConciergeRepository: AiConciergeRepository = AiConciergeRepository()
) : ViewModel() {

    // Tab navigation
    private val _selectedTab = MutableStateFlow(AppTab.HOME)
    val selectedTab: StateFlow<AppTab> = _selectedTab.asStateFlow()

    fun selectTab(tab: AppTab) {
        _selectedTab.value = tab
    }

    // User profile observation from Firestore
    val userProfile: StateFlow<UserProfile?> = firestoreRepository.observeUserProfile(userId)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = null
        )

    // Reservations observation
    val reservations: StateFlow<List<Reservation>> = firestoreRepository.observeReservations(userId)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = emptyList()
        )

    // Orders observation
    val orders: StateFlow<List<Order>> = firestoreRepository.observeOrders(userId)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = emptyList()
        )

    // Vouchers observation
    val vouchers: StateFlow<List<RewardVoucher>> = firestoreRepository.observeVouchers(userId)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = emptyList()
        )

    // Community reviews
    val reviews: StateFlow<List<Review>> = firestoreRepository.observeReviews()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = emptyList()
        )

    // Digital Menu State
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _selectedCategory = MutableStateFlow<MenuCategory?>(null)
    val selectedCategory: StateFlow<MenuCategory?> = _selectedCategory.asStateFlow()

    private val _selectedDietaryFilter = MutableStateFlow<DietaryType?>(null)
    val selectedDietaryFilter: StateFlow<DietaryType?> = _selectedDietaryFilter.asStateFlow()

    val filteredMenuItems: StateFlow<List<MenuItem>> = combine(
        _searchQuery,
        _selectedCategory,
        _selectedDietaryFilter
    ) { query, category, dietary ->
        MenuDataSource.menuItems.filter { item ->
            val matchesQuery = query.isBlank() ||
                    item.name.contains(query, ignoreCase = true) ||
                    item.description.contains(query, ignoreCase = true)
            val matchesCategory = category == null || item.category == category
            val matchesDietary = dietary == null || item.dietaryType == dietary
            matchesQuery && matchesCategory && matchesDietary
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000L),
        initialValue = MenuDataSource.menuItems
    )

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun selectCategory(category: MenuCategory?) {
        _selectedCategory.value = if (_selectedCategory.value == category) null else category
    }

    fun selectDietaryFilter(dietary: DietaryType?) {
        _selectedDietaryFilter.value = if (_selectedDietaryFilter.value == dietary) null else dietary
    }

    // Cart State
    private val _cartItems = MutableStateFlow<List<CartItem>>(emptyList())
    val cartItems: StateFlow<List<CartItem>> = _cartItems.asStateFlow()

    val cartTotalCount: StateFlow<Int> = _cartItems.combine(_cartItems) { items, _ ->
        items.sumOf { it.quantity }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), 0)

    val cartTotalPrice: StateFlow<Int> = _cartItems.combine(_cartItems) { items, _ ->
        items.sumOf { it.totalPrice }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), 0)

    fun addToCart(item: MenuItem, portion: PortionSelection = PortionSelection.SINGLE) {
        val current = _cartItems.value.toMutableList()
        val index = current.indexOfFirst { it.item.id == item.id && it.portion == portion }
        if (index != -1) {
            val existing = current[index]
            current[index] = existing.copy(quantity = existing.quantity + 1)
        } else {
            current.add(CartItem(item = item, portion = portion, quantity = 1))
        }
        _cartItems.value = current
    }

    fun updateCartItemQuantity(cartItem: CartItem, delta: Int) {
        val current = _cartItems.value.toMutableList()
        val index = current.indexOfFirst { it.item.id == cartItem.item.id && it.portion == cartItem.portion }
        if (index != -1) {
            val newQty = current[index].quantity + delta
            if (newQty <= 0) {
                current.removeAt(index)
            } else {
                current[index] = current[index].copy(quantity = newQty)
            }
            _cartItems.value = current
        }
    }

    fun clearCart() {
        _cartItems.value = emptyList()
    }

    // Checkout Flow
    private val _isSubmittingOrder = MutableStateFlow(false)
    val isSubmittingOrder: StateFlow<Boolean> = _isSubmittingOrder.asStateFlow()

    private val _orderSuccessMessage = MutableStateFlow<String?>(null)
    val orderSuccessMessage: StateFlow<String?> = _orderSuccessMessage.asStateFlow()

    fun submitOrder(orderType: String, paymentMethod: String, onComplete: () -> Unit) {
        val items = _cartItems.value
        if (items.isEmpty()) return

        val totalAmount = items.sumOf { it.totalPrice }.toLong()
        val summary = items.joinToString(", ") { "${it.quantity}x ${it.item.name} (${it.portion.name})" }

        viewModelScope.launch {
            _isSubmittingOrder.value = true
            val success = firestoreRepository.placeOrder(
                itemSummary = summary,
                totalAmount = totalAmount,
                orderType = orderType,
                paymentMethod = paymentMethod
            )
            _isSubmittingOrder.value = false
            if (success) {
                clearCart()
                _orderSuccessMessage.value = "Order placed successfully! Earned ${(totalAmount / 10).coerceAtLeast(10)} loyalty points."
                onComplete()
            }
        }
    }

    fun dismissOrderSuccess() {
        _orderSuccessMessage.value = null
    }

    // Table Reservation Submission
    private val _isSubmittingReservation = MutableStateFlow(false)
    val isSubmittingReservation: StateFlow<Boolean> = _isSubmittingReservation.asStateFlow()

    private val _reservationSuccessMessage = MutableStateFlow<String?>(null)
    val reservationSuccessMessage: StateFlow<String?> = _reservationSuccessMessage.asStateFlow()

    fun bookTable(
        guestName: String,
        guestPhone: String,
        partySize: Long,
        seatingArea: String,
        date: String,
        timeSlot: String,
        specialRequests: String?,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            _isSubmittingReservation.value = true
            val success = firestoreRepository.createReservation(
                guestName = guestName,
                guestPhone = guestPhone,
                partySize = partySize,
                seatingArea = seatingArea,
                date = date,
                timeSlot = timeSlot,
                specialRequests = specialRequests
            )
            _isSubmittingReservation.value = false
            if (success) {
                _reservationSuccessMessage.value = "Table booked for $guestName on $date at $timeSlot! +50 Loyalty Points credited."
                onSuccess()
            }
        }
    }

    fun cancelReservation(reservationId: String) {
        viewModelScope.launch {
            firestoreRepository.cancelReservation(reservationId)
        }
    }

    fun dismissReservationSuccess() {
        _reservationSuccessMessage.value = null
    }

    // Rewards Redemption
    private val _redemptionMessage = MutableStateFlow<String?>(null)
    val redemptionMessage: StateFlow<String?> = _redemptionMessage.asStateFlow()

    fun redeemCatalogReward(title: String, pointsCost: Long) {
        viewModelScope.launch {
            val success = firestoreRepository.redeemReward(title, pointsCost)
            if (success) {
                _redemptionMessage.value = "Voucher created for $title! Find it in your Active Vouchers."
            } else {
                _redemptionMessage.value = "Insufficient points to claim this reward."
            }
        }
    }

    fun dismissRedemptionMessage() {
        _redemptionMessage.value = null
    }

    // Review Submission
    private val _isSubmittingReview = MutableStateFlow(false)
    val isSubmittingReview: StateFlow<Boolean> = _isSubmittingReview.asStateFlow()

    fun submitReview(
        authorName: String,
        rating: Long,
        foodRating: Long,
        serviceRating: Long,
        comment: String,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            _isSubmittingReview.value = true
            val success = firestoreRepository.submitReview(
                authorName = authorName,
                rating = rating,
                foodRating = foodRating,
                serviceRating = serviceRating,
                comment = comment
            )
            _isSubmittingReview.value = false
            if (success) {
                onSuccess()
            }
        }
    }

    // AI Culinary Concierge & Sommelier
    private val _chatMessages = MutableStateFlow<List<ChatMessage>>(
        listOf(
            ChatMessage(
                sender = "chef",
                message = "Welcome to D Velora! I am Chef Velora, your AI culinary sommelier. Would you like a personalized food & coffee pairing, table seating recommendations, or directions to our restaurant?"
            )
        )
    )
    val chatMessages: StateFlow<List<ChatMessage>> = _chatMessages.asStateFlow()

    private val _isAiThinking = MutableStateFlow(false)
    val isAiThinking: StateFlow<Boolean> = _isAiThinking.asStateFlow()

    private val _highThinkingMode = MutableStateFlow(true)
    val highThinkingMode: StateFlow<Boolean> = _highThinkingMode.asStateFlow()

    fun toggleHighThinking(enabled: Boolean) {
        _highThinkingMode.value = enabled
    }

    fun askChefVelora(query: String) {
        if (query.isBlank()) return
        val userMsg = ChatMessage(sender = "user", message = query)
        _chatMessages.value = _chatMessages.value + userMsg
        _isAiThinking.value = true

        viewModelScope.launch {
            val responseText = aiConciergeRepository.getCulinaryAdvice(
                userPrompt = query,
                useDeepThinking = _highThinkingMode.value
            )
            _isAiThinking.value = false
            _chatMessages.value = _chatMessages.value + ChatMessage(sender = "chef", message = responseText)
        }
    }

    // Notifications Center
    val offers: List<SpecialOffer> = MenuDataSource.specialOffers
}
