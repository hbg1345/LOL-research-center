package com.example.lol_research_center.ui.home
import androidx.core.os.bundleOf
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
import androidx.navigation.fragment.findNavController   // ← 이 한 줄


import com.example.lol_research_center.model.ChampionDataLoader
class HomeFragment : Fragment() {

    // ───────────────────────────────── Binding ─────────────────────────────────
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val champs: List<ChampionInfo> by lazy {
        ChampionDataLoader.loadChampionsFromAsset(requireContext(), "champions.json")
    }

    // ─────────────────────────────── 샘플 데이터 ────────────────────────────────

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
        val pickerMode = arguments?.getBoolean("pickerMode") ?: false

        val adapter = ImageGridAdapter(champs) { champ ->
            if (pickerMode) {
                println("hihihi")
                // ① 선택된 챔피언을 갖고 아이템 선택 화면으로 이동
                val bundle = bundleOf("champion" to champ)
                findNavController().navigate(
                    R.id.action_selectChampion_to_selectItems, bundle
                )
            } else {
                println("byebyebyebye")
                // ② 평소엔 상세 보기 등 기존 동작
//                showChampionDetail(champ)
            }
        }

        binding.recyclerView.layoutManager = GridLayoutManager(requireContext(), 3)
        binding.recyclerView.adapter = adapter
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


}
