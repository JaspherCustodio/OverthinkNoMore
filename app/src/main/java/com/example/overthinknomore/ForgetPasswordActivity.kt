package com.example.overthinknomore

import android.os.Bundle
import android.util.Patterns
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.overthinknomore.databinding.ActivityForgetPasswordBinding
import com.google.firebase.auth.FirebaseAuth

class ForgetPasswordActivity : BaseActivity() {
    private var binding: ActivityForgetPasswordBinding? = null
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityForgetPasswordBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        auth = FirebaseAuth.getInstance()

        binding?.btnForgotPasswordSubmit?.setOnClickListener { sendPasswordResetEmail() }

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                finish()
            }
        })
    }

    private fun sendPasswordResetEmail() {
        val email = binding?.etForgotPasswordEmail?.text.toString().trim()

        if (email.isEmpty()) {
            binding?.tilEmailForgetPassword?.error = "Email is required."
            return
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            binding?.tilEmailForgetPassword?.error = "Enter a valid email."
            return
        } else {
            binding?.tilEmailForgetPassword?.error = null
        }

        showProgressBar()

        auth.sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                hideProgressBar()

                if (task.isSuccessful) {
                    binding?.tilEmailForgetPassword?.visibility = View.GONE
                    binding?.tvSubmitMsg?.visibility = View.VISIBLE
                    binding?.btnForgotPasswordSubmit?.visibility = View.GONE
                } else {
                    val errorMessage = task.exception?.message ?: "Failed to send request. Try again later."
                    showToast(this, errorMessage)
                }
        }
    }
}