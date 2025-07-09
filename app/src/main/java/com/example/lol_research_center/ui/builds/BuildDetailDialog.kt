// ui/builds/BuildDetailDialog.kt
package com.example.lol_research_center.ui.builds

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.example.lol_research_center.model.BuildInfo

class BuildDetailDialog : DialogFragment() {

    companion object {
        private const val ARG_BUILD = "arg_build"

        fun show(fm: androidx.fragment.app.FragmentManager, build: BuildInfo) {
            BuildDetailDialog().apply {
                arguments = Bundle().apply { putParcelable(ARG_BUILD, build) }
            }.show(fm, "BuildDetailDialog")
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val build: BuildInfo = requireArguments().getParcelable(ARG_BUILD)!!

        /* TODO: 커스텀 레이아웃을 inflate 해서 AlertDialog.Builder#setView 로 넣으세요 */
        return AlertDialog.Builder(requireContext())
            .setTitle(build.champion.name)
            .setMessage("스킬 Q 데미지: ${build.calcResult.q}\n…")
            .setPositiveButton(android.R.string.ok, null)
            .create()
    }
}

