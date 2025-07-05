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
import com.example.lol_research_center.R


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
            // 리스트 항목 클릭 시 상세 다이얼로그/화면으로 이동
            BuildDetailDialog.show(childFragmentManager, build)
        }

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
            findNavController().navigate(R.id.action_builds_to_selectChampion)
        }


        /* LiveData 관찰 */
        vm.builds.observe(viewLifecycleOwner) { adapter.submitList(it) }
    }

    override fun onDestroyView() {
        super.onDestroyView(); _binding = null
    }
}
