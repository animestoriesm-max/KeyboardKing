package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.example.ui.screens.GameOverScreen
import com.example.ui.screens.MenuScreen
import com.example.ui.screens.PlayingScreen
import com.example.ui.theme.MyApplicationTheme
import com.example.viewmodel.GameState
import com.example.viewmodel.GameViewModel

class MainActivity : ComponentActivity() {
  private val viewModel: GameViewModel by viewModels()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setContent {
      MyApplicationTheme {
        NeonGameApp(viewModel)
      }
    }
  }
}

@Composable
fun NeonGameApp(viewModel: GameViewModel) {
    val gameState by viewModel.gameState.collectAsState()
    val language by viewModel.language.collectAsState()
    
    when (gameState) {
        GameState.MENU -> {
            MenuScreen(
                currentLanguage = language,
                onStartGame = { viewModel.startGame() },
                onSetMode = { viewModel.setMode(it) },
                onToggleLanguage = { viewModel.toggleLanguage() }
            )
        }
        GameState.PLAYING -> {
            val currentWord by viewModel.currentWord.collectAsState()
            val typedWord by viewModel.typedWord.collectAsState()
            val score by viewModel.score.collectAsState()
            val combo by viewModel.combo.collectAsState()
            val lives by viewModel.lives.collectAsState()
            val timeLeft by viewModel.timeLeft.collectAsState()
            val particleTrigger by viewModel.particleTrigger.collectAsState()
            
            PlayingScreen(
                currentWord = currentWord,
                typedWord = typedWord,
                score = score,
                combo = combo,
                lives = lives,
                timeLeft = timeLeft,
                language = language,
                particleTrigger = particleTrigger,
                particleColor = viewModel.particleColor,
                onKeyPressed = { viewModel.onKeyPressed(it) },
                onQuit = { viewModel.quitGame() }
            )
        }
        GameState.GAME_OVER -> {
            val score by viewModel.score.collectAsState()
            GameOverScreen(
                score = score,
                onRestart = { viewModel.startGame() },
                onMenu = { viewModel.quitGame() }
            )
        }
    }
}

