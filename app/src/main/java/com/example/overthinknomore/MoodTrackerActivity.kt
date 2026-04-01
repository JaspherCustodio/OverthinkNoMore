package com.example.overthinknomore

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.MaterialTheme
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore

class MoodTrackerActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val db = FirebaseFirestore.getInstance()

        setContent {
            MaterialTheme {
                MoodTrackerPage(
                    onMoodSaved = { mood, note ->
                        saveMoodToFirebase(db, mood, note)
                    }
                )
            }
        }
    }

    private fun saveMoodToFirebase(db: FirebaseFirestore, mood: String, note: String){
        val moodEntry = hashMapOf(
            "mood" to mood,
            "note" to note,
            "timestamp" to FieldValue.serverTimestamp()
        )

        db.collection("moods")
            .add(moodEntry)
            .addOnSuccessListener {
                Toast.makeText(this, "Mood saved! 🌿", Toast.LENGTH_SHORT).show()
                finish()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_LONG).show()
            }
    }
}