// ui/notifications/NotificationsFragment.kt
package com.example.lol_research_center.ui.notifications

import android.os.Build
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import com.example.lol_research_center.databinding.FragmentNotificationsBinding
import com.example.lol_research_center.model.BuildInfo
import com.example.lol_research_center.model.Skill

class NotificationsFragment : Fragment() {

    private var _binding: FragmentNotificationsBinding? = null
    private val binding get() = _binding!!

    private var build: BuildInfo? = null          // ← 전달받은 BuildInfo

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // ── argument(Parcelable) 꺼내기 ──
        build = if (Build.VERSION.SDK_INT >= 33) {
            arguments?.getParcelable("build", BuildInfo::class.java)
        } else {
            @Suppress("DEPRECATION")
            arguments?.getParcelable("build")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNotificationsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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

            val mp = it.champion.stats.mp
            binding.manaBar.max = mp
            binding.manaBar.progress = mp / 3             // 예: 50% 상태
            binding.manaText.text = "${mp / 2} / $mp"

            // 4. 아이템 6칸 채우기
            val itemSlots = listOf(
                binding.imageView10, binding.imageView11, binding.imageView12,
                binding.imageView13, binding.imageView14, binding.imageView15
            )
            itemSlots.forEachIndexed { idx, img ->
                if (idx < it.items.size) {
                    img.setImageResource(it.items[idx].imageResId)
                } else {
                    img.setImageDrawable(null)            // 빈칸일 때 투명
                }
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
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
