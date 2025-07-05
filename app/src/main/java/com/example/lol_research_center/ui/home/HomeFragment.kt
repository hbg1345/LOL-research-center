package com.example.lol_research_center.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.example.lol_research_center.R
import com.example.lol_research_center.databinding.FragmentHomeBinding
import com.example.lol_research_center.model.ImageItem
import com.example.lol_research_center.model.Stats

class HomeFragment : Fragment() {

    // ───────────────────────────────── Binding ─────────────────────────────────
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    // ─────────────────────────────── 샘플 데이터 ────────────────────────────────
    private val champs by lazy {
        listOf(
            ImageItem(
                champDrawable = R.drawable.velkoz,
                name          = "velkoz",
                type          = "Support",
                stats         = Stats(
                    ad = 60, ap = 0, hp = 650, mp = 0,
                    critical = 0, attackSpeed = 0, ar = 38, mr = 32
                ),
                itemDrawables = listOf(
                    R.drawable.amplifying_tome,
                    R.drawable.blasting_wand,
                    R.drawable.blasting_wand,
                    R.drawable.doran_ring,
                    R.drawable.cull,
                    R.drawable.faerie_charm
                )
            ),
            ImageItem(
                champDrawable = R.drawable.caitlyn,
                name          = "caitlyn",
                type          = "ad carry",
                stats         = Stats(
                    ad = 60, ap = 0, hp = 650, mp = 0,
                    critical = 0, attackSpeed = 0, ar = 38, mr = 32
                ),
                itemDrawables = listOf(
                    R.drawable.amplifying_tome,
                    R.drawable.blasting_wand,
                    R.drawable.blasting_wand,
                    R.drawable.doran_ring,
                    R.drawable.cull,
                    R.drawable.faerie_charm
                )
            ),
            ImageItem(
                champDrawable = R.drawable.camille,
                name          = "camille",
                type          = "top",
                stats         = Stats(
                    ad = 60, ap = 0, hp = 650, mp = 0,
                    critical = 0, attackSpeed = 0, ar = 38, mr = 32
                ),
                itemDrawables = listOf(
                    R.drawable.amplifying_tome,
                    R.drawable.blasting_wand,
                    R.drawable.blasting_wand,
                    R.drawable.doran_ring,
                    R.drawable.cull,
                    R.drawable.faerie_charm
                )
            ),
            ImageItem(
                champDrawable = R.drawable.leesin,
                name          = "leesin",
                type          = "jungle",
                stats         = Stats(
                    ad = 60, ap = 0, hp = 650, mp = 0,
                    critical = 0, attackSpeed = 0, ar = 38, mr = 32
                ),
                itemDrawables = listOf(
                    R.drawable.amplifying_tome,
                    R.drawable.blasting_wand,
                    R.drawable.blasting_wand,
                    R.drawable.doran_ring,
                    R.drawable.cull,
                    R.drawable.faerie_charm
                )
            ),
            ImageItem(
                champDrawable = R.drawable.graves,
                name          = "graves",
                type          = "Support",
                stats         = Stats(
                    ad = 60, ap = 0, hp = 650, mp = 0,
                    critical = 0, attackSpeed = 0, ar = 38, mr = 32
                ),
                itemDrawables = listOf(
                    R.drawable.amplifying_tome,
                    R.drawable.blasting_wand,
                    R.drawable.blasting_wand,
                    R.drawable.doran_ring,
                    R.drawable.cull,
                    R.drawable.faerie_charm
                )
            ),
            ImageItem(
                champDrawable = R.drawable.ezreal,
                name          = "ezreal",
                type          = "Support",
                stats         = Stats(
                    ad = 60, ap = 0, hp = 650, mp = 0,
                    critical = 0, attackSpeed = 0, ar = 38, mr = 32
                ),
                itemDrawables = listOf(
                    R.drawable.amplifying_tome,
                    R.drawable.blasting_wand,
                    R.drawable.blasting_wand,
                    R.drawable.doran_ring,
                    R.drawable.cull,
                    R.drawable.faerie_charm
                )
            ),
            ImageItem(
                champDrawable = R.drawable.missfortune,
                name          = "missfortune",
                type          = "Support",
                stats         = Stats(
                    ad = 60, ap = 0, hp = 650, mp = 0,
                    critical = 0, attackSpeed = 0, ar = 38, mr = 32
                ),
                itemDrawables = listOf(
                    R.drawable.amplifying_tome,
                    R.drawable.blasting_wand,
                    R.drawable.blasting_wand,
                    R.drawable.doran_ring,
                    R.drawable.cull,
                    R.drawable.faerie_charm
                )
            ),
            ImageItem(
                champDrawable = R.drawable.neeko,
                name          = "neeko",
                type          = "Support",
                stats         = Stats(
                    ad = 60, ap = 0, hp = 650, mp = 0,
                    critical = 0, attackSpeed = 0, ar = 38, mr = 32
                ),
                itemDrawables = listOf(
                    R.drawable.amplifying_tome,
                    R.drawable.blasting_wand,
                    R.drawable.blasting_wand,
                    R.drawable.doran_ring,
                    R.drawable.cull,
                    R.drawable.faerie_charm
                )
            ),
            ImageItem(
                champDrawable = R.drawable.orianna,
                name          = "oriana",
                type          = "Support",
                stats         = Stats(
                    ad = 60, ap = 0, hp = 650, mp = 0,
                    critical = 0, attackSpeed = 0, ar = 38, mr = 32
                ),
                itemDrawables = listOf(
                    R.drawable.amplifying_tome,
                    R.drawable.blasting_wand,
                    R.drawable.blasting_wand,
                    R.drawable.doran_ring,
                    R.drawable.cull,
                    R.drawable.faerie_charm
                )
            ),
            ImageItem(
                champDrawable = R.drawable.riven,
                name          = "riven",
                type          = "Support",
                stats         = Stats(
                    ad = 60, ap = 0, hp = 650, mp = 0,
                    critical = 0, attackSpeed = 0, ar = 38, mr = 32
                ),
                itemDrawables = listOf(
                    R.drawable.amplifying_tome,
                    R.drawable.blasting_wand,
                    R.drawable.blasting_wand,
                    R.drawable.doran_ring,
                    R.drawable.cull,
                    R.drawable.faerie_charm
                )
            ),
            ImageItem(
                champDrawable = R.drawable.twistedfate,
                name          = "twistedfate",
                type          = "Support",
                stats         = Stats(
                    ad = 60, ap = 0, hp = 650, mp = 0,
                    critical = 0, attackSpeed = 0, ar = 38, mr = 32
                ),
                itemDrawables = listOf(
                    R.drawable.amplifying_tome,
                    R.drawable.blasting_wand,
                    R.drawable.blasting_wand,
                    R.drawable.doran_ring,
                    R.drawable.cull,
                    R.drawable.faerie_charm
                )
            ),
            ImageItem(
                champDrawable = R.drawable.vi,
                name          = "vi",
                type          = "Support",
                stats         = Stats(
                    ad = 60, ap = 0, hp = 650, mp = 0,
                    critical = 0, attackSpeed = 0, ar = 38, mr = 32
                ),
                itemDrawables = listOf(
                    R.drawable.amplifying_tome,
                    R.drawable.blasting_wand,
                    R.drawable.blasting_wand,
                    R.drawable.doran_ring,
                    R.drawable.cull,
                    R.drawable.faerie_charm
                )
            ),
            ImageItem(
                champDrawable = R.drawable.irelia,
                name          = "irelia",
                type          = "mid",
                stats         = Stats(
                    ad = 60, ap = 0, hp = 650, mp = 0,
                    critical = 0, attackSpeed = 0, ar = 38, mr = 32
                ),
                itemDrawables = listOf(
                    R.drawable.amplifying_tome,
                    R.drawable.blasting_wand,
                    R.drawable.blasting_wand,
                    R.drawable.doran_ring,
                    R.drawable.cull,
                    R.drawable.faerie_charm
                )
            ),
            ImageItem(
                champDrawable = R.drawable.zed,
                name          = "zed",
                type          = "Support",
                stats         = Stats(
                    ad = 60, ap = 0, hp = 650, mp = 0,
                    critical = 0, attackSpeed = 0, ar = 38, mr = 32
                ),
                itemDrawables = listOf(
                    R.drawable.amplifying_tome,
                    R.drawable.blasting_wand,
                    R.drawable.blasting_wand,
                    R.drawable.doran_ring,
                    R.drawable.cull,
                    R.drawable.faerie_charm
                )
            ),
            ImageItem(
                champDrawable = R.drawable.leblanc,
                name          = "leblanc",
                type          = "mid",
                stats         = Stats(
                    ad = 60, ap = 0, hp = 650, mp = 0,
                    critical = 0, attackSpeed = 0, ar = 38, mr = 32
                ),
                itemDrawables = listOf(
                    R.drawable.amplifying_tome,
                    R.drawable.blasting_wand,
                    R.drawable.blasting_wand,
                    R.drawable.doran_ring,
                    R.drawable.cull,
                    R.drawable.faerie_charm
                )
            ),
            ImageItem(
                champDrawable = R.drawable.kaisa,
                name          = "kaisa",
                type          = "ad carry",
                stats         = Stats(
                    ad = 60, ap = 0, hp = 650, mp = 0,
                    critical = 0, attackSpeed = 0, ar = 38, mr = 32
                ),
                itemDrawables = listOf(
                    R.drawable.amplifying_tome,
                    R.drawable.blasting_wand,
                    R.drawable.blasting_wand,
                    R.drawable.doran_ring,
                    R.drawable.cull,
                    R.drawable.faerie_charm
                )
            ),
            ImageItem(
                champDrawable = R.drawable.ashe,
                name          = "ashe",
                type          = "ad carry",
                stats         = Stats(
                    ad = 60, ap = 0, hp = 650, mp = 0,
                    critical = 0, attackSpeed = 0, ar = 38, mr = 32
                ),
                itemDrawables = listOf(
                    R.drawable.amplifying_tome,
                    R.drawable.blasting_wand,
                    R.drawable.blasting_wand,
                    R.drawable.doran_ring,
                    R.drawable.cull,
                    R.drawable.faerie_charm
                )
            ),
            ImageItem(
                champDrawable = R.drawable.katarina,
                name          = "katarina",
                type          = "mid",
                stats         = Stats(
                    ad = 60, ap = 0, hp = 650, mp = 0,
                    critical = 0, attackSpeed = 0, ar = 38, mr = 32
                ),
                itemDrawables = listOf(
                    R.drawable.amplifying_tome,
                    R.drawable.blasting_wand,
                    R.drawable.blasting_wand,
                    R.drawable.doran_ring,
                    R.drawable.cull,
                    R.drawable.faerie_charm
                )
            ),
            ImageItem(
                champDrawable = R.drawable.riven,
                name          = "riven",
                type          = "top",
                stats         = Stats(
                    ad = 60, ap = 0, hp = 650, mp = 0,
                    critical = 0, attackSpeed = 0, ar = 38, mr = 32
                ),
                itemDrawables = listOf(
                    R.drawable.amplifying_tome,
                    R.drawable.blasting_wand,
                    R.drawable.blasting_wand,
                    R.drawable.doran_ring,
                    R.drawable.cull,
                    R.drawable.faerie_charm
                )
            ),
            ImageItem(
                champDrawable = R.drawable.vayne,
                name          = "vayne",
                type          = "ad carry",
                stats         = Stats(
                    ad = 60, ap = 0, hp = 650, mp = 0,
                    critical = 0, attackSpeed = 0, ar = 38, mr = 32
                ),
                itemDrawables = listOf(
                    R.drawable.amplifying_tome,
                    R.drawable.blasting_wand,
                    R.drawable.blasting_wand,
                    R.drawable.doran_ring,
                    R.drawable.cull,
                    R.drawable.faerie_charm
                )
            ),
            /* TODO: 다른 챔피언도 추가 */
        )
    }

    // ───────────────────────────── Lifecycle ──────────────────────────────
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    // ───────────────────────────── Private Helpers ─────────────────────────────
    private fun setupRecyclerView() = with(binding.recyclerView) {
        layoutManager = GridLayoutManager(requireContext(), 3)           // 3-열 그리드
        adapter       = ImageGridAdapter(champs) { clickedItem ->
            println("${clickedItem.name}")
            // 클릭 시 원하는 동작을 여기서 정의 (토스트, 다이얼로그 등)
            // 예) Toast.makeText(context, "${clickedItem.name} 선택", LENGTH_SHORT).show()
        }
        setHasFixedSize(true)                                            // 성능 최적화
    }
}
