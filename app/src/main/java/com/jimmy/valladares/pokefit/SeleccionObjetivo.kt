package com.jimmy.valladares.pokefit

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class SeleccionObjetivo : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_seleccion_objetivo)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }        // Configurar botón de regresar
        val btnBack = findViewById<TextView>(R.id.btn_back)
        btnBack.setOnClickListener {
            finish() // Cierra esta actividad y regresa a la anterior
        }

        // Configurar botón "Velocidad"
        val btnSpeed = findViewById<CardView>(R.id.btn_speed)
        btnSpeed.setOnClickListener {
            val intent = Intent(this, EleccionPokemon::class.java)
            intent.putExtra("TIPO_ENTRENAMIENTO", "VELOCIDAD")
            startActivity(intent)
        }

        // Configurar botón "Fuerza"
        val btnStrength = findViewById<CardView>(R.id.btn_strength)
        btnStrength.setOnClickListener {
            val intent = Intent(this, EleccionPokemon::class.java)
            intent.putExtra("TIPO_ENTRENAMIENTO", "FUERZA")
            startActivity(intent)
        }

        // Configurar botón "Resistencia"
        val btnEndurance = findViewById<CardView>(R.id.btn_endurance)
        btnEndurance.setOnClickListener {
            val intent = Intent(this, EleccionPokemon::class.java)
            intent.putExtra("TIPO_ENTRENAMIENTO", "RESISTENCIA")
            startActivity(intent)
        }
    }
}