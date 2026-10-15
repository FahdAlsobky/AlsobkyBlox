package com.example

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.ViewGroup
import android.webkit.CookieManager
import android.webkit.WebChromeClient
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.OpenInBrowser
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import androidx.core.content.ContextCompat
import com.example.notification.UpdateNotificationHelper
import com.example.ui.theme.BfBorder
import com.example.ui.theme.BfCyan
import com.example.ui.theme.BfDarkBg
import com.example.ui.theme.BfDarkCard
import com.example.ui.theme.BfDarkSurface
import com.example.ui.theme.BfDarkSurfaceVariant
import com.example.ui.theme.BfGold
import com.example.ui.theme.BfGoldLight
import com.example.ui.theme.BfPurpleLight
import com.example.ui.theme.BfPurplePrimary
import com.example.ui.theme.BfTextMuted
import com.example.ui.theme.BfTextPrimary
import com.example.ui.theme.BfTextSecondary
import com.example.ui.theme.BfWarningAmber
import com.example.ui.theme.BfWinGreen
import com.example.ui.theme.BloxFruitsTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        UpdateNotificationHelper.initChannel(this)
        enableEdgeToEdge()

        val initialUrl = intent?.getStringExtra("TARGET_URL") ?: BASE_URL

        setContent {
            BloxFruitsTheme {
                BloxFruitsWebLauncher(startUrl = initialUrl)
            }
        }
    }
}

private const val BASE_URL = "https://bloxfruitsvalues.com"

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun BloxFruitsWebLauncher(startUrl: String = BASE_URL) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    var webViewInstance by remember { mutableStateOf<WebView?>(null) }
    var currentUrl by remember { mutableStateOf(startUrl) }
    var isLoading by remember { mutableStateOf(true) }
    var progress by remember { mutableIntStateOf(0) }
    var hasError by remember { mutableStateOf(false) }
    var canGoBack by remember { mutableStateOf(false) }
    var canGoForward by remember { mutableStateOf(false) }

    // Notifications Dialog & State
    var showNotificationDialog by remember { mutableStateOf(false) }
    var notificationsEnabled by remember {
        mutableStateOf(UpdateNotificationHelper.isNotificationsEnabled(context))
    }
    var isCheckingUpdates by remember { mutableStateOf(false) }
    var lastCheckTime by remember {
        mutableStateOf(UpdateNotificationHelper.getLastCheckTimeFormatted(context))
    }

    // Permission launcher for Android 13+
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            notificationsEnabled = true
            UpdateNotificationHelper.setNotificationsEnabled(context, true)
            Toast.makeText(context, "تم تفعيل إشعارات تحديث الموقع بنجاح 🔔", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, "يرجى منح إذن الإشعارات لتنبيهك عند كل تحديث", Toast.LENGTH_LONG).show()
        }
    }

    // Automatic check on launch in background
    LaunchedEffect(Unit) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val hasPerm = ContextCompat.checkSelfPermission(
                context,
                android.Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
            if (!hasPerm) {
                permissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS)
            }
        }
        UpdateNotificationHelper.checkWebsiteForUpdates(context)
        lastCheckTime = UpdateNotificationHelper.getLastCheckTimeFormatted(context)
    }

    // Intercept hardware back button to navigate web history
    BackHandler(enabled = canGoBack) {
        webViewInstance?.let { webView ->
            if (webView.canGoBack()) {
                webView.goBack()
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BfDarkBg)
            .statusBarsPadding()
            .navigationBarsPadding()
    ) {
        // Top Launcher App Bar
        Surface(
            color = BfDarkSurface,
            modifier = Modifier.fillMaxWidth(),
            border = androidx.compose.foundation.BorderStroke(1.dp, BfBorder.copy(alpha = 0.5f))
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    // Left: Logo & FahdAlsobky فهد السبكي
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(start = 4.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(34.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(BfDarkSurfaceVariant)
                                .border(1.dp, BfPurplePrimary, RoundedCornerShape(8.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.ic_blox_icon_1792079118760),
                                contentDescription = "Logo",
                                modifier = Modifier.size(28.dp).clip(RoundedCornerShape(6.dp))
                            )
                        }

                        Spacer(modifier = Modifier.width(8.dp))

                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "FahdAlsobky",
                                    color = Color.White,
                                    fontWeight = FontWeight.ExtraBold,
                                    fontSize = 13.sp,
                                    letterSpacing = 0.5.sp
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Surface(
                                    shape = RoundedCornerShape(4.dp),
                                    color = BfGold.copy(alpha = 0.18f),
                                    border = androidx.compose.foundation.BorderStroke(0.8.dp, BfGold.copy(alpha = 0.7f))
                                ) {
                                    Text(
                                        text = "فهد السبكي",
                                        color = BfGoldLight,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 11.sp,
                                        modifier = Modifier.padding(horizontal = 5.dp, vertical = 1.dp)
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(2.dp))
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(6.dp)
                                        .clip(CircleShape)
                                        .background(BfCyan)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "bloxfruitsvalues.com",
                                    color = BfPurpleLight,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }
                    }

                    // Right: Navigation Actions (Notifications Bell, Back, Forward, Refresh, Open in Browser)
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        // Notification Bell Button
                        IconButton(
                            onClick = { showNotificationDialog = true },
                            modifier = Modifier
                                .size(36.dp)
                                .testTag("btn_notifications")
                        ) {
                            Icon(
                                imageVector = if (notificationsEnabled) Icons.Default.NotificationsActive else Icons.Default.Notifications,
                                contentDescription = "إشعارات التحديثات",
                                tint = if (notificationsEnabled) BfGold else BfTextMuted,
                                modifier = Modifier.size(20.dp)
                            )
                        }

                        IconButton(
                            onClick = { webViewInstance?.goBack() },
                            enabled = canGoBack,
                            modifier = Modifier
                                .size(36.dp)
                                .testTag("btn_back")
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "للخلف",
                                tint = if (canGoBack) BfPurpleLight else BfTextMuted,
                                modifier = Modifier.size(20.dp)
                            )
                        }

                        IconButton(
                            onClick = { webViewInstance?.goForward() },
                            enabled = canGoForward,
                            modifier = Modifier
                                .size(36.dp)
                                .testTag("btn_forward")
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                contentDescription = "للأمام",
                                tint = if (canGoForward) BfPurpleLight else BfTextMuted,
                                modifier = Modifier.size(20.dp)
                            )
                        }

                        IconButton(
                            onClick = {
                                hasError = false
                                webViewInstance?.reload()
                            },
                            modifier = Modifier
                                .size(36.dp)
                                .testTag("btn_reload")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Refresh,
                                contentDescription = "إعادة تحميل",
                                tint = BfCyan,
                                modifier = Modifier.size(20.dp)
                            )
                        }

                        IconButton(
                            onClick = {
                                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(currentUrl))
                                context.startActivity(intent)
                            },
                            modifier = Modifier
                                .size(36.dp)
                                .testTag("btn_open_browser")
                        ) {
                            Icon(
                                imageVector = Icons.Default.OpenInBrowser,
                                contentDescription = "فتح بالمتصفح الخارجي",
                                tint = BfGold,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }

                // Quick Navigation Shortcut Chips
                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp, vertical = 4.dp),
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    contentPadding = PaddingValues(bottom = 4.dp)
                ) {
                    item {
                        FilterChip(
                            selected = currentUrl.trimEnd('/') == BASE_URL,
                            onClick = {
                                hasError = false
                                webViewInstance?.loadUrl(BASE_URL)
                            },
                            label = { Text("🏠 الرئيسية", fontSize = 11.sp) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = BfPurplePrimary,
                                selectedLabelColor = Color.White,
                                containerColor = BfDarkCard,
                                labelColor = BfTextSecondary
                            ),
                            modifier = Modifier.testTag("chip_home")
                        )
                    }

                    item {
                        FilterChip(
                            selected = currentUrl.contains("/calculator"),
                            onClick = {
                                hasError = false
                                webViewInstance?.loadUrl("$BASE_URL/calculator")
                            },
                            label = { Text("🧮 الحاسبة (Calculator)", fontSize = 11.sp) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = BfPurplePrimary,
                                selectedLabelColor = Color.White,
                                containerColor = BfDarkCard,
                                labelColor = BfTextSecondary
                            ),
                            modifier = Modifier.testTag("chip_calculator")
                        )
                    }

                    item {
                        FilterChip(
                            selected = currentUrl.contains("/values"),
                            onClick = {
                                hasError = false
                                webViewInstance?.loadUrl("$BASE_URL/values")
                            },
                            label = { Text("💎 قيم الفواكه (Values)", fontSize = 11.sp) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = BfPurplePrimary,
                                selectedLabelColor = Color.White,
                                containerColor = BfDarkCard,
                                labelColor = BfTextSecondary
                            ),
                            modifier = Modifier.testTag("chip_values")
                        )
                    }

                    item {
                        FilterChip(
                            selected = currentUrl.contains("/gamepasses"),
                            onClick = {
                                hasError = false
                                webViewInstance?.loadUrl("$BASE_URL/gamepasses")
                            },
                            label = { Text("🎮 جيم باس (Gamepasses)", fontSize = 11.sp) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = BfPurplePrimary,
                                selectedLabelColor = Color.White,
                                containerColor = BfDarkCard,
                                labelColor = BfTextSecondary
                            ),
                            modifier = Modifier.testTag("chip_gamepasses")
                        )
                    }
                }

                // Loading progress indicator
                if (isLoading) {
                    LinearProgressIndicator(
                        progress = { progress / 100f },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(2.5.dp),
                        color = BfCyan,
                        trackColor = BfDarkSurfaceVariant
                    )
                }
            }
        }

        // Main WebView area
        Box(modifier = Modifier.weight(1f).fillMaxWidth()) {
            AndroidView(
                factory = { ctx ->
                    WebView(ctx).apply {
                        layoutParams = ViewGroup.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT
                        )

                        // Cookie setup
                        val cookieManager = CookieManager.getInstance()
                        cookieManager.setAcceptCookie(true)
                        cookieManager.setAcceptThirdPartyCookies(this, true)

                        // WebSettings for full modern web application support
                        settings.apply {
                            javaScriptEnabled = true
                            domStorageEnabled = true
                            databaseEnabled = true
                            useWideViewPort = true
                            loadWithOverviewMode = true
                            setSupportZoom(true)
                            builtInZoomControls = true
                            displayZoomControls = false
                            mixedContentMode = WebSettings.MIXED_CONTENT_COMPATIBILITY_MODE
                            cacheMode = WebSettings.LOAD_DEFAULT
                            userAgentString = settings.userAgentString + " BloxFruitsValuesLauncher/1.0"
                        }

                        webViewClient = object : WebViewClient() {
                            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                                super.onPageStarted(view, url, favicon)
                                isLoading = true
                                url?.let { currentUrl = it }
                                canGoBack = canGoBack()
                                canGoForward = canGoForward()
                            }

                            override fun onPageFinished(view: WebView?, url: String?) {
                                super.onPageFinished(view, url)
                                isLoading = false
                                url?.let { currentUrl = it }
                                canGoBack = canGoBack()
                                canGoForward = canGoForward()
                            }

                            override fun shouldOverrideUrlLoading(
                                view: WebView?,
                                request: WebResourceRequest?
                            ): Boolean {
                                val targetUrl = request?.url?.toString() ?: return false
                                // Stay inside WebView for bloxfruitsvalues.com
                                if (targetUrl.contains("bloxfruitsvalues.com")) {
                                    return false
                                }
                                // External protocols (roblox, discord, mailto, etc.) or external sites open via Intent
                                return try {
                                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(targetUrl))
                                    ctx.startActivity(intent)
                                    true
                                } catch (e: Exception) {
                                    false
                                }
                            }

                            override fun onReceivedError(
                                view: WebView?,
                                request: WebResourceRequest?,
                                error: WebResourceError?
                            ) {
                                super.onReceivedError(view, request, error)
                                if (request?.isForMainFrame == true) {
                                    hasError = true
                                    isLoading = false
                                }
                            }
                        }

                        webChromeClient = object : WebChromeClient() {
                            override fun onProgressChanged(view: WebView?, newProgress: Int) {
                                super.onProgressChanged(view, newProgress)
                                progress = newProgress
                                if (newProgress >= 100) {
                                    isLoading = false
                                }
                            }
                        }

                        loadUrl(currentUrl)
                        webViewInstance = this
                    }
                },
                modifier = Modifier
                    .fillMaxSize()
                    .testTag("webview_main_container")
            )

            // Offline / Connection error fallback
            if (hasError) {
                Surface(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                    color = BfDarkSurface.copy(alpha = 0.96f),
                    shape = RoundedCornerShape(16.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, BfBorder)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Box(
                            modifier = Modifier
                                .size(64.dp)
                                .clip(CircleShape)
                                .background(BfWarningAmber.copy(alpha = 0.2f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Warning,
                                contentDescription = null,
                                tint = BfWarningAmber,
                                modifier = Modifier.size(36.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = "تعذر الاتصال بموقع bloxfruitsvalues.com",
                            color = BfTextPrimary,
                            fontWeight = FontWeight.Bold,
                            fontSize = 17.sp
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "يرجى التحقق من اتصالك بالإنترنت ثم الضغط على زر إعادة المحاولة لفتح الموقع.",
                            color = BfTextSecondary,
                            fontSize = 13.sp,
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                            lineHeight = 18.sp
                        )

                        Spacer(modifier = Modifier.height(24.dp))

                        Button(
                            onClick = {
                                hasError = false
                                isLoading = true
                                webViewInstance?.reload()
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = BfPurplePrimary,
                                contentColor = Color.White
                            ),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.testTag("btn_retry_connection")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Refresh,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("إعادة المحاولة الآن", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }

    // Website Update Notifications Dialog
    if (showNotificationDialog) {
        Dialog(onDismissRequest = { showNotificationDialog = false }) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth(0.95f)
                    .clip(RoundedCornerShape(20.dp))
                    .border(1.5.dp, BfPurplePrimary, RoundedCornerShape(20.dp)),
                color = BfDarkSurface
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                ) {
                    // Dialog Header
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(38.dp)
                                    .clip(CircleShape)
                                    .background(BfGold.copy(alpha = 0.2f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.NotificationsActive,
                                    contentDescription = null,
                                    tint = BfGold,
                                    modifier = Modifier.size(22.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = "إشعارات تحديث الموقع",
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp
                                )
                                Text(
                                    text = "bloxfruitsvalues.com Alerts",
                                    color = BfPurpleLight,
                                    fontSize = 11.sp
                                )
                            }
                        }

                        IconButton(
                            onClick = { showNotificationDialog = false },
                            modifier = Modifier.size(32.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "إغلاق",
                                tint = BfTextSecondary
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Switch Row
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(BfDarkSurfaceVariant)
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "تفعيل إشعارات التحديثات",
                                color = BfTextPrimary,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp
                            )
                            Text(
                                text = "إرسال إشعار فوري عند تعديل قيم الفواكه أو نزول فواكه وجيم باس جديدة",
                                color = BfTextSecondary,
                                fontSize = 11.sp,
                                lineHeight = 16.sp
                            )
                        }

                        Switch(
                            checked = notificationsEnabled,
                            onCheckedChange = { isChecked ->
                                if (isChecked && Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                                    val hasPerm = ContextCompat.checkSelfPermission(
                                        context,
                                        android.Manifest.permission.POST_NOTIFICATIONS
                                    ) == PackageManager.PERMISSION_GRANTED
                                    if (!hasPerm) {
                                        permissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS)
                                        return@Switch
                                    }
                                }
                                notificationsEnabled = isChecked
                                UpdateNotificationHelper.setNotificationsEnabled(context, isChecked)
                                val msg = if (isChecked) "تم تفعيل الإشعارات بنجاح" else "تم إيقاف الإشعارات"
                                Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                            },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = BfGold,
                                checkedTrackColor = BfPurplePrimary,
                                uncheckedThumbColor = BfTextMuted,
                                uncheckedTrackColor = BfDarkCard
                            ),
                            modifier = Modifier.testTag("switch_notifications_toggle")
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Last Checked Status
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "آخر فحص لتحديثات الموقع:",
                            color = BfTextSecondary,
                            fontSize = 11.sp
                        )
                        Text(
                            text = lastCheckTime,
                            color = BfCyan,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }

                    Spacer(modifier = Modifier.height(18.dp))

                    // Action: Check For Updates Now
                    Button(
                        onClick = {
                            coroutineScope.launch {
                                isCheckingUpdates = true
                                val hasNewUpdate = UpdateNotificationHelper.checkWebsiteForUpdates(context)
                                isCheckingUpdates = false
                                lastCheckTime = UpdateNotificationHelper.getLastCheckTimeFormatted(context)
                                if (!hasNewUpdate) {
                                    Toast.makeText(context, "أنت على أحدث نسخة من الموقع حالياً! ✅", Toast.LENGTH_SHORT).show()
                                }
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = BfPurplePrimary),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(44.dp)
                            .testTag("btn_check_updates_now"),
                        enabled = !isCheckingUpdates
                    ) {
                        if (isCheckingUpdates) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(18.dp),
                                color = Color.White,
                                strokeWidth = 2.dp
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("جاري فحص الموقع...", fontSize = 12.sp)
                        } else {
                            Icon(
                                imageVector = Icons.Default.Sync,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("فحص التحديثات الآن 🔄", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Action: Send Test Notification
                    OutlinedButton(
                        onClick = {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                                val hasPerm = ContextCompat.checkSelfPermission(
                                    context,
                                    android.Manifest.permission.POST_NOTIFICATIONS
                                ) == PackageManager.PERMISSION_GRANTED
                                if (!hasPerm) {
                                    permissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS)
                                    return@OutlinedButton
                                }
                            }
                            UpdateNotificationHelper.showUpdateNotification(
                                context,
                                title = "🔔 إشعار تجريبي: تحديث قيم Blox Fruits!",
                                body = "تم رصد تحديث جديد لقيم الفواكه في bloxfruitsvalues.com. اضغط للمشاهدة الآن!",
                                targetUrl = "https://bloxfruitsvalues.com"
                            )
                            Toast.makeText(context, "تم إرسال إشعار تجريبي إلى هاتفك! 🔔", Toast.LENGTH_SHORT).show()
                        },
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = BfGold),
                        border = androidx.compose.foundation.BorderStroke(1.dp, BfGold.copy(alpha = 0.7f)),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(44.dp)
                            .testTag("btn_test_notification")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Notifications,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp),
                            tint = BfGold
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("إرسال إشعار تجريبي للهاتف 📱", fontWeight = FontWeight.SemiBold, fontSize = 12.sp)
                    }
                }
            }
        }
    }
}
