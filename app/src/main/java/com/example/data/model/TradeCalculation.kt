package com.example.data.model

import androidx.compose.ui.graphics.Color
import com.example.ui.theme.BfFairBlue
import com.example.ui.theme.BfLoseRed
import com.example.ui.theme.BfWarningAmber
import com.example.ui.theme.BfWinGreen
import kotlin.math.abs
import kotlin.math.max

enum class TradeVerdict(
    val title: String,
    val arabicTitle: String,
    val description: String,
    val arabicDescription: String,
    val color: Color,
    val icon: String
) {
    HUGE_WIN(
        "HUGE WIN",
        "مكسب ضخم جداً 🏆",
        "You are gaining massive value from this trade!",
        "أنت تكسب قيمة خيالية في هذه الصفقة! اقبل فوراً!",
        BfWinGreen,
        "🏆"
    ),
    WIN(
        "WIN",
        "مكسب رابح 🟢",
        "Good profit! Their offer is more valuable than yours.",
        "صفقة رابحة! عرض الطرف الآخر أعلى قيمة من عرضك.",
        BfWinGreen,
        "🟢"
    ),
    FAIR(
        "FAIR TRADE",
        "صفقة عادلة ⚖️",
        "Both offers have equal or very close market value.",
        "الصفقة متكافئة وعادلة جداً بين الطرفين حسب السوق.",
        BfFairBlue,
        "⚖️"
    ),
    SMALL_LOSE(
        "SMALL LOSE",
        "خسارة طفيفة 🟠",
        "You are giving away slightly more value. Only accept if you need their items.",
        "أنت تدفع قيمة أكبر بقليل، لا توافق إلا إذا كنت تريد الفاكهة بشدة.",
        BfWarningAmber,
        "🟠"
    ),
    HUGE_LOSE(
        "HUGE L",
        "خسارة فادحة 🔴",
        "Big loss! You are overpaying by a lot.",
        "خسارة كبيرة! أنت تدفع أضعاف قيمة ما ستحصل عليه!",
        BfLoseRed,
        "🔴"
    ),
    INCOMPLETE(
        "ADD ITEMS",
        "أضف عناصر ➕",
        "Select fruits or gamepasses on both sides to evaluate trade.",
        "قم بإضافة الفواكه أو الجيم باس في الطرفين لاحتساب الصفقة.",
        BfFairBlue,
        "➕"
    )
}

data class TradeEvaluation(
    val yourTotalValue: Long,
    val theirTotalValue: Long,
    val valueDifference: Long,
    val percentDifference: Double,
    val verdict: TradeVerdict,
    val yourTotalBeli: Long,
    val theirTotalBeli: Long,
    val beliDifferencePercent: Double,
    val isBeliWithin40Percent: Boolean,
    val beliDifferenceNeeded: Long
)

object TradeEvaluator {

    fun evaluate(yourSlots: List<TradeSlotItem>, theirSlots: List<TradeSlotItem>): TradeEvaluation {
        val yourVal = yourSlots.sumOf { it.tradingValue }
        val theirVal = theirSlots.sumOf { it.tradingValue }

        val yourBeli = yourSlots.sumOf { it.beliValue }
        val theirBeli = theirSlots.sumOf { it.beliValue }

        if (yourSlots.isEmpty() || theirSlots.isEmpty()) {
            return TradeEvaluation(
                yourTotalValue = yourVal,
                theirTotalValue = theirVal,
                valueDifference = 0,
                percentDifference = 0.0,
                verdict = TradeVerdict.INCOMPLETE,
                yourTotalBeli = yourBeli,
                theirTotalBeli = theirBeli,
                beliDifferencePercent = 0.0,
                isBeliWithin40Percent = true,
                beliDifferenceNeeded = 0
            )
        }

        val diff = theirVal - yourVal
        val percent = if (yourVal > 0) {
            ((diff.toDouble() / yourVal.toDouble()) * 100.0)
        } else {
            100.0
        }

        val verdict = when {
            percent >= 25.0 -> TradeVerdict.HUGE_WIN
            percent >= 8.0 -> TradeVerdict.WIN
            percent >= -8.0 -> TradeVerdict.FAIR
            percent >= -25.0 -> TradeVerdict.SMALL_LOSE
            else -> TradeVerdict.HUGE_LOSE
        }

        // Blox Fruits 40% Beli rule check
        val maxBeli = max(yourBeli, theirBeli)
        val isPureGamepass = maxBeli == 0L

        val beliDiffPercent = if (maxBeli > 0) {
            (abs(yourBeli - theirBeli).toDouble() / maxBeli.toDouble()) * 100.0
        } else {
            0.0
        }

        val isWithin40 = isPureGamepass || beliDiffPercent <= 40.0
        val diffBeli = abs(yourBeli - theirBeli)
        val neededToBalance = if (!isWithin40 && maxBeli > 0) {
            // Needed to bring within 40%: min required = 0.6 * maxBeli
            val minRequired = (maxBeli * 0.6).toLong()
            val currentLesser = if (yourBeli < theirBeli) yourBeli else theirBeli
            max(0L, minRequired - currentLesser)
        } else {
            0L
        }

        return TradeEvaluation(
            yourTotalValue = yourVal,
            theirTotalValue = theirVal,
            valueDifference = diff,
            percentDifference = percent,
            verdict = verdict,
            yourTotalBeli = yourBeli,
            theirTotalBeli = theirBeli,
            beliDifferencePercent = beliDiffPercent,
            isBeliWithin40Percent = isWithin40,
            beliDifferenceNeeded = neededToBalance
        )
    }
}
