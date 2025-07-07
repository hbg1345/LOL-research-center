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
        with(binding) {
            imageView2.setImageResource(info.champion.champDrawable)
            with(info.champion.stats) {
                textViewAd.text = attackdamage.toString()
                textViewAp.text = ap.toString()
                textViewAs.text = attackspeed.toString()
                textViewMr.text = spellblock.toString()
                textViewAr.text = armor.toString()
                textViewMs.text = movespeed.toString()

                healthBar.apply { max = hp; progress = hp }
                healthText.text = hp.toString()

                manaBar.apply { max = mp; progress = mp / 2 }
                manaText.text = "${mp / 2} / $mp"
            }
        }
    }

    private fun setupItems(info: BuildInfo) {
        val slots = listOf(
            binding.imageView10, binding.imageView11, binding.imageView12,
            binding.imageView13, binding.imageView14, binding.imageView15
        )
        slots.forEachIndexed { idx, img ->
            info.items.getOrNull(idx)?.let { img.setImageResource(it.imageResId) }
                ?: img.setImageDrawable(null)
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

    private fun setupLevelButtons() {
        binding.levelUpButton.setOnClickListener {
            selectedSkill?.let { skill ->
                val maxLvl = when (skill.skillTitle) {
                    buildInfo?.champion?.skills?.p?.skillTitle -> 0
                    buildInfo?.champion?.skills?.r?.skillTitle -> 3
                    else -> 5
                }
                skill.skillLevel = (skill.skillLevel + 1).coerceAtMost(maxLvl)
                showSkill(skill)
            }
        }
        binding.levelDownButton.setOnClickListener {
            selectedSkill?.let { skill ->
                skill.skillLevel = (skill.skillLevel - 1).coerceAtLeast(0)
                showSkill(skill)
            }
        }
    }

    private fun showSkill(skill: Skill) {
        val stats = buildInfo?.champion?.stats ?: return
        val damage = calcDamage(skill, stats)
        with(binding) {
            skillImg.setImageResource(skill.skillDrawable)
            skillTitleText.text = skill.skillTitle
            skilllevelText.text = if (skill.skillTitle == buildInfo?.champion?.skills?.p?.skillTitle) "-"
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
}
