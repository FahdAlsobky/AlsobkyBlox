package com.example.ui.screens

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.compose.BackHandler
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.OpenInBrowser
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.ui.theme.BfBorder
import com.example.ui.theme.BfCyan
import com.example.ui.theme.BfDarkBg
import com.example.ui.theme.BfDarkCard
import com.example.ui.theme.BfDarkSurface
import com.example.ui.theme.BfDarkSurfaceVariant
import com.example.ui.theme.BfGold
import com.example.ui.theme.BfPurpleLight
import com.example.ui.theme.BfPurplePrimary
import com.example.ui.theme.BfTextMuted
import com.example.ui.theme.BfTextPrimary
import com.example.ui.theme.BfTextSecondary
import com.example.ui.theme.BfWarningAmber

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun WebPortalScreen(
    initialUrl: String = "https://bloxfruitsvalues.com",
    onSwitchToNativeCalculator: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var webViewInstance by remember { mutableStateOf<WebView?>(null) }
    var currentUrl by remember { mutableStateOf(initialUrl) }
    var pageTitle by remember { mutableStateOf("Blox Fruits Values") }
    var isLoading by remember { mutableStateOf(true) }
    var progress by remember { mutableIntStateOf(0) }
    var hasError by remember { mutableStateOf(false) }
    var canGoBack by remember { mutableStateOf(false) }
    var canGoForward by remember { mutableStateOf(false) }

    // Intercept hardware back button to navigate WebView back
    BackHandler(enabled = canGoBack) {
        webViewInstance?.goBack()
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(BfDarkBg)
    ) {
        // Web Control Bar
        Surface(
            color = BfDarkSurface,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    // Navigation controls
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        IconButton(
                            onClick = { webViewInstance?.goBack() },
                            enabled = canGoBack,
                            modifier = Modifier.testTag("webview_back_button")
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "للخلف",
                                tint = if (canGoBack) BfPurpleLight else BfTextMuted
                            )
                        }

                        IconButton(
                            onClick = { webViewInstance?.goForward() },
                            enabled = canGoForward,
                            modifier = Modifier.testTag("webview_forward_button")
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                contentDescription = "للأمام",
                                tint = if (canGoForward) BfPurpleLight else BfTextMuted
                            )
                        }

                        IconButton(
                            onClick = {
                                hasError = false
                                webViewInstance?.reload()
                            },
                            modifier = Modifier.testTag("webview_refresh_button")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Refresh,
                                contentDescription = "تحديث",
                                tint = BfCyan
                            )
                        }
                    }

                    // URL address pill
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 4.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(BfDarkSurfaceVariant)
                            .border(1.dp, BfBorder, RoundedCornerShape(8.dp))
                            .padding(horizontal = 8.dp, vertical = 6.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "🔒",
                                fontSize = 11.sp
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = currentUrl.replace("https://", "").replace("http://", ""),
                                color = BfTextPrimary,
                                fontSize = 11.sp,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }

                    // Open in external browser
                    IconButton(
                        onClick = {
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(currentUrl))
                            context.startActivity(intent)
                        },
                        modifier = Modifier.testTag("webview_open_external_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.OpenInBrowser,
                            contentDescription = "فتح في المتصفح الخارجي",
                            tint = BfGold
                        )
                    }
                }

                // Quick links row
                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp, vertical = 4.dp),
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    contentPadding = PaddingValues(bottom = 4.dp)
                ) {
                    item {
                        FilterChip(
                            selected = currentUrl.trimEnd('/') == "https://bloxfruitsvalues.com",
                            onClick = {
                                hasError = false
                                webViewInstance?.loadUrl("https://bloxfruitsvalues.com")
                            },
                            label = { Text("🏠 الرئيسية", fontSize = 11.sp) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = BfPurplePrimary,
                                selectedLabelColor = Color.White,
                                containerColor = BfDarkCard,
                                labelColor = BfTextSecondary
                            )
                        )
                    }

                    item {
                        FilterChip(
                            selected = currentUrl.contains("/calculator"),
                            onClick = {
                                hasError = false
                                webViewInstance?.loadUrl("https://bloxfruitsvalues.com/calculator")
                            },
                            label = { Text("🧮 حاسبة الموقع", fontSize = 11.sp) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = BfPurplePrimary,
                                selectedLabelColor = Color.White,
                                containerColor = BfDarkCard,
                                labelColor = BfTextSecondary
                            )
                        )
                    }

                    item {
                        FilterChip(
                            selected = currentUrl.contains("/values"),
                            onClick = {
                                hasError = false
                                webViewInstance?.loadUrl("https://bloxfruitsvalues.com/values")
                            },
                            label = { Text("💎 قيم الفواكه", fontSize = 11.sp) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = BfPurplePrimary,
                                selectedLabelColor = Color.White,
                                containerColor = BfDarkCard,
                                labelColor = BfTextSecondary
                            )
                        )
                    }

                    item {
                        FilterChip(
                            selected = currentUrl.contains("/gamepasses"),
                            onClick = {
                                hasError = false
                                webViewInstance?.loadUrl("https://bloxfruitsvalues.com/gamepasses")
                            },
                            label = { Text("🎮 جيم باس", fontSize = 11.sp) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = BfPurplePrimary,
                                selectedLabelColor = Color.White,
                                containerColor = BfDarkCard,
                                labelColor = BfTextSecondary
                            )
                        )
                    }
                }

                // Progress Bar
                if (isLoading) {
                    LinearProgressIndicator(
                        progress = { progress / 100f },
                        modifier = Modifier.fillMaxWidth(),
                        color = BfPurplePrimary,
                        trackColor = BfDarkSurfaceVariant,
                    )
                }
            }
        }

        // Web Content or Error State
        Box(modifier = Modifier.weight(1f)) {
            AndroidView(
                factory = { ctx ->
                    WebView(ctx).apply {
                        layoutParams = ViewGroup.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT
                        )

                        settings.apply {
                            javaScriptEnabled = true
                            domStorageEnabled = true
                            databaseEnabled = true
                            useWideViewPort = true
                            loadWithOverviewMode = true
                            setSupportZoom(true)
                            builtInZoomControls = true
                            displayZoomControls = false
                            cacheMode = WebSettings.LOAD_DEFAULT
                            userAgentString = settings.userAgentString + " BloxFruitsValuesApp/1.0"
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
                                pageTitle = view?.title ?: "Blox Fruits Values"
                                canGoBack = canGoBack()
                                canGoForward = canGoForward()
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

                            override fun onReceivedTitle(view: WebView?, title: String?) {
                                super.onReceivedTitle(view, title)
                                title?.let { pageTitle = it }
                            }
                        }

                        loadUrl(initialUrl)
                        webViewInstance = this
                    }
                },
                modifier = Modifier.fillMaxSize()
            )

            // Offline / Error banner overlay if network is down
            if (hasError) {
                Surface(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                    color = BfDarkSurface.copy(alpha = 0.95f),
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
                        Icon(
                            imageVector = Icons.Default.Warning,
                            contentDescription = null,
                            tint = BfWarningAmber,
                            modifier = Modifier.size(52.dp)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "تعذر تحميل موقع bloxfruitsvalues.com",
                            color = BfTextPrimary,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "تأكد من اتصالك بالإنترنت، أو يمكنك استخدام الحاسبة وقاعدة بيانات الفواكه المدمجة في التطبيق دون إنترنت!",
                            color = BfTextSecondary,
                            fontSize = 13.sp,
                            lineHeight = 18.sp
                        )
                        Spacer(modifier = Modifier.height(20.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                            Button(
                                onClick = {
                                    hasError = false
                                    isLoading = true
                                    webViewInstance?.reload()
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = BfPurplePrimary)
                            ) {
                                Text("إعادة المحاولة")
                            }

                            Button(
                                onClick = onSwitchToNativeCalculator,
                                colors = ButtonDefaults.buttonColors(containerColor = BfGold, contentColor = Color.Black)
                            ) {
                                Text("الحاسبة المدمجة")
                            }
                        }
                    }
                }
            }
        }
    }
}
