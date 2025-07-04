package com.example.lol_research_center.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.lol_research_center.R
import com.example.lol_research_center.databinding.FragmentDashboardBinding

class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val dashboardViewModel =
            ViewModelProvider(this).get(DashboardViewModel::class.java)

        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val recyclerView = binding.recyclerView
        recyclerView.layoutManager = androidx.recyclerview.widget.GridLayoutManager(context, 3)

        val items = listOf(
            ItemData(R.drawable.amplifying_tome, "Amplifying Tome", emptyMap()),
            ItemData(R.drawable.bf_sword, "B.F. Sword", emptyMap()),
            ItemData(R.drawable.blasting_wand, "Blasting Wand", emptyMap()),
            ItemData(R.drawable.cloak_of_agility, "Cloak of Agility", emptyMap()),
            ItemData(R.drawable.cloth_armor, "Cloth Armor", emptyMap()),
            ItemData(R.drawable.cull, "Cull", emptyMap()),
            ItemData(R.drawable.dagger, "Dagger", emptyMap()),
            ItemData(R.drawable.dark_seal, "Dark Seal", emptyMap()),
            ItemData(R.drawable.doran_blade, "Doran Blade", emptyMap()),
            ItemData(R.drawable.doran_ring, "Doran Ring", emptyMap()),
            ItemData(R.drawable.doran_shield, "Doran Shield", emptyMap()),
            ItemData(R.drawable.faerie_charm, "Faerie Charm", emptyMap()),
            ItemData(R.drawable.glowing_mote, "Glowing Mote", emptyMap()),
            ItemData(R.drawable.gustwalker_hatchling, "Gustwalker Hatchling", emptyMap()),
            ItemData(R.drawable.long_sword, "Long Sword", emptyMap()),
            ItemData(R.drawable.mosstomper_seedling, "Mosstomper Seedling", emptyMap()),
            ItemData(R.drawable.needlessly_large_rod, "Needlessly Large Rod", emptyMap()),
            ItemData(R.drawable.null_magic_mantle, "Null Magic Mantle", emptyMap()),
            ItemData(R.drawable.pickaxe, "Pickaxe", emptyMap()),
            ItemData(R.drawable.rejuvenation_bead, "Rejuvenation Bead", emptyMap()),
            ItemData(R.drawable.ruby_crystal, "Ruby Crystal", emptyMap()),
            ItemData(R.drawable.runic_compass, "Runic Compass", emptyMap()),
            ItemData(R.drawable.sapphire_crystal, "Sapphire Crystal", emptyMap()),
            ItemData(R.drawable.scorchclaw_pup, "Scorchclaw Pup", emptyMap()),
            ItemData(R.drawable.tear_of_the_goddess, "Tear of the Goddess", emptyMap()),
            ItemData(R.drawable.world_atlas, "World Atlas", emptyMap()),
        )

        val adapter = DashboardAdapter(items)
        recyclerView.adapter = adapter

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}