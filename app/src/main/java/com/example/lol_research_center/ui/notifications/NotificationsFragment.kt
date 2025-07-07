// ui/notifications/NotificationsFragment.kt
package com.example.lol_research_center.ui.notifications

import android.os.Build
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.lol_research_center.R
import com.example.lol_research_center.databinding.FragmentNotificationsBinding
import com.example.lol_research_center.model.BuildInfo
import com.example.lol_research_center.database.AppDatabase
import androidx.room.Room
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import com.example.lol_research_center.model.Skill

class NotificationsFragment : Fragment() {

    private var _binding: FragmentNotificationsBinding? = null
    private val binding get() = _binding!!

    private var build: BuildInfo? = null          // ← 전달받은 BuildInfo
    private lateinit var db: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // ── argument(Parcelable) 꺼내기 ──
        build = if (Build.VERSION.SDK_INT >= 33) {
            arguments?.getParcelable("build", BuildInfo::class.java)
        } else {
            @Suppress("DEPRECATION")
            arguments?.getParcelable("build")
        }

        // 데이터베이스 인스턴스 초기화
        db = AppDatabase.getDatabase(requireContext().applicationContext)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNotificationsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        build?.let { buildInfo ->
            // --- 1) 우선 스킬 아이콘을 미리 세팅 ---
            binding.imageButton1.imgSkill.setImageResource(buildInfo.champion.skills.p.skillDrawable)
            binding.imageButton2.imgSkill.setImageResource(buildInfo.champion.skills.q.skillDrawable)
            binding.imageButton3.imgSkill.setImageResource(buildInfo.champion.skills.w.skillDrawable)
            binding.imageButton4.imgSkill.setImageResource(buildInfo.champion.skills.e.skillDrawable)
            binding.imageButton5.imgSkill.setImageResource(buildInfo.champion.skills.r.skillDrawable)

            // --- 2) 공통 함수 정의 ---
            fun showSkill(skill: Skill, title: String) {
                // 아이콘 교체
                binding.skillImg.setImageResource(skill.skillDrawable)
                // 타이틀 교체 (P, Q, W, E, R)
                binding.skillTitleText.text = title
                // 레벨
                binding.skilllevelText.text = "Lv ${skill.skillLevel}"
                // 설명
                binding.textView2.text = skill.skillInfo
            }

            // --- 3) 리스너 등록 ---
            binding.imageButton1.imgSkill.setOnClickListener {
                showSkill(buildInfo.champion.skills.p, buildInfo.champion.skills.p.skillTitle)
            }
            binding.imageButton2.imgSkill.setOnClickListener {
                showSkill(buildInfo.champion.skills.q, buildInfo.champion.skills.q.skillTitle)
            }
            binding.imageButton3.imgSkill.setOnClickListener {
                showSkill(buildInfo.champion.skills.w, buildInfo.champion.skills.w.skillTitle)
            }
            binding.imageButton4.imgSkill.setOnClickListener {
                showSkill(buildInfo.champion.skills.e, buildInfo.champion.skills.e.skillTitle)
            }
            binding.imageButton5.imgSkill.setOnClickListener {
                showSkill(buildInfo.champion.skills.r, buildInfo.champion.skills.r.skillTitle)
            }

            // (선택) 첫 화면에 Q 스킬을 기본으로 띄우고 싶다면:
            showSkill(buildInfo.champion.skills.q, "Q")
        }
        build?.let {
            with(it.champion.stats) {
                binding.textViewAd.text = attackdamage.toString()
                binding.textViewAp.text = ap.toString()
                binding.textViewAs.text = attackspeed.toString()
                binding.textViewMr.text = spellblock.toString()
                binding.textViewAr.text = armor.toString()
                binding.textViewMs.text = "???"
            }
            // 3. 체력 / 마나 ProgressBar
            val hp = it.champion.stats.hp
            binding.healthBar.max = hp
            binding.healthBar.progress = hp               // 현재 HP (예시로 풀피)
            binding.healthText.text = hp.toString()
        val pickerMode = arguments?.getBoolean("pickerMode") ?: false

        binding.exitButton.visibility = View.VISIBLE

        binding.exitButton.setOnClickListener {
            findNavController().navigate(R.id.buildsFragment)
        }

        build?.let { buildInfo ->
            buildInfo.champion?.let { champion ->
                // 1. 챔피언 아이콘 & 이름
                champion.champDrawable?.let { binding.imageView2.setImageResource(it) }
                // 챔피언 이름은 TextView가 없으므로 추가하지 않음

                // 2. 기본 스탯 숫자 채우기
                champion.stats?.let { stats ->
                    binding.textViewAd.text = stats.attackdamage?.toString() ?: "N/A"
                    binding.textViewAp.text = stats.ap?.toString() ?: "N/A"
                    binding.textViewAs.text = stats.attackspeed?.toString() ?: "N/A"
                    binding.textViewMr.text = stats.spellblock?.toString() ?: "N/A"
                    binding.textViewAr.text = stats.armor?.toString() ?: "N/A"
                    binding.textViewMs.text = stats.movespeed?.toString() ?: "N/A"

                    // 3. 체력 / 마나 ProgressBar
                    stats.hp?.let { hp ->
                        binding.healthBar.max = hp
                        binding.healthBar.progress = hp
                        binding.healthText.text = hp.toString()
                    } ?: run {
                        binding.healthBar.max = 100
                        binding.healthBar.progress = 0
                        binding.healthText.text = "N/A"
                    }

                    stats.mp?.let { mp ->
                        binding.manaBar.max = mp
                        binding.manaBar.progress = mp / 2 // 예: 50% 상태
                        binding.manaText.text = "${mp / 2} / $mp"
                    } ?: run {
                        binding.manaBar.max = 100
                        binding.manaBar.progress = 0
                        binding.manaText.text = "N/A"
                    }
                }
            }

            // 4. 아이템 6칸 채우기
            val itemSlots = listOf(
                binding.imageView10, binding.imageView11, binding.imageView12,
                binding.imageView13, binding.imageView14, binding.imageView15
            )
            buildInfo.items?.let { items ->
                itemSlots.forEachIndexed { idx, img ->
                    if (idx < items.size) {
                        items[idx].imageResId?.let { img.setImageResource(it) }
                    } else {
                        img.setImageDrawable(null) // 빈칸일 때 투명
                    }
                }
            } ?: run {
                itemSlots.forEach { it.setImageDrawable(null) } // items가 null이면 모두 투명
            }

        } ?: run {
            // build가 null일 경우 처리 (예: 에러 메시지 표시 또는 UI 숨기기)
            // 현재는 아무것도 하지 않아도 앱이 크래시되지 않음
            // 필요하다면 여기에 UI를 비활성화하거나 메시지를 표시하는 코드를 추가할 수 있습니다.
        }
        super.onViewCreated(view, savedInstanceState)

        build?.let { buildInfo ->
            val stats = buildInfo.champion.stats

            // 1) 계산 함수 정의
            fun calcDamage(skill: Skill): Int {
                // 1. 어떤 base 데미지 리스트를 쓸지 결정
                val baseList = when (skill.skillType) {
                    "ad"  -> skill.skillDamageAd
                    "ap"  -> skill.skillDamageAp
                    "fix" -> skill.skillDamageFix
                    else  -> emptyList()
                }
                // 레벨은 1~5 이므로 인덱스는 level-1
                val base = baseList.getOrNull(skill.skillLevel - 1) ?: 0

                // 2. coefficient 들을 stats 에 곱해서 합산
                val bonusAd = stats.attackdamage * skill.skillAdCoeff
                val bonusAp = stats.ap * skill.skillApCoeff
                val bonusAr = stats.armor * skill.skillArCoeff
                val bonusMr = stats.spellblock * skill.skillMrCoeff
                val bonusHp = stats.hp * skill.skillHpCoeff

                // 3. 최종 데미지 (Float 더하기 → Int 로 잘라냄)
                return (base + bonusAd + bonusAp + bonusAr + bonusMr + bonusHp).toInt()
            }

            // 2) 예시: Q 스킬의 데미지를 textView2 에 찍어보기
            val qSkill = buildInfo.champion.skills.q
            val qDmg = calcDamage(qSkill)
            binding.textView2.text = "Q 스킬 데미지: $qDmg"
        }

        binding.saveButton.setOnClickListener {
            build?.let { buildInfo ->
                CoroutineScope(Dispatchers.IO).launch {
                    val championName = buildInfo.champion.name
                    val itemNames = buildInfo.items.map { it.name }.sorted()

                    val allBuilds = db.buildInfoDao().getAllBuilds()
                    val isDuplicate = allBuilds.any { existingBuild ->
                        val existingChampionName = existingBuild.champion.name
                        val existingItemNames = existingBuild.items.map { it.name }.sorted()
                        existingChampionName == championName && existingItemNames == itemNames
                    }

                    if (!isDuplicate) {
                        db.buildInfoDao().insertBuildInfo(buildInfo)
                        launch(Dispatchers.Main) {
                            Toast.makeText(context, "빌드 정보가 저장되었습니다.", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        launch(Dispatchers.Main) {
                            Toast.makeText(context, "이미 동일한 빌드 정보가 존재합니다.", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            } ?: run {
                Toast.makeText(context, "저장할 빌드 정보가 없습니다.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
