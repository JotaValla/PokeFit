package com.jimmy.valladares.pokefit

import android.content.Intent
import android.os.Bundle
import android.widget.SeekBar
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class ObjetivoPasos : AppCompatActivity() {
    
    private lateinit var tvStepsCount: TextView
    private lateinit var sliderSteps: SeekBar
    private var currentSteps: Int = 5000
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_objetivo_pasos)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        
        // Inicializar vistas
        tvStepsCount = findViewById(R.id.tv_steps_count)
        sliderSteps = findViewById(R.id.slider_steps)
        
        // Configurar botón de regresar
        val btnBack = findViewById<TextView>(R.id.btn_back)
        btnBack.setOnClickListener {
            finish() // Cierra esta actividad y regresa a la anterior
        }
        
        // Configurar slider
        sliderSteps.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                // Asegurar un mínimo de 1000 pasos
                currentSteps = if (progress < 1000) 1000 else progress
                tvStepsCount.text = currentSteps.toString()
            }
            
            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                // No se necesita implementación
            }
            
            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                // No se necesita implementación
            }
        })
          // Configurar botón continuar
        val btnContinue = findViewById<CardView>(R.id.btn_continue)
        btnContinue.setOnClickListener {
            // Guardar la meta de pasos (aquí usaríamos SharedPreferences en una implementación real)
            val stepGoal = currentSteps
            
            // Navegar a EleccionPokemon con tipo PASOS para sedentarismo
            val intent = Intent(this, EleccionPokemon::class.java)
            intent.putExtra("TIPO_ENTRENAMIENTO", "PASOS")
            intent.putExtra("OBJETIVO_PASOS", stepGoal)
            startActivity(intent)
        }
    }
}