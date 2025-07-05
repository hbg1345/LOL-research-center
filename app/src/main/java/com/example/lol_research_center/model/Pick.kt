package com.example.lol_research_center.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Pick(
    var championInfo : ChampionInfo,
    var itemList: List<ItemData>,
    var computedStats: Stats
) : Parcelable
