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
    
    // Lista actual de Pokémon mostrados
    private var currentPokemonList = listOf<Pokemon>()
    
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
            Pokemon("Geodude", "geodude")
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
        
        // Inicializar todas las vistas
        initViews()
        
        // Configurar botón de regresar
        val btnBack = findViewById<TextView>(R.id.btn_back)
        btnBack.setOnClickListener {
            finish()
        }
        
        // Obtener el tipo de entrenamiento del intent
        val tipoEntrenamiento = intent.getStringExtra("TIPO_ENTRENAMIENTO") ?: "PASOS"
        
        // Actualizar el título
        updateHeaderTitle(tipoEntrenamiento)
        
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
        
        val headerView = findViewById<TextView>(R.id.tv_header)
        if (headerView != null) {
            tvHeader = headerView
        } else {
            Toast.makeText(this, "Error: tv_header no encontrado en el layout", Toast.LENGTH_LONG).show()
            finish()
            return
        }
    }
    
    private fun updateHeaderTitle(tipo: String) {
        if (::tvHeader.isInitialized) {
            val headerText = when (tipo) {
                "PASOS" -> "Elige Pokemon Pasos"
                "VELOCIDAD" -> "Elige Pokemon Velocidad"
                "FUERZA" -> "Elige Pokemon Fuerza"
                "RESISTENCIA" -> "Elige Pokemon Resistencia"
                else -> "Elige Pokemon"
            }
            tvHeader.text = headerText
        }
    }
    
    private fun loadPokemonByType(type: String) {
        currentPokemonList = pokemonMap[type] ?: pokemonMap["PASOS"]!!
        
        if (::gifPokemon1.isInitialized && ::gifPokemon2.isInitialized && ::gifPokemon3.isInitialized) {
            loadPokemonImage(currentPokemonList[0], gifPokemon1, tvPokemon1)
            loadPokemonImage(currentPokemonList[1], gifPokemon2, tvPokemon2)
            loadPokemonImage(currentPokemonList[2], gifPokemon3, tvPokemon3)
        }
    }
    
    private fun loadPokemonImage(pokemon: Pokemon, gifView: GifImageView, textView: TextView) {
        val resourceId = resources.getIdentifier(pokemon.resourceName, "drawable", packageName)
        
        if (resourceId != 0) {
            gifView.setImageResource(resourceId)
        } else {
            val defaultResource = resources.getIdentifier("pokeball", "drawable", packageName)
            if (defaultResource != 0) {
                gifView.setImageResource(defaultResource)
            }
        }
        
        textView.text = pokemon.name
    }
    
    private fun setupPokemonClickListeners() {
        if (::gifPokemon1.isInitialized && ::gifPokemon2.isInitialized && ::gifPokemon3.isInitialized) {
            gifPokemon1.setOnClickListener {
                if (currentPokemonList.isNotEmpty()) {
                    selectPokemon(currentPokemonList[0])
                }
            }
            
            gifPokemon2.setOnClickListener {
                if (currentPokemonList.size > 1) {
                    selectPokemon(currentPokemonList[1])
                }
            }
            
            gifPokemon3.setOnClickListener {
                if (currentPokemonList.size > 2) {
                    selectPokemon(currentPokemonList[2])
                }
            }
        }
    }
    
    private fun selectPokemon(pokemon: Pokemon) {
        Toast.makeText(this, "Has seleccionado a ${pokemon.name}", Toast.LENGTH_SHORT).show()
        
        // Obtener datos del intent
        val tipoEntrenamiento = intent.getStringExtra("TIPO_ENTRENAMIENTO") ?: "PASOS"
        val objetivoPasos = intent.getIntExtra("OBJETIVO_PASOS", 0)
        
        // Guardar la selección
        saveSelectedPokemon(pokemon, tipoEntrenamiento, objetivoPasos)
        
        // Navegar según el tipo de entrenamiento
        navigateBasedOnTrainingType(tipoEntrenamiento, pokemon, objetivoPasos)
    }

    private fun navigateBasedOnTrainingType(tipoEntrenamiento: String, pokemon: Pokemon, objetivoPasos: Int) {
        try {
            val intent = when (tipoEntrenamiento) {
                "PASOS" -> {
                    // Para entrenamientos de pasos, ir a Home
                    Intent(this, Home::class.java)
                }
                "FUERZA" -> {
                    // Para entrenamientos de fuerza, ir a StatsFuerza
                    Intent(this, StatsFuerza::class.java)
                }
                "VELOCIDAD" -> {
                    // Para entrenamientos de velocidad, ir a Home (no hay stats específico)
                    Intent(this, Home::class.java)
                }
                "RESISTENCIA" -> {
                    // Para entrenamientos de resistencia, ir a Home (no hay stats específico)
                    Intent(this, Home::class.java)
                }
                else -> {
                    // Por defecto, ir a Home
                    Intent(this, Home::class.java)
                }
            }

            // Agregar datos extras al intent (esto se ejecuta para todos los casos)
            intent.putExtra("POKEMON_SELECCIONADO", pokemon.name)
            intent.putExtra("POKEMON_RESOURCE", pokemon.resourceName)
            intent.putExtra("TIPO_ENTRENAMIENTO", tipoEntrenamiento)
            intent.putExtra("OBJETIVO_PASOS", objetivoPasos)

            // Guardar también en SharedPreferences para persistencia
            saveSelectedPokemon(pokemon, tipoEntrenamiento)

            startActivity(intent)
            finishAffinity() // Cerrar todas las actividades anteriores

        } catch (e: Exception) {
            Toast.makeText(this, "Error al navegar: ${e.message}", Toast.LENGTH_LONG).show()

            // Como fallback, navegar a Home
            navigateToHomeFallback(pokemon, tipoEntrenamiento, objetivoPasos)
        }
    }

    // Función auxiliar para guardar el Pokémon seleccionado
    private fun saveSelectedPokemon(pokemon: Pokemon, tipoEntrenamiento: String) {
        val sharedPref = getSharedPreferences("PokeFitPrefs", MODE_PRIVATE)
        with(sharedPref.edit()) {
            putString("selected_pokemon_name", pokemon.name)
            putString("selected_pokemon_resource", pokemon.resourceName)
            putString("training_type", tipoEntrenamiento)
            apply()
        }
    }

    // Función auxiliar para navegación de fallback
    private fun navigateToHomeFallback(pokemon: Pokemon, tipoEntrenamiento: String, objetivoPasos: Int) {
        try {
            val fallbackIntent = Intent(this, Home::class.java)
            fallbackIntent.putExtra("POKEMON_SELECCIONADO", pokemon.name)
            fallbackIntent.putExtra("POKEMON_RESOURCE", pokemon.resourceName)
            fallbackIntent.putExtra("TIPO_ENTRENAMIENTO", tipoEntrenamiento)
            fallbackIntent.putExtra("OBJETIVO_PASOS", objetivoPasos)

            // Guardar en SharedPreferences como backup
            saveSelectedPokemon(pokemon, tipoEntrenamiento)

            startActivity(fallbackIntent)
            finishAffinity()

        } catch (e: Exception) {
            Toast.makeText(this, "Error crítico de navegación: ${e.message}", Toast.LENGTH_LONG).show()
            // Si todo falla, al menos guardar los datos
            saveSelectedPokemon(pokemon, tipoEntrenamiento)
        }
    }
    
    private fun saveSelectedPokemon(pokemon: Pokemon, tipoEntrenamiento: String, objetivoPasos: Int) {
        try {
            val sharedPref = getSharedPreferences("PokeFitPrefs", MODE_PRIVATE)
            with(sharedPref.edit()) {
                putString("selected_pokemon_name", pokemon.name)
                putString("selected_pokemon_resource", pokemon.resourceName)
                putString("training_type", tipoEntrenamiento)
                putInt("steps_goal", objetivoPasos)
                apply()
            }
        } catch (e: Exception) {
            Toast.makeText(this, "Error al guardar selección: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }
    
    data class Pokemon(val name: String, val resourceName: String)
}