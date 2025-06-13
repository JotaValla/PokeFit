package com.jimmy.valladares.pokefit

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide

class StatsFuerza : AppCompatActivity() {

    // UI Components
    private lateinit var machampGif: ImageView
    private lateinit var levelText: TextView
    private lateinit var progressBar: ProgressBar
    private lateinit var currentStreakText: TextView
    private lateinit var weeklyExpText: TextView

    // Navigation
    private lateinit var navHome: LinearLayout
    private lateinit var navStats: LinearLayout
    private lateinit var navTrain: LinearLayout
    private lateinit var navProfile: LinearLayout

    // Mock data
    private val currentLevel = 200
    private val maxLevel = 500
    private val currentProgress = 75
    private val currentStreak = 5
    private val weeklyExp = 225

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_stats_fuerza)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0)
            insets
        }

        initializeViews()
        setupNavigation()
        loadMachampGif()
        updateStatsData()
    }

    private fun initializeViews() {
        machampGif = findViewById(R.id.iv_machamp)
        levelText = findViewById(R.id.tv_level)
        progressBar = findViewById(R.id.progress_bar)
        currentStreakText = findViewById(R.id.tv_current_streak)
        weeklyExpText = findViewById(R.id.tv_weekly_exp)

        // Navigation
        navHome = findViewById(R.id.nav_home)
        navStats = findViewById(R.id.nav_stats)
        navTrain = findViewById(R.id.nav_train)
        navProfile = findViewById(R.id.nav_profile)
    }

    private fun setupNavigation() {
        navHome.setOnClickListener {
            startActivity(Intent(this, Home::class.java))
            finish()
        }

        navStats.setOnClickListener {
            // Ya estamos en Stats
        }

        navTrain.setOnClickListener {
            startActivity(Intent(this, EntrenamientoFuerza::class.java))
            finish()
        }

        navProfile.setOnClickListener {
            startActivity(Intent(this, Perfil::class.java))
            finish()
        }
    }

    private fun loadMachampGif() {
        // Load Machamp GIF using Glide
        Glide.with(this)
            .asGif()
            .load(R.drawable.machamp_gif)
            .into(machampGif)
    }

    private fun updateStatsData() {
        // Update level display
        levelText.text = currentLevel.toString()
        
        // Update progress bar
        progressBar.progress = currentProgress
        
        // Update streak
        currentStreakText.text = "$currentStreak días"
        
        // Update weekly experience
        weeklyExpText.text = "$weeklyExp exp"
        
        // Animate progress bar
        animateProgressBar()
    }

    private fun animateProgressBar() {
        // Animate progress bar from 0 to current value
        val animator = android.animation.ObjectAnimator.ofInt(progressBar, "progress", 0, currentProgress)
        animator.duration = 1500
        animator.interpolator = android.view.animation.DecelerateInterpolator()
        animator.start()
    }

    override fun onResume() {
        super.onResume()
        // Reload GIF to ensure it's playing
        loadMachampGif()
    }
}