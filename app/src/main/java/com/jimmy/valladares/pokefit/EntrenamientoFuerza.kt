package com.jimmy.valladares.pokefit

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import java.util.concurrent.TimeUnit

class EntrenamientoFuerza : AppCompatActivity() {

    // UI Components
    private lateinit var timerText: TextView
    private lateinit var statusIcon: TextView
    private lateinit var pikachuGif: ImageView
    private lateinit var exerciseSpinner: Spinner
    private lateinit var startButton: Button
    private lateinit var addSetButton: Button
    private lateinit var finishButton: Button
    private lateinit var exerciseTable: LinearLayout
    private lateinit var tableRows: LinearLayout
    private lateinit var currentStatusText: TextView
    private lateinit var scrollView: ScrollView

    // Navigation
    private lateinit var navHome: LinearLayout
    private lateinit var navStats: LinearLayout
    private lateinit var navTrain: LinearLayout
    private lateinit var navProfile: LinearLayout

    // Timer variables
    private var timer: CountDownTimer? = null
    private var timeElapsed = 0L
    private var isTrainingActive = false
    private var currentSetNumber = 3

    // Exercise data
    private val exerciseOptions = arrayOf("Selecciona una opción", "Press de banca", "Sentadilla libre", "Peso muerto")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_entrenamiento_fuerza)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            // Solo aplicamos padding top y lateral, no bottom para que la navegación llegue al fondo
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0)
            insets
        }

        initializeViews()
        setupSpinner()
        setupNavigation()
        setupClickListeners()
    }

    private fun initializeViews() {
        timerText = findViewById(R.id.tv_timer)
        statusIcon = findViewById(R.id.tv_status_icon)
        pikachuGif = findViewById(R.id.iv_pikachu_running)
        exerciseSpinner = findViewById(R.id.spinner_exercise_type)
        startButton = findViewById(R.id.btn_start_training)
        addSetButton = findViewById(R.id.btn_add_set)
        finishButton = findViewById(R.id.btn_finish_training)
        exerciseTable = findViewById(R.id.ll_exercise_table)
        tableRows = findViewById(R.id.ll_table_rows)
        currentStatusText = findViewById(R.id.tv_current_status)
        scrollView = findViewById(R.id.scroll_view)

        // Navigation
        navHome = findViewById(R.id.nav_home)
        navStats = findViewById(R.id.nav_stats)
        navTrain = findViewById(R.id.nav_train)
        navProfile = findViewById(R.id.nav_profile)
    }

    private fun setupSpinner() {
        val adapter = ArrayAdapter(this, R.layout.spinner_item, exerciseOptions)
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item)
        exerciseSpinner.adapter = adapter

        exerciseSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                if (position > 0) {
                    startButton.isEnabled = true
                    startButton.alpha = 1.0f
                } else {
                    startButton.isEnabled = false
                    startButton.alpha = 0.5f
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        // Initially disable start button
        startButton.isEnabled = false
        startButton.alpha = 0.5f
    }

    private fun setupNavigation() {
        navHome.setOnClickListener {
            startActivity(Intent(this, Home::class.java))
            finish()
        }

        navStats.setOnClickListener {
            startActivity(Intent(this, StatsFuerza::class.java))
            finish()
        }

        navTrain.setOnClickListener {
            // Ya estamos en Train
        }

        navProfile.setOnClickListener {
            startActivity(Intent(this, Perfil::class.java))
            finish()
        }
    }

    private fun setupClickListeners() {
        startButton.setOnClickListener {
            startTraining()
        }

        addSetButton.setOnClickListener {
            addNewSet()
        }

        finishButton.setOnClickListener {
            finishTraining()
        }

        currentStatusText.setOnClickListener {
            completeCurrentSet()
        }
    }

    private fun startTraining() {
        if (!isTrainingActive) {
            isTrainingActive = true
            exerciseTable.visibility = View.VISIBLE
            finishButton.visibility = View.VISIBLE
            pikachuGif.visibility = View.VISIBLE
            statusIcon.text = "⏱️"

            // Load Pikachu running GIF
            Glide.with(this)
                .asGif()
                .load(R.drawable.pikachu_running)
                .into(pikachuGif)

            startTimer()
            startButton.text = "Pausar Entrenamiento"
        } else {
            pauseTraining()
        }
    }

    private fun pauseTraining() {
        timer?.cancel()
        isTrainingActive = false
        statusIcon.text = "⏸️"
        pikachuGif.visibility = View.GONE
        startButton.text = "Reanudar Entrenamiento"
    }

    private fun startTimer() {
        timer = object : CountDownTimer(Long.MAX_VALUE, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                timeElapsed += 1000
                updateTimerDisplay()
            }

            override fun onFinish() {
                // Timer will run indefinitely
            }
        }.start()
    }

    private fun updateTimerDisplay() {
        val minutes = TimeUnit.MILLISECONDS.toMinutes(timeElapsed)
        val seconds = TimeUnit.MILLISECONDS.toSeconds(timeElapsed) % 60
        timerText.text = String.format("%02d:%02d", minutes, seconds)
    }

    private fun completeCurrentSet() {
        if (currentStatusText.text == "No") {
            // Mark current set as completed
            currentStatusText.text = "✓"
            currentStatusText.setTextColor(getColor(R.color.exp_green))

            // Change background to completed style
            val currentRow = findViewById<LinearLayout>(R.id.ll_current_row)
            currentRow.background = getDrawable(R.drawable.table_row_background)
            currentRow.alpha = 0.7f

            // Add new active row
            addNewSet()
        }
    }

    private fun addNewSet() {
        currentSetNumber++

        // Create new row
        val newRow = layoutInflater.inflate(R.layout.table_row_active, null) as LinearLayout

        // Set data for new row
        val prevText = newRow.findViewById<TextView>(R.id.tv_prev)
        val serieText = newRow.findViewById<TextView>(R.id.tv_serie)
        val kgText = newRow.findViewById<TextView>(R.id.tv_kg)
        val repsText = newRow.findViewById<TextView>(R.id.tv_reps)
        val statusText = newRow.findViewById<TextView>(R.id.tv_status)

        prevText.text = "8 x 12kg"
        serieText.text = currentSetNumber.toString()
        kgText.text = "12"
        repsText.text = "6"
        statusText.text = "No"

        statusText.setOnClickListener {
            if (statusText.text == "No") {
                statusText.text = "✓"
                statusText.setTextColor(getColor(R.color.exp_green))
                newRow.background = getDrawable(R.drawable.table_row_background)
                newRow.alpha = 0.7f

                // Add another new set
                addNewSet()
            }
        }

        // Update current row reference
        newRow.id = R.id.ll_current_row
        currentStatusText = statusText

        // Add to table
        tableRows.addView(newRow)
        
        // Scroll to bottom to show new row
        scrollView.post {
            scrollView.fullScroll(View.FOCUS_DOWN)
        }
    }

    private fun finishTraining() {
        timer?.cancel()
        isTrainingActive = false

        // Show custom toast
        showCustomToast("¡Entrenamiento completado! Excelente trabajo 💪")

        // Return to home after 3 seconds
        android.os.Handler().postDelayed({
            startActivity(Intent(this, Home::class.java))
            finish()
        }, 3000)
    }

    private fun showCustomToast(message: String) {
        val inflater = LayoutInflater.from(this)
        val layout = inflater.inflate(R.layout.custom_toast, null)
        
        val textView = layout.findViewById<TextView>(R.id.tv_toast_message)
        textView.text = message
        
        val toast = Toast(this)
        toast.duration = Toast.LENGTH_LONG
        toast.setGravity(Gravity.CENTER, 0, 0)
        toast.view = layout
        toast.show()
    }

    override fun onDestroy() {
        super.onDestroy()
        timer?.cancel()
    }
}