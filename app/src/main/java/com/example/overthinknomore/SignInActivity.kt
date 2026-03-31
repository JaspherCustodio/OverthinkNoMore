package com.example.overthinknomore

import android.content.Intent
import androidx.credentials.CredentialManager
import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.credentials.Credential
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.lifecycle.lifecycleScope
import com.example.overthinknomore.databinding.ActivitySignInBinding
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.auth
import kotlinx.coroutines.launch

class SignInActivity : BaseActivity() {
    private var binding: ActivitySignInBinding? = null
    private lateinit var credentialManager: CredentialManager
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        credentialManager = CredentialManager.create(this)
        auth = FirebaseAuth.getInstance()

        binding?.tvSignUp?.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
        }

        binding?.tvForgotPassword?.setOnClickListener {
            startActivity(Intent(this, ForgetPasswordActivity::class.java))
        }

        binding?.btnSignIn?.setOnClickListener { signInUser() }

        binding?.btnGoogle?.setOnClickListener { signInWithGoogle() }

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                finish()
            }
        })
    }

    private fun signInUser() {
        val email = binding?.etEmail?.text.toString().trim()
        val password = binding?.etPassword?.text.toString().trim()

        if (email.isEmpty()) {
            binding?.tilEmail?.error = "Email is required."
            return
        } else {
            binding?.tilEmail?.error = null
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding?.tilEmail?.error = "Enter a valid email."
            return
        } else {
            binding?.tilEmail?.error = null
        }

        if (password.isEmpty()) {
            binding?.tilPassword?.error = "Password is required."
            return
        } else {
            binding?.tilPassword?.error = null
        }

        if (password.length < 8) {
            binding?.tilPassword?.error = "Password must be at least 8 characters."
            return
        } else {
            binding?.tilPassword?.error = null
        }

        showProgressBar()

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                hideProgressBar()

                if (task.isSuccessful) {
                    showToast(this, "Login successful!")

                    startActivity(Intent(this, MainActivity::class.java))
                    finish()

                } else {
                    val errorMessage = when {
                        task.exception?.message?.contains("no user record.") == true ->
                            "Account does not exist."

                        task.exception?.message?.contains("password is invalid.") == true ->
                            "Incorrect password."

                        task.exception?.message?.contains("badly formatted.") == true ->
                            "Invalid email format."

                        else -> task.exception?.message ?: "Login failed. Please try again later."
                    }

                    showToast(this, errorMessage)
                }
            }
    }

    private fun signInWithGoogle() {

        val googleIdOption = GetGoogleIdOption.Builder()
            .setServerClientId(getString(R.string.default_web_client_id))
            .setFilterByAuthorizedAccounts(false)
            .build()

        val request = GetCredentialRequest.Builder()
            .addCredentialOption(googleIdOption)
            .build()

        lifecycleScope.launch {
            try {
                val result = credentialManager.getCredential(
                    context = this@SignInActivity,
                    request = request
                )

                handleSignIn(result.credential)

            } catch (e: Exception) {
                showToast(this@SignInActivity, "Google Sign-In failed.")
            }
        }
    }

    private fun handleSignIn(credential: Credential) {

        if (credential is CustomCredential &&
            credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL
        ) {

            val googleCredential =
                GoogleIdTokenCredential.createFrom(credential.data)

            firebaseAuthWithGoogle(googleCredential.idToken)

        } else {
            showToast(this, "Invalid Google credential.")
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {

        val credential = GoogleAuthProvider.getCredential(idToken, null)

        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->

                if (task.isSuccessful) {
                    showToast(this, "Google Sign-In successful!")

                    startActivity(Intent(this, MainActivity::class.java))
                    finish()

                } else {
                    showToast(this, "Authentication Failed.")
                }
            }
    }
}