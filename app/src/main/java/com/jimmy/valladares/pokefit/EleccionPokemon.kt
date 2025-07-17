package com.jimmy.valladares.pokefit

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import pl.droidsonroids.gif.GifImageView
import java.util.Locale

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
            Pokemon("Geodude", "geodude") // Cambiado el duplicado
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

        try {
            ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
                val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
                insets
            }

            // Inicializar vistas
            initViews()

            // Configurar botón de regresar
            setupBackButton()

            // Obtener el tipo de entrenamiento del intent
            val tipoEntrenamiento = intent.getStringExtra("TIPO_ENTRENAMIENTO") ?: "PASOS"
            Log.d("EleccionPokemon", "Tipo de entrenamiento recibido: $tipoEntrenamiento")

            // Actualizar título según el tipo de entrenamiento
            updateHeader(tipoEntrenamiento)

            // Cargar los Pokémon adecuados según el tipo
            loadPokemonByType(tipoEntrenamiento)

            // Configurar click listeners para seleccionar Pokémon
            setupPokemonClickListeners()

        } catch (e: Exception) {
            Log.e("EleccionPokemon", "Error en onCreate: ${e.message}", e)
            Toast.makeText(this, "Error al cargar la pantalla", Toast.LENGTH_SHORT).show()
        }
    }

    private fun initViews() {
        try {
            gifPokemon1 = findViewById(R.id.gif_pokemon1)
            gifPokemon2 = findViewById(R.id.gif_pokemon2)
            gifPokemon3 = findViewById(R.id.gif_pokemon3)

            tvPokemon1 = findViewById(R.id.tv_pokemon1)
            tvPokemon2 = findViewById(R.id.tv_pokemon2)
            tvPokemon3 = findViewById(R.id.tv_pokemon3)
            tvHeader = findViewById(R.id.tv_header)

            Log.d("EleccionPokemon", "Vistas inicializadas correctamente")
        } catch (e: Exception) {
            Log.e("EleccionPokemon", "Error al inicializar vistas: ${e.message}", e)
            throw e
        }
    }

    private fun setupBackButton() {
        try {
            val btnBack = findViewById<TextView>(R.id.btn_back)
            btnBack.setOnClickListener {
                finish()
            }
        } catch (e: Exception) {
            Log.e("EleccionPokemon", "Error al configurar botón de regreso: ${e.message}", e)
        }
    }

    private fun updateHeader(tipoEntrenamiento: String) {
        try {
            val headerText = when (tipoEntrenamiento.uppercase(Locale.getDefault())) {
                "PASOS" -> "Elige Pokémon Pasos"
                "VELOCIDAD" -> "Elige Pokémon Velocidad"
                "FUERZA" -> "Elige Pokémon Fuerza"
                "RESISTENCIA" -> "Elige Pokémon Resistencia"
                else -> "Elige tu Pokémon"
            }
            tvHeader.text = headerText
        } catch (e: Exception) {
            Log.e("EleccionPokemon", "Error al actualizar header: ${e.message}", e)
            tvHeader.text = "Elige tu Pokémon"
        }
    }

    private fun loadPokemonByType(type: String) {
        try {
            val normalizedType = type.uppercase(Locale.getDefault())
            val pokemonList = pokemonMap[normalizedType] ?: pokemonMap["PASOS"]!!

            Log.d("EleccionPokemon", "Cargando Pokémon para tipo: $normalizedType")

            // Cargar primer Pokémon
            loadPokemonToView(pokemonList[0], gifPokemon1, tvPokemon1, "pokemon1")

            // Cargar segundo Pokémon
            loadPokemonToView(pokemonList[1], gifPokemon2, tvPokemon2, "pokemon2")

            // Cargar tercer Pokémon
            loadPokemonToView(pokemonList[2], gifPokemon3, tvPokemon3, "pokemon3")

        } catch (e: Exception) {
            Log.e("EleccionPokemon", "Error al cargar Pokémon: ${e.message}", e)
            Toast.makeText(this, "Error al cargar los Pokémon", Toast.LENGTH_SHORT).show()
        }
    }

    private fun loadPokemonToView(pokemon: Pokemon, gifView: GifImageView, textView: TextView, viewName: String) {
        try {
            val resourceId = resources.getIdentifier(pokemon.resourceName, "drawable", packageName)

            if (resourceId != 0) {
                gifView.setImageResource(resourceId)
                textView.text = pokemon.name
                Log.d("EleccionPokemon", "Pokémon ${pokemon.name} cargado en $viewName")
            } else {
                Log.w("EleccionPokemon", "No se encontró recurso para ${pokemon.resourceName}")
                // Usar una imagen por defecto o manejar el error
                textView.text = pokemon.name
                // Podrías poner una imagen por defecto aquí
            }
        } catch (e: Exception) {
            Log.e("EleccionPokemon", "Error al cargar ${pokemon.name} en $viewName: ${e.message}", e)
            textView.text = pokemon.name
        }
    }

    private fun setupPokemonClickListeners() {
        try {
            // Click listener para el primer Pokémon
            gifPokemon1.setOnClickListener {
                selectPokemon(tvPokemon1.text.toString(), 1)
            }

            // Click listener para el segundo Pokémon
            gifPokemon2.setOnClickListener {
                selectPokemon(tvPokemon2.text.toString(), 2)
            }

            // Click listener para el tercer Pokémon
            gifPokemon3.setOnClickListener {
                selectPokemon(tvPokemon3.text.toString(), 3)
            }

            Log.d("EleccionPokemon", "Click listeners configurados")
        } catch (e: Exception) {
            Log.e("EleccionPokemon", "Error al configurar click listeners: ${e.message}", e)
        }
    }

    private fun selectPokemon(pokemonName: String, position: Int) {
        try {
            Log.d("EleccionPokemon", "Pokémon seleccionado: $pokemonName (posición $position)")
            Toast.makeText(this, "Has seleccionado a $pokemonName", Toast.LENGTH_SHORT).show()

            // Aquí podrías guardar la selección en SharedPreferences
            val sharedPref = getSharedPreferences("PokeFitPrefs", MODE_PRIVATE)
            with(sharedPref.edit()) {
                putString("POKEMON_SELECCIONADO", pokemonName)
                apply()
            }

            // Navegar a la siguiente pantalla
            val intent = Intent(this, Home::class.java)
            intent.putExtra("POKEMON_SELECCIONADO", pokemonName)
            startActivity(intent)

        } catch (e: Exception) {
            Log.e("EleccionPokemon", "Error al seleccionar Pokémon: ${e.message}", e)
            Toast.makeText(this, "Error al seleccionar Pokémon", Toast.LENGTH_SHORT).show()
        }
    }

    // Clase auxiliar para representar a un Pokémon
    data class Pokemon(val name: String, val resourceName: String)
}