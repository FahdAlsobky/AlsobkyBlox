package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Security
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.BfCyan
import com.example.ui.theme.BfDarkBg
import com.example.ui.theme.BfDarkCard
import com.example.ui.theme.BfDarkCardElevated
import com.example.ui.theme.BfDarkSurfaceVariant
import com.example.ui.theme.BfGold
import com.example.ui.theme.BfPurpleLight
import com.example.ui.theme.BfPurplePrimary
import com.example.ui.theme.BfTextPrimary
import com.example.ui.theme.BfTextSecondary
import com.example.ui.theme.BfWarningAmber
import com.example.ui.theme.BfWinGreen

@Composable
fun GuideScreen(
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(BfDarkBg)
            .padding(horizontal = 14.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(6.dp))
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .border(1.dp, BfGold.copy(alpha = 0.5f), RoundedCornerShape(16.dp)),
                color = BfDarkCardElevated
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(BfGold.copy(alpha = 0.2f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Lightbulb,
                                contentDescription = null,
                                tint = BfGold,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "دليل وأسرار تداول بلوكس فروت",
                                color = Color.White,
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 16.sp
                            )
                            Text(
                                text = "Blox Fruits Trading Master Guide",
                                color = BfGold,
                                fontSize = 11.sp
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "تعلم كيفية تقييم الصفقات باحتراف وتجنب الخسارة ومعرفة متى تقبل أو ترفض التداول في المقهى والقصر.",
                        color = BfTextSecondary,
                        fontSize = 12.sp,
                        lineHeight = 18.sp
                    )
                }
            }
        }

        // Rule 1: 40% Beli difference
        item {
            GuideSectionCard(
                title = "1. قاعدة الـ 40% بيلي الرسمية (40% Beli Rule)",
                badgeColor = BfPurplePrimary,
                content = "في لعبة Blox Fruits، لا تسمح طاولات التداول بإتمام أي صفقة إذا كان الفرق بين مجموع سعر البيلي للطرف الأول والطرف الثاني يتجاوز 40%!\n\n" +
                    "• حل المشكلة: إذا كنت تقدم فاكهة غالية مثل Kitsune (8M بيلي) مقابل فواكه بقيمة بيلي أقل، ستحتاج أن يضيف الطرف الآخر فواكه مثل Quake أو Spider أو Love أو Gravity لموازنة سعر البيلي، حتى لو كانت تلك الفواكه غير مطلوبة في القيمة السوقية."
            )
        }

        // Rule 2: Trading Value vs In-Game Beli
        item {
            GuideSectionCard(
                title = "2. الفرق بين القيمة السوقية وسعر البيلي",
                badgeColor = BfCyan,
                content = "سعر البيلي المكتوب على الفاكهة في المتجر لا يعكس قيمتها الحقيقية في التداول!\n\n" +
                    "• مثال حي: فاكهة Buddha سعرها في المتجر 1.2M بيلي فقط، لكن قيمتها السوقية في التداول تتجاوز 10M لأنها ملكة رفع المستوى والمهمات!\n" +
                    "• فاكهة Gravity سعرها في المتجر 2.5M بيلي، لكن قيمتها في التداول 2.5M فقط وطلبها 2/10 لأن استخدامها ضعيف."
            )
        }

        // Rule 3: Physical vs Perm Fruits
        item {
            GuideSectionCard(
                title = "3. الفواكه العادية (Physical) مقابل الدائمة (Perm)",
                badgeColor = BfGold,
                content = "• الفواكه العادية: تستهلك مرة واحدة، وإذا قمت بتبديل الفاكهة ستفقدها إلا إذا أكلت فاكهة جديدة مخزنة.\n\n" +
                    "• الفواكه الدائمة (Permanent): تُشترى بالروبوكس وتمنحك حق تجهيز الفاكهة في أي لحظة ومن أي مكان مجاناً مدى الحياة، لذلك تكون قيمتها أضعاف الفاكهة العادية (مثلاً Perm Kitsune قيمتها 350M!)."
            )
        }

        // Rule 4: Overpay & High Demand Fruits
        item {
            GuideSectionCard(
                title = "4. الفواكه ذات الطلب الخارق (Overpay)",
                badgeColor = BfWinGreen,
                content = "هناك فواكه يقبل اللاعبون دائماً دفع زيادة (Overpay) للحصول عليها:\n\n" +
                    "🔥 Kitsune & Dragon: قمة الهرم التداولي في اللعبة.\n" +
                    "⚡ Portal: مطلوبة بشدة من مستخدمي السيوف والـ Bounty Hunters.\n" +
                    "🟡 Buddha: أسهل فاكهة للتداول لأن كل لاعب جديد يحتاجها للـ Grinding.\n" +
                    "🥐 Dough: الطلب عليها دائم ومستمر لقوتها في الـ PvP بعد إيقاظ V2."
            )
        }

        // Rule 5: Safety and Scam Prevention
        item {
            GuideSectionCard(
                title = "5. نصائح الأمان وتجنب الاحتيال (Safety Tips)",
                badgeColor = BfWarningAmber,
                content = "• لا تضغط Accept إلا بعد فحص عناصر الطرف الآخر بدقة في آخر ثانيتين.\n" +
                    "• احذر من حركة التبديل السريع (Switching) حيث يقوم بعض المحتالين بإزالة الفاكهة ووضع بديل رخيص قبل انتهاء العد التنازلي.\n" +
                    "• لا تقبل أبداً الروابط المشبوهة أو السيرفرات الخاصة المزيفة خارج اللعبة."
            )
        }

        item {
            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@Composable
fun GuideSectionCard(
    title: String,
    badgeColor: Color,
    content: String
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .border(1.dp, badgeColor.copy(alpha = 0.4f), RoundedCornerShape(14.dp)),
        colors = CardDefaults.cardColors(containerColor = BfDarkCard),
        shape = RoundedCornerShape(14.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(10.dp)
                        .clip(CircleShape)
                        .background(badgeColor)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = title,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = content,
                color = BfTextSecondary,
                fontSize = 12.sp,
                lineHeight = 19.sp
            )
        }
    }
}
