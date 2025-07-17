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
        
        // IMPORTANTE: Primero inicializar todas las vistas
        initViews()
        
        // Configurar botón de regresar
        val btnBack = findViewById<TextView>(R.id.btn_back)
        btnBack.setOnClickListener {
            finish()
        }
        
        // Obtener el tipo de entrenamiento del intent
        val tipoEntrenamiento = intent.getStringExtra("TIPO_ENTRENAMIENTO") ?: "PASOS"
        
        // Ahora sí actualizar el título (después de initViews)
        updateHeaderTitle(tipoEntrenamiento)
        
        // Cargar los Pokémon adecuados según el tipo
        loadPokemonByType(tipoEntrenamiento)
        
        // Configurar click listeners para seleccionar Pokémon
        setupPokemonClickListeners()
    }
    
    private fun initViews() {
        // Inicializar todas las vistas con verificación de existencia
        gifPokemon1 = findViewById(R.id.gif_pokemon1)
        gifPokemon2 = findViewById(R.id.gif_pokemon2)
        gifPokemon3 = findViewById(R.id.gif_pokemon3)
        
        tvPokemon1 = findViewById(R.id.tv_pokemon1)
        tvPokemon2 = findViewById(R.id.tv_pokemon2)
        tvPokemon3 = findViewById(R.id.tv_pokemon3)
        
        // Verificar que tv_header existe en el layout
        val headerView = findViewById<TextView>(R.id.tv_header)
        if (headerView != null) {
            tvHeader = headerView
        } else {
            // Si no existe, crear un comportamiento por defecto
            Toast.makeText(this, "Error: tv_header no encontrado en el layout", Toast.LENGTH_LONG).show()
            finish()
            return
        }
    }
    
    private fun updateHeaderTitle(tipo: String) {
        // Verificar que tvHeader esté inicializado
        if (::tvHeader.isInitialized) {
            val headerText = when (tipo) {
                "PASOS" -> "Elige Pokemon Pasos"
                "VELOCIDAD" -> "Elige Pokemon Velocidad"
                "FUERZA" -> "Elige Pokemon Fuerza"
                "RESISTENCIA" -> "Elige Pokemon Resistencia"
                else -> "Elige Pokemon"
            }
            tvHeader.text = headerText
        } else {
            Toast.makeText(this, "Error: Header no inicializado", Toast.LENGTH_SHORT).show()
        }
    }
    
    private fun loadPokemonByType(type: String) {
        currentPokemonList = pokemonMap[type] ?: pokemonMap["PASOS"]!!
        
        // Verificar que las vistas estén inicializadas antes de usarlas
        if (::gifPokemon1.isInitialized && ::gifPokemon2.isInitialized && ::gifPokemon3.isInitialized) {
            // Cargar primer Pokémon
            loadPokemonImage(currentPokemonList[0], gifPokemon1, tvPokemon1)
            
            // Cargar segundo Pokémon
            loadPokemonImage(currentPokemonList[1], gifPokemon2, tvPokemon2)
            
            // Cargar tercer Pokémon
            loadPokemonImage(currentPokemonList[2], gifPokemon3, tvPokemon3)
        } else {
            Toast.makeText(this, "Error: Vistas no inicializadas", Toast.LENGTH_SHORT).show()
        }
    }
    
    private fun loadPokemonImage(pokemon: Pokemon, gifView: GifImageView, textView: TextView) {
        // Intentar cargar la imagen del Pokémon
        val resourceId = resources.getIdentifier(pokemon.resourceName, "drawable", packageName)
        
        if (resourceId != 0) {
            gifView.setImageResource(resourceId)
        } else {
            // Si no se encuentra la imagen, usar una imagen por defecto
            val defaultResource = resources.getIdentifier("pokeball", "drawable", packageName)
            if (defaultResource != 0) {
                gifView.setImageResource(defaultResource)
            }
            Toast.makeText(this, "Imagen no encontrada para ${pokemon.name}", Toast.LENGTH_SHORT).show()
        }
        
        textView.text = pokemon.name
    }
    
    private fun setupPokemonClickListeners() {
        // Verificar que las vistas estén inicializadas
        if (::gifPokemon1.isInitialized && ::gifPokemon2.isInitialized && ::gifPokemon3.isInitialized) {
            // Click listener para el primer Pokémon
            gifPokemon1.setOnClickListener {
                if (currentPokemonList.isNotEmpty()) {
                    selectPokemon(currentPokemonList[0])
                }
            }
            
            // Click listener para el segundo Pokémon
            gifPokemon2.setOnClickListener {
                if (currentPokemonList.size > 1) {
                    selectPokemon(currentPokemonList[1])
                }
            }
            
            // Click listener para el tercer Pokémon
            gifPokemon3.setOnClickListener {
                if (currentPokemonList.size > 2) {
                    selectPokemon(currentPokemonList[2])
                }
            }
        }
    }
    
    private fun selectPokemon(pokemon: Pokemon) {
        // Mostrar confirmación
        Toast.makeText(this, "Has seleccionado a ${pokemon.name}", Toast.LENGTH_SHORT).show()
        
        // Obtener datos adicionales del intent
        val tipoEntrenamiento = intent.getStringExtra("TIPO_ENTRENAMIENTO") ?: "PASOS"
        val objetivoPasos = intent.getIntExtra("OBJETIVO_PASOS", 0)
        
        // Guardar la selección en SharedPreferences
        saveSelectedPokemon(pokemon, tipoEntrenamiento, objetivoPasos)
        
        // Navegar a la siguiente pantalla
        try {
            val intent = Intent(this, Home::class.java)
            intent.putExtra("POKEMON_SELECCIONADO", pokemon.name)
            intent.putExtra("POKEMON_RESOURCE", pokemon.resourceName)
            intent.putExtra("TIPO_ENTRENAMIENTO", tipoEntrenamiento)
            intent.putExtra("OBJETIVO_PASOS", objetivoPasos)
            startActivity(intent)
            
            // Opcional: cerrar todas las actividades anteriores
            finishAffinity()
        } catch (e: Exception) {
            Toast.makeText(this, "Error al navegar a Home: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }
    
    // Método para guardar la selección
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
    
    // Clase auxiliar para representar a un Pokémon
    data class Pokemon(val name: String, val resourceName: String)
}