package com.example.lol_research_center.ui.notifications

import android.os.Build
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.lol_research_center.databinding.FragmentNotificationsBinding
import com.example.lol_research_center.model.BuildInfo
import com.example.lol_research_center.database.AppDatabase
import androidx.room.Room
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

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
        db = Room.databaseBuilder(
            requireContext().applicationContext,
            AppDatabase::class.java, "lol-research-db"
        ).build()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNotificationsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        build?.let {
            // 1. 챔피언 아이콘 & 이름 (원한다면 TextView 추가)
            binding.imageView2.setImageResource(it.champion.champDrawable)

            // 2. 기본 스탯 숫자 채우기
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

        binding.saveButton.setOnClickListener {
            build?.let { buildInfo ->
                CoroutineScope(Dispatchers.IO).launch {
                    db.buildInfoDao().insertBuildInfo(buildInfo)
                    // UI 업데이트는 메인 스레드에서
                    launch(Dispatchers.Main) {
                        Toast.makeText(context, "빌드 정보가 저장되었습니다.", Toast.LENGTH_SHORT).show()
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