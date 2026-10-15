package com.example.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.example.MainActivity
import com.example.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.HttpURLConnection
import java.net.URL
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object UpdateNotificationHelper {
    const val CHANNEL_ID = "blox_fruits_updates_channel"
    private const val PREFS_NAME = "bf_notification_prefs"
    private const val KEY_NOTIFICATIONS_ENABLED = "notifications_enabled"
    private const val KEY_LAST_UPDATE_TAG = "last_website_tag"
    private const val KEY_LAST_CHECK_TIME = "last_check_timestamp"

    fun initChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "تحديثات أسعار بلوكس فروت"
            val descriptionText = "إشعارات فورية عند تحديث قيم الفواكه والجيم باس في موقع bloxfruitsvalues.com"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
                enableVibration(true)
                setShowBadge(true)
            }
            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    fun isNotificationsEnabled(context: Context): Boolean {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return prefs.getBoolean(KEY_NOTIFICATIONS_ENABLED, true)
    }

    fun setNotificationsEnabled(context: Context, enabled: Boolean) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit().putBoolean(KEY_NOTIFICATIONS_ENABLED, enabled).apply()
    }

    fun getLastCheckTimeFormatted(context: Context): String {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val ts = prefs.getLong(KEY_LAST_CHECK_TIME, 0L)
        return if (ts > 0) {
            val sdf = SimpleDateFormat("HH:mm - yyyy/MM/dd", Locale.getDefault())
            sdf.format(Date(ts))
        } else {
            "لم يتم الفحص بعد"
        }
    }

    fun showUpdateNotification(
        context: Context,
        title: String = "🔔 تحديث جديد في أسعار Blox Fruits!",
        body: String = "تم تحديث قيم الفواكه والطلب في bloxfruitsvalues.com. اضغط للمشاهدة الآن!",
        targetUrl: String = "https://bloxfruitsvalues.com"
    ) {
        // Check permission on Android 13+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val hasPermission = ContextCompat.checkSelfPermission(
                context,
                android.Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
            if (!hasPermission) return
        }

        if (!isNotificationsEnabled(context)) return

        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            putExtra("TARGET_URL", targetUrl)
        }

        val pendingIntent = PendingIntent.getActivity(
            context,
            101,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_blox_icon_1792079118760)
            .setContentTitle(title)
            .setContentText(body)
            .setStyle(NotificationCompat.BigTextStyle().bigText(body))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .build()

        try {
            val manager = NotificationManagerCompat.from(context)
            manager.notify(System.currentTimeMillis().toInt(), notification)
        } catch (e: SecurityException) {
            e.printStackTrace()
        }
    }

    suspend fun checkWebsiteForUpdates(context: Context): Boolean = withContext(Dispatchers.IO) {
        try {
            val url = URL("https://bloxfruitsvalues.com")
            val connection = (url.openConnection() as HttpURLConnection).apply {
                connectTimeout = 7000
                readTimeout = 7000
                requestMethod = "HEAD"
                setRequestProperty("User-Agent", "Mozilla/5.0 (Android; BloxFruitsApp)")
            }

            val eTag = connection.getHeaderField("ETag") ?: ""
            val lastModified = connection.getHeaderField("Last-Modified") ?: ""
            val currentSignature = "$eTag-$lastModified"
            connection.disconnect()

            val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            val previousSignature = prefs.getString(KEY_LAST_UPDATE_TAG, "")
            val now = System.currentTimeMillis()

            prefs.edit().putLong(KEY_LAST_CHECK_TIME, now).apply()

            if (currentSignature.isNotBlank() && currentSignature != previousSignature) {
                prefs.edit().putString(KEY_LAST_UPDATE_TAG, currentSignature).apply()
                // Website updated! Trigger notification
                withContext(Dispatchers.Main) {
                    showUpdateNotification(
                        context,
                        title = "🔥 تحديث جديد في موقع Blox Fruits Values!",
                        body = "تم رصد تحديث جديد لقيم الفواكه والجيم باس في موقع bloxfruitsvalues.com. تفقده الآن!",
                        targetUrl = "https://bloxfruitsvalues.com/values"
                    )
                }
                true
            } else {
                false
            }
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
}
