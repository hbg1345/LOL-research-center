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
    val skills: Skills,
    val lore: String
) : Parcelable

@Parcelize
data class Stats(
    val armorPenetration: Float,
    val armorPenetrationPercent: Float,
    val magicPenetration: Float,
    val magicPenetrationPercent: Float,
    val attackdamage: Int, //
    val attackdamageperlevel: Float,
    val ap: Int, //
    val hp: Int, //
    val mp: Int, //
    val crit: Int, //
    val attackspeed: Float, //
    val attackspeedperlevel: Float,
    val armor: Int,//
    val spellblock: Int, //
    val hpperlevel: Int,
    val mpperlevel: Int,
    val movespeed: Int,
    val armorperlevel: Float,
    val spellblockperlevel: Float,
    val hpregen: Float,
    val hpregenperlevel: Float,
    val mpregen: Float,
    val mpregenperlevel: Float,
    val critperlevel: Float // 오타 수정: critperlever → critperlevel
) : Parcelable


@Parcelize
data class Skill(
    val skillTitle : String,
    val skillDrawable : Int,
    var skillLevel: Int, //스킬 레벨
    val skillDamageAd: List<Int>, //기본 스킬 데미지
    val skillDamageAp: List<Int>,
    val skillDamageFix: List<Int>,
    val coolDown: List<Int>,
    val cost : List<Int>,
    val skillApCoeff: Float,
    val skillAdCoeff: Float,
    val skillArCoeff: Float,
    val skillMrCoeff: Float,
    val skillHpCoeff: Float,
    val skillType: String,
    val skillInfo: String
) : Parcelable
//챔피언 이름, 타입,