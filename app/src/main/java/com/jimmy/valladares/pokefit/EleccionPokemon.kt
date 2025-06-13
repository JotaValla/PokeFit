package com.jimmy.valladares.pokefit

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import pl.droidsonroids.gif.GifImageView

class EleccionPokemon : AppCompatActivity() {
    
    private lateinit var gifPokemon1: GifImageView
    private lateinit var gifPokemon2: GifImageView
    private lateinit var gifPokemon3: GifImageView
    
    private lateinit var tvPokemon1: TextView
    private lateinit var tvPokemon2: TextView
    private lateinit var tvPokemon3: TextView
    private lateinit var tvHeader: TextView
    
    // Mapeo de tipos de entrenamiento a listas de Pokémon
    private val pokemonMap = mapOf(
        "PASOS" to listOf(
            Pokemon("Totodile", "totodile"),
            Pokemon("Eevee", "eevee"),
            Pokemon("Piplup", "piplup")
        ),
        "VELOCIDAD" to listOf(
            Pokemon("Pidgey", "pidgey"),
            Pokemon("Pikachu", "pikachu"),
            Pokemon("Treecko", "treecko")
        ),
        "FUERZA" to listOf(
            Pokemon("Machop", "machop"),
            Pokemon("Torchic", "torchic"),
            Pokemon("Torchic", "torchic") // Parece repetido en la imagen, podrías cambiarlo por otro Pokémon
        ),
        "RESISTENCIA" to listOf(
            Pokemon("Horsea", "horsea"),
            Pokemon("Charmander", "charmander"),
            Pokemon("Tepig", "tepig")
        )
    )
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_eleccion_pokemon)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        
        // Inicializar vistas
        initViews()
        
        // Configurar botón de regresar
        val btnBack = findViewById<TextView>(R.id.btn_back)
        btnBack.setOnClickListener {
            finish()
        }
        
        // Obtener el tipo de entrenamiento del intent
        val tipoEntrenamiento = intent.getStringExtra("TIPO_ENTRENAMIENTO") ?: "PASOS"
        
        // Actualizar título según el tipo de entrenamiento
        tvHeader.text = "Elige Pokemon ${tipoEntrenamiento.lowercase().capitalize()}"
        
        // Cargar los Pokémon adecuados según el tipo
        loadPokemonByType(tipoEntrenamiento)
        
        // Configurar click listeners para seleccionar Pokémon
        setupPokemonClickListeners()
    }
    
    private fun initViews() {
        gifPokemon1 = findViewById(R.id.gif_pokemon1)
        gifPokemon2 = findViewById(R.id.gif_pokemon2)
        gifPokemon3 = findViewById(R.id.gif_pokemon3)
        
        tvPokemon1 = findViewById(R.id.tv_pokemon1)
        tvPokemon2 = findViewById(R.id.tv_pokemon2)
        tvPokemon3 = findViewById(R.id.tv_pokemon3)
        tvHeader = findViewById(R.id.tv_header)
    }
    
    private fun loadPokemonByType(type: String) {
        val pokemonList = pokemonMap[type] ?: pokemonMap["PASOS"]!! // Default a PASOS si no encuentra
        
        // Cargar primer Pokémon
        val resourceId1 = resources.getIdentifier(pokemonList[0].resourceName, "drawable", packageName)
        gifPokemon1.setImageResource(resourceId1)
        tvPokemon1.text = pokemonList[0].name
        
        // Cargar segundo Pokémon
        val resourceId2 = resources.getIdentifier(pokemonList[1].resourceName, "drawable", packageName)
        gifPokemon2.setImageResource(resourceId2)
        tvPokemon2.text = pokemonList[1].name
        
        // Cargar tercer Pokémon
        val resourceId3 = resources.getIdentifier(pokemonList[2].resourceName, "drawable", packageName)
        gifPokemon3.setImageResource(resourceId3)
        tvPokemon3.text = pokemonList[2].name
    }
    
    private fun setupPokemonClickListeners() {
        // Click listener para el primer Pokémon
        gifPokemon1.setOnClickListener {
            selectPokemon(tvPokemon1.text.toString())
        }
        
        // Click listener para el segundo Pokémon
        gifPokemon2.setOnClickListener {
            selectPokemon(tvPokemon2.text.toString())
        }
        
        // Click listener para el tercer Pokémon
        gifPokemon3.setOnClickListener {
            selectPokemon(tvPokemon3.text.toString())
        }
    }
    
    private fun selectPokemon(pokemonName: String) {
        // Guardar la selección del Pokémon y avanzar a la siguiente pantalla
        Toast.makeText(this, "Has seleccionado a $pokemonName", Toast.LENGTH_SHORT).show()
        
        // Aquí podrías guardar la selección en SharedPreferences
        // y navegar a la siguiente pantalla
        val intent = Intent(this, Home::class.java)
        intent.putExtra("POKEMON_SELECCIONADO", pokemonName)
        startActivity(intent)
    }
    
    // Clase auxiliar para representar a un Pokémon
    data class Pokemon(val name: String, val resourceName: String)
}