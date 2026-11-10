package com.example.data.repository

import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.ai.ai
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.withTimeout

private const val TAG = "AiConciergeRepo"

data class ChatMessage(
    val id: String,
    val sender: String, // "USER" or "CHEF"
    val text: String,
    val timestamp: Long = System.currentTimeMillis(),
    val isThinking: Boolean = false
)

class AiConciergeRepository {

    // Lazy initialization as required by Firebase AI Logic
    private val thinkingModel by lazy {
        try {
            Firebase.ai.generativeModel("gemini-4-argon")
        } catch (e: Exception) {
            Log.w(TAG, "Falling back to gemini-3.8-flash for thinking model: ${e.message}")
            Firebase.ai.generativeModel("gemini-3.8-flash")
        }
    }

    private val mapsGroundingModel by lazy {
        Firebase.ai.generativeModel("gemini-3.8-flash")
    }

    private val restaurantContext = """
        You are "Chef Velora", the esteemed Head Chef and Culinary Sommelier of "D Velora Cafe & Restaurant" (Est. 2026).
        Atmosphere: Warm luxury artisanal cafe by day, vibrant authentic family restaurant by evening.
        House Signature Drink: Almond Blueberry Latte (Medium-Dark roast, roasted almond milk, wild blueberry reduction, smooth fruity acidity).
        Key Specialties:
        - Starters: Paneer Tikka (charcoal roasted), Chicken Chilli Boneless, Chicken 65, Chicken Lollipop, Paneer Chilli, Baby Corn Chilli.
        - Fast Food: Kolkata Style Chicken Roll, Veg Hakka Chowmein, Paneer Roll.
        - Main Course Veg: Paneer Butter Masala (silky rich), Paneer Handi, Paneer Tikka Masala, Mushroom Masala.
        - Main Course Non-Veg: Dehati Chicken (Chef's rustic signature with whole spices and mustard oil), Tandoori Butter Chicken, Mutton Kosha.
        - Biryani & Breads: Dum Chicken Biryani, Royal Paneer Biryani, Garlic Naan, Dal Makhani (overnight simmer).
        - Desserts: Authentic Matka Kulfi in clay pot, Warm Gulab Jamun.
        Location & Seating: Indoor Dining Hall, Patio Terrace, Window Booths, Private Family Dining.
        Give warm, expert, gracious answers. Highlight portion advice (Half vs Full), food pairings, allergy guidance, and reservation tips.
    """.trimIndent()

    suspend fun getChefRecommendation(userPrompt: String, useDeepThinking: Boolean = true): String {
        return try {
            withTimeout(60_000L) {
                val model = if (useDeepThinking) thinkingModel else mapsGroundingModel
                val prompt = "$restaurantContext\n\nGuest Query: $userPrompt\nChef Velora Response:"
                val response = model.generateContent(prompt)
                response.text ?: "Chef Velora recommends our signature Almond Blueberry Latte paired with Paneer Tikka or Dehati Chicken."
            }
        } catch (e: TimeoutCancellationException) {
            Log.e(TAG, "Chef Velora AI response timed out", e)
            "Chef Velora is currently busy in the kitchen crafting fresh brews. Please try your question again in a moment!"
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Log.e(TAG, "AI Concierge error", e)
            "Welcome to D Velora! Today we especially recommend our House Signature Almond Blueberry Latte alongside our Dum Chicken Biryani or Paneer Butter Masala."
        }
    }
}
