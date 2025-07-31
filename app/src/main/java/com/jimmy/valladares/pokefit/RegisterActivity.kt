package com.jimmy.valladares.pokefit

import android.content.Intent
import android.os.Bundle
import android.util.Log
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

class RegisterActivity : AppCompatActivity() {

    // UI Components
    private lateinit var editTextEmail: EditText
    private lateinit var editTextPassword: EditText
    private lateinit var editTextConfirmPassword: EditText
    private lateinit var buttonRegister: CardView
    private lateinit var buttonBackToLogin: CardView
    private lateinit var progressBarRegister: ProgressBar

    // Firebase Auth
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_register)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Inicializar Firebase Auth
        auth = Firebase.auth

        // Inicializar vistas
        initializeViews()

        // Configurar eventos
        setupClickListeners()
    }

    private fun initializeViews() {
        editTextEmail = findViewById(R.id.editTextEmail)
        editTextPassword = findViewById(R.id.editTextPassword)
        editTextConfirmPassword = findViewById(R.id.editTextConfirmPassword)
        buttonRegister = findViewById(R.id.buttonRegister)
        buttonBackToLogin = findViewById(R.id.buttonBackToLogin)
        progressBarRegister = findViewById(R.id.progressBarRegister)
    }

    private fun setupClickListeners() {
        buttonRegister.setOnClickListener {
            val email = editTextEmail.text.toString().trim()
            val password = editTextPassword.text.toString().trim()
            val confirmPassword = editTextConfirmPassword.text.toString().trim()

            if (validateInput(email, password, confirmPassword)) {
                createUser(email, password)
            }
        }

        buttonBackToLogin.setOnClickListener {
            finish()
        }
    }

    private fun validateInput(email: String, password: String, confirmPassword: String): Boolean {
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

        if (confirmPassword.isEmpty()) {
            editTextConfirmPassword.error = "Confirma tu contraseña"
            editTextConfirmPassword.requestFocus()
            return false
        }

        if (password != confirmPassword) {
            editTextConfirmPassword.error = "Las contraseñas no coinciden"
            editTextConfirmPassword.requestFocus()
            return false
        }

        return true
    }

    private fun createUser(email: String, password: String) {
        showLoading(true)

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                showLoading(false)

                if (task.isSuccessful) {
                    Log.d(TAG, "createUserWithEmail:success")

                    Toast.makeText(this, "¡Cuenta creada exitosamente! Bienvenido, Entrenador!", Toast.LENGTH_SHORT).show()

                    // Ir a la encuesta para nuevos usuarios
                    val intent = Intent(this, Encuesta::class.java)
                    intent.putExtra(LoginActivity.EXTRA_LOGIN, email)
                    startActivity(intent)
                    finish()

                } else {
                    Log.w(TAG, "createUserWithEmail:failure", task.exception)

                    val errorMessage = when (task.exception?.message) {
                        "The email address is already in use by another account." -> "Ya existe una cuenta con este email"
                        "The email address is badly formatted." -> "El formato del email no es válido"
                        "The given password is invalid. [ Password should be at least 6 characters ]" -> "La contraseña debe tener al menos 6 caracteres"
                        "A network error (such as timeout, interrupted connection or unreachable host) has occurred." -> "Error de conexión. Verifica tu internet"
                        else -> "Error al crear la cuenta. Inténtalo de nuevo"
                    }

                    Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show()
                }
            }
    }

    private fun showLoading(show: Boolean) {
        if (show) {
            progressBarRegister.visibility = android.view.View.VISIBLE
            buttonRegister.isEnabled = false
            buttonBackToLogin.isEnabled = false
        } else {
            progressBarRegister.visibility = android.view.View.GONE
            buttonRegister.isEnabled = true
            buttonBackToLogin.isEnabled = true
        }
    }

    companion object {
        private const val TAG = "RegisterActivity"
    }
}