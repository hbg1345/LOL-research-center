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

                healthBar.apply {
                    max = hp
                    progress = hp
                }
                healthText.text = hp.toString()

                manaBar.apply {
                    max = mp
                    progress = mp / 2
                }
                manaText.text = "${mp / 2} / $mp"
            }
        }
    }

    private fun setupItems(info: BuildInfo) {
        val slots = listOf(
            binding.imageView10, binding.imageView11, binding.imageView12,
            binding.imageView13, binding.imageView14, binding.imageView15
        )
        // Populate each slot or clear
        slots.forEachIndexed { index, imageView ->
            val item = info.items.getOrNull(index)
            if (item != null) imageView.setImageResource(item.imageResId)
            else imageView.setImageDrawable(null)
        }
    }

    private fun setupSkills(info: BuildInfo) {
        val skills = info.champion.skills
        // mapping of view to skill and title
        val mapping = listOf(
            binding.imageButton1.imgSkill to Pair(skills.p, skills.p.skillTitle),
            binding.imageButton2.imgSkill to Pair(skills.q, skills.q.skillTitle),
            binding.imageButton3.imgSkill to Pair(skills.w, skills.w.skillTitle),
            binding.imageButton4.imgSkill to Pair(skills.e, skills.e.skillTitle),
            binding.imageButton5.imgSkill to Pair(skills.r, skills.r.skillTitle)
        )

        // set icons and listeners
        mapping.forEach { (view, pair) ->
            val (skill, title) = pair
            view.setImageResource(skill.skillDrawable)
            view.setOnClickListener { showSkill(skill, title) }
        }

        // default display
        showSkill(skills.q, skills.q.skillTitle)
    }

    private fun showSkill(skill: Skill, title: String) {
        with(binding) {
            skillImg.setImageResource(skill.skillDrawable)
            skillTitleText.text = skill.skillTitle
            skilllevelText.text = "Lv ${skill.skillLevel}"
        }
    }

    private fun setupSaveButton(info: BuildInfo) {
        binding.saveButton.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                val championName = info.champion.name
                val itemNames = info.items.map { it.name }.sorted()

                val isDuplicate = db.buildInfoDao().getAllBuilds().any { existing ->
                    existing.champion.name == championName &&
                            existing.items.map { it.name }.sorted() == itemNames
                }

                CoroutineScope(Dispatchers.Main).launch {
                    if (!isDuplicate) {
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

    private fun calcDamage(skill: Skill, stats: Stats): Int {
        val base = when (skill.skillType) {
            "ad"  -> skill.skillDamageAd.getOrNull(skill.skillLevel - 1) ?: 0
            "ap"  -> skill.skillDamageAp.getOrNull(skill.skillLevel - 1) ?: 0
            "fix" -> skill.skillDamageFix.getOrNull(skill.skillLevel - 1) ?: 0
            else   -> 0
        }
        val bonus = stats.attackdamage * skill.skillAdCoeff +
                stats.ap * skill.skillApCoeff +
                stats.armor * skill.skillArCoeff +
                stats.spellblock * skill.skillMrCoeff +
                stats.hp * skill.skillHpCoeff
        return (base + bonus).toInt()
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}
