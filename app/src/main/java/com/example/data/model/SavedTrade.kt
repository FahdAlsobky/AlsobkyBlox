package com.example.data.model

import android.content.Context
import android.content.SharedPreferences
import com.example.data.repository.FruitRepository
import org.json.JSONArray
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class SavedTrade(
    val id: String = java.util.UUID.randomUUID().toString(),
    val timestamp: Long = System.currentTimeMillis(),
    val yourItems: List<TradeSlotItem>,
    val theirItems: List<TradeSlotItem>,
    val evaluation: TradeEvaluation,
    val note: String = ""
) {
    val formattedDate: String
        get() {
            val sdf = SimpleDateFormat("yyyy/MM/dd HH:mm", Locale.getDefault())
            return sdf.format(Date(timestamp))
        }
}

class SavedTradesRepository(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("blox_saved_trades", Context.MODE_PRIVATE)

    fun getSavedTrades(): List<SavedTrade> {
        val jsonString = prefs.getString("trades_list", null) ?: return emptyList()
        val list = mutableListOf<SavedTrade>()
        try {
            val jsonArray = JSONArray(jsonString)
            for (i in 0 until jsonArray.length()) {
                val obj = jsonArray.getJSONObject(i)
                val id = obj.getString("id")
                val timestamp = obj.getLong("timestamp")
                val note = obj.optString("note", "")

                val yourItems = parseSlotItems(obj.getJSONArray("your_items"))
                val theirItems = parseSlotItems(obj.getJSONArray("their_items"))

                val evaluation = TradeEvaluator.evaluate(yourItems, theirItems)
                list.add(SavedTrade(id, timestamp, yourItems, theirItems, evaluation, note))
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return list.sortedByDescending { it.timestamp }
    }

    fun saveTrade(trade: SavedTrade) {
        val current = getSavedTrades().toMutableList()
        current.removeAll { it.id == trade.id }
        current.add(0, trade)
        persist(current)
    }

    fun deleteTrade(tradeId: String) {
        val current = getSavedTrades().toMutableList()
        current.removeAll { it.id == tradeId }
        persist(current)
    }

    fun clearAll() {
        prefs.edit().remove("trades_list").apply()
    }

    private fun persist(trades: List<SavedTrade>) {
        val jsonArray = JSONArray()
        for (trade in trades) {
            val obj = JSONObject()
            obj.put("id", trade.id)
            obj.put("timestamp", trade.timestamp)
            obj.put("note", trade.note)

            val yourArr = JSONArray()
            trade.yourItems.forEach { slot ->
                val slotObj = JSONObject()
                slotObj.put("fruit_id", slot.fruit.id)
                slotObj.put("is_perm", slot.isPerm)
                yourArr.put(slotObj)
            }
            obj.put("your_items", yourArr)

            val theirArr = JSONArray()
            trade.theirItems.forEach { slot ->
                val slotObj = JSONObject()
                slotObj.put("fruit_id", slot.fruit.id)
                slotObj.put("is_perm", slot.isPerm)
                theirArr.put(slotObj)
            }
            obj.put("their_items", theirArr)

            jsonArray.put(obj)
        }
        prefs.edit().putString("trades_list", jsonArray.toString()).apply()
    }

    private fun parseSlotItems(arr: JSONArray): List<TradeSlotItem> {
        val items = mutableListOf<TradeSlotItem>()
        for (i in 0 until arr.length()) {
            val o = arr.getJSONObject(i)
            val fruitId = o.getString("fruit_id")
            val isPerm = o.optBoolean("is_perm", false)
            val fruit = FruitRepository.findById(fruitId)
            if (fruit != null) {
                items.add(TradeSlotItem(fruit, isPerm))
            }
        }
        return items
    }
}
