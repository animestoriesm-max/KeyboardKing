package com.example.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.audio.SoundManager
import com.example.data.GameRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

enum class GameMode { LETTER, WORD }
enum class GameState { MENU, PLAYING, GAME_OVER }

data class PowerupState(
    val shield: Boolean = false,
    val freezeTime: Boolean = false,
    val doubleScore: Boolean = false
)

class GameViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = GameRepository(application.applicationContext)
    val soundManager = SoundManager()

    private val _gameState = MutableStateFlow(GameState.MENU)
    val gameState: StateFlow<GameState> = _gameState.asStateFlow()

    private val _gameMode = MutableStateFlow(GameMode.LETTER)
    val gameMode: StateFlow<GameMode> = _gameMode.asStateFlow()

    private val _language = MutableStateFlow("en")
    val language: StateFlow<String> = _language.asStateFlow()

    private val _score = MutableStateFlow(0)
    val score: StateFlow<Int> = _score.asStateFlow()

    private val _combo = MutableStateFlow(0)
    val combo: StateFlow<Int> = _combo.asStateFlow()

    private val _lives = MutableStateFlow(3)
    val lives: StateFlow<Int> = _lives.asStateFlow()

    private val _timeLeft = MutableStateFlow(60f)
    val timeLeft: StateFlow<Float> = _timeLeft.asStateFlow()

    private val _currentWord = MutableStateFlow("")
    val currentWord: StateFlow<String> = _currentWord.asStateFlow()

    private val _typedWord = MutableStateFlow("")
    val typedWord: StateFlow<String> = _typedWord.asStateFlow()

    private val _particleTrigger = MutableStateFlow(false)
    val particleTrigger: StateFlow<Boolean> = _particleTrigger.asStateFlow()
    var particleColor = com.example.ui.theme.NeonCyan

    private val _powerupState = MutableStateFlow(PowerupState())
    val powerupState: StateFlow<PowerupState> = _powerupState.asStateFlow()

    private var timerJob: Job? = null

    val enLetters = ('A'..'Z').map { it.toString() }
    val arLetters = listOf("ا", "ب", "ت", "ث", "ج", "ح", "خ", "د", "ذ", "ر", "ز", "س", "ش", "ص", "ض", "ط", "ظ", "ع", "غ", "ف", "ق", "ك", "ل", "م", "ن", "ه", "و", "ي")

    val enWords = listOf("NEON", "CYBER", "PUNK", "GAME", "CODE", "TYPE", "FAST", "QUICK", "REACT", "FLAME", "GLOW", "LASER")
    val arWords = listOf("لعبة", "سريع", "طباعة", "نيون", "ضوء", "سيبر", "برمجة", "نار", "فلاش", "ليزر")

    init {
        viewModelScope.launch {
            repository.languageFlow.collect { lang ->
                _language.value = lang
            }
        }
        viewModelScope.launch {
            repository.soundMutedFlow.collect { muted ->
                soundManager.isMuted = muted
            }
        }
    }

    fun setMode(mode: GameMode) {
        _gameMode.value = mode
    }

    fun toggleLanguage() {
        val newLang = if (_language.value == "en") "ar" else "en"
        viewModelScope.launch { repository.setLanguage(newLang) }
    }

    fun toggleSound() {
        viewModelScope.launch { repository.setSoundMuted(!soundManager.isMuted) }
    }

    fun startGame() {
        _score.value = 0
        _combo.value = 0
        _lives.value = 3
        _timeLeft.value = 60f
        _typedWord.value = ""
        _powerupState.value = PowerupState()
        _gameState.value = GameState.PLAYING
        nextWord()
        startTimer()
    }

    private fun startTimer() {
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            while (_timeLeft.value > 0 && _gameState.value == GameState.PLAYING) {
                delay(100)
                if (!_powerupState.value.freezeTime) {
                    _timeLeft.value -= 0.1f
                }
            }
            if (_timeLeft.value <= 0) {
                gameOver()
            }
        }
    }

    private fun nextWord() {
        val list = if (_gameMode.value == GameMode.LETTER) {
            if (_language.value == "en") enLetters else arLetters
        } else {
            if (_language.value == "en") enWords else arWords
        }
        _currentWord.value = list.random()
        _typedWord.value = ""
    }

    fun onKeyPressed(char: String) {
        if (_gameState.value != GameState.PLAYING) return

        if (_gameMode.value == GameMode.LETTER) {
            if (char.equals(_currentWord.value, ignoreCase = true)) {
                correctInput()
            } else {
                wrongInput()
            }
        } else {
            val nextExpectedChar = _currentWord.value[_typedWord.value.length].toString()
            if (char.equals(nextExpectedChar, ignoreCase = true)) {
                _typedWord.value += char
                if (_typedWord.value.length == _currentWord.value.length) {
                    correctInput()
                }
            } else {
                wrongInput()
            }
        }
    }

    private fun correctInput() {
        soundManager.playCorrect()
        _combo.value += 1
        val points = (10 + _combo.value * 2) * (if (_powerupState.value.doubleScore) 2 else 1)
        _score.value += points
        
        // Time bonus
        _timeLeft.value += 1f
        
        triggerParticles(com.example.ui.theme.NeonGreen)
        nextWord()
    }

    private fun wrongInput() {
        soundManager.playWrong()
        _combo.value = 0
        
        if (_powerupState.value.shield) {
            _powerupState.value = _powerupState.value.copy(shield = false)
        } else {
            _lives.value -= 1
            if (_lives.value <= 0) {
                gameOver()
            } else {
                triggerParticles(com.example.ui.theme.NeonRed)
            }
        }
    }

    private fun triggerParticles(color: androidx.compose.ui.graphics.Color) {
        particleColor = color
        _particleTrigger.value = !_particleTrigger.value
    }

    private fun gameOver() {
        _gameState.value = GameState.GAME_OVER
        timerJob?.cancel()
        soundManager.playGameOver()
        viewModelScope.launch {
            repository.saveHighScore(_score.value)
        }
    }
    
    fun quitGame() {
        _gameState.value = GameState.MENU
        timerJob?.cancel()
    }

    override fun onCleared() {
        super.onCleared()
        soundManager.release()
    }
}
