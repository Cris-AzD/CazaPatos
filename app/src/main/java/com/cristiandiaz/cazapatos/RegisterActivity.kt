package com.cristiandiaz.cazapatos

import android.os.Bundle
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class RegisterActivity : AppCompatActivity() {

    private lateinit var editTextEmail: EditText
    private lateinit var editTextPassword: EditText
    private lateinit var editTextConfirmPassword: EditText
    private lateinit var buttonSignUp: Button
    private lateinit var buttonBackToLogin: Button

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        // Initialize Firebase Auth
        auth = Firebase.auth

        // Inicialización de las vistas
        editTextEmail = findViewById(R.id.editTextEmailRegister)
        editTextPassword = findViewById(R.id.editTextPasswordRegister)
        editTextConfirmPassword = findViewById(R.id.editTextConfirmPassword)
        buttonSignUp = findViewById(R.id.buttonSignUp)
        buttonBackToLogin = findViewById(R.id.buttonBackToLogin)

        // Eventos OnClick
        buttonSignUp.setOnClickListener {
            // Primero, validar los datos. Si no son válidos, la función retorna.
            if (validateData()) {
                // Si los datos son válidos, proceder con el registro en Firebase
                val email = editTextEmail.text.toString()
                val password = editTextPassword.text.toString()
                registerUser(email, password)
            }
        }

        buttonBackToLogin.setOnClickListener {
            // Cierra la actividad actual y regresa a la anterior
            finish()
        }
    }

    private fun validateData(): Boolean {
        // ... (el código de validación de campos sigue siendo el mismo)
        val email = editTextEmail.text.toString().trim()
        val password = editTextPassword.text.toString().trim()
        val confirmPassword = editTextConfirmPassword.text.toString().trim()

        if (email.isEmpty()) {
            editTextEmail.error = getString(R.string.error_email_required)
            editTextEmail.requestFocus()
            return false
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editTextEmail.error = getString(R.string.error_email_invalid)
            editTextEmail.requestFocus()
            return false
        }
        if (password.isEmpty()) {
            editTextPassword.error = getString(R.string.error_password_required)
            editTextPassword.requestFocus()
            return false
        }
        if (password.length < 8) {
            editTextPassword.error = getString(R.string.error_password_min_length_8)
            editTextPassword.requestFocus()
            return false
        }
        if (confirmPassword.isEmpty()) {
            editTextConfirmPassword.error = getString(R.string.error_confirm_password_required)
            editTextConfirmPassword.requestFocus()
            return false
        }
        if (password != confirmPassword) {
            editTextConfirmPassword.error = getString(R.string.error_passwords_no_match)
            editTextConfirmPassword.requestFocus()
            return false
        }
        return true
    }

    //Registro del nuevo usuario en FireBase
    private fun registerUser(email: String, password: String) {
        // 1. Llamada a Firebase para crear el usuario
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                // 2. Si el registro en Firebase fue exitoso
                if (task.isSuccessful) {
                    // Muestra un mensaje de éxito
                    Toast.makeText(
                        baseContext,
                        getString(R.string.register_success),
                        Toast.LENGTH_SHORT
                    ).show()

                    // REDIRECCIÓN A LOGIN
                    finish()
                } else {
                    // 3. Si el registro falló
                    //MENSAJE DE ERROR:
                    Toast.makeText(
                        baseContext,
                        "Fallo en el registro: ${task.exception?.message}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
    }
}