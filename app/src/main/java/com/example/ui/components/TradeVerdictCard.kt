package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.BloxFruit
import com.example.data.model.TradeEvaluation
import com.example.data.model.TradeVerdict
import com.example.ui.theme.BfDarkCard
import com.example.ui.theme.BfDarkCardElevated
import com.example.ui.theme.BfDarkSurfaceVariant
import com.example.ui.theme.BfGold
import com.example.ui.theme.BfPurplePrimary
import com.example.ui.theme.BfTextMuted
import com.example.ui.theme.BfTextPrimary
import com.example.ui.theme.BfTextSecondary
import com.example.ui.theme.BfWarningAmber
import com.example.ui.theme.BfWinGreen
import kotlin.math.roundToInt

@Composable
fun TradeVerdictCard(
    evaluation: TradeEvaluation,
    onSaveTrade: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .border(
                width = 2.dp,
                color = evaluation.verdict.color.copy(alpha = 0.8f),
                shape = RoundedCornerShape(16.dp)
            ),
        colors = CardDefaults.cardColors(containerColor = BfDarkCardElevated),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Main Verdict Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(46.dp)
                            .clip(CircleShape)
                            .background(evaluation.verdict.color.copy(alpha = 0.2f))
                            .border(1.5.dp, evaluation.verdict.color, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = evaluation.verdict.icon,
                            fontSize = 24.sp
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column {
                        Text(
                            text = evaluation.verdict.arabicTitle,
                            color = evaluation.verdict.color,
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 18.sp
                        )
                        Text(
                            text = evaluation.verdict.title,
                            color = BfTextSecondary,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }

                // Difference Badge
                if (evaluation.verdict != TradeVerdict.INCOMPLETE) {
                    val sign = if (evaluation.valueDifference > 0) "+" else ""
                    val percentStr = "${(evaluation.percentDifference * 10).roundToInt() / 10.0}%"
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = evaluation.verdict.color.copy(alpha = 0.15f),
                        border = androidx.compose.foundation.BorderStroke(1.dp, evaluation.verdict.color)
                    ) {
                        Column(
                            horizontalAlignment = Alignment.End,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = "$sign${BloxFruit.formatValue(evaluation.valueDifference)}",
                                color = evaluation.verdict.color,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp
                            )
                            Text(
                                text = "($sign$percentStr)",
                                color = evaluation.verdict.color,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Verdict explanation in Arabic
            Text(
                text = evaluation.verdict.arabicDescription,
                color = BfTextPrimary,
                fontSize = 13.sp,
                lineHeight = 18.sp
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Values breakdown
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(BfDarkSurfaceVariant)
                    .padding(10.dp),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = "قيمة عرضك", color = BfTextSecondary, fontSize = 11.sp)
                    Text(
                        text = BloxFruit.formatValue(evaluation.yourTotalValue),
                        color = BfTextPrimary,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp
                    )
                    Text(
                        text = "Beli: ${BloxFruit.formatBeli(evaluation.yourTotalBeli)}",
                        color = BfTextMuted,
                        fontSize = 10.sp
                    )
                }

                Box(
                    modifier = Modifier
                        .width(1.dp)
                        .height(36.dp)
                        .background(BfDarkCard)
                )

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = "قيمة عرض الطرف الآخر", color = BfTextSecondary, fontSize = 11.sp)
                    Text(
                        text = BloxFruit.formatValue(evaluation.theirTotalValue),
                        color = BfGold,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp
                    )
                    Text(
                        text = "Beli: ${BloxFruit.formatBeli(evaluation.theirTotalBeli)}",
                        color = BfTextMuted,
                        fontSize = 10.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // 40% Beli Limit Status (Blox Fruits Rule)
            if (evaluation.verdict != TradeVerdict.INCOMPLETE) {
                val isBeliOk = evaluation.isBeliWithin40Percent
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = if (isBeliOk) BfWinGreen.copy(alpha = 0.12f) else BfWarningAmber.copy(alpha = 0.15f),
                    border = androidx.compose.foundation.BorderStroke(
                        1.dp,
                        if (isBeliOk) BfWinGreen.copy(alpha = 0.5f) else BfWarningAmber
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = if (isBeliOk) Icons.Default.CheckCircle else Icons.Default.Warning,
                            contentDescription = null,
                            tint = if (isBeliOk) BfWinGreen else BfWarningAmber,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            if (isBeliOk) {
                                Text(
                                    text = "قاعدة الـ 40% بيلي: مسموح باللعبة ✅",
                                    color = BfWinGreen,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp
                                )
                                Text(
                                    text = "فرق البيلي (${(evaluation.beliDifferencePercent * 10).roundToInt() / 10.0}%) ضمن الحد المسموح للتداول على طاولة المقهى والقصر.",
                                    color = BfTextSecondary,
                                    fontSize = 11.sp
                                )
                            } else {
                                Text(
                                    text = "تحذير قاعدة الـ 40% بيلي: مرفوض باللعبة ⚠️",
                                    color = BfWarningAmber,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp
                                )
                                Text(
                                    text = "فرق البيلي (${(evaluation.beliDifferencePercent * 10).roundToInt() / 10.0}%) يتجاوز 40%! أضف فاكهة بقيمة لا تقل عن ${BloxFruit.formatBeli(evaluation.beliDifferenceNeeded)} لموازنة الصفقة.",
                                    color = BfTextPrimary,
                                    fontSize = 11.sp
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Save button
                Button(
                    onClick = onSaveTrade,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("save_trade_button"),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = BfPurplePrimary,
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.BookmarkBorder,
                        contentDescription = "حفظ الصفقة",
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "حفظ الصفقة في السجل",
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp
                    )
                }
            }
        }
    }
}
