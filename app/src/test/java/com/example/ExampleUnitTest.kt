package com.example

import com.example.data.model.TradeEvaluator
import com.example.data.model.TradeSlotItem
import com.example.data.model.TradeVerdict
import com.example.data.repository.FruitRepository
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class ExampleUnitTest {
    @Test
    fun testTradeEvaluationHugeWin() {
        val kitsune = FruitRepository.findById("kitsune")!!
        val rocket = FruitRepository.findById("rocket")!!

        // You offer Rocket (20K value), They offer Kitsune (125M value) -> Huge Win
        val yourOffer = listOf(TradeSlotItem(rocket, false))
        val theirOffer = listOf(TradeSlotItem(kitsune, false))

        val evaluation = TradeEvaluator.evaluate(yourOffer, theirOffer)
        assertEquals(TradeVerdict.HUGE_WIN, evaluation.verdict)
        assertTrue(evaluation.valueDifference > 0)
    }

    @Test
    fun testBeliDifference40PercentRule() {
        val kitsune = FruitRepository.findById("kitsune")!! // 8,000,000 Beli
        val rocket = FruitRepository.findById("rocket")!!   // 5,000 Beli

        // Difference between 8M and 5K is almost 100%, well above 40%
        val yourOffer = listOf(TradeSlotItem(kitsune, false))
        val theirOffer = listOf(TradeSlotItem(rocket, false))

        val evaluation = TradeEvaluator.evaluate(yourOffer, theirOffer)
        assertFalse(evaluation.isBeliWithin40Percent)
        assertTrue(evaluation.beliDifferenceNeeded > 0)
    }

    @Test
    fun testFruitRepositoryContents() {
        assertTrue(FruitRepository.allItems.isNotEmpty())
        val dragon = FruitRepository.findById("dragon")
        assertTrue(dragon != null)
        assertEquals(10, dragon?.demand)
    }
}
