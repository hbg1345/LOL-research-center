// ui/builds/BuildsFragment.kt
package com.example.lol_research_center.ui.builds

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.lol_research_center.databinding.FragmentBuildsBinding
import com.example.lol_research_center.model.BuildInfo
import androidx.navigation.fragment.findNavController
import android.os.Build
import android.util.Log
import androidx.core.os.bundleOf
import com.example.lol_research_center.R
import com.example.lol_research_center.model.ChampionInfo
import com.example.lol_research_center.model.Lane
import com.example.lol_research_center.model.Skill
import com.example.lol_research_center.model.SkillDamageSet
import com.example.lol_research_center.model.Skills
import com.example.lol_research_center.model.Stats


//import dagger.hilt.android.AndroidEntryPoint  // Hilt 사용 시

//@AndroidEntryPoint          // 없으면 제거
class BuildsFragment : Fragment() {

    private var _binding: FragmentBuildsBinding? = null
    private val binding get() = _binding!!

    private val vm: BuildsViewModel by viewModels()
    private lateinit var adapter: BuildListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBuildsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        adapter = BuildListAdapter { build ->
            val bundle = bundleOf("build" to build)
            findNavController().navigate(
                R.id.action_builds_to_notification,
                bundle
            )
        }
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = adapter

        /* ─────────────────────  테스트용 더미 한 장  ───────────────────── */
        val dummyBuild = createDummyBuild()          // ▼ 아래에 함수 정의
        vm.addBuild(dummyBuild)

        adapter.submitList(listOf(dummyBuild))
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = this@BuildsFragment.adapter
            setHasFixedSize(true)
        }

        /* (+) FloatingActionButton */
        binding.fabAdd.setOnClickListener {
            Log.d("Builds", "+ 버튼 클릭")

            /* 1) FragmentResult 리스너 */
            parentFragmentManager.setFragmentResultListener(
                "build_result", viewLifecycleOwner
            ) { _, bundle ->
                val build: BuildInfo = if (android.os.Build.VERSION.SDK_INT >= 33) {
                    bundle.getParcelable("build", BuildInfo::class.java)!!   // API 33+
                } else {
                    @Suppress("DEPRECATION")
                    bundle.getParcelable<BuildInfo>("build")!!               // API 32-
                }
                vm.addBuild(build)
            }
            /* 2) 단순 Navigation ID 사용 */
            val args = bundleOf("pickerMode" to true)
            findNavController().navigate(
                R.id.action_builds_to_selectChampion,
                args
            )
        }


        /* LiveData 관찰 */
        vm.builds.observe(viewLifecycleOwner) { adapter.submitList(it) }
    }

    override fun onDestroyView() {
        super.onDestroyView(); _binding = null
    }
}

private fun createDummyBuild(): BuildInfo {
    val stats = Stats(
        attackdamage = 68,
        attackdamageperlevel = 3.5f,
        ap = 0,
        hp = 575,
        mp = 200,
        crit = 0,
        attackspeed = 0.651f,
        attackspeedperlevel = 3.0f,
        armor = 36,
        spellblock = 32,
        hpperlevel = 100,
        mpperlevel = 0,
        movespeed = 345,
        armorperlevel = 4.0f,
        spellblockperlevel = 1.5f,
        hpregen = 7.5f,
        hpregenperlevel = 0.7f,
        mpregen = 50f,
        mpregenperlevel = 0f,
        critperlevel = 0f
    )
    val skills = Skills(
        p = Skill(1, listOf(0,0,0,0,0),0f,0f,0f,0f,0f,"Passive"),
        q = Skill(5, listOf(55,80,105,130,155),0f,1f,0f,0f,0f,"Active"),
        w = Skill(5, listOf(40,60,80,100,120),0f,0.5f,0f,0f,0.08f,"Active"),
        e = Skill(5, listOf(100,130,160,190,220),0f,1f,0f,0f,0f,"Active"),
        r = Skill(3, listOf(150,300,450),0f,2f,0f,0f,0f,"Ultimate")
    )
    val champ = ChampionInfo(
        champDrawable = R.drawable.leesin,   // 임시 아이콘
        name          = "Lee Sin",
        lane          = Lane.JUNGLE,
        stats         = stats,
        itemDrawables = emptyList(),
        skills        = skills
    )
    return BuildInfo(
        champion   = champ,
        items      = emptyList(),
        calcResult = SkillDamageSet(0,0,0,0,0)
    )
}