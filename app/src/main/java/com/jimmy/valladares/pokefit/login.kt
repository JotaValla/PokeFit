package com.jimmy.valladares.pokefit

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginActivity : AppCompatActivity() {

    // UI Components
    private lateinit var editTextEmail: EditText
    private lateinit var editTextPassword: EditText
    private lateinit var buttonLogin: CardView
    private lateinit var buttonNewUser: CardView
    private lateinit var checkBoxRecordarme: CheckBox
    private lateinit var progressBarLogin: ProgressBar

    // Firebase Auth
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Inicializar Firebase Auth
        auth = Firebase.auth

        // Inicializar vistas
        initializeViews()

        // Leer datos de preferencias (si los hay)
        loadSavedCredentials()

        // Configurar eventos
        setupClickListeners()

        // Verificar si el usuario ya está autenticado
        checkCurrentUser()
    }

    private fun initializeViews() {
        editTextEmail = findViewById(R.id.editTextEmail)
        editTextPassword = findViewById(R.id.editTextPassword)
        buttonLogin = findViewById(R.id.buttonLogin)
        buttonNewUser = findViewById(R.id.buttonNewUser)
        checkBoxRecordarme = findViewById(R.id.checkBoxRecordarme)
        progressBarLogin = findViewById(R.id.progressBarLogin)
    }

    private fun setupClickListeners() {
        buttonLogin.setOnClickListener {
            val email = editTextEmail.text.toString().trim()
            val password = editTextPassword.text.toString().trim()

            if (validateInput(email, password)) {
                saveCredentialsIfNeeded()
                authenticateUser(email, password)
            }
        }

        buttonNewUser.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }

    private fun validateInput(email: String, password: String): Boolean {
        if (email.isEmpty()) {
            editTextEmail.error = "El email es requerido"
            editTextEmail.requestFocus()
            return false
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editTextEmail.error = "Ingresa un email válido"
            editTextEmail.requestFocus()
            return false
        }

        if (password.isEmpty()) {
            editTextPassword.error = "La contraseña es requerida"
            editTextPassword.requestFocus()
            return false
        }

        if (password.length < 6) {
            editTextPassword.error = "La contraseña debe tener al menos 6 caracteres"
            editTextPassword.requestFocus()
            return false
        }

        return true
    }

    private fun authenticateUser(email: String, password: String) {
        showLoading(true)

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                showLoading(false)

                if (task.isSuccessful) {
                    Log.d(TAG, "signInWithEmail:success")
                    val user = auth.currentUser

                    // Mostrar mensaje de éxito
                    Toast.makeText(this, "¡Bienvenido de vuelta, Entrenador!", Toast.LENGTH_SHORT).show()

                    // Verificar si es el primer login del usuario
                    val sharedPref = getSharedPreferences("PokeFitPrefs", MODE_PRIVATE)
                    val isFirstTime = sharedPref.getBoolean("first_time_${user?.uid}", true)

                    if (isFirstTime) {
                        // Si es primera vez, ir a la encuesta
                        val intent = Intent(this, Encuesta::class.java)
                        intent.putExtra(EXTRA_LOGIN, user?.email)
                        startActivity(intent)
                    } else {
                        // Si ya ha usado la app, ir directamente a Home
                        val intent = Intent(this, Home::class.java)
                        intent.putExtra(EXTRA_LOGIN, user?.email)
                        startActivity(intent)
                    }

                    finish()
                } else {
                    Log.w(TAG, "signInWithEmail:failure", task.exception)

                    // Mostrar mensaje de error más amigable
                    val errorMessage = when (task.exception?.message) {
                        "The email address is badly formatted." -> "El formato del email no es válido"
                        "The password is invalid or the user does not have a password." -> "Contraseña incorrecta"
                        "There is no user record corresponding to this identifier. The user may have been deleted." -> "No existe una cuenta con este email"
                        "A network error (such as timeout, interrupted connection or unreachable host) has occurred." -> "Error de conexión. Verifica tu internet"
                        else -> "Error de autenticación. Verifica tus credenciales"
                    }

                    Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show()
                }
            }
    }

    private fun checkCurrentUser() {
        val currentUser = auth.currentUser
        if (currentUser != null) {
            // Usuario ya autenticado, ir a Home
            val intent = Intent(this, Home::class.java)
            intent.putExtra(EXTRA_LOGIN, currentUser.email)
            startActivity(intent)
            finish()
        }
    }

    private fun loadSavedCredentials() {
        val sharedPref = getSharedPreferences("LoginPrefs", MODE_PRIVATE)
        val savedEmail = sharedPref.getString("saved_email", "")
        val savedPassword = sharedPref.getString("saved_password", "")
        val rememberMe = sharedPref.getBoolean("remember_me", false)

        if (rememberMe && !savedEmail.isNullOrEmpty()) {
            editTextEmail.setText(savedEmail)
            editTextPassword.setText(savedPassword)
            checkBoxRecordarme.isChecked = true
        }
    }

    private fun saveCredentialsIfNeeded() {
        val sharedPref = getSharedPreferences("LoginPrefs", MODE_PRIVATE)
        val editor = sharedPref.edit()

        if (checkBoxRecordarme.isChecked) {
            editor.putString("saved_email", editTextEmail.text.toString().trim())
            editor.putString("saved_password", editTextPassword.text.toString().trim())
            editor.putBoolean("remember_me", true)
        } else {
            editor.remove("saved_email")
            editor.remove("saved_password")
            editor.putBoolean("remember_me", false)
        }

        editor.apply()
    }

    private fun showLoading(show: Boolean) {
        if (show) {
            progressBarLogin.visibility = android.view.View.VISIBLE
            buttonLogin.isEnabled = false
            buttonNewUser.isEnabled = false
        } else {
            progressBarLogin.visibility = android.view.View.GONE
            buttonLogin.isEnabled = true
            buttonNewUser.isEnabled = true
        }
    }

    override fun onStart() {
        super.onStart()
        // Verificar si el usuario ya está autenticado al iniciar
        checkCurrentUser()
    }

    companion object {
        private const val TAG = "LoginActivity"
        const val EXTRA_LOGIN = "extra_login"
    }
}