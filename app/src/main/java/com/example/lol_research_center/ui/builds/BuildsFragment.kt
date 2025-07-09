// ui/builds/BuildsFragment.kt
package com.example.lol_research_center.ui.builds

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.fragment.app.activityViewModels
import com.example.lol_research_center.ui.viewmodel.BuildViewModel
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.lol_research_center.databinding.FragmentBuildsBinding
import com.example.lol_research_center.model.BuildInfo
import androidx.fragment.app.activityViewModels // Add this import
import androidx.navigation.fragment.findNavController
import com.example.lol_research_center.R // Add this import



//import dagger.hilt.android.AndroidEntryPoint  // Hilt 사용 시

//@AndroidEntryPoint          // 없으면 제거
class BuildsFragment : Fragment() {

    private var _binding: FragmentBuildsBinding? = null
    private val binding get() = _binding!!

    private val vm: BuildsViewModel by viewModels()
    private val buildViewModel: BuildViewModel by activityViewModels() // Get instance of BuildViewModel
    private lateinit var adapter: BuildListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBuildsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        adapter = BuildListAdapter(
            onClick = { build ->
                val bundle = bundleOf("build" to build)
                findNavController().navigate(
                    R.id.action_builds_to_notification,
                    bundle
                )
            },
            onDeleteClick = { build ->
                android.app.AlertDialog.Builder(requireContext())
                    .setTitle("빌드 삭제")
                    .setMessage("정말 이 빌드를 삭제하시겠습니까?")
                    .setPositiveButton("예") { dialog, which ->
                        vm.deleteBuild(build)
                    }
                    .setNegativeButton("아니오", null)
                    .show()
            }
        )
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = adapter

        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = this@BuildsFragment.adapter
            setHasFixedSize(true)
        }

        /* (+) FloatingActionButton */
        binding.fabAdd.setOnClickListener {
            Log.d("Builds", "+ 버튼 클릭")

            // Reset the current build in BuildViewModel before starting a new one
            buildViewModel.resetCurrentBuild()

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
