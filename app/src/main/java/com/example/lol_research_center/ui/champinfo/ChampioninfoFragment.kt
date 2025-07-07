package com.example.lol_research_center.ui.champinfo

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.lol_research_center.R
import com.example.lol_research_center.model.ChampionInfo
import com.example.lol_research_center.model.Skill

class ChampioninfoFragment : Fragment() {

    private lateinit var champInfo: ChampionInfo
    private lateinit var skillDescTv: TextView
    private lateinit var champInfoTv: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        champInfo = if (Build.VERSION.SDK_INT >= 33) {
            requireArguments().getParcelable("championInfo", ChampionInfo::class.java)!!
        } else {
            @Suppress("DEPRECATION")
            requireArguments().getParcelable("championInfo")!!
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_champinfo, container, false)

        /* 1) TextView 바인딩 */
        skillDescTv = view.findViewById(R.id.textView2)
        champInfoTv = view.findViewById(R.id.textView)

        /* 2) 스킬 FrameLayout 바인딩 */
        val skillP = view.findViewById<FrameLayout>(R.id.imageButton1)
        val skillQ = view.findViewById<FrameLayout>(R.id.imageButton2)
        val skillW = view.findViewById<FrameLayout>(R.id.imageButton3)
        val skillE = view.findViewById<FrameLayout>(R.id.imageButton4)
        val skillR = view.findViewById<FrameLayout>(R.id.imageButton5)

        /* 3) 아이콘·쿨다운·키 라벨 세팅 */
        bindSkillIcon(skillP, champInfo.skills.p, 'P')
        bindSkillIcon(skillQ, champInfo.skills.q, 'Q')
        bindSkillIcon(skillW, champInfo.skills.w, 'W')
        bindSkillIcon(skillE, champInfo.skills.e, 'E')
        bindSkillIcon(skillR, champInfo.skills.r, 'R')

        /* 4) 클릭 시 설명 표시 */
        skillP.setOnClickListener { showSkillInfo(champInfo.skills.p.skillInfo) }
        skillQ.setOnClickListener { showSkillInfo(champInfo.skills.q.skillInfo) }
        skillW.setOnClickListener { showSkillInfo(champInfo.skills.w.skillInfo) }
        skillE.setOnClickListener { showSkillInfo(champInfo.skills.e.skillInfo) }
        skillR.setOnClickListener { showSkillInfo(champInfo.skills.r.skillInfo) }

        /* 5) 초기 출력 */
        showSkillInfo(champInfo.skills.p.skillInfo)
        showChampInfo(champInfo.lore)

        return view
    }

    /** FrameLayout(스킬 버튼) 내부 위젯에 값 설정 */
    private fun bindSkillIcon(root: FrameLayout, skill: Skill, key: Char) {
        val img = root.findViewById<ImageView>(R.id.imgSkill)
        val tvCd = root.findViewById<TextView>(R.id.tvCooldown)
        val tvKey = root.findViewById<TextView>(R.id.tvKey)

        /* 아이콘 이미지 */
        img.setImageResource(skill.skillDrawable)

        /* 쿨다운: 0 이하면 숨김 */
        val cd = 50   // baseCooldown 필드를 Skill 모델에 추가해 두었다면
        if (cd != null && cd > 0) {
            tvCd.text = cd.toString()
            tvCd.visibility = View.VISIBLE
        } else {
            tvCd.visibility = View.GONE
        }

        /* 키 라벨(Q/W/E/R/P) */
        tvKey.text = key.toString()
    }

    private fun showSkillInfo(info: String) {
        skillDescTv.text = info
    }

    private fun showChampInfo(info: String) {
        champInfoTv.text = info
    }
}
