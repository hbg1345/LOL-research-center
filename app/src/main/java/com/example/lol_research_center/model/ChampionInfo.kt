package com.example.lol_research_center.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

// 모델은 화면·탭 어디서든 재사용할 수 있게 패키지 하나에 모아 둡니다.
@Parcelize
data class Skills(
    val p: Skill,
    val q: Skill,
    val w: Skill,
    val e: Skill,
    val r: Skill
) : Parcelable

enum class Lane { TOP, JUNGLE, MID, ADC, SUPPORT }

@Parcelize
data class ChampionInfo(
    val champDrawable: Int,
    val name: String,
    val lane: Lane,              // ← type → lane
    val stats: Stats,
    val itemDrawables: List<Int>,
    val skills: Skills
) : Parcelable

@Parcelize
data class Stats(
    val ad: Int, val ap: Int, val hp: Int, val mp: Int,
    val critical: Int, val attackSpeed: Int, val ar: Int, val mr: Int,
) : Parcelable

@Parcelize
data class Skill(
    val skillLevel : Int, //스킬 레벨
    val skillDamage : List<Int>, //기본 스킬 데미지
    val skillApCoeff : Float,
    val skillAdCoeff : Float,
    val skillArCoeff: Float,
    val skillMrCoeff : Float,
    val skillHpCoeff : Float,
    val skillType : String,
) : Parcelable
//챔피언 이름, 타입,