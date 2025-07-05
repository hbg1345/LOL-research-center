package com.example.lol_research_center.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.example.lol_research_center.R
import com.example.lol_research_center.databinding.FragmentHomeBinding
import com.example.lol_research_center.model.ChampionInfo
import com.example.lol_research_center.model.Lane
import com.example.lol_research_center.model.Skill
import com.example.lol_research_center.model.Skills
import com.example.lol_research_center.model.Stats

import com.example.lol_research_center.model.ChampionDataLoader
class HomeFragment : Fragment() {

    // ───────────────────────────────── Binding ─────────────────────────────────
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val champs: List<ChampionInfo> by lazy {
        ChampionDataLoader.loadChampionsFromAsset(requireContext(), "champions.json")
    }

    // ─────────────────────────────── 샘플 데이터 ────────────────────────────────
    private val champs_ by lazy {
        listOf(
            ChampionInfo(
                champDrawable = R.drawable.leesin,
                name          = "leesin",
                lane          = Lane.MID,
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
                ),
                skills = Skills(
                    p = Skill(
                        skillType   = "ad",
                        skillLevel  = 1,                 // 예시
                        skillDamage = listOf(100, 130),  // 예시
                        skillApCoeff = 0.0f,
                        skillAdCoeff = 1.2f,
                        skillArCoeff = 0f,
                        skillMrCoeff = 0f,
                        skillHpCoeff = 0f
                    ),

                    q = Skill(
                        skillType   = "ad",
                        skillLevel  = 1,                 // 예시
                        skillDamage = listOf(100, 130),  // 예시
                        skillApCoeff = 0.0f,
                        skillAdCoeff = 1.2f,
                        skillArCoeff = 0f,
                        skillMrCoeff = 0f,
                        skillHpCoeff = 0f
                    ),
                    w = Skill(
                        skillType   = "ad",
                        skillLevel  = 1,                 // 예시
                        skillDamage = listOf(100, 130),  // 예시
                        skillApCoeff = 0.0f,
                        skillAdCoeff = 1.2f,
                        skillArCoeff = 0f,
                        skillMrCoeff = 0f,
                        skillHpCoeff = 0f
                    ),
                    e= Skill(
                        skillType   = "ad",
                        skillLevel  = 1,                 // 예시
                        skillDamage = listOf(100, 130),  // 예시
                        skillApCoeff = 0.0f,
                        skillAdCoeff = 1.2f,
                        skillArCoeff = 0f,
                        skillMrCoeff = 0f,
                        skillHpCoeff = 0f
                    ),
                    r= Skill(
                        skillType   = "ad",
                        skillLevel  = 1,                 // 예시
                        skillDamage = listOf(100, 130),  // 예시
                        skillApCoeff = 0.0f,
                        skillAdCoeff = 1.2f,
                        skillArCoeff = 0f,
                        skillMrCoeff = 0f,
                        skillHpCoeff = 0f
                    )

                ),

            ),
//            ChampionInfo(
//                champDrawable = R.drawable.velkoz,
//                name          = "velkoz",
//                type          = "Support",
//                stats         = Stats(
//                    ad = 60, ap = 0, hp = 650, mp = 0,
//                    critical = 0, attackSpeed = 0, ar = 38, mr = 32
//                ),
//                itemDrawables = listOf(
//                    R.drawable.amplifying_tome,
//                    R.drawable.blasting_wand,
//                    R.drawable.blasting_wand,
//                    R.drawable.doran_ring,
//                    R.drawable.cull,
//                    R.drawable.faerie_charm
//                )
//            ),
//            ChampionInfo(
//                champDrawable = R.drawable.caitlyn,
//                name          = "caitlyn",
//                type          = "ad carry",
//                stats         = Stats(
//                    ad = 60, ap = 0, hp = 650, mp = 0,
//                    critical = 0, attackSpeed = 0, ar = 38, mr = 32
//                ),
//                itemDrawables = listOf(
//                    R.drawable.amplifying_tome,
//                    R.drawable.blasting_wand,
//                    R.drawable.blasting_wand,
//                    R.drawable.doran_ring,
//                    R.drawable.cull,
//                    R.drawable.faerie_charm
//                )
//            ),
//            ChampionInfo(
//                champDrawable = R.drawable.camille,
//                name          = "camille",
//                type          = "top",
//                stats         = Stats(
//                    ad = 60, ap = 0, hp = 650, mp = 0,
//                    critical = 0, attackSpeed = 0, ar = 38, mr = 32
//                ),
//                itemDrawables = listOf(
//                    R.drawable.amplifying_tome,
//                    R.drawable.blasting_wand,
//                    R.drawable.blasting_wand,
//                    R.drawable.doran_ring,
//                    R.drawable.cull,
//                    R.drawable.faerie_charm
//                )
//            ),
//            ChampionInfo(
//                champDrawable = R.drawable.graves,
//                name          = "graves",
//                type          = "Support",
//                stats         = Stats(
//                    ad = 60, ap = 0, hp = 650, mp = 0,
//                    critical = 0, attackSpeed = 0, ar = 38, mr = 32
//                ),
//                itemDrawables = listOf(
//                    R.drawable.amplifying_tome,
//                    R.drawable.blasting_wand,
//                    R.drawable.blasting_wand,
//                    R.drawable.doran_ring,
//                    R.drawable.cull,
//                    R.drawable.faerie_charm
//                )
//            ),
//            ChampionInfo(
//                champDrawable = R.drawable.ezreal,
//                name          = "ezreal",
//                type          = "Support",
//                stats         = Stats(
//                    ad = 60, ap = 0, hp = 650, mp = 0,
//                    critical = 0, attackSpeed = 0, ar = 38, mr = 32
//                ),
//                itemDrawables = listOf(
//                    R.drawable.amplifying_tome,
//                    R.drawable.blasting_wand,
//                    R.drawable.blasting_wand,
//                    R.drawable.doran_ring,
//                    R.drawable.cull,
//                    R.drawable.faerie_charm
//                )
//            ),
//            ChampionInfo(
//                champDrawable = R.drawable.missfortune,
//                name          = "missfortune",
//                type          = "Support",
//                stats         = Stats(
//                    ad = 60, ap = 0, hp = 650, mp = 0,
//                    critical = 0, attackSpeed = 0, ar = 38, mr = 32
//                ),
//                itemDrawables = listOf(
//                    R.drawable.amplifying_tome,
//                    R.drawable.blasting_wand,
//                    R.drawable.blasting_wand,
//                    R.drawable.doran_ring,
//                    R.drawable.cull,
//                    R.drawable.faerie_charm
//                )
//            ),
//            ChampionInfo(
//                champDrawable = R.drawable.neeko,
//                name          = "neeko",
//                type          = "Support",
//                stats         = Stats(
//                    ad = 60, ap = 0, hp = 650, mp = 0,
//                    critical = 0, attackSpeed = 0, ar = 38, mr = 32
//                ),
//                itemDrawables = listOf(
//                    R.drawable.amplifying_tome,
//                    R.drawable.blasting_wand,
//                    R.drawable.blasting_wand,
//                    R.drawable.doran_ring,
//                    R.drawable.cull,
//                    R.drawable.faerie_charm
//                )
//            ),
//            ChampionInfo(
//                champDrawable = R.drawable.orianna,
//                name          = "oriana",
//                type          = "Support",
//                stats         = Stats(
//                    ad = 60, ap = 0, hp = 650, mp = 0,
//                    critical = 0, attackSpeed = 0, ar = 38, mr = 32
//                ),
//                itemDrawables = listOf(
//                    R.drawable.amplifying_tome,
//                    R.drawable.blasting_wand,
//                    R.drawable.blasting_wand,
//                    R.drawable.doran_ring,
//                    R.drawable.cull,
//                    R.drawable.faerie_charm
//                )
//            ),
//            ChampionInfo(
//                champDrawable = R.drawable.riven,
//                name          = "riven",
//                type          = "Support",
//                stats         = Stats(
//                    ad = 60, ap = 0, hp = 650, mp = 0,
//                    critical = 0, attackSpeed = 0, ar = 38, mr = 32
//                ),
//                itemDrawables = listOf(
//                    R.drawable.amplifying_tome,
//                    R.drawable.blasting_wand,
//                    R.drawable.blasting_wand,
//                    R.drawable.doran_ring,
//                    R.drawable.cull,
//                    R.drawable.faerie_charm
//                )
//            ),
//            ChampionInfo(
//                champDrawable = R.drawable.twistedfate,
//                name          = "twistedfate",
//                type          = "Support",
//                stats         = Stats(
//                    ad = 60, ap = 0, hp = 650, mp = 0,
//                    critical = 0, attackSpeed = 0, ar = 38, mr = 32
//                ),
//                itemDrawables = listOf(
//                    R.drawable.amplifying_tome,
//                    R.drawable.blasting_wand,
//                    R.drawable.blasting_wand,
//                    R.drawable.doran_ring,
//                    R.drawable.cull,
//                    R.drawable.faerie_charm
//                )
//            ),
//            ChampionInfo(
//                champDrawable = R.drawable.vi,
//                name          = "vi",
//                type          = "Support",
//                stats         = Stats(
//                    ad = 60, ap = 0, hp = 650, mp = 0,
//                    critical = 0, attackSpeed = 0, ar = 38, mr = 32
//                ),
//                itemDrawables = listOf(
//                    R.drawable.amplifying_tome,
//                    R.drawable.blasting_wand,
//                    R.drawable.blasting_wand,
//                    R.drawable.doran_ring,
//                    R.drawable.cull,
//                    R.drawable.faerie_charm
//                )
//            ),
//            ChampionInfo(
//                champDrawable = R.drawable.irelia,
//                name          = "irelia",
//                type          = "mid",
//                stats         = Stats(
//                    ad = 60, ap = 0, hp = 650, mp = 0,
//                    critical = 0, attackSpeed = 0, ar = 38, mr = 32
//                ),
//                itemDrawables = listOf(
//                    R.drawable.amplifying_tome,
//                    R.drawable.blasting_wand,
//                    R.drawable.blasting_wand,
//                    R.drawable.doran_ring,
//                    R.drawable.cull,
//                    R.drawable.faerie_charm
//                )
//            ),
//            ChampionInfo(
//                champDrawable = R.drawable.zed,
//                name          = "zed",
//                type          = "Support",
//                stats         = Stats(
//                    ad = 60, ap = 0, hp = 650, mp = 0,
//                    critical = 0, attackSpeed = 0, ar = 38, mr = 32
//                ),
//                itemDrawables = listOf(
//                    R.drawable.amplifying_tome,
//                    R.drawable.blasting_wand,
//                    R.drawable.blasting_wand,
//                    R.drawable.doran_ring,
//                    R.drawable.cull,
//                    R.drawable.faerie_charm
//                )
//            ),
//            ChampionInfo(
//                champDrawable = R.drawable.leblanc,
//                name          = "leblanc",
//                type          = "mid",
//                stats         = Stats(
//                    ad = 60, ap = 0, hp = 650, mp = 0,
//                    critical = 0, attackSpeed = 0, ar = 38, mr = 32
//                ),
//                itemDrawables = listOf(
//                    R.drawable.amplifying_tome,
//                    R.drawable.blasting_wand,
//                    R.drawable.blasting_wand,
//                    R.drawable.doran_ring,
//                    R.drawable.cull,
//                    R.drawable.faerie_charm
//                )
//            ),
//            ChampionInfo(
//                champDrawable = R.drawable.kaisa,
//                name          = "kaisa",
//                type          = "ad carry",
//                stats         = Stats(
//                    ad = 60, ap = 0, hp = 650, mp = 0,
//                    critical = 0, attackSpeed = 0, ar = 38, mr = 32
//                ),
//                itemDrawables = listOf(
//                    R.drawable.amplifying_tome,
//                    R.drawable.blasting_wand,
//                    R.drawable.blasting_wand,
//                    R.drawable.doran_ring,
//                    R.drawable.cull,
//                    R.drawable.faerie_charm
//                )
//            ),
//            ChampionInfo(
//                champDrawable = R.drawable.ashe,
//                name          = "ashe",
//                type          = "ad carry",
//                stats         = Stats(
//                    ad = 60, ap = 0, hp = 650, mp = 0,
//                    critical = 0, attackSpeed = 0, ar = 38, mr = 32
//                ),
//                itemDrawables = listOf(
//                    R.drawable.amplifying_tome,
//                    R.drawable.blasting_wand,
//                    R.drawable.blasting_wand,
//                    R.drawable.doran_ring,
//                    R.drawable.cull,
//                    R.drawable.faerie_charm
//                )
//            ),
//            ChampionInfo(
//                champDrawable = R.drawable.katarina,
//                name          = "katarina",
//                type          = "mid",
//                stats         = Stats(
//                    ad = 60, ap = 0, hp = 650, mp = 0,
//                    critical = 0, attackSpeed = 0, ar = 38, mr = 32
//                ),
//                itemDrawables = listOf(
//                    R.drawable.amplifying_tome,
//                    R.drawable.blasting_wand,
//                    R.drawable.blasting_wand,
//                    R.drawable.doran_ring,
//                    R.drawable.cull,
//                    R.drawable.faerie_charm
//                )
//            ),
//            ChampionInfo(
//                champDrawable = R.drawable.riven,
//                name          = "riven",
//                type          = "top",
//                stats         = Stats(
//                    ad = 60, ap = 0, hp = 650, mp = 0,
//                    critical = 0, attackSpeed = 0, ar = 38, mr = 32
//                ),
//                itemDrawables = listOf(
//                    R.drawable.amplifying_tome,
//                    R.drawable.blasting_wand,
//                    R.drawable.blasting_wand,
//                    R.drawable.doran_ring,
//                    R.drawable.cull,
//                    R.drawable.faerie_charm
//                )
//            ),
//            ChampionInfo(
//                champDrawable = R.drawable.vayne,
//                name          = "vayne",
//                type          = "ad carry",
//                stats         = Stats(
//                    ad = 60, ap = 0, hp = 650, mp = 0,
//                    critical = 0, attackSpeed = 0, ar = 38, mr = 32
//                ),
//                itemDrawables = listOf(
//                    R.drawable.amplifying_tome,
//                    R.drawable.blasting_wand,
//                    R.drawable.blasting_wand,
//                    R.drawable.doran_ring,
//                    R.drawable.cull,
//                    R.drawable.faerie_charm
//                )
//            ),
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
        val adapter = ImageGridAdapter(champs) { /* 클릭 처리 */ }
        binding.recyclerView.layoutManager = GridLayoutManager(requireContext(), 3)
        binding.recyclerView.adapter = adapter

        /* SearchView */
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextChange(text: String?): Boolean {
                adapter.updateFilter(newQuery = text)
                return true
            }
            override fun onQueryTextSubmit(q: String?) = false
        })

        /* ChipGroup */
        binding.laneGroup.setOnCheckedStateChangeListener { _, ids ->
            val lane = when (ids.firstOrNull()) {
                R.id.chipTop      -> Lane.TOP
                R.id.chipJungle   -> Lane.JUNGLE
                R.id.chipMid      -> Lane.MID
                R.id.chipAdc      -> Lane.ADC
                R.id.chipSupport  -> Lane.SUPPORT
                else              -> null            // 아무 것도 선택 안 함
            }
            adapter.updateFilter(newLane = lane)
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    // ───────────────────────────── Private Helpers ─────────────────────────────
    private fun setupRecyclerView() = with(binding.recyclerView) {
        layoutManager = GridLayoutManager(requireContext(), 3)
        val adapter = ImageGridAdapter(champs) { println(it.name) }
        this.adapter = adapter
        setHasFixedSize(true)

        /* ── SearchView 리스너 ── */
        // HomeFragment.kt (검색창 리스너 부분만 교체)

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextChange(newText: String?): Boolean {
                adapter.updateFilter(newQuery = newText)    // ← 여기!
                return true
            }
            override fun onQueryTextSubmit(q: String?) = false
        })

    }

}
