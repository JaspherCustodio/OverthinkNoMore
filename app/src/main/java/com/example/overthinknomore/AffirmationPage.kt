package com.example.overthinknomore

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun AffirmationPage() {
    // 1. State for the list and inputs
    var note by remember { mutableStateOf("") }
    var affirmationsList by remember { mutableStateOf(listOf<String>()) }

    // 2. State for Editing (Pop-up)
    var showEditDialog by remember { mutableStateOf(false) }
    var editingIndex by remember { mutableIntStateOf(-1) }
    var editedNote by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF9576EF))
            .statusBarsPadding()
    ) {
        Text(
            text = "Words of Affirmation ✨",
            color = Color.White,
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(20.dp)
        )

        // --- INPUT SECTION ---
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(Color.White)
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "What is your affirmation today?",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF4A3E75)
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = note,
                onValueChange = { note = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("e.g., I am capable of great things...") },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF9576EF),
                    unfocusedBorderColor = Color.LightGray
                ),
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    if (note.isNotBlank()) {
                        affirmationsList = affirmationsList + note
                        note = "" // Clear input after saving
                    }
                },
                modifier = Modifier.fillMaxWidth().height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF9576EF)),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Save to My Sanctuary", color = Color.White, fontWeight = FontWeight.Bold)
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // --- DISPLAY LIST SECTION ---
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
                .background(Color.White)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(affirmationsList.asReversed().indices.toList()) { index ->
                val actualIndex = affirmationsList.size - 1 - index
                val item = affirmationsList[actualIndex]

                AffirmationCard(
                    text = item,
                    onEdit = {
                        editingIndex = actualIndex
                        editedNote = item
                        showEditDialog = true
                    },
                    onDelete = {
                        affirmationsList = affirmationsList.toMutableList().apply { removeAt(actualIndex) }
                    }
                )
            }
        }
    }

    // --- EDIT DIALOG (Like the About Pop-up) ---
    if (showEditDialog) {
        AlertDialog(
            onDismissRequest = { showEditDialog = false },
            title = { Text("Edit Affirmation") },
            text = {
                OutlinedTextField(
                    value = editedNote,
                    onValueChange = { editedNote = it },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )
            },
            confirmButton = {
                TextButton(onClick = {
                    if (editedNote.isNotBlank()) {
                        val newList = affirmationsList.toMutableList()
                        newList[editingIndex] = editedNote
                        affirmationsList = newList
                        showEditDialog = false
                    }
                }) {
                    Text("Update", color = Color(0xFF9576EF))
                }
            },
            dismissButton = {
                TextButton(onClick = { showEditDialog = false }) {
                    Text("Cancel", color = Color.Gray)
                }
            }
        )
    }
}

@Composable
fun AffirmationCard(text: String, onEdit: () -> Unit, onDelete: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF3EFFF))
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = text,
                modifier = Modifier.weight(1f),
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Justify,
                color = Color(0xFF4A3E75)
            )

            IconButton(onClick = onEdit) {
                Icon(Icons.Default.Edit, contentDescription = "Edit", tint = Color(0xFF9576EF))
            }

            IconButton(onClick = onDelete) {
                Icon(Icons.Default.Delete, contentDescription = "Delete", tint = Color.Red.copy(alpha = 0.6f))
            }
        }
    }
}