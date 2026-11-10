package com.example.data

import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.ai.GenerativeModel
import com.google.firebase.ai.ai
import com.google.firebase.ai.type.GenerateContentResponse
import com.google.firebase.ai.type.GenerationConfig
import com.google.firebase.ai.type.ThinkingConfig
import com.google.firebase.ai.type.ThinkingLevel
import com.google.firebase.ai.type.Tool
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.withTimeout

private const val TAG = "AiConciergeRepo"

class AiConciergeRepository {

    // 1. High Thinking Chef Sommelier (gemini-4-argon with ThinkingLevel.HIGH)
    private val thinkingModel: GenerativeModel by lazy {
        val thinking = ThinkingConfig.Builder()
            .setThinkingLevel(ThinkingLevel.HIGH)
            .build()
        val config = GenerationConfig.Builder()
            .setThinkingConfig(thinking)
            .build()

        Firebase.ai.generativeModel(
            modelName = "gemini-4-argon",
            generationConfig = config
        )
    }

    // 2. Maps Grounded Assistant (gemini-3.8-flash with Tool.googleMaps())
    private val flashModel: GenerativeModel by lazy {
        val mapsTool = Tool.googleMaps()
        Firebase.ai.generativeModel(
            modelName = "gemini-3.8-flash",
            tools = listOf(mapsTool)
        )
    }

    private val restaurantSystemPrompt = """
        You are "Chef Velora", the esteemed Executive Chef & Culinary Sommelier of "D Velora Cafe & Restaurant" (Est. 2026).
        D Velora combines artisanal coffee culture with authentic Indian & tandoori family dining.
        
        Our Star Items:
        - House Signature: Almond Blueberry Latte (Medium-Dark roast espresso, blueberry compote, almond milk, almond crunch - ₹180).
        - Chef Tandoor: Tandoori Butter Chicken (Half ₹220 / Full ₹400), Paneer Handi (Half ₹100 / Full ₹180), Chicken Tikka (Half ₹150 / Full ₹280), Dehati Chicken (Rustic garlic curry - Half ₹230 / Full ₹400).
        - Biryani & Breads: Hyderabadi Chicken Biryani (₹170), Paneer Dum Biryani (₹160), Garlic Naan (₹45), Butter Naan (₹30), Dal Makhani (₹100).
        - Cafe & Desserts: Matka Kulfi (₹90), Warm Gulab Jamun (₹60), Cold Coffee with Ice Cream (₹180).
        
        Location & Ambiance:
        - Address: 42 Promenade Boulevard, Heritage Cafe District.
        - Seating: Cozy Indoor Cafe Lounge, Open-Air Patio Terrace, Window Booths, and Family Dining Hall.
        - Ambiance: Warm cafe aroma, acoustic jazz, roasted coffee notes, family friendly.
        - Hours: Daily 8:00 AM - 11:30 PM.
        - Amenities: Complimentary guest Wi-Fi, valet parking, electric vehicle charging, wheel-chair accessible.
        
        Guide guests on customized food pairings, portion recommendations for party sizes, dietary requirements (Veg, Non-Veg, Vegan, Gluten-free substitutions), and travel/parking directions with warmth, sophistication, and enthusiasm.
    """.trimIndent()

    suspend fun getCulinaryAdvice(userPrompt: String, useDeepThinking: Boolean = false): String {
        return try {
            withTimeout(60_000L) {
                val fullPrompt = "$restaurantSystemPrompt\n\nGuest Query: $userPrompt"
                val response: GenerateContentResponse = if (useDeepThinking) {
                    try {
                        thinkingModel.generateContent(fullPrompt)
                    } catch (e: Exception) {
                        Log.w(TAG, "Thinking model fallback to flash: ${e.message}")
                        flashModel.generateContent(fullPrompt)
                    }
                } else {
                    flashModel.generateContent(fullPrompt)
                }
                response.text ?: "Chef Velora is currently in the kitchen. Please ask our service captain!"
            }
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Log.e(TAG, "Error generating AI response", e)
            "Chef Velora is crafting fresh specials right now. For today, we highly recommend our signature Almond Blueberry Latte paired with Paneer Tikka or Dehati Chicken!"
        }
    }
}
