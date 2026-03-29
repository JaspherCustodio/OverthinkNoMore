package com.example.overthinknomore

import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.overthinknomore.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {
    private var binding: ActivityMainBinding? = null
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        auth = FirebaseAuth.getInstance()

        binding?.btnThoughtDump?.setOnClickListener {
            startActivity(Intent(this, ThoughtDumpActivity::class.java))
        }

        binding?.profileSection?.setOnClickListener {
            if (auth.currentUser != null) {
                auth.signOut()
                startActivity(Intent(this, GetStartedActivity::class.java))
                finish()
            }
        }
    }
}