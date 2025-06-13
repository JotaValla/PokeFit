package com.jimmy.valladares.pokefit

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide

class Home : AppCompatActivity() {
    
    private lateinit var progressFill: View
    private lateinit var progressText: TextView
    private lateinit var maxText: TextView
    private lateinit var currentLevelText: TextView
    private lateinit var pokemonNameText: TextView
    private lateinit var pokemonGif: ImageView
    
    // Navegación
    private lateinit var navHome: LinearLayout
    private lateinit var navStats: LinearLayout
    private lateinit var navTrain: LinearLayout
    private lateinit var navProfile: LinearLayout
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_home)
        
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            // Solo aplicamos padding top y lateral, no bottom para que la navegación llegue al fondo
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0)
            insets
        }
        
        initializeViews()
        setupNavigation()
        updatePokemonData()
    }
    
    private fun initializeViews() {
        progressFill = findViewById(R.id.progress_fill)
        progressText = findViewById(R.id.tv_progress_text)
        maxText = findViewById(R.id.tv_max_text)
        currentLevelText = findViewById(R.id.tv_current_level)
        pokemonNameText = findViewById(R.id.tv_pokemon_name)
        pokemonGif = findViewById(R.id.iv_pokemon_gif)
        
        // Cargar el GIF usando Glide
        Glide.with(this)
            .asGif()
            .load(R.drawable.machamp_gif)
            .into(pokemonGif)
        
        // Navegación
        navHome = findViewById(R.id.nav_home)
        navStats = findViewById(R.id.nav_stats)
        navTrain = findViewById(R.id.nav_train)
        navProfile = findViewById(R.id.nav_profile)
    }
    
    private fun setupNavigation() {
        navHome.setOnClickListener {
            // Ya estamos en Home, no necesita hacer nada o añadir feedback visual
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
            startActivity(Intent(this, Perfil::class.java))
            finish()
        }
    }
    
    private fun updatePokemonData() {
        // Datos de ejemplo - estos vendrían de tu base de datos o API
        val currentLevel = 1
        val currentExp = 200
        val maxExp = 600
        val pokemonName = "Garchomp"
        
        // Actualizar UI
        currentLevelText.text = currentLevel.toString()
        pokemonNameText.text = pokemonName
        progressText.text = currentExp.toString()
        maxText.text = maxExp.toString()
        
        // Configurar el ancho de la barra de progreso
        progressFill.post {
            val progressBackground = findViewById<View>(R.id.progress_background_view)
            val containerWidth = progressBackground.width
            val progressPercentage = currentExp.toFloat() / maxExp.toFloat()
            val newWidth = (containerWidth * progressPercentage).toInt()
            
            val layoutParams = progressFill.layoutParams
            layoutParams.width = maxOf(newWidth, 40) // Mínimo 40dp para que se vea
            progressFill.layoutParams = layoutParams
        }
    }
}