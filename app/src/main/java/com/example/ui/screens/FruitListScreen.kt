package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
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
import com.example.data.model.BloxFruit
import com.example.data.model.FruitRarity
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
import com.example.ui.theme.BfWinGreen

@Composable
fun FruitListScreen(
    modifier: Modifier = Modifier
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedRarity by remember { mutableStateOf<FruitRarity?>(null) }
    var sortBy by remember { mutableStateOf("value_desc") }
    var showSortMenu by remember { mutableStateOf(false) }
    var selectedFruitForDetails by remember { mutableStateOf<BloxFruit?>(null) }

    val fruitsList = remember(searchQuery, selectedRarity, sortBy) {
        FruitRepository.filterItems(
            query = searchQuery,
            rarity = selectedRarity,
            sortBy = sortBy
        )
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(BfDarkBg)
            .padding(horizontal = 14.dp)
    ) {
        Spacer(modifier = Modifier.height(10.dp))

        // Search bar & Sort Button
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = {
                    Text(
                        "ابحث بالاسم: Kitsune، كيتسوني، Dragon...",
                        color = BfTextMuted,
                        fontSize = 13.sp
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "بحث",
                        tint = BfPurpleLight
                    )
                },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(onClick = { searchQuery = "" }) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "مسح",
                                tint = BfTextSecondary
                            )
                        }
                    }
                },
                modifier = Modifier
                    .weight(1f)
                    .testTag("fruit_list_search_input"),
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

            Spacer(modifier = Modifier.width(8.dp))

            // Sort dropdown trigger
            Box {
                IconButton(
                    onClick = { showSortMenu = true },
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(BfDarkCard)
                        .border(1.dp, BfBorder, RoundedCornerShape(12.dp))
                        .size(52.dp)
                        .testTag("sort_menu_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.FilterList,
                        contentDescription = "ترتيب",
                        tint = BfCyan
                    )
                }

                DropdownMenu(
                    expanded = showSortMenu,
                    onDismissRequest = { showSortMenu = false },
                    modifier = Modifier.background(BfDarkSurface)
                ) {
                    DropdownMenuItem(
                        text = { Text("الأعلى قيمة عادية (Physical)", color = BfTextPrimary) },
                        onClick = {
                            sortBy = "value_desc"
                            showSortMenu = false
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("الأعلى طلباً (Demand)", color = BfTextPrimary) },
                        onClick = {
                            sortBy = "demand_desc"
                            showSortMenu = false
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("الأعلى قيمة دائمة (Perm)", color = BfTextPrimary) },
                        onClick = {
                            sortBy = "perm_desc"
                            showSortMenu = false
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("سعر البيلي باللعبة (Beli)", color = BfTextPrimary) },
                        onClick = {
                            sortBy = "beli_desc"
                            showSortMenu = false
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("ترتيب أبجدي (A-Z)", color = BfTextPrimary) },
                        onClick = {
                            sortBy = "name"
                            showSortMenu = false
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Rarity Filter Pills
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            contentPadding = PaddingValues(horizontal = 2.dp)
        ) {
            item {
                FilterChip(
                    selected = selectedRarity == null,
                    onClick = { selectedRarity = null },
                    label = { Text("الكل (${FruitRepository.allItems.size})", fontSize = 11.sp) },
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

        // Header summary
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "قائمة أسعار وقيم الفواكه المحدثة",
                color = BfTextSecondary,
                fontSize = 12.sp
            )
            Text(
                text = "${fruitsList.size} عنصر",
                color = BfPurpleLight,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Fruit Cards
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            contentPadding = PaddingValues(bottom = 20.dp)
        ) {
            items(fruitsList) { fruit ->
                FruitDetailCard(
                    fruit = fruit,
                    onClick = { selectedFruitForDetails = fruit }
                )
            }
        }
    }

    // Detail Dialog
    selectedFruitForDetails?.let { fruit ->
        FruitDetailModal(
            fruit = fruit,
            onDismiss = { selectedFruitForDetails = null }
        )
    }
}

@Composable
fun FruitDetailCard(
    fruit: BloxFruit,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .border(
                1.dp,
                fruit.rarity.color.copy(alpha = 0.4f),
                RoundedCornerShape(14.dp)
            )
            .clickable { onClick() }
            .testTag("fruit_item_${fruit.id}"),
        colors = CardDefaults.cardColors(containerColor = BfDarkCardElevated),
        shape = RoundedCornerShape(14.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            // Row 1: Icon, Names & Badges
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    // Icon Box
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(fruit.rarity.color.copy(alpha = 0.2f))
                            .border(1.2.dp, fruit.rarity.color, RoundedCornerShape(10.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = fruit.trend.icon,
                            fontSize = 22.sp
                        )
                    }

                    Spacer(modifier = Modifier.width(10.dp))

                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = fruit.name,
                                color = BfTextPrimary,
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 15.sp
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = fruit.arabicName,
                                color = BfTextSecondary,
                                fontSize = 12.sp
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
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "• ${fruit.fruitType.displayName}",
                                color = BfTextMuted,
                                fontSize = 10.sp
                            )
                            if (fruit.awakening) {
                                Surface(
                                    shape = RoundedCornerShape(4.dp),
                                    color = BfGold.copy(alpha = 0.15f)
                                ) {
                                    Text(
                                        text = "V2 إيقاظ",
                                        color = BfGold,
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                                    )
                                }
                            }
                        }
                    }
                }

                // Trend badge
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = BfDarkSurfaceVariant,
                    border = androidx.compose.foundation.BorderStroke(1.dp, BfBorder)
                ) {
                    Text(
                        text = "${fruit.trend.icon} ${fruit.trend.displayName}",
                        color = BfCyan,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Values row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(BfDarkSurfaceVariant)
                    .padding(8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Physical value
                Column {
                    Text(
                        text = if (fruit.isGamepass) "قيمة الجيم باس" else "قيمة العادية (Physical)",
                        color = BfTextSecondary,
                        fontSize = 10.sp
                    )
                    Text(
                        text = fruit.displayPhysicalValue,
                        color = BfPurpleLight,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp
                    )
                }

                // Perm value
                if (!fruit.isGamepass) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = "قيمة الدائمة (Perm)", color = BfTextSecondary, fontSize = 10.sp)
                        Text(
                            text = fruit.displayPermValue,
                            color = BfGold,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                    }
                }

                // In-game Price
                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = if (fruit.isGamepass) "روبوكس" else "سعر البيلي",
                        color = BfTextSecondary,
                        fontSize = 10.sp
                    )
                    Text(
                        text = if (fruit.isGamepass) "${fruit.robuxPrice} R$" else fruit.displayBeli,
                        color = BfTextPrimary,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 12.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Demand progress bar
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "مستوى الطلب (Demand): ${fruit.demand}/10",
                    color = BfTextSecondary,
                    fontSize = 11.sp
                )
                Spacer(modifier = Modifier.width(8.dp))
                LinearProgressIndicator(
                    progress = { fruit.demand / 10f },
                    modifier = Modifier
                        .weight(1f)
                        .height(6.dp)
                        .clip(RoundedCornerShape(3.dp)),
                    color = if (fruit.demand >= 8) BfGold else if (fruit.demand >= 5) BfPurplePrimary else BfTextMuted,
                    trackColor = BfDarkCard
                )
            }
        }
    }
}

@Composable
fun FruitDetailModal(
    fruit: BloxFruit,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
                .border(1.5.dp, fruit.rarity.color, RoundedCornerShape(20.dp)),
            color = BfDarkSurface
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(50.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(fruit.rarity.color.copy(alpha = 0.25f))
                                .border(1.5.dp, fruit.rarity.color, RoundedCornerShape(12.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = fruit.trend.icon, fontSize = 26.sp)
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = fruit.name,
                                color = BfTextPrimary,
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 18.sp
                            )
                            Text(
                                text = fruit.arabicName,
                                color = fruit.rarity.color,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    IconButton(onClick = onDismiss) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "إغلاق",
                            tint = BfTextSecondary
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Key metrics Grid
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(BfDarkCardElevated)
                        .padding(14.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    MetricRow(label = "الندرة (Rarity)", value = fruit.rarity.displayName, color = fruit.rarity.color)
                    MetricRow(label = "النوع (Type)", value = fruit.fruitType.displayName, color = BfCyan)
                    MetricRow(label = "قيمة التداول العادية (Physical)", value = fruit.displayPhysicalValue, color = BfPurpleLight)
                    if (!fruit.isGamepass) {
                        MetricRow(label = "قيمة التداول الدائمة (Perm)", value = fruit.displayPermValue, color = BfGold)
                        MetricRow(label = "سعر البيلي باللعبة (Beli)", value = fruit.displayBeli, color = BfTextPrimary)
                    }
                    MetricRow(label = "سعر الروبوكس (Robux)", value = "${fruit.robuxPrice} R$", color = BfGold)
                    MetricRow(label = "حالة السوق", value = "${fruit.trend.icon} ${fruit.trend.displayName} (${fruit.trend.arabicName})", color = BfCyan)
                    MetricRow(label = "مستوى الطلب في التداول", value = "${fruit.demand} من 10", color = BfWinGreen)
                    if (fruit.awakening) {
                        MetricRow(label = "إيقاظ الفاكهة (Awakening)", value = "نعم (V2 متاح في الريدز)", color = BfGold)
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Trading tips & notes
                if (fruit.notes.isNotBlank()) {
                    Text(
                        text = "💡 نصيحة التداول والاستخدام:",
                        color = BfGold,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = fruit.notes,
                        color = BfTextPrimary,
                        fontSize = 12.sp,
                        lineHeight = 18.sp
                    )
                }
            }
        }
    }
}

@Composable
fun MetricRow(label: String, value: String, color: Color) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = label, color = BfTextSecondary, fontSize = 11.sp)
        Text(text = value, color = color, fontWeight = FontWeight.Bold, fontSize = 12.sp)
    }
}
