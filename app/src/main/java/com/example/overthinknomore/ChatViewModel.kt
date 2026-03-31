package com.example.overthinknomore

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Firebase
import com.google.firebase.ai.GenerativeModel
import com.google.firebase.ai.ai
import com.google.firebase.ai.type.GenerativeBackend
import com.google.firebase.ai.type.content
import kotlinx.coroutines.launch

class ChatViewModel: ViewModel() {

    val messageList by lazy {
        mutableStateListOf<MessageModel>()
    }
    private val model = Firebase.ai(
        backend = GenerativeBackend.googleAI()
    ).generativeModel(
        modelName = "gemini-3.1-flash-lite-preview",
        systemInstruction = content {
            text("""
                You are a supportive mental health assistant for the "OverthinkNoMore" app. 
                Your goal is to help users manage overthinking, anxiety, and stress.
                
                STRICT RULES:
                1. Only discuss topics related to mental health, wellbeing, emotions, and mindfulness.
                2. If a user asks something unrelated (e.g., math, coding, general trivia, history, or "1+1"), 
                   politely decline and remind them that you are here specifically to support their mental health.
                3. Be empathetic, grounded, and non-judgmental.
                4. If the user expresses thoughts of self-harm, provide resources for professional help immediately.
            """.trimIndent())
        }
    )
    @RequiresApi(Build.VERSION_CODES.VANILLA_ICE_CREAM)
    fun sendMessage(question: String){
        viewModelScope.launch {
            try {
                val chat = model.startChat(
                    history = messageList.map {
                        content(it.role) {
                            text(it.message)
                        }
                    }.toList()
                )
                messageList.add(MessageModel(question, "user"))
                messageList.add(MessageModel("Typing...", "model"))

                val response = chat.sendMessage(question)
                messageList.removeLast()
                messageList.add(MessageModel(response.text.toString(), "model"))
            } catch (e: Exception){
                messageList.removeLast()
                messageList.add(MessageModel(e.message.toString(), "model"))
            }
        }
    }
}