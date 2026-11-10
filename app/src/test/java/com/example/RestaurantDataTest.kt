package com.example

import com.example.data.model.CartItem
import com.example.data.model.MenuItem
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class RestaurantDataTest {

    @Test
    fun testAlmondBlueberryLatteIsPresent() {
        val latte = MenuItem(
            id = "sig_almond_latte",
            name = "Almond Blueberry Latte",
            description = "House Signature",
            category = "Cafe Signatures",
            halfPrice = null,
            fullPrice = 220.0,
            isVeg = true,
            isSignature = true
        )
        assertEquals("Almond Blueberry Latte", latte.name)
        assertTrue(latte.isSignature)
        assertEquals(220.0, latte.fullPrice, 0.01)
    }

    @Test
    fun testCartCalculationHalfVsFull() {
        val paneerTikka = MenuItem(
            id = "str_paneer_tikka",
            name = "Paneer Tikka",
            description = "Spicy tandoori",
            category = "Starters",
            halfPrice = 100.0,
            fullPrice = 180.0
        )

        val halfCart = CartItem(item = paneerTikka, isHalfPortion = true, quantity = 2)
        assertEquals(200.0, halfCart.totalPrice, 0.01)

        val fullCart = CartItem(item = paneerTikka, isHalfPortion = false, quantity = 1)
        assertEquals(180.0, fullCart.totalPrice, 0.01)
    }
}
