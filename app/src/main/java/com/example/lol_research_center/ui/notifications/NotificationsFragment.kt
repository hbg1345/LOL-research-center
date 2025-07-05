// ui/notifications/NotificationsFragment.kt
package com.example.lol_research_center.ui.notifications

import android.os.Build
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import com.example.lol_research_center.databinding.FragmentNotificationsBinding
import com.example.lol_research_center.model.BuildInfo

class NotificationsFragment : Fragment() {

    private var _binding: FragmentNotificationsBinding? = null
    private val binding get() = _binding!!

    private lateinit var build: BuildInfo          // ← 전달받은 BuildInfo

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // ── argument(Parcelable) 꺼내기 ──
        build = if (Build.VERSION.SDK_INT >= 33) {
            requireArguments().getParcelable("build", BuildInfo::class.java)!!
        } else {
            @Suppress("DEPRECATION")
            requireArguments().getParcelable("build")!!
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNotificationsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        /* 1. 챔피언 아이콘 & 이름 (원한다면 TextView 추가) */
        binding.imageView2.setImageResource(build.champion.champDrawable)

//        /* 2. 기본 스탯 숫자 채우기 */
//        with(build.champion.stats) {
//            textView_ad.text = ad.toString()
//            binding.textView_ap.text = ap.toString()
//            binding.textView_as.text = attackSpeed.toString()
//            binding.textView_mr.text = mr.toString()
//            binding.textView_ar.text = ar.toString()
//            binding.textView_ms.text = "???"
//        }

        /* 3. 체력 / 마나 ProgressBar */
        val hp = build.champion.stats.hp
        binding.healthBar.max = hp
        binding.healthBar.progress = hp               // 현재 HP (예시로 풀피)
        binding.healthText.text = hp.toString()

        val mp = build.champion.stats.mp
        binding.manaBar.max = mp
        binding.manaBar.progress = mp / 3             // 예: 50% 상태
        binding.manaText.text = "${mp / 2} / $mp"

        /* 4. 아이템 6칸 채우기 */
        val itemSlots = listOf(
            binding.imageView10, binding.imageView11, binding.imageView12,
            binding.imageView13, binding.imageView14, binding.imageView15
        )
        itemSlots.forEachIndexed { idx, img ->
            if (idx < build.items.size) {
                img.setImageResource(build.items[idx].imageResId)
            } else {
                img.setImageDrawable(null)            // 빈칸일 때 투명
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
