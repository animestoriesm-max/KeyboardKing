package com.example.audio

import android.media.AudioManager
import android.media.ToneGenerator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SoundManager {
    private var toneGenerator: ToneGenerator? = null
    var isMuted = false

    init {
        try {
            toneGenerator = ToneGenerator(AudioManager.STREAM_MUSIC, 100)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun playCorrect() {
        if (isMuted) return
        CoroutineScope(Dispatchers.IO).launch {
            toneGenerator?.startTone(ToneGenerator.TONE_PROP_BEEP, 100)
        }
    }

    fun playWrong() {
        if (isMuted) return
        CoroutineScope(Dispatchers.IO).launch {
            toneGenerator?.startTone(ToneGenerator.TONE_CDMA_ABBR_ALERT, 200)
        }
    }

    fun playCombo() {
        if (isMuted) return
        CoroutineScope(Dispatchers.IO).launch {
            toneGenerator?.startTone(ToneGenerator.TONE_SUP_PIP, 50)
            kotlinx.coroutines.delay(100)
            toneGenerator?.startTone(ToneGenerator.TONE_SUP_PIP, 50)
        }
    }

    fun playLevelUp() {
        if (isMuted) return
        CoroutineScope(Dispatchers.IO).launch {
            toneGenerator?.startTone(ToneGenerator.TONE_CDMA_ALERT_CALL_GUARD, 300)
        }
    }

    fun playGameOver() {
        if (isMuted) return
        CoroutineScope(Dispatchers.IO).launch {
            toneGenerator?.startTone(ToneGenerator.TONE_CDMA_SOFT_ERROR_LITE, 500)
        }
    }

    fun playPowerup() {
        if (isMuted) return
        CoroutineScope(Dispatchers.IO).launch {
            toneGenerator?.startTone(ToneGenerator.TONE_CDMA_KEYPAD_VOLUME_KEY_LITE, 150)
        }
    }

    fun release() {
        toneGenerator?.release()
        toneGenerator = null
    }
}
