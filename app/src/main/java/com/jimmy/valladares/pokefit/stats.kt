package com.jimmy.valladares.pokefit

import android.os.Bundle
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import pl.droidsonroids.gif.GifImageView

class StatsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_stats)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Cargar el GIF de Treecko
        val gifPokemon = findViewById<GifImageView>(R.id.gif_pokemon)
        gifPokemon.setImageResource(R.drawable.treecko)

        // Configurar las pokebolas de los días de la semana
        // Miércoles y Viernes muestran master ball (objetivo alcanzado)
        configureGoalAchievement()

        // Configurar navegación
        setupNavigation()
    }

    private fun configureGoalAchievement() {
        // Los días donde se alcanzó el objetivo (Miércoles y Viernes en este caso)
        // muestran la master ball en lugar de la pokeball regular

        // Miércoles
        val imgMiercoles = findViewById<ImageView>(R.id.img_ball_mie)
        imgMiercoles.setImageResource(R.drawable.master_ball)

        // Viernes
        val imgViernes = findViewById<ImageView>(R.id.img_ball_vie)
        imgViernes.setImageResource(R.drawable.master_ball)
    }

    private fun setupNavigation() {
        // Configurar los botones de navegación
        val navHome = findViewById<ImageView>(R.id.nav_home)
        val navStats = findViewById<ImageView>(R.id.nav_stats)
        val navTraining = findViewById<ImageView>(R.id.nav_training)
        val navProfile = findViewById<ImageView>(R.id.nav_profile)

        // Aquí pondrías los click listeners para cada ícono de navegación
        navHome.setOnClickListener {
            // Navegar a Home
            // startActivity(Intent(this, Home::class.java))
        }

        // Stats ya está seleccionado

        navTraining.setOnClickListener {
            // Navegar a Training
            // startActivity(Intent(this, Training::class.java))
        }

        navProfile.setOnClickListener {
            // Navegar a Profile
            // startActivity(Intent(this, Perfil::class.java))
        }
    }
}