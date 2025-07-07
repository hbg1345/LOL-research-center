package com.example.lol_research_center.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import com.google.gson.annotations.SerializedName

@Parcelize
data class ItemData(
    val imageResId: Int,
    val name: String,
    val description: String? = null,
    val colloq: String? = null,
    val plaintext: String? = null,
    val into: List<String>? = null,
    val from: List<String>? = null,
    val image: ImageInfo? = null,
    val gold: GoldInfo? = null,
    val tags: List<String>? = null,
    val maps: Map<String, Boolean>? = null,
    val stats: ItemStats? = null,
    val consumed: Boolean? = null,
    val stacks: Int? = null,
    val depth: Int? = null,
    val inStore: Boolean? = null,
    val hideFromAll: Boolean? = null,
    val requiredChampion: String? = null,
    val requiredAlly: String? = null,
    val effect: Map<String, String>? = null,
    val specialRecipe: Int? = null,
    val group: String? = null,
    val rune: RuneInfo? = null
) : Parcelable

@Parcelize
data class ImageInfo(
    val full: String,
    val sprite: String,
    val group: String,
    val x: Int,
    val y: Int,
    val w: Int,
    val h: Int
) : Parcelable

@Parcelize
data class GoldInfo(
    val base: Int,
    val purchasable: Boolean,
    val total: Int,
    val sell: Int
) : Parcelable

@Parcelize
data class RuneInfo(
    val isrune: Boolean,
    val tier: Int,
    val type: String
) : Parcelable

@Parcelize
data class ItemStats(
    @SerializedName("FlatHPPoolMod") val flatHPPoolMod: Double? = null,
    @SerializedName("rFlatHPModPerLevel") val rFlatHPModPerLevel: Double? = null,
    @SerializedName("FlatMPPoolMod") val flatMPPoolMod: Double? = null,
    @SerializedName("rFlatMPModPerLevel") val rFlatMPModPerLevel: Double? = null,
    @SerializedName("PercentHPPoolMod") val percentHPPoolMod: Double? = null,
    @SerializedName("PercentMPPoolMod") val percentMPPoolMod: Double? = null,
    @SerializedName("FlatHPRegenMod") val flatHPRegenMod: Double? = null,
    @SerializedName("rFlatHPRegenModPerLevel") val rFlatHPRegenModPerLevel: Double? = null,
    @SerializedName("PercentHPRegenMod") val percentHPRegenMod: Double? = null,
    @SerializedName("FlatMPRegenMod") val flatMPRegenMod: Double? = null,
    @SerializedName("rFlatMPRegenModPerLevel") val rFlatMPRegenModPerLevel: Double? = null,
    @SerializedName("PercentMPRegenMod") val percentMPRegenMod: Double? = null,
    @SerializedName("FlatArmorMod") val flatArmorMod: Double? = null,
    @SerializedName("rFlatArmorModPerLevel") val rFlatArmorModPerLevel: Double? = null,
    @SerializedName("PercentArmorMod") val percentArmorMod: Double? = null,
    @SerializedName("rFlatArmorPenetrationMod") val rFlatArmorPenetrationMod: Double? = null,
    @SerializedName("rFlatArmorPenetrationModPerLevel") val rFlatArmorPenetrationModPerLevel: Double? = null,
    @SerializedName("rPercentArmorPenetrationMod") val rPercentArmorPenetrationMod: Double? = null,
    @SerializedName("rPercentArmorPenetrationModPerLevel") val rPercentArmorPenetrationModPerLevel: Double? = null,
    @SerializedName("FlatPhysicalDamageMod") val flatPhysicalDamageMod: Double? = null,
    @SerializedName("rFlatPhysicalDamageModPerLevel") val rFlatPhysicalDamageModPerLevel: Double? = null,
    @SerializedName("PercentPhysicalDamageMod") val percentPhysicalDamageMod: Double? = null,
    @SerializedName("FlatMagicDamageMod") val flatMagicDamageMod: Double? = null,
    @SerializedName("rFlatMagicDamageModPerLevel") val rFlatMagicDamageModPerLevel: Double? = null,
    @SerializedName("PercentMagicDamageMod") val percentMagicDamageMod: Double? = null,
    @SerializedName("FlatMovementSpeedMod") val flatMovementSpeedMod: Double? = null,
    @SerializedName("rFlatMovementSpeedModPerLevel") val rFlatMovementSpeedModPerLevel: Double? = null,
    @SerializedName("PercentMovementSpeedMod") val percentMovementSpeedMod: Double? = null,
    @SerializedName("rPercentMovementSpeedModPerLevel") val rPercentMovementSpeedModPerLevel: Double? = null,
    @SerializedName("FlatAttackSpeedMod") val flatAttackSpeedMod: Double? = null,
    @SerializedName("PercentAttackSpeedMod") val percentAttackSpeedMod: Double? = null,
    @SerializedName("rPercentAttackSpeedModPerLevel") val rPercentAttackSpeedModPerLevel: Double? = null,
    @SerializedName("rFlatDodgeMod") val rFlatDodgeMod: Double? = null,
    @SerializedName("rFlatDodgeModPerLevel") val rFlatDodgeModPerLevel: Double? = null,
    @SerializedName("PercentDodgeMod") val percentDodgeMod: Double? = null,
    @SerializedName("FlatCritChanceMod") val flatCritChanceMod: Double? = null,
    @SerializedName("rFlatCritChanceModPerLevel") val rFlatCritChanceModPerLevel: Double? = null,
    @SerializedName("PercentCritChanceMod") val percentCritChanceMod: Double? = null,
    @SerializedName("FlatCritDamageMod") val flatCritDamageMod: Double? = null,
    @SerializedName("rFlatCritDamageModPerLevel") val rFlatCritDamageModPerLevel: Double? = null,
    @SerializedName("PercentCritDamageMod") val percentCritDamageMod: Double? = null,
    @SerializedName("FlatBlockMod") val flatBlockMod: Double? = null,
    @SerializedName("PercentBlockMod") val percentBlockMod: Double? = null,
    @SerializedName("FlatSpellBlockMod") val flatSpellBlockMod: Double? = null,
    @SerializedName("rFlatSpellBlockModPerLevel") val rFlatSpellBlockModPerLevel: Double? = null,
    @SerializedName("PercentSpellBlockMod") val percentSpellBlockMod: Double? = null,
    @SerializedName("FlatEXPBonus") val flatEXPBonus: Double? = null,
    @SerializedName("PercentEXPBonus") val percentEXPBonus: Double? = null,
    @SerializedName("rPercentCooldownMod") val rPercentCooldownMod: Double? = null,
    @SerializedName("rPercentCooldownModPerLevel") val rPercentCooldownModPerLevel: Double? = null,
    @SerializedName("rFlatTimeDeadMod") val rFlatTimeDeadMod: Double? = null,
    @SerializedName("rFlatTimeDeadModPerLevel") val rFlatTimeDeadModPerLevel: Double? = null,
    @SerializedName("rPercentTimeDeadMod") val rPercentTimeDeadMod: Double? = null,
    @SerializedName("rPercentTimeDeadModPerLevel") val rPercentTimeDeadModPerLevel: Double? = null,
    @SerializedName("rFlatGoldPer10Mod") val rFlatGoldPer10Mod: Double? = null,
    @SerializedName("rFlatMagicPenetrationMod") val rFlatMagicPenetrationMod: Double? = null,
    @SerializedName("rFlatMagicPenetrationModPerLevel") val rFlatMagicPenetrationModPerLevel: Double? = null,
    @SerializedName("rPercentMagicPenetrationMod") val rPercentMagicPenetrationMod: Double? = null,
    @SerializedName("rPercentMagicPenetrationModPerLevel") val rPercentMagicPenetrationModPerLevel: Double? = null,
    @SerializedName("FlatEnergyRegenMod") val flatEnergyRegenMod: Double? = null,
    @SerializedName("rFlatEnergyRegenModPerLevel") val rFlatEnergyRegenModPerLevel: Double? = null,
    @SerializedName("FlatEnergyPoolMod") val flatEnergyPoolMod: Double? = null,
    @SerializedName("rFlatEnergyPoolModPerLevel") val rFlatEnergyPoolModPerLevel: Double? = null,
    @SerializedName("PercentLifeStealMod") val percentLifeStealMod: Double? = null,
    @SerializedName("PercentSpellVampMod") val percentSpellVampMod: Double? = null,
) : Parcelable