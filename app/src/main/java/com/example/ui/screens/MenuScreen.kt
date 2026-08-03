package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.particles.BackgroundParticles
import com.example.ui.theme.*
import com.example.viewmodel.GameMode

@Composable
fun MenuScreen(
    currentLanguage: String,
    onStartGame: () -> Unit,
    onSetMode: (GameMode) -> Unit,
    onToggleLanguage: () -> Unit
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
                text = "NEON TYPE",
                color = NeonCyan,
                fontSize = 48.sp,
                fontWeight = FontWeight.ExtraBold,
                modifier = Modifier.padding(bottom = 48.dp)
            )

            NeonButton(
                text = "START",
                color = NeonGreen,
                onClick = onStartGame
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                NeonButton(
                    text = "LETTER",
                    color = NeonPink,
                    onClick = { onSetMode(GameMode.LETTER) },
                    modifier = Modifier.weight(1f).padding(start = 32.dp)
                )
                NeonButton(
                    text = "WORD",
                    color = NeonPurple,
                    onClick = { onSetMode(GameMode.WORD) },
                    modifier = Modifier.weight(1f).padding(end = 32.dp)
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = onToggleLanguage,
                colors = ButtonDefaults.buttonColors(containerColor = SurfaceLight)
            ) {
                Text(if (currentLanguage == "en") "English" else "عربي", color = TextPrimary)
            }
        }
    }
}

@Composable
fun NeonButton(
    text: String,
    color: androidx.compose.ui.graphics.Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(containerColor = SurfaceDark),
        shape = RoundedCornerShape(12.dp),
        modifier = modifier
            .height(56.dp)
            .fillMaxWidth(0.6f)
    ) {
        Text(
            text = text,
            color = color,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )
    }
}
