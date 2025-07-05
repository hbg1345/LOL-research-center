package com.example.lol_research_center.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ItemData(
    val imageResId: Int,
    val name: String,
    val stats: Stats,
)  : Parcelable