package com.example.lol_research_center.model

import android.content.Context
import com.google.gson.Gson


data class JsonItemData(
    val image_name: String,
    val name: String,
    val stats: Stats
)

object ItemDataLoader {
    fun loadItemsFromAsset(context: Context, fileName: String): List<ItemData> {
        val jsonString: String
        try {
            jsonString = context.assets.open(fileName).bufferedReader().use { it.readText() }
        } catch (ioException: java.io.IOException) {
            ioException.printStackTrace()
            return emptyList()
        }

        val listType = object : com.google.gson.reflect.TypeToken<List<JsonItemData>>() {}.type
        val jsonItems: List<JsonItemData> = Gson().fromJson(jsonString, listType)

        return jsonItems.map { jsonItem ->
            val imageResId = context.resources.getIdentifier(
                jsonItem.image_name,
                "drawable",
                context.packageName
            )
            ItemData(imageResId, jsonItem.name, jsonItem.stats)
        }
    }
}