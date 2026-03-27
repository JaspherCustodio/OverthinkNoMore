package com.example.overthinknomore

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import androidx.activity.enableEdgeToEdge
import com.example.overthinknomore.databinding.ActivitySignUpBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthUserCollisionException

class SignUpActivity : BaseActivity() {

    private var binding: ActivitySignUpBinding? = null
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        auth = FirebaseAuth.getInstance()

        binding?.tvSignIn?.setOnClickListener {
            startActivity(Intent(this, SignInActivity::class.java))
            finish()
        }

        binding?.btnSignUp?.setOnClickListener { signUpUser() }
    }

    private fun signUpUser() {
        val email = binding?.etEmail?.text.toString().trim()
        val password = binding?.etCreatePassword?.text.toString().trim()
        val confirmPassword = binding?.etConfirmPassword?.text.toString().trim()


        if (email.isEmpty()) {
            binding?.tilEmail?.error = "Email is required."
            return
        } else {
            binding?.tilEmail?.error = null
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding?.tilEmail?.error = "Enter a valid email."
            return
        } else if (!email.endsWith("@gmail.com")) {
            binding?.tilEmail?.error = "Only Gmail accounts allowed."
            return
        } else {
            binding?.tilEmail?.error = null
        }

        if (password.isEmpty()) {
            binding?.tilCreatePassword?.error = "Password is required."
            return
        } else {
            binding?.tilCreatePassword?.error = null
        }

        val passwordPattern =
            Regex("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[_@#\$%^&+=!]).{8,}$")

        if (!passwordPattern.containsMatchIn(password)) {
            binding?.tilCreatePassword?.error =
                "Password must be at least 8 characters and include uppercase, lowercase, number, and special character."
            return
        } else {
            binding?.tilCreatePassword?.error = null
        }

        if (confirmPassword.isEmpty()) {
            binding?.tilConfirmPassword?.error = "Confirm your password."
            return
        } else {
            binding?.tilConfirmPassword?.error = null
        }

        if (password != confirmPassword) {
            binding?.tilConfirmPassword?.error = "Passwords do not match."
            return
        } else {
            binding?.tilConfirmPassword?.error = null
        }

        showProgressBar()

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                hideProgressBar()

                if (task.isSuccessful) {
                    showToast(this,"Account created successfully!")

                    startActivity(Intent(this, MainActivity::class.java))
                    finish()

                } else {
                    val errorMessage = when (task.exception) {

                        is FirebaseAuthUserCollisionException ->
                            "This email is already registered."

                        else ->
                            task.exception?.message ?: "Signup failed."
                    }

                    showToast(this,errorMessage)
                }
            }
    }
}