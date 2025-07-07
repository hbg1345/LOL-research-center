package com.example.lol_research_center.ui.notifications

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.lol_research_center.R
import com.example.lol_research_center.database.AppDatabase
import com.example.lol_research_center.databinding.FragmentNotificationsBinding
import com.example.lol_research_center.model.BuildInfo
import com.example.lol_research_center.model.ChampionInfo
import com.example.lol_research_center.model.ItemData
import com.example.lol_research_center.model.ItemStats
import com.example.lol_research_center.model.Skill
import com.example.lol_research_center.model.Stats
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NotificationsFragment : Fragment() {

    private var _binding: FragmentNotificationsBinding? = null
    private val binding get() = _binding!!

    private var buildInfo: BuildInfo? = null
    private val db by lazy { AppDatabase.getDatabase(requireContext().applicationContext) }

    // 현재 선택된 스킬
    private var selectedSkill: Skill? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        buildInfo = arguments?.let {
            if (Build.VERSION.SDK_INT >= 33) it.getParcelable("build", BuildInfo::class.java)
            else @Suppress("DEPRECATION") it.getParcelable("build")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNotificationsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        buildInfo?.let { info ->
            setupChampionInfo(info)
            setupItems(info)
            setupSkills(info)
            setupLevelButtons()
            setupExitButton()
            setupSaveButton(info)
        } ?: run {
            Toast.makeText(context, "빌드 정보가 없습니다.", Toast.LENGTH_SHORT).show()
            findNavController().popBackStack()
        }
    }

    private fun setupChampionInfo(info: BuildInfo) {
        binding.imageView2.setImageResource(info.champion.champDrawable)
        updateStatsUI(info.champion, info.items, info.champion.level) // 초기 레벨로 스탯 계산 및 UI 업데이트
    }

    private fun calculateTotalStats(champion: ChampionInfo, items: List<ItemData>, level: Int): Stats {
        val baseStats = champion.stats
        var totalAD = baseStats.attackdamage.toDouble()
        var totalAP = baseStats.ap.toDouble()
        var totalAS = baseStats.attackspeed.toDouble()
        var totalMR = baseStats.spellblock.toDouble()
        var totalAR = baseStats.armor.toDouble()
        var totalMS = baseStats.movespeed.toDouble()
        var totalHP = baseStats.hp.toDouble()
        var totalMP = baseStats.mp.toDouble()

        // 레벨에 따른 성장 스탯 계산 (챔피언 레벨은 1부터 시작)
        if (level > 1) {
            totalAD += baseStats.attackdamageperlevel * (level - 1)
            totalHP += baseStats.hpperlevel * (level - 1)
            totalMP += baseStats.mpperlevel * (level - 1)
            totalAR += baseStats.armorperlevel * (level - 1)
            totalMR += baseStats.spellblockperlevel * (level - 1)
            totalAS += baseStats.attackspeedperlevel * (level - 1)
            // TODO: 다른 스탯들도 레벨 성장에 따라 추가
        }

        // 아이템 스탯 합산
        items.forEach { item ->
            item.stats?.let { itemStats ->
                itemStats.flatPhysicalDamageMod?.let { totalAD += it }
                itemStats.flatMagicDamageMod?.let { totalAP += it }
                itemStats.flatAttackSpeedMod?.let { totalAS += it }
                itemStats.flatSpellBlockMod?.let { totalMR += it }
                itemStats.flatArmorMod?.let { totalAR += it }
                itemStats.flatMovementSpeedMod?.let { totalMS += it }
                itemStats.flatHPPoolMod?.let { totalHP += it }
                itemStats.flatMPPoolMod?.let { totalMP += it }
                // TODO: 다른 아이템 스탯들도 추가
            }
        }

        return Stats(
            attackdamage = totalAD.toInt(),
            ap = totalAP.toInt(),
            attackspeed = totalAS.toFloat(),
            spellblock = totalMR.toInt(),
            armor = totalAR.toInt(),
            movespeed = totalMS.toInt(),
            hp = totalHP.toInt(),
            mp = totalMP.toInt(),
            // 나머지 스탯은 기본값 또는 0으로 설정
            attackdamageperlevel = baseStats.attackdamageperlevel,
            hpperlevel = baseStats.hpperlevel,
            mpperlevel = baseStats.mpperlevel,
            armorperlevel = baseStats.armorperlevel,
            spellblockperlevel = baseStats.spellblockperlevel,
            hpregen = baseStats.hpregen,
            hpregenperlevel = baseStats.hpregenperlevel,
            mpregen = baseStats.mpregen,
            mpregenperlevel = baseStats.mpregenperlevel,
            crit = baseStats.crit,
            critperlevel = baseStats.critperlevel,
            attackspeedperlevel = baseStats.attackspeedperlevel
        )
    }

    private fun updateStatsUI(champion: ChampionInfo, items: List<ItemData>, level: Int) {
        val calculatedStats = calculateTotalStats(champion, items, level)
        with(binding) {
            textViewAd.text = calculatedStats.attackdamage.toString()
            textViewAp.text = calculatedStats.ap.toString()
            textViewAs.text = String.format("%.2f", calculatedStats.attackspeed) // 소수점 2자리까지 표시
            textViewMr.text = calculatedStats.spellblock.toString()
            textViewAr.text = calculatedStats.armor.toString()
            textViewMs.text = calculatedStats.movespeed.toString()

            healthBar.apply { max = calculatedStats.hp; progress = calculatedStats.hp }
            healthText.text = calculatedStats.hp.toString()

            manaBar.apply { max = calculatedStats.mp; progress = calculatedStats.mp / 2 } // 마나 현재값은 임시로 절반으로 설정
            manaText.text = "${calculatedStats.mp / 2} / ${calculatedStats.mp}"
        }
    }

    private fun setupItems(info: BuildInfo) {
        val imageSlots = listOf(
            binding.imageView10, binding.imageView11, binding.imageView12,
            binding.imageView13, binding.imageView14, binding.imageView15
        )
        val textSlots = listOf(
            binding.itemStatText10, binding.itemStatText11, binding.itemStatText12,
            binding.itemStatText13, binding.itemStatText14, binding.itemStatText15
        )

        imageSlots.forEachIndexed { idx, img ->
            info.items.getOrNull(idx)?.let { item ->
                img.setImageResource(item.imageResId)
                textSlots[idx].text = formatItemStats(item.stats) // 스탯 텍스트 설정
            } ?: run {
                img.setImageDrawable(null)
                textSlots[idx].text = "" // 아이템이 없으면 스탯 텍스트도 비움
            }
        }
    }

    private fun setupSkills(info: BuildInfo) {
        val skills = info.champion.skills
        val mapping = listOf(
            binding.imageButton1.imgSkill to skills.p,
            binding.imageButton2.imgSkill to skills.q,
            binding.imageButton3.imgSkill to skills.w,
            binding.imageButton4.imgSkill to skills.e,
            binding.imageButton5.imgSkill to skills.r
        )
        mapping.forEach { (view, skill) ->
            view.setImageResource(skill.skillDrawable)
            view.setOnClickListener {
                selectedSkill = skill
                showSkill(skill)
            }
        }
        // 기본 선택: Q
        selectedSkill = skills.q
        showSkill(skills.q)
    }

    private var currentChampionLevel: Int = 1 // 챔피언 레벨을 저장할 변수

    private fun setupLevelButtons() {
        // 초기 챔피언 레벨 설정
        buildInfo?.champion?.level?.let {
            currentChampionLevel = it
            binding.skilllevelText.text = currentChampionLevel.toString() // 챔피언 레벨 표시
        }

        binding.levelUpButton.setOnClickListener {
            currentChampionLevel = (currentChampionLevel + 1).coerceAtMost(18) // 최대 레벨 18
            binding.skilllevelText.text = currentChampionLevel.toString()
            buildInfo?.let { info ->
                updateStatsUI(info.champion, info.items, currentChampionLevel)
            }
        }
        binding.levelDownButton.setOnClickListener {
            currentChampionLevel = (currentChampionLevel - 1).coerceAtLeast(1) // 최소 레벨 1
            binding.skilllevelText.text = currentChampionLevel.toString()
            buildInfo?.let { info ->
                updateStatsUI(info.champion, info.items, currentChampionLevel)
            }
        }
    }

    private fun showSkill(skill: Skill) {
        val champion = buildInfo?.champion ?: return
        val items = buildInfo?.items ?: return
        val calculatedStats = calculateTotalStats(champion, items, currentChampionLevel) // 현재 챔피언 레벨 사용
        val damage = calcDamage(skill, calculatedStats) // 계산된 스탯 사용
        with(binding) {
            skillImg.setImageResource(skill.skillDrawable)
            skillTitleText.text = skill.skillTitle
            skilllevelText.text = if (skill.skillTitle == champion.skills.p.skillTitle) "-"
            else skill.skillLevel.toString()
            // 총 데미지 표시
            dealTotal.text = damage.toString()
        }
    }

    /**
     * 스킬 데미지 계산
     * base: skillDamageX[level - 1]
     * bonus: stats * coeff
     */
    private fun calcDamage(skill: Skill, stats: Stats): Int {
        val lvl = skill.skillLevel
        if (lvl <= 0) return 0
        val baseList = when (skill.skillType) {
            "ad" -> skill.skillDamageAd
            "ap" -> skill.skillDamageAp
            "fix" -> skill.skillDamageFix
            else -> emptyList()
        }
        val base = baseList.getOrNull(lvl - 1) ?: 0
        val bonus = stats.attackdamage * skill.skillAdCoeff +
                stats.ap * skill.skillApCoeff +
                stats.armor * skill.skillArCoeff +
                stats.spellblock * skill.skillMrCoeff +
                stats.hp * skill.skillHpCoeff
        return (base + bonus).toInt()
    }

    private fun setupSaveButton(info: BuildInfo) {
        binding.saveButton.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                val championName = info.champion.name
                val itemNames = info.items.map { it.name }.sorted()
                val duplicate = db.buildInfoDao().getAllBuilds().any { existing ->
                    existing.champion.name == championName &&
                            existing.items.map { it.name }.sorted() == itemNames
                }
                CoroutineScope(Dispatchers.Main).launch {
                    if (!duplicate) {
                        db.buildInfoDao().insertBuildInfo(info)
                        Toast.makeText(context, "빌드 정보가 저장되었습니다.", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(context, "이미 동일한 빌드 정보가 존재합니다.", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun setupExitButton() {
        binding.exitButton.apply {
            visibility = View.VISIBLE
            setOnClickListener { findNavController().navigate(R.id.buildsFragment) }
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    // 아이템 스탯을 포맷팅하는 헬퍼 함수
    private fun formatItemStats(itemStats: ItemStats?): String {
        if (itemStats == null) return ""

        val statsList = mutableListOf<String>()

        itemStats.flatPhysicalDamageMod?.let { if (it != 0.0) statsList.add("공격력: ${it.toInt()}") }
        itemStats.flatMagicDamageMod?.let { if (it != 0.0) statsList.add("주문력: ${it.toInt()}") }
        itemStats.flatAttackSpeedMod?.let { if (it != 0.0) statsList.add("공격 속도: ${String.format("%.2f", it)}") }
        itemStats.flatArmorMod?.let { if (it != 0.0) statsList.add("방어력: ${it.toInt()}") }
        itemStats.flatSpellBlockMod?.let { if (it != 0.0) statsList.add("마법 저항력: ${it.toInt()}") }
        itemStats.flatHPPoolMod?.let { if (it != 0.0) statsList.add("체력: ${it.toInt()}") }
        itemStats.flatMPPoolMod?.let { if (it != 0.0) statsList.add("마나: ${it.toInt()}") }
        itemStats.flatMovementSpeedMod?.let { if (it != 0.0) statsList.add("이동 속도: ${it.toInt()}") }
        itemStats.percentAttackSpeedMod?.let { if (it != 0.0) statsList.add("공격 속도 %: ${String.format("%.2f", it * 100)}%") }
        itemStats.percentHPPoolMod?.let { if (it != 0.0) statsList.add("체력 %: ${String.format("%.2f", it * 100)}%") }
        itemStats.percentMPPoolMod?.let { if (it != 0.0) statsList.add("마나 %: ${String.format("%.2f", it * 100)}%") }
        itemStats.flatHPRegenMod?.let { if (it != 0.0) statsList.add("체력 재생: ${it.toInt()}") }
        itemStats.flatMPRegenMod?.let { if (it != 0.0) statsList.add("마나 재생: ${it.toInt()}") }
        itemStats.flatCritChanceMod?.let { if (it != 0.0) statsList.add("치명타 확률: ${it.toInt()}") }
        itemStats.flatCritDamageMod?.let { if (it != 0.0) statsList.add("치명타 피해: ${it.toInt()}") }
        itemStats.percentLifeStealMod?.let { if (it != 0.0) statsList.add("생명력 흡수 %: ${String.format("%.2f", it * 100)}%") }
        itemStats.percentSpellVampMod?.let { if (it != 0.0) statsList.add("주문 흡혈 %: ${String.format("%.2f", it * 100)}%") }
        itemStats.rPercentCooldownMod?.let { if (it != 0.0) statsList.add("스킬 가속 %: ${String.format("%.2f", it * 100)}%") }
        itemStats.rFlatArmorPenetrationMod?.let { if (it != 0.0) statsList.add("물리 관통력: ${it.toInt()}") }
        itemStats.rFlatMagicPenetrationMod?.let { if (it != 0.0) statsList.add("마법 관통력: ${it.toInt()}") }
        itemStats.rPercentArmorPenetrationMod?.let { if (it != 0.0) statsList.add("물리 관통력 %: ${String.format("%.2f", it * 100)}%") }
        itemStats.rPercentMagicPenetrationMod?.let { if (it != 0.0) statsList.add("마법 관통력 %: ${String.format("%.2f", it * 100)}%") }


        return statsList.joinToString("\n")
    }
}
