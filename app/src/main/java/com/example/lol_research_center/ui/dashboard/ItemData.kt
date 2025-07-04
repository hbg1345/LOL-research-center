package com.example.lol_research_center.ui.dashboard

data class ItemData(
    val imageResId: Int,
    val name: String,
    val attributes: Map<String, Int> = emptyMap()
)