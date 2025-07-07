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

        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = this@BuildsFragment.adapter
            setHasFixedSize(true)
        }

        /* (+) FloatingActionButton */
        binding.fabAdd.setOnClickListener {
            Log.d("Builds", "+ 버튼 클릭")

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
