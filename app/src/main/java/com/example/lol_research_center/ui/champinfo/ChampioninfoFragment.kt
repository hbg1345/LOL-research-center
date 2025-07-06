package com.example.lol_research_center.ui.champinfo

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.lol_research_center.R
import com.example.lol_research_center.model.BuildInfo
import com.example.lol_research_center.model.ChampionInfo


class ChampioninfoFragment : Fragment() {

    private lateinit var champinfo: ChampionInfo          // ← 전달받은 BuildInfo

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        champinfo = if (Build.VERSION.SDK_INT >= 33) {
            requireArguments().getParcelable("championInfo", ChampionInfo::class.java)!!
        } else {
            @Suppress("DEPRECATION")
            requireArguments().getParcelable("build")!!
        }
        println(champinfo.name)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_champinfo, container, false)
    }
}

