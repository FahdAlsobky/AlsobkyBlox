package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Badge
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.BloxFruit
import com.example.data.model.TradeSlotItem
import com.example.ui.theme.BfBorder
import com.example.ui.theme.BfDarkCard
import com.example.ui.theme.BfDarkCardElevated
import com.example.ui.theme.BfDarkSurfaceVariant
import com.example.ui.theme.BfGold
import com.example.ui.theme.BfPurpleLight
import com.example.ui.theme.BfPurplePrimary
import com.example.ui.theme.BfTextMuted
import com.example.ui.theme.BfTextPrimary
import com.example.ui.theme.BfTextSecondary

@Composable
fun TradeSlotCard(
    slotIndex: Int,
    slotItem: TradeSlotItem?,
    onAddClick: () -> Unit,
    onRemoveClick: () -> Unit,
    onTogglePerm: () -> Unit,
    modifier: Modifier = Modifier
) {
    if (slotItem == null) {
        // Empty Slot
        Box(
            modifier = modifier
                .fillMaxWidth()
                .height(76.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(BfDarkCard.copy(alpha = 0.5f))
                .border(
                    width = 1.dp,
                    color = BfBorder.copy(alpha = 0.7f),
                    shape = RoundedCornerShape(12.dp)
                )
                .clickable { onAddClick() }
                .testTag("add_slot_${slotIndex}"),
            contentAlignment = Alignment.Center
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(BfPurplePrimary.copy(alpha = 0.2f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "أضف فاكهة",
                        tint = BfPurpleLight,
                        modifier = Modifier.size(20.dp)
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "خانة ${slotIndex + 1}: أضف فاكهة / جيم باس",
                    color = BfTextSecondary,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    } else {
        // Filled Slot
        val fruit = slotItem.fruit
        Surface(
            modifier = modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .border(
                    width = 1.5.dp,
                    color = fruit.rarity.color.copy(alpha = 0.6f),
                    shape = RoundedCornerShape(12.dp)
                ),
            color = BfDarkCardElevated,
            shape = RoundedCornerShape(12.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    // Left: Fruit Icon & Name
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(1f)
                    ) {
                        // Rarity color dot / badge
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(fruit.rarity.color.copy(alpha = 0.2f))
                                .border(1.dp, fruit.rarity.color, RoundedCornerShape(8.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = fruit.trend.icon,
                                fontSize = 18.sp
                            )
                        }

                        Spacer(modifier = Modifier.width(8.dp))

                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = fruit.name,
                                    color = BfTextPrimary,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = fruit.arabicName,
                                    color = BfTextSecondary,
                                    fontSize = 12.sp,
                                    maxLines = 1
                                )
                            }
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Text(
                                    text = fruit.rarity.displayName,
                                    color = fruit.rarity.color,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                                if (!fruit.isGamepass && fruit.beliPrice > 0) {
                                    Text(
                                        text = "• Beli: ${fruit.displayBeli}",
                                        color = BfTextMuted,
                                        fontSize = 11.sp
                                    )
                                }
                            }
                        }
                    }

                    // Remove button
                    IconButton(
                        onClick = onRemoveClick,
                        modifier = Modifier
                            .size(32.dp)
                            .testTag("remove_slot_${slotIndex}")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "حذف العنصر",
                            tint = BfTextSecondary,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(6.dp))

                // Value and Perm/Physical Switch
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    // Physical vs Perm chip
                    if (!fruit.isGamepass) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            FilterChip(
                                selected = !slotItem.isPerm,
                                onClick = { if (slotItem.isPerm) onTogglePerm() },
                                label = { Text("عادية (Physical)", fontSize = 11.sp) },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = BfPurplePrimary,
                                    selectedLabelColor = Color.White,
                                    containerColor = BfDarkSurfaceVariant,
                                    labelColor = BfTextSecondary
                                ),
                                modifier = Modifier.height(28.dp)
                            )

                            FilterChip(
                                selected = slotItem.isPerm,
                                onClick = { if (!slotItem.isPerm) onTogglePerm() },
                                label = { Text("دائمة (Perm)", fontSize = 11.sp) },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = BfGold,
                                    selectedLabelColor = Color.Black,
                                    containerColor = BfDarkSurfaceVariant,
                                    labelColor = BfTextSecondary
                                ),
                                modifier = Modifier.height(28.dp)
                            )
                        }
                    } else {
                        Badge(
                            containerColor = BfGold.copy(alpha = 0.2f),
                            contentColor = BfGold
                        ) {
                            Text(
                                text = "Gamepass (${fruit.robuxPrice} R$)",
                                fontSize = 11.sp,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }

                    // Current Value Display
                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            text = "القيمة: ${BloxFruit.formatValue(slotItem.tradingValue)}",
                            color = if (slotItem.isPerm) BfGold else BfPurpleLight,
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 13.sp
                        )
                    }
                }
            }
        }
    }
}
