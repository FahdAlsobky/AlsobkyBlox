package com.example.data.model

import androidx.compose.ui.graphics.Color
import com.example.ui.theme.RarityCommon
import com.example.ui.theme.RarityGamepass
import com.example.ui.theme.RarityLegendary
import com.example.ui.theme.RarityMythical
import com.example.ui.theme.RarityRare
import com.example.ui.theme.RarityUncommon

enum class FruitRarity(val displayName: String, val arabicName: String, val color: Color) {
    MYTHICAL("Mythical", "أسطوري خرافي", RarityMythical),
    LEGENDARY("Legendary", "أسطوري", RarityLegendary),
    RARE("Rare", "نادر", RarityRare),
    UNCOMMON("Uncommon", "غير شائع", RarityUncommon),
    COMMON("Common", "شائع", RarityCommon),
    GAMEPASS("Gamepass", "جيم باس", RarityGamepass)
}

enum class FruitType(val displayName: String, val arabicName: String) {
    BEAST("Beast (Zoan)", "وحش (زوان)"),
    ELEMENTAL("Elemental (Logia)", "عنصري (لوجيا)"),
    NATURAL("Natural (Paramecia)", "طبيعي (باراميسيا)"),
    GAMEPASS("Gamepass", "جيم باس")
}

enum class FruitTrend(val icon: String, val displayName: String, val arabicName: String) {
    HYPED("🔥", "Hyped", "شعبية هائلة"),
    RISING("📈", "Rising", "في صعود"),
    STABLE("➡️", "Stable", "مستقر"),
    DROPPING("📉", "Dropping", "في هبوط")
}

data class BloxFruit(
    val id: String,
    val name: String,
    val arabicName: String,
    val rarity: FruitRarity,
    val fruitType: FruitType,
    val beliPrice: Long,
    val robuxPrice: Int,
    val physicalValue: Long,
    val permValue: Long,
    val demand: Int, // 1 to 10
    val trend: FruitTrend,
    val awakening: Boolean = false,
    val isGamepass: Boolean = false,
    val notes: String = ""
) {
    val displayBeli: String
        get() = formatBeli(beliPrice)

    val displayPhysicalValue: String
        get() = formatValue(physicalValue)

    val displayPermValue: String
        get() = formatValue(permValue)

    companion object {
        fun formatValue(value: Long): String {
            return when {
                value >= 1_000_000_000L -> {
                    val formatted = (value / 100_000_000.0) / 10.0
                    "${formatted}B"
                }
                value >= 1_000_000L -> {
                    val formatted = (value / 100_000.0) / 10.0
                    "${formatted}M"
                }
                value >= 1_000L -> {
                    val formatted = (value / 100.0) / 10.0
                    "${formatted}K"
                }
                else -> value.toString()
            }
        }

        fun formatBeli(beli: Long): String {
            return "$" + java.text.NumberFormat.getIntegerInstance().format(beli)
        }
    }
}

data class TradeSlotItem(
    val fruit: BloxFruit,
    val isPerm: Boolean = false
) {
    val tradingValue: Long
        get() = if (isPerm) fruit.permValue else fruit.physicalValue

    val beliValue: Long
        get() = fruit.beliPrice
}
