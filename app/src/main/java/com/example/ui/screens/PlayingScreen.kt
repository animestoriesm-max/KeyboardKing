package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.VirtualKeyboard
import com.example.ui.particles.BackgroundParticles
import com.example.ui.particles.ParticleSystem
import com.example.ui.theme.*

@Composable
fun PlayingScreen(
    currentWord: String,
    typedWord: String,
    score: Int,
    combo: Int,
    lives: Int,
    timeLeft: Float,
    language: String,
    particleTrigger: Boolean,
    particleColor: Color,
    onKeyPressed: (String) -> Unit,
    onQuit: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground)
    ) {
        BackgroundParticles()

        val config = LocalConfiguration.current
        val w = config.screenWidthDp.dp.value
        val h = config.screenHeightDp.dp.value
        
        ParticleSystem(
            trigger = particleTrigger,
            originX = w / 2f,
            originY = h / 2f,
            color = particleColor
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // HUD
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onQuit) {
                    Icon(Icons.Default.Close, contentDescription = "Quit", tint = TextSecondary)
                }
                
                Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    Text(text = "SCORE\n$score", color = NeonCyan, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    Text(text = "COMBO\n${combo}x", color = NeonPink, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    Text(text = "LIVES\n$lives", color = NeonRed, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                }
            }

            // Timer bar
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .background(SurfaceLight, RoundedCornerShape(4.dp))
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .fillMaxWidth(timeLeft / 60f)
                        .background(NeonYellow, RoundedCornerShape(4.dp))
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            // Word display
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = buildAnnotatedString {
                        withStyle(style = SpanStyle(color = NeonGreen)) {
                            append(typedWord)
                        }
                        withStyle(style = SpanStyle(color = TextPrimary)) {
                            append(currentWord.drop(typedWord.length))
                        }
                    },
                    fontSize = 64.sp,
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = 8.sp
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            // Keyboard
            VirtualKeyboard(
                language = language,
                onKeyPressed = onKeyPressed
            )
            
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
