package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.particles.BackgroundParticles
import com.example.ui.theme.*

@Composable
fun GameOverScreen(
    score: Int,
    onRestart: () -> Unit,
    onMenu: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground)
    ) {
        BackgroundParticles()
        
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "GAME OVER",
                color = NeonRed,
                fontSize = 56.sp,
                fontWeight = FontWeight.ExtraBold,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            
            Text(
                text = "SCORE: $score",
                color = NeonCyan,
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 48.dp)
            )

            NeonButton(
                text = "RESTART",
                color = NeonGreen,
                onClick = onRestart
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            NeonButton(
                text = "MENU",
                color = NeonPink,
                onClick = onMenu
            )
        }
    }
}
