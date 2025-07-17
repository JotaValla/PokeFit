package com.jimmy.valladares.pokefit

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
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
        updateNavigationState()
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
        // Configurar los botones de navegación usando LinearLayout (que contienen los ImageView)
        val navHome = findViewById<LinearLayout>(R.id.nav_home)
        val navStats = findViewById<LinearLayout>(R.id.nav_stats)
        val navTrain = findViewById<LinearLayout>(R.id.nav_train) // Corregido: nav_train, no nav_training
        val navProfile = findViewById<LinearLayout>(R.id.nav_profile)

        // Click listeners para cada sección de navegación
        navHome.setOnClickListener {
            navigateToActivity(Home::class.java)
        }

        // Stats ya está seleccionado (no necesita listener si estamos en StatsActivity)
        navStats.setOnClickListener {
            // Ya estamos en Stats, no hacer nada o mostrar mensaje
        }

        navTrain.setOnClickListener {
            navigateToActivity(EntrenamientoFuerza::class.java)
        }

        navProfile.setOnClickListener {
            navigateToActivity(Perfil::class.java)
        }
    }


    private fun navigateToActivity(activityClass: Class<*>) {
        val intent = Intent(this, activityClass)
        startActivity(intent)
        // No usar finish() aquí para mantener el stack de navegación
        overridePendingTransition(0, 0) // Transición suave sin animación
    }


    private fun updateNavigationState() {
        // Obtener las referencias a los LinearLayout
        val navHome = findViewById<LinearLayout>(R.id.nav_home)
        val navStats = findViewById<LinearLayout>(R.id.nav_stats)
        val navTrain = findViewById<LinearLayout>(R.id.nav_train)
        val navProfile = findViewById<LinearLayout>(R.id.nav_profile)

        // Obtener los ImageView dentro de cada LinearLayout (primer hijo)
        val homeIcon = navHome.getChildAt(0) as? ImageView
        val statsIcon = navStats.getChildAt(0) as? ImageView
        val trainIcon = navTrain.getChildAt(0) as? ImageView
        val profileIcon = navProfile.getChildAt(0) as? ImageView

        // Resetear alpha de todos los íconos
        homeIcon?.alpha = 0.5f
        statsIcon?.alpha = 1.0f // Stats está activo
        trainIcon?.alpha = 0.5f
        profileIcon?.alpha = 0.5f
    }
}