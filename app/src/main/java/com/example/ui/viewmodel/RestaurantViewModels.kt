package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.model.CartItem
import com.example.data.model.MenuItem
import com.example.data.model.Order
import com.example.data.model.Reservation
import com.example.data.model.Review
import com.example.data.model.Reward
import com.example.data.model.SpecialOffer
import com.example.data.model.User
import com.example.data.repository.AiConciergeRepository
import com.example.data.repository.ChatMessage
import com.example.data.repository.RestaurantRepository
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID

class MenuViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = RestaurantRepository(application)

    val menuItems: List<MenuItem> = repository.getMenuCatalog()
    val offers: List<SpecialOffer> = repository.getOffers()

    val categories: List<String> = listOf(
        "All Items",
        "Cafe Signatures",
        "Starters",
        "Fast Food",
        "Indian Veg",
        "Indian Non-Veg",
        "Biryani & Rice",
        "Roti & Dal",
        "Desserts"
    )

    private val _selectedCategory = MutableStateFlow("All Items")
    val selectedCategory: StateFlow<String> = _selectedCategory.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _vegFilterOnly = MutableStateFlow(false)
    val vegFilterOnly: StateFlow<Boolean> = _vegFilterOnly.asStateFlow()

    // Shopping Cart State
    private val _cartItems = MutableStateFlow<List<CartItem>>(emptyList())
    val cartItems: StateFlow<List<CartItem>> = _cartItems.asStateFlow()

    private val _orderPlacementState = MutableStateFlow<OrderPlacementState>(OrderPlacementState.Idle)
    val orderPlacementState: StateFlow<OrderPlacementState> = _orderPlacementState.asStateFlow()

    fun selectCategory(category: String) {
        _selectedCategory.value = category
    }

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun toggleVegFilter() {
        _vegFilterOnly.value = !_vegFilterOnly.value
    }

    fun addToCart(item: MenuItem, isHalfPortion: Boolean = false) {
        val current = _cartItems.value.toMutableList()
        val existingIndex = current.indexOfFirst { it.item.id == item.id && it.isHalfPortion == isHalfPortion }
        if (existingIndex >= 0) {
            val existing = current[existingIndex]
            current[existingIndex] = existing.copy(quantity = existing.quantity + 1)
        } else {
            current.add(CartItem(item = item, isHalfPortion = isHalfPortion, quantity = 1))
        }
        _cartItems.value = current
    }

    fun updateQuantity(cartItem: CartItem, delta: Int) {
        val current = _cartItems.value.toMutableList()
        val index = current.indexOfFirst { it.item.id == cartItem.item.id && it.isHalfPortion == cartItem.isHalfPortion }
        if (index >= 0) {
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

    fun checkoutOrder(orderType: String, paymentMethod: String) {
        if (_cartItems.value.isEmpty()) return
        viewModelScope.launch {
            _orderPlacementState.value = OrderPlacementState.Submitting
            try {
                val order = repository.placeOrder(_cartItems.value, orderType, paymentMethod)
                _cartItems.value = emptyList()
                _orderPlacementState.value = OrderPlacementState.Success(order)
            } catch (e: Exception) {
                _orderPlacementState.value = OrderPlacementState.Error(e.localizedMessage ?: "Failed to place order")
            }
        }
    }

    fun resetOrderState() {
        _orderPlacementState.value = OrderPlacementState.Idle
    }
}

sealed interface OrderPlacementState {
    data object Idle : OrderPlacementState
    data object Submitting : OrderPlacementState
    data class Success(val order: Order) : OrderPlacementState
    data class Error(val message: String) : OrderPlacementState
}

class ReservationViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = RestaurantRepository(application)
    private val auth = Firebase.auth

    private val _reservationState = MutableStateFlow<ReservationState>(ReservationState.Idle)
    val reservationState: StateFlow<ReservationState> = _reservationState.asStateFlow()

    val userReservations: StateFlow<List<Reservation>> = auth.currentUser?.uid?.let { uid ->
        repository.observeReservations(uid).stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = emptyList()
        )
    } ?: MutableStateFlow(emptyList())

    fun bookTable(
        guestName: String,
        guestPhone: String,
        partySize: Long,
        seatingArea: String,
        date: String,
        timeSlot: String,
        specialRequests: String
    ) {
        viewModelScope.launch {
            _reservationState.value = ReservationState.Submitting
            try {
                val reservation = Reservation(
                    guestName = guestName,
                    guestPhone = guestPhone,
                    partySize = partySize,
                    seatingArea = seatingArea,
                    date = date,
                    timeSlot = timeSlot,
                    specialRequests = specialRequests
                )
                val id = repository.createReservation(reservation)
                _reservationState.value = ReservationState.Success(id)
            } catch (e: Exception) {
                _reservationState.value = ReservationState.Error(e.localizedMessage ?: "Reservation failed")
            }
        }
    }

    fun resetState() {
        _reservationState.value = ReservationState.Idle
    }
}

sealed interface ReservationState {
    data object Idle : ReservationState
    data object Submitting : ReservationState
    data class Success(val reservationId: String) : ReservationState
    data class Error(val message: String) : ReservationState
}

class RewardsViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = RestaurantRepository(application)
    private val auth = Firebase.auth

    val userProfile: StateFlow<User?> = auth.currentUser?.uid?.let { uid ->
        repository.observeUser(uid).stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = null
        )
    } ?: MutableStateFlow(null)

    val userRewards: StateFlow<List<Reward>> = auth.currentUser?.uid?.let { uid ->
        repository.observeRewards(uid).stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = emptyList()
        )
    } ?: MutableStateFlow(emptyList())

    val userOrders: StateFlow<List<Order>> = auth.currentUser?.uid?.let { uid ->
        repository.observeOrders(uid).stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = emptyList()
        )
    } ?: MutableStateFlow(emptyList())

    private val _redeemState = MutableStateFlow<RedeemState>(RedeemState.Idle)
    val redeemState: StateFlow<RedeemState> = _redeemState.asStateFlow()

    fun redeemReward(rewardTitle: String, pointsCost: Long) {
        viewModelScope.launch {
            _redeemState.value = RedeemState.Redeeming
            try {
                val code = repository.redeemReward(rewardTitle, pointsCost)
                _redeemState.value = RedeemState.Success(code)
            } catch (e: Exception) {
                _redeemState.value = RedeemState.Error(e.localizedMessage ?: "Failed to redeem reward")
            }
        }
    }

    fun clearRedeemState() {
        _redeemState.value = RedeemState.Idle
    }
}

sealed interface RedeemState {
    data object Idle : RedeemState
    data object Redeeming : RedeemState
    data class Success(val voucherCode: String) : RedeemState
    data class Error(val message: String) : RedeemState
}

class ConciergeViewModel : AndroidViewModel(Application()) {
    private val repository = AiConciergeRepository()

    private val _messages = MutableStateFlow<List<ChatMessage>>(
        listOf(
            ChatMessage(
                id = "welcome_1",
                sender = "CHEF",
                text = "Welcome to D Velora Cafe & Restaurant! I am Chef Velora. How may I assist your palate today? Ask me about our signature Almond Blueberry Latte, dinner pairings, table bookings, or directions!"
            )
        )
    )
    val messages: StateFlow<List<ChatMessage>> = _messages.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    fun sendMessage(userText: String, useThinking: Boolean = true) {
        if (userText.isBlank()) return
        val userMsg = ChatMessage(id = UUID.randomUUID().toString(), sender = "USER", text = userText)
        val current = _messages.value.toMutableList()
        current.add(userMsg)
        _messages.value = current

        viewModelScope.launch {
            _isLoading.value = true
            val reply = repository.getChefRecommendation(userText, useThinking)
            val chefMsg = ChatMessage(id = UUID.randomUUID().toString(), sender = "CHEF", text = reply)
            val updated = _messages.value.toMutableList()
            updated.add(chefMsg)
            _messages.value = updated
            _isLoading.value = false
        }
    }
}

class ReviewsViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = RestaurantRepository(application)

    val reviews: StateFlow<List<Review>> = repository.observeReviews().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000L),
        initialValue = emptyList()
    )

    private val _submitState = MutableStateFlow<ReviewSubmitState>(ReviewSubmitState.Idle)
    val submitState: StateFlow<ReviewSubmitState> = _submitState.asStateFlow()

    fun submitFeedback(
        authorName: String,
        rating: Long,
        foodRating: Long,
        serviceRating: Long,
        ambianceRating: Long,
        comment: String
    ) {
        viewModelScope.launch {
            _submitState.value = ReviewSubmitState.Submitting
            try {
                repository.submitReview(
                    authorName = authorName,
                    rating = rating,
                    foodRating = foodRating,
                    serviceRating = serviceRating,
                    ambianceRating = ambianceRating,
                    comment = comment
                )
                _submitState.value = ReviewSubmitState.Success
            } catch (e: Exception) {
                _submitState.value = ReviewSubmitState.Error(e.localizedMessage ?: "Failed to submit review")
            }
        }
    }

    fun resetSubmitState() {
        _submitState.value = ReviewSubmitState.Idle
    }
}

sealed interface ReviewSubmitState {
    data object Idle : ReviewSubmitState
    data object Submitting : ReviewSubmitState
    data object Success : ReviewSubmitState
    data class Error(val message: String) : ReviewSubmitState
}
