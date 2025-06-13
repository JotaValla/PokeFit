package com.jimmy.valladares.pokefit

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide

class Perfil : AppCompatActivity() {
    
    private lateinit var trainerGif: ImageView
    
    // Navegación
    private lateinit var navHome: LinearLayout
    private lateinit var navStats: LinearLayout
    private lateinit var navTrain: LinearLayout
    private lateinit var navProfile: LinearLayout
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_perfil)
        
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            // Solo aplicamos padding top y lateral, no bottom para que la navegación llegue al fondo
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0)
            insets
        }
        
        initializeViews()
        setupNavigation()
        loadTrainerGif()
    }
    
    private fun initializeViews() {
        trainerGif = findViewById(R.id.iv_trainer_gif)
        
        // Navegación
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
            startActivity(Intent(this, StatsFuerza::class.java))
            finish()
        }
        
        navTrain.setOnClickListener {
            startActivity(Intent(this, EntrenamientoFuerza::class.java))
            finish()
        }
        
        navProfile.setOnClickListener {
            // Ya estamos en Profile, no hacer nada
        }
    }
    
    private fun loadTrainerGif() {
        Glide.with(this)
            .asGif()
            .load(R.drawable.rizzo_gif)
            .into(trainerGif)
    }
}