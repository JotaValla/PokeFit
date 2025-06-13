package com.jimmy.valladares.pokefit

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
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

        // Configurar botón "Salir de sedentarismo"
        val btnSedentary = findViewById<CardView>(R.id.btn_sedentary)
        btnSedentary.setOnClickListener {
            // Aquí puedes navegar a la siguiente actividad según esta selección
            // Por ejemplo: 
            // val intent = Intent(this, ObjetivoPasos::class.java)
            // intent.putExtra("OBJETIVO", "SEDENTARISMO")
            // startActivity(intent)

            Toast.makeText(this, "Objetivo: Salir del sedentarismo", Toast.LENGTH_SHORT).show()
        }

        // Configurar botón "Mejorar mi entrenamiento"
        val btnImprove = findViewById<CardView>(R.id.btn_improve)
        btnImprove.setOnClickListener {
            // Aquí puedes navegar a la siguiente actividad según esta selección
            // Por ejemplo:
            // val intent = Intent(this, EntrenamientoFuerza::class.java)
            // intent.putExtra("OBJETIVO", "ENTRENAMIENTO")
            // startActivity(intent)

            Toast.makeText(this, "Objetivo: Mejorar mi entrenamiento", Toast.LENGTH_SHORT).show()
        }
    }
}