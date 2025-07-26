package com.jimmy.valladares.pokefit

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class Encuesta : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_encuesta)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        
        // Habilitar el botón de navegación hacia atrás en la barra de acción
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        
        // Configurar botón "Salir de sedentarismo"
        val btnSedentary = findViewById<CardView>(R.id.btn_sedentary)
        btnSedentary.setOnClickListener {
            // Navegar a la pantalla de Objetivo de Pasos
            val intent = Intent(this, ObjetivoPasos::class.java)
            intent.putExtra("OBJETIVO", "SEDENTARISMO")
            startActivity(intent)
        }

        // Configurar botón "Mejorar mi entrenamiento"
        val btnImprove = findViewById<CardView>(R.id.btn_improve)
        btnImprove.setOnClickListener {
            // Navegar a la pantalla de Selección de Objetivo
            val intent = Intent(this, SeleccionObjetivo::class.java)
            intent.putExtra("OBJETIVO", "ENTRENAMIENTO")
            startActivity(intent)
        }
    }
    
    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}