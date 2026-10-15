package com.example.ui.screens

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DeleteSweep
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.SwapVert
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.BloxFruit
import com.example.data.model.SavedTrade
import com.example.data.model.SavedTradesRepository
import com.example.data.model.TradeEvaluator
import com.example.data.model.TradeSlotItem
import com.example.ui.components.FruitSelectorDialog
import com.example.ui.components.TradeSlotCard
import com.example.ui.components.TradeVerdictCard
import com.example.ui.theme.BfBorder
import com.example.ui.theme.BfCyan
import com.example.ui.theme.BfDarkBg
import com.example.ui.theme.BfDarkCard
import com.example.ui.theme.BfDarkCardElevated
import com.example.ui.theme.BfDarkSurface
import com.example.ui.theme.BfDarkSurfaceVariant
import com.example.ui.theme.BfGold
import com.example.ui.theme.BfPurpleLight
import com.example.ui.theme.BfPurplePrimary
import com.example.ui.theme.BfTextMuted
import com.example.ui.theme.BfTextPrimary
import com.example.ui.theme.BfTextSecondary

enum class TargetSide {
    YOUR_OFFER,
    THEIR_OFFER
}

@Composable
fun CalculatorScreen(
    onOpenWebPortal: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val savedTradesRepo = remember { SavedTradesRepository(context) }

    val yourOfferSlots = remember { mutableStateListOf<TradeSlotItem?>() }
    val theirOfferSlots = remember { mutableStateListOf<TradeSlotItem?>() }

    // Ensure 4 slots on each side
    if (yourOfferSlots.isEmpty()) {
        repeat(4) { yourOfferSlots.add(null) }
    }
    if (theirOfferSlots.isEmpty()) {
        repeat(4) { theirOfferSlots.add(null) }
    }

    var showSelectorDialog by remember { mutableStateOf(false) }
    var targetSide by remember { mutableStateOf(TargetSide.YOUR_OFFER) }
    var targetSlotIndex by remember { mutableStateOf(0) }

    val evaluation = remember(yourOfferSlots.toList(), theirOfferSlots.toList()) {
        val yourValid = yourOfferSlots.filterNotNull()
        val theirValid = theirOfferSlots.filterNotNull()
        TradeEvaluator.evaluate(yourValid, theirValid)
    }

    // Function to swap offers
    fun swapOffers() {
        val temp = yourOfferSlots.toList()
        yourOfferSlots.clear()
        yourOfferSlots.addAll(theirOfferSlots)
        theirOfferSlots.clear()
        theirOfferSlots.addAll(temp)
    }

    // Function to clear all slots
    fun clearAll() {
        for (i in 0 until 4) {
            yourOfferSlots[i] = null
            theirOfferSlots[i] = null
        }
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(BfDarkBg)
            .padding(horizontal = 14.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Hero Header Banner
        item {
            Spacer(modifier = Modifier.height(6.dp))
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(130.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .border(1.dp, BfPurplePrimary.copy(alpha = 0.4f), RoundedCornerShape(16.dp)),
                color = BfDarkCardElevated
            ) {
                Box(modifier = Modifier.fillMaxSize()) {
                    Image(
                        painter = painterResource(id = R.drawable.bf_hero_banner_1792079130745),
                        contentDescription = "Blox Fruits Arena",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )

                    // Gradient overlay
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.horizontalGradient(
                                    colors = listOf(
                                        BfDarkBg.copy(alpha = 0.92f),
                                        BfDarkBg.copy(alpha = 0.65f),
                                        Color.Transparent
                                    )
                                )
                            )
                    )

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(14.dp),
                        verticalArrangement = Arrangement.Center
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "حاسبة تداول بلوكس فروت",
                                color = Color.White,
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 18.sp
                            )
                        }
                        Text(
                            text = "Blox Fruits Values Calculator (W / F / L)",
                            color = BfGold,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 12.sp
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "احسب الفارق وتحقق من قاعدة الـ 40% بيلي الرسمية",
                            color = BfTextSecondary,
                            fontSize = 11.sp
                        )
                    }

                    // Direct Web Portal button in top right
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(8.dp)
                    ) {
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = BfDarkSurface.copy(alpha = 0.85f),
                            border = androidx.compose.foundation.BorderStroke(1.dp, BfPurplePrimary),
                            modifier = Modifier.clickable { onOpenWebPortal() }
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Language,
                                    contentDescription = "الموقع",
                                    tint = BfCyan,
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "الموقع لايف",
                                    color = BfTextPrimary,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }
        }

        // Action Toolbar (Swap & Clear)
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "عرضك (Your Offer)",
                    color = BfPurpleLight,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp
                )

                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    Button(
                        onClick = { swapOffers() },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = BfDarkCard,
                            contentColor = BfCyan
                        ),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier
                            .height(34.dp)
                            .testTag("swap_offers_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.SwapVert,
                            contentDescription = "تبديل",
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("تبديل العرضين", fontSize = 11.sp)
                    }

                    Button(
                        onClick = { clearAll() },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = BfDarkCard,
                            contentColor = BfTextSecondary
                        ),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier
                            .height(34.dp)
                            .testTag("clear_all_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.DeleteSweep,
                            contentDescription = "مسح",
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("مسح", fontSize = 11.sp)
                    }
                }
            }
        }

        // Your Offer Slots (4 items)
        items(4) { index ->
            TradeSlotCard(
                slotIndex = index,
                slotItem = yourOfferSlots[index],
                onAddClick = {
                    targetSide = TargetSide.YOUR_OFFER
                    targetSlotIndex = index
                    showSelectorDialog = true
                },
                onRemoveClick = {
                    yourOfferSlots[index] = null
                },
                onTogglePerm = {
                    val current = yourOfferSlots[index]
                    if (current != null) {
                        yourOfferSlots[index] = current.copy(isPerm = !current.isPerm)
                    }
                }
            )
        }

        // VS Divider
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(1.dp)
                        .background(BfBorder)
                )
                Box(
                    modifier = Modifier
                        .padding(horizontal = 12.dp)
                        .clip(CircleShape)
                        .background(BfDarkCardElevated)
                        .border(1.5.dp, BfGold, CircleShape)
                        .padding(horizontal = 14.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "⚡ VS ⚡",
                        color = BfGold,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 13.sp
                    )
                }
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(1.dp)
                        .background(BfBorder)
                )
            }
        }

        // Their Offer Header
        item {
            Text(
                text = "عرض الطرف الآخر (Their Offer)",
                color = BfGold,
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp
            )
        }

        // Their Offer Slots (4 items)
        items(4) { index ->
            TradeSlotCard(
                slotIndex = index,
                slotItem = theirOfferSlots[index],
                onAddClick = {
                    targetSide = TargetSide.THEIR_OFFER
                    targetSlotIndex = index
                    showSelectorDialog = true
                },
                onRemoveClick = {
                    theirOfferSlots[index] = null
                },
                onTogglePerm = {
                    val current = theirOfferSlots[index]
                    if (current != null) {
                        theirOfferSlots[index] = current.copy(isPerm = !current.isPerm)
                    }
                }
            )
        }

        // Verdict & Evaluation Card
        item {
            Spacer(modifier = Modifier.height(4.dp))
            TradeVerdictCard(
                evaluation = evaluation,
                onSaveTrade = {
                    val yourValid = yourOfferSlots.filterNotNull()
                    val theirValid = theirOfferSlots.filterNotNull()
                    if (yourValid.isNotEmpty() && theirValid.isNotEmpty()) {
                        savedTradesRepo.saveTrade(
                            SavedTrade(
                                yourItems = yourValid,
                                theirItems = theirValid,
                                evaluation = evaluation,
                                note = "تقييم: ${evaluation.verdict.arabicTitle}"
                            )
                        )
                        Toast.makeText(context, "تم حفظ الصفقة في السجل بنجاح! 💾", Toast.LENGTH_SHORT).show()
                    }
                }
            )
            Spacer(modifier = Modifier.height(20.dp))
        }
    }

    // Modal to choose fruit
    if (showSelectorDialog) {
        FruitSelectorDialog(
            targetSideName = if (targetSide == TargetSide.YOUR_OFFER) "عرضك" else "عرض الطرف الآخر",
            onDismiss = { showSelectorDialog = false },
            onItemSelected = { selectedItem ->
                if (targetSide == TargetSide.YOUR_OFFER) {
                    yourOfferSlots[targetSlotIndex] = selectedItem
                } else {
                    theirOfferSlots[targetSlotIndex] = selectedItem
                }
                showSelectorDialog = false
            }
        )
    }
}
