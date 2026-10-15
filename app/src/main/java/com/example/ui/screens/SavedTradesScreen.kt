package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BookmarkRemove
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.BloxFruit
import com.example.data.model.SavedTrade
import com.example.data.model.SavedTradesRepository
import com.example.ui.theme.BfBorder
import com.example.ui.theme.BfDarkBg
import com.example.ui.theme.BfDarkCardElevated
import com.example.ui.theme.BfDarkSurface
import com.example.ui.theme.BfDarkSurfaceVariant
import com.example.ui.theme.BfGold
import com.example.ui.theme.BfPurpleLight
import com.example.ui.theme.BfPurplePrimary
import com.example.ui.theme.BfTextMuted
import com.example.ui.theme.BfTextPrimary
import com.example.ui.theme.BfTextSecondary

@Composable
fun SavedTradesScreen(
    onNavigateToCalculator: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val repository = remember { SavedTradesRepository(context) }
    var tradesList by remember { mutableStateOf(repository.getSavedTrades()) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(BfDarkBg)
            .padding(horizontal = 14.dp)
    ) {
        Spacer(modifier = Modifier.height(10.dp))

        // Top Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "سجل الصفقات المحفوظة",
                    color = BfTextPrimary,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 17.sp
                )
                Text(
                    text = "${tradesList.size} صفقة محفوظة",
                    color = BfTextSecondary,
                    fontSize = 12.sp
                )
            }

            if (tradesList.isNotEmpty()) {
                Button(
                    onClick = {
                        repository.clearAll()
                        tradesList = emptyList()
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = BfDarkSurfaceVariant,
                        contentColor = BfTextSecondary
                    ),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.height(34.dp).testTag("clear_history_button")
                ) {
                    Text("مسح الكل", fontSize = 11.sp)
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        if (tradesList.isEmpty()) {
            // Empty State
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(30.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.BookmarkRemove,
                        contentDescription = null,
                        tint = BfTextMuted,
                        modifier = Modifier.size(56.dp)
                    )
                    Spacer(modifier = Modifier.height(14.dp))
                    Text(
                        text = "لا توجد صفقات محفوظة بعد",
                        color = BfTextPrimary,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "قم بحساب الصفقات في الحاسبة واحفظها لمراجعتها في أي وقت!",
                        color = BfTextSecondary,
                        fontSize = 13.sp,
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(18.dp))
                    Button(
                        onClick = onNavigateToCalculator,
                        colors = ButtonDefaults.buttonColors(containerColor = BfPurplePrimary),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text("الذهاب للحاسبة الآن", fontWeight = FontWeight.Bold)
                    }
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(bottom = 20.dp)
            ) {
                items(tradesList, key = { it.id }) { trade ->
                    SavedTradeCard(
                        trade = trade,
                        onDelete = {
                            repository.deleteTrade(trade.id)
                            tradesList = repository.getSavedTrades()
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun SavedTradeCard(
    trade: SavedTrade,
    onDelete: () -> Unit
) {
    val eval = trade.evaluation

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .border(
                1.dp,
                eval.verdict.color.copy(alpha = 0.5f),
                RoundedCornerShape(14.dp)
            ),
        colors = CardDefaults.cardColors(containerColor = BfDarkCardElevated),
        shape = RoundedCornerShape(14.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            // Header: Verdict & Date & Delete
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(eval.verdict.color.copy(alpha = 0.2f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = eval.verdict.icon, fontSize = 16.sp)
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(
                            text = eval.verdict.arabicTitle,
                            color = eval.verdict.color,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                        Text(
                            text = trade.formattedDate,
                            color = BfTextMuted,
                            fontSize = 10.sp
                        )
                    }
                }

                IconButton(
                    onClick = onDelete,
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "حذف",
                        tint = BfTextMuted,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Two columns: Your Items vs Their Items
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(BfDarkSurfaceVariant)
                    .padding(10.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Your items column
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "عرضك (${BloxFruit.formatValue(eval.yourTotalValue)})",
                        color = BfPurpleLight,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    trade.yourItems.forEach { item ->
                        Text(
                            text = "• ${item.fruit.name} ${if (item.isPerm) "(Perm)" else ""}",
                            color = BfTextPrimary,
                            fontSize = 11.sp
                        )
                    }
                }

                Box(
                    modifier = Modifier
                        .width(1.dp)
                        .height(50.dp)
                        .background(BfBorder)
                        .align(Alignment.CenterVertically)
                )

                Spacer(modifier = Modifier.width(10.dp))

                // Their items column
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "عرضهم (${BloxFruit.formatValue(eval.theirTotalValue)})",
                        color = BfGold,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    trade.theirItems.forEach { item ->
                        Text(
                            text = "• ${item.fruit.name} ${if (item.isPerm) "(Perm)" else ""}",
                            color = BfTextPrimary,
                            fontSize = 11.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            // 40% Beli rule note
            Text(
                text = if (eval.isBeliWithin40Percent) "✅ متوافق مع قاعدة 40% بيلي" else "⚠️ يتجاوز فرق 40% بيلي",
                color = if (eval.isBeliWithin40Percent) BfTextSecondary else BfGold,
                fontSize = 10.sp
            )
        }
    }
}
