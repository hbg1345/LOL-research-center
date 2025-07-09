package com.example.lol_research_center

import android.graphics.Rect
import android.os.Bundle
import android.view.View
import android.view.ViewTreeObserver
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.lol_research_center.databinding.ActivityMainBinding
import com.google.android.material.appbar.MaterialToolbar
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var isKeyboardShowing = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 1) view binding inflate & setContentView
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 2) Toolbar를 ActionBar로 등록
        setSupportActionBar(binding.topAppBar)

        // 3) NavController와 BottomNav, AppBarConfiguration 연결
        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        val appBarConfig = AppBarConfiguration(
            // 탑레벨 목적지 ID들(하단 탭에 해당하는 fragment ID들) 넣어주세요
            setOf(
                R.id.navigation_home,
                R.id.buildsFragment,
                /* R.id.otherTabFragment */
            )
        )
        setupActionBarWithNavController(navController, appBarConfig)
        binding.navView.setupWithNavController(navController)

        // 4) (기존) 시스템 바 & 키보드 리스너
        WindowCompat.setDecorFitsSystemWindows(window, false)
        binding.root.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            val r = Rect()
            override fun onGlobalLayout() {
                binding.root.getWindowVisibleDisplayFrame(r)
                val screenHeight = binding.root.rootView.height
                val keypadHeight = screenHeight - r.bottom
                if (keypadHeight > screenHeight * 0.15) {
                    if (!isKeyboardShowing) {
                        isKeyboardShowing = true
                        binding.navView.visibility = View.GONE
                    }
                } else {
                    if (isKeyboardShowing) {
                        isKeyboardShowing = false
                        binding.navView.visibility = View.VISIBLE
                    }
                }
            }
        })
    }

    // 뒤로 버튼 누르면 NavController로 위로 이동
    override fun onSupportNavigateUp(): Boolean {
        return findNavController(R.id.nav_host_fragment_activity_main)
            .navigateUp() || super.onSupportNavigateUp()
    }
}
