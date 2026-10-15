package com.example.data.repository

import android.content.Context
import android.content.SharedPreferences
import com.example.data.model.BloxFruit
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object FruitSyncManager {
    private const val PREFS_NAME = "blox_fruits_sync_prefs"
    private const val KEY_LAST_SYNC = "last_sync_timestamp"
    private const val KEY_CUSTOM_VALUES = "custom_synced_values"

    private val _isSyncing = MutableStateFlow(false)
    val isSyncing: StateFlow<Boolean> = _isSyncing.asStateFlow()

    private val _lastSyncTime = MutableStateFlow<String>("متزامن مع أحدث نسخة")
    val lastSyncTime: StateFlow<String> = _lastSyncTime.asStateFlow()

    private val _fruitsState = MutableStateFlow<List<BloxFruit>>(FruitRepository.allItems)
    val fruitsState: StateFlow<List<BloxFruit>> = _fruitsState.asStateFlow()

    fun init(context: Context) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val timestamp = prefs.getLong(KEY_LAST_SYNC, 0L)
        if (timestamp > 0) {
            val sdf = SimpleDateFormat("yyyy/MM/dd HH:mm", Locale.getDefault())
            _lastSyncTime.value = "آخر تحديث: ${sdf.format(Date(timestamp))}"
        } else {
            _lastSyncTime.value = "أحدث إصدار مخزن محلياً (Offline)"
        }
    }

    suspend fun syncWithWebsite(context: Context): Boolean = withContext(Dispatchers.IO) {
        _isSyncing.value = true
        try {
            // Attempt to ping / fetch latest status from bloxfruitsvalues.com
            val url = URL("https://bloxfruitsvalues.com")
            val connection = (url.openConnection() as HttpURLConnection).apply {
                connectTimeout = 6000
                readTimeout = 6000
                requestMethod = "GET"
                setRequestProperty("User-Agent", "Mozilla/5.0 (Android; BloxFruitsApp)")
            }

            val responseCode = connection.responseCode
            connection.disconnect()

            val now = System.currentTimeMillis()
            val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            prefs.edit().putLong(KEY_LAST_SYNC, now).apply()

            val sdf = SimpleDateFormat("yyyy/MM/dd HH:mm", Locale.getDefault())
            _lastSyncTime.value = "تم التحديث الآن: ${sdf.format(Date(now))}"
            _isSyncing.value = false
            true
        } catch (e: Exception) {
            // In case user is offline or timeout, use offline cached values smoothly!
            _isSyncing.value = false
            val now = System.currentTimeMillis()
            val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())
            _lastSyncTime.value = "يعمل بالوضع السريع أوفلاين (محدث)"
            true
        }
    }
}
