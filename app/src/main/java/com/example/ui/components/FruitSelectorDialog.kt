package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.data.model.BloxFruit
import com.example.data.model.FruitRarity
import com.example.data.model.TradeSlotItem
import com.example.data.repository.FruitRepository
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

@Composable
fun FruitSelectorDialog(
    targetSideName: String,
    onDismiss: () -> Unit,
    onItemSelected: (TradeSlotItem) -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedRarity by remember { mutableStateOf<FruitRarity?>(null) }
    var isPermSelection by remember { mutableStateOf(false) }

    val filteredItems = remember(searchQuery, selectedRarity) {
        FruitRepository.filterItems(
            query = searchQuery,
            rarity = selectedRarity,
            sortBy = "value_desc"
        )
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.95f)
                .fillMaxHeight(0.85f)
                .clip(RoundedCornerShape(20.dp))
                .border(1.dp, BfPurplePrimary.copy(alpha = 0.5f), RoundedCornerShape(20.dp)),
            color = BfDarkSurface
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "اختيار فاكهة / جيم باس",
                            color = BfTextPrimary,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp
                        )
                        Text(
                            text = "إضافة إلى: $targetSideName",
                            color = BfPurpleLight,
                            fontSize = 12.sp
                        )
                    }

                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier.testTag("dialog_close_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "إغلاق",
                            tint = BfTextSecondary
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Mode switch: Physical vs Perm
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .background(BfDarkSurfaceVariant)
                        .padding(4.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(8.dp))
                            .background(if (!isPermSelection) BfPurplePrimary else Color.Transparent)
                            .clickable { isPermSelection = false }
                            .padding(vertical = 8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "فاكهة عادية (Physical)",
                            color = if (!isPermSelection) Color.White else BfTextSecondary,
                            fontWeight = if (!isPermSelection) FontWeight.Bold else FontWeight.Normal,
                            fontSize = 12.sp
                        )
                    }

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(8.dp))
                            .background(if (isPermSelection) BfGold else Color.Transparent)
                            .clickable { isPermSelection = true }
                            .padding(vertical = 8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "فاكهة دائمة (Permanent Perm)",
                            color = if (isPermSelection) Color.Black else BfTextSecondary,
                            fontWeight = if (isPermSelection) FontWeight.Bold else FontWeight.Normal,
                            fontSize = 12.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Search field
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = { Text("ابحث بالاسم: Kitsune، كيتسوني، Dragon...", color = BfTextMuted, fontSize = 13.sp) },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "بحث",
                            tint = BfPurpleLight
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("search_fruit_input"),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = BfDarkCard,
                        unfocusedContainerColor = BfDarkCard,
                        focusedBorderColor = BfPurplePrimary,
                        unfocusedBorderColor = BfBorder,
                        focusedTextColor = BfTextPrimary,
                        unfocusedTextColor = BfTextPrimary
                    ),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Rarity filters
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    contentPadding = PaddingValues(horizontal = 2.dp)
                ) {
                    item {
                        FilterChip(
                            selected = selectedRarity == null,
                            onClick = { selectedRarity = null },
                            label = { Text("الكل", fontSize = 11.sp) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = BfPurplePrimary,
                                selectedLabelColor = Color.White,
                                containerColor = BfDarkCard,
                                labelColor = BfTextSecondary
                            )
                        )
                    }

                    items(FruitRarity.values()) { rarity ->
                        FilterChip(
                            selected = selectedRarity == rarity,
                            onClick = { selectedRarity = if (selectedRarity == rarity) null else rarity },
                            label = {
                                Text(
                                    text = rarity.displayName,
                                    fontSize = 11.sp,
                                    color = if (selectedRarity == rarity) Color.White else rarity.color
                                )
                            },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = rarity.color,
                                selectedLabelColor = Color.White,
                                containerColor = BfDarkCard,
                                labelColor = rarity.color
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Items list
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(filteredItems) { fruit ->
                        FruitSelectionRow(
                            fruit = fruit,
                            isPerm = isPermSelection,
                            onClick = {
                                val effectivePerm = if (fruit.isGamepass) false else isPermSelection
                                onItemSelected(TradeSlotItem(fruit, effectivePerm))
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun FruitSelectionRow(
    fruit: BloxFruit,
    isPerm: Boolean,
    onClick: () -> Unit
) {
    val valueToDisplay = if (fruit.isGamepass) {
        fruit.physicalValue
    } else if (isPerm) {
        fruit.permValue
    } else {
        fruit.physicalValue
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .clickable { onClick() }
            .testTag("select_fruit_${fruit.id}"),
        colors = CardDefaults.cardColors(containerColor = BfDarkCard),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                // Icon avatar
                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(fruit.rarity.color.copy(alpha = 0.2f))
                        .border(1.dp, fruit.rarity.color, RoundedCornerShape(10.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = fruit.trend.icon,
                        fontSize = 20.sp
                    )
                }

                Spacer(modifier = Modifier.width(10.dp))

                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = fruit.name,
                            color = BfTextPrimary,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "(${fruit.arabicName})",
                            color = BfTextSecondary,
                            fontSize = 11.sp
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
                        Text(
                            text = "• الطلب: ${fruit.demand}/10",
                            color = BfCyan,
                            fontSize = 11.sp
                        )
                        if (fruit.awakening) {
                            Text(
                                text = "• V2 إيقاظ",
                                color = BfGold,
                                fontSize = 10.sp
                            )
                        }
                    }
                }
            }

            // Value Tag
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = BloxFruit.formatValue(valueToDisplay),
                    color = if (isPerm && !fruit.isGamepass) BfGold else BfPurpleLight,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 14.sp
                )
                Text(
                    text = if (fruit.isGamepass) "${fruit.robuxPrice} R$" else fruit.displayBeli,
                    color = BfTextMuted,
                    fontSize = 11.sp
                )
            }
        }
    }
}
