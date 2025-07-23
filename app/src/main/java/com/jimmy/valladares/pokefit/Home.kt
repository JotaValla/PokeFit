package com.jimmy.valladares.pokefit

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import android.animation.*
import android.view.ViewGroup
import android.view.animation.DecelerateInterpolator
import android.view.animation.BounceInterpolator
import android.widget.FrameLayout
import android.widget.ProgressBar
import androidx.core.os.HandlerCompat.postDelayed
import kotlin.math.max
import android.os.Handler
import android.os.Looper


class Home : AppCompatActivity() {
    private val handler = Handler(Looper.getMainLooper())
    // UI Components existentes
    private lateinit var machampGif: ImageView
    private lateinit var levelText: TextView
    private lateinit var progressBar: ProgressBar
    private lateinit var currentStreakText: TextView
    private lateinit var weeklyExpText: TextView

    // Componentes de la barra de EXP dinámica
    private lateinit var progressFill: View
    private lateinit var tvCurrentLevel: TextView
    private lateinit var tvProgressText: TextView
    private lateinit var tvMaxText: TextView
    private lateinit var tvProgressPercentage: TextView
    private lateinit var tvExpGained: TextView
    private lateinit var tvExpNeeded: TextView
    private lateinit var streakBonusContainer: LinearLayout
    private lateinit var progressContainer: FrameLayout

    // Datos de experiencia
    private var currentExp = 200        // EXP actual del usuario
    private var currentLevel = 1        // Nivel actual
    private var hasStreakBonus = true   // Si tiene bonus de racha
    private var maxBarWidth = 0         // Ancho máximo de la barra

    private lateinit var pokemonNameText: TextView
    private lateinit var pokemonGif: ImageView

    // Navegación
    private lateinit var navHome: LinearLayout
    private lateinit var navStats: LinearLayout
    private lateinit var navTrain: LinearLayout
    private lateinit var navProfile: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_home)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0)
            insets
        }

        initializeViews()
        setupNavigation()
        updatePokemonData()
    }

    private fun initializeViews() {
        // Componentes de la barra de EXP
        progressFill = findViewById(R.id.progress_fill)
        tvCurrentLevel = findViewById(R.id.tv_current_level)
        tvProgressText = findViewById(R.id.tv_progress_text)
        tvMaxText = findViewById(R.id.tv_max_text)
        tvProgressPercentage = findViewById(R.id.tv_progress_percentage)
        tvExpGained = findViewById(R.id.tv_exp_gained)
        tvExpNeeded = findViewById(R.id.tv_exp_needed)
        streakBonusContainer = findViewById(R.id.streak_bonus_container)
        progressContainer = findViewById(R.id.fl_progress_main_container)

        // Pokemon
        pokemonNameText = findViewById(R.id.tv_pokemon_name)
        pokemonGif = findViewById(R.id.iv_pokemon_gif)

        // Cargar el GIF usando Glide
        Glide.with(this)
            .asGif()
            .load(R.drawable.machamp_gif)
            .into(pokemonGif)

        // Navegación
        navHome = findViewById(R.id.nav_home)
        navStats = findViewById(R.id.nav_stats)
        navTrain = findViewById(R.id.nav_train)
        navProfile = findViewById(R.id.nav_profile)

        // Calcular ancho máximo de la barra después de que se dibuje
        progressContainer.viewTreeObserver.addOnGlobalLayoutListener {
            if (maxBarWidth == 0) {
                maxBarWidth = progressContainer.width - 44 // Restar el ancho del círculo
                updateExpBar()
            }
        }
    }

    private fun setupNavigation() {
        navHome.setOnClickListener {
            // Ya estamos en Home, no necesita hacer nada
        }

        navStats.setOnClickListener {
            startActivity(Intent(this, StatsFuerza::class.java))
            finish()
        }

        navTrain.setOnClickListener {
            startActivity(Intent(this, EntrenamientoFuerza::class.java))
            finish()
        }

        navProfile.setOnClickListener {
            startActivity(Intent(this, Perfil::class.java))
            finish()
        }
    }

    private fun updatePokemonData() {
        // Datos de ejemplo - estos vendrían de tu base de datos o API
        val pokemonName = "Garchomp"

        // Actualizar UI del Pokemon
        pokemonNameText.text = pokemonName

        // Actualizar barra de EXP
        updateExpBar()
    }

    private fun updateExpBar() {
        if (maxBarWidth == 0) return // Esperar a que se calcule el ancho

        // Calcular EXP necesaria para el nivel actual y siguiente
        val currentLevelExp = calculateExpForLevel(currentLevel)
        val nextLevelExp = calculateExpForLevel(currentLevel + 1)
        val expInCurrentLevel = currentExp - currentLevelExp
        val expNeededForLevel = nextLevelExp - currentLevelExp

        // Calcular progreso
        val progressPercentage = if (expNeededForLevel > 0) {
            (expInCurrentLevel.toFloat() / expNeededForLevel.toFloat() * 100).coerceIn(0f, 100f)
        } else 100f

        val expRemaining = max(0, nextLevelExp - currentExp)

        // Calcular ancho de la barra de progreso
        val fillWidth = (44 + (maxBarWidth - 44) * (progressPercentage / 100f)).toInt()

        // Actualizar UI
        tvCurrentLevel.text = currentLevel.toString()
        tvProgressText.text = currentExp.toString()
        tvMaxText.text = nextLevelExp.toString()
        tvProgressPercentage.text = "${progressPercentage.toInt()}%"
        tvExpNeeded.text = if (expRemaining > 0) "$expRemaining EXP para nivel ${currentLevel + 1}" else "¡Nivel máximo!"

        // Animar el ancho de la barra de progreso
        animateProgressFill(fillWidth)

        // Posicionar el indicador de porcentaje
        positionPercentageIndicator(fillWidth)

        // Mostrar/ocultar bonus de racha
        streakBonusContainer.visibility = if (hasStreakBonus) View.VISIBLE else View.GONE
    }

    private fun calculateExpForLevel(level: Int): Int {
        // Fórmula para calcular EXP total necesaria para alcanzar un nivel
        return when {
            level <= 1 -> 0
            level <= 10 -> (level - 1) * 100 + ((level - 1) * (level - 2) * 25)
            else -> calculateExpForLevel(10) + (level - 10) * 300
        }
    }

    private fun animateProgressFill(targetWidth: Int) {
        val currentWidth = progressFill.layoutParams.width

        val animator = ValueAnimator.ofInt(currentWidth, targetWidth)
        animator.duration = 1500
        animator.interpolator = DecelerateInterpolator()

        animator.addUpdateListener { animation ->
            val animatedWidth = animation.animatedValue as Int
            val layoutParams = progressFill.layoutParams
            layoutParams.width = animatedWidth
            progressFill.layoutParams = layoutParams
        }

        animator.start()
    }

    private fun positionPercentageIndicator(fillWidth: Int) {
        val layoutParams = tvProgressPercentage.layoutParams as FrameLayout.LayoutParams

        // Posicionar el indicador al final de la barra de progreso
        val indicatorPosition = fillWidth - 20 // Ajustar para centrar el indicador
        layoutParams.marginStart = indicatorPosition.coerceAtLeast(50) // Mínimo para que no se salga

        tvProgressPercentage.layoutParams = layoutParams
    }

    // Función para agregar experiencia (llamar cuando complete ejercicios)
    fun addExperience(expGained: Int) {
        val actualExpGained = if (hasStreakBonus) (expGained * 1.5).toInt() else expGained
        val oldExp = currentExp
        val oldLevel = currentLevel

        currentExp += actualExpGained

        // Mostrar animación de ganancia de EXP
        showExpGainedAnimation(actualExpGained, hasStreakBonus)

        // Verificar si subió de nivel
        val newLevel = calculateLevelFromExp(currentExp)
        if (newLevel > oldLevel) {
            currentLevel = newLevel
            showLevelUpAnimation()
        }

        // Actualizar la barra después de un pequeño delay para que se vea la animación
        handler.postDelayed({
            updateExpBar()
        }, 500)
    }

    private fun calculateLevelFromExp(totalExp: Int): Int {
        var level = 1
        while (calculateExpForLevel(level + 1) <= totalExp) {
            level++
        }
        return level
    }

    private fun showExpGainedAnimation(expGained: Int, hasBonus: Boolean) {
        val bonusText = if (hasBonus) " (+50%)" else ""
        tvExpGained.text = "+$expGained EXP$bonusText"
        tvExpGained.visibility = View.VISIBLE

        // Animación de aparición y movimiento hacia arriba
        val translateY = ObjectAnimator.ofFloat(tvExpGained, "translationY", 0f, -80f)
        val alpha = ObjectAnimator.ofFloat(tvExpGained, "alpha", 0f, 1f, 1f, 0f)
        val scaleX = ObjectAnimator.ofFloat(tvExpGained, "scaleX", 0.5f, 1.2f, 1f)
        val scaleY = ObjectAnimator.ofFloat(tvExpGained, "scaleY", 0.5f, 1.2f, 1f)

        val animatorSet = AnimatorSet()
        animatorSet.playTogether(translateY, alpha, scaleX, scaleY)
        animatorSet.duration = 2000

        animatorSet.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                tvExpGained.visibility = View.GONE
                tvExpGained.translationY = 0f
                tvExpGained.alpha = 1f
                tvExpGained.scaleX = 1f
                tvExpGained.scaleY = 1f
            }
        })

        animatorSet.start()
    }

    private fun showLevelUpAnimation() {
        // Animación del círculo de nivel
        val bounceX = ObjectAnimator.ofFloat(tvCurrentLevel, "scaleX", 1f, 1.5f, 1f)
        val bounceY = ObjectAnimator.ofFloat(tvCurrentLevel, "scaleY", 1f, 1.5f, 1f)

        val bounceSet = AnimatorSet()
        bounceSet.playTogether(bounceX, bounceY)
        bounceSet.duration = 600
        bounceSet.interpolator = BounceInterpolator()

        bounceSet.start()

        // Mostrar texto de level up
        val levelUpText = "¡NIVEL ${currentLevel}!"
        showExpGainedAnimation(0, false) // Reutilizar la animación pero con texto personalizado
        tvExpGained.text = levelUpText
        tvExpGained.setTextColor(resources.getColor(android.R.color.holo_orange_light, null))
    }

    // Función para simular ganancia de EXP (para testing)
    fun simulateExpGain() {
        addExperience(25)
    }

    // Ejemplo de uso: llamar esto cuando el usuario complete un ejercicio
    private fun onExerciseCompleted() {
        // Ejemplo: dar 30 EXP por completar un ejercicio
        addExperience(30)
    }

    override fun onResume() {
        super.onResume()
        // Recargar GIF para asegurar que se reproduzca
        Glide.with(this)
            .asGif()
            .load(R.drawable.machamp_gif)
            .into(pokemonGif)
    }
}