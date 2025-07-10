package com.example.lol_research_center.model

data class SkillCombo(
    val name: String,
    val skillDrawables: List<Int>, // 10개
    val skillKeys: List<String>,   // 10개 (예: "Q", "W"...)
    val damage: Int,
    val description: String
)