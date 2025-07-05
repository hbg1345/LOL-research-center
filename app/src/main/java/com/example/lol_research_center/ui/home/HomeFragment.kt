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
        val adapter = ImageGridAdapter(champs) {
            println(it.name)
        }
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
