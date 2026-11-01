package com.careloop.app.service

import android.content.Context
import android.speech.tts.TextToSpeech
import java.util.Locale

class TextToSpeechManager(context: Context) : TextToSpeech.OnInitListener {
    private var tts: TextToSpeech? = TextToSpeech(context.applicationContext, this)
    private var isInitialized = false

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            val result = tts?.setLanguage(Locale.getDefault())
            if (result != TextToSpeech.LANG_MISSING_DATA && result != TextToSpeech.LANG_NOT_SUPPORTED) {
                isInitialized = true
                tts?.setSpeechRate(0.85f) // slightly slower for elderly clarity
            }
        }
    }

    fun speakReminder(title: String) {
        if (isInitialized && tts != null) {
            val spokenText = "Reminder. It is time for: $title. Please confirm on your screen."
            tts?.speak(spokenText, TextToSpeech.QUEUE_FLUSH, null, "careloop_tts_${System.currentTimeMillis()}")
        }
    }

    fun shutdown() {
        tts?.stop()
        tts?.shutdown()
        tts = null
    }
}

