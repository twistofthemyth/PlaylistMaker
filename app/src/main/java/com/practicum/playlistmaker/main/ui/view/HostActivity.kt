package com.practicum.playlistmaker.main.ui.view

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.ActivityHostBinding

class HostActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHostBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        binding = ActivityHostBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupNavBarVisibility()
        bindNavBarAndController()

        
    }

    private fun bindNavBarAndController() {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragment_container_view) as NavHostFragment
        val navController = navHostFragment.navController
        binding.navView.setupWithNavController(navController)
    }

    private fun setupNavBarVisibility() {
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { view, insets ->
            val imeVisible = insets.isVisible(WindowInsetsCompat.Type.ime())
            val navBarVisible = insets.isVisible(WindowInsetsCompat.Type.navigationBars())
            binding.navView.isVisible = !imeVisible

            if (!imeVisible && navBarVisible) {
                binding.navView.setPadding(0, 0, 0, insets.getInsets(WindowInsetsCompat.Type.navigationBars()).bottom)
            } else {
                binding.navView.setPadding(0, 0, 0, 0)
            }

            insets
        }
    }
}