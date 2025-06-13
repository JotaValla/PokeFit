package com.jimmy.valladares.pokefit

import android.content.Intent
import android.os.Bundle
import android.widget.FrameLayout
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class WelcomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.welcome_main)
        
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        
        // Configurar el botón de continuar
        val btnContinue = findViewById<FrameLayout>(R.id.btn_continue)
        btnContinue.setOnClickListener {
            // Aquí puedes navegar a la siguiente actividad
            // Por ejemplo: startActivity(Intent(this, NextActivity::class.java))
            // finish() // Opcional: cerrar esta actividad
        }
    }
}