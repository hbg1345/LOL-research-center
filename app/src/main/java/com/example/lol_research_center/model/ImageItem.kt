package com.example.lol_research_center.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

// 모델은 화면·탭 어디서든 재사용할 수 있게 패키지 하나에 모아 둡니다.
@Parcelize
data class ImageItem(
    val champDrawable: Int,   // 챔피언 초상화
    val name: String,
    val type: String,
    val stats: Stats,
    val itemDrawables: List<Int>   // ★ 아이템 6개의 이미지도 Int 리스트로
) : Parcelable

@Parcelize
data class Stats(
    val ad: Int, val ap: Int, val hp: Int, val mp: Int,
    val critical: Int, val attackSpeed: Int, val def: Int, val mr: Int
) : Parcelable

//챔피언 이름, 타입,