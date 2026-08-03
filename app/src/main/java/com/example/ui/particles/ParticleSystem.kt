package com.example.ui.particles

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import com.example.ui.theme.*
import kotlin.random.Random

data class Particle(
    var x: Float,
    var y: Float,
    var vx: Float,
    var vy: Float,
    var life: Float,
    val maxLife: Float,
    val color: Color,
    val size: Float
)

@Composable
fun ParticleSystem(
    modifier: Modifier = Modifier,
    trigger: Boolean,
    originX: Float,
    originY: Float,
    color: Color = NeonCyan
) {
    var particles by remember { mutableStateOf(emptyList<Particle>()) }
    
    LaunchedEffect(trigger) {
        if (trigger) {
            val newParticles = List(20) {
                Particle(
                    x = originX,
                    y = originY,
                    vx = Random.nextFloat() * 10 - 5,
                    vy = Random.nextFloat() * 10 - 5,
                    life = 1f,
                    maxLife = Random.nextFloat() * 0.5f + 0.5f,
                    color = color,
                    size = Random.nextFloat() * 10 + 5
                )
            }
            particles = newParticles
        }
    }

    val infiniteTransition = rememberInfiniteTransition(label = "particles")
    val time by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(animation = tween(16, easing = LinearEasing)),
        label = "particleTime"
    )

    Canvas(modifier = modifier.fillMaxSize()) {
        val dt = 0.016f // Roughly 60fps
        particles = particles.mapNotNull { p ->
            p.x += p.vx
            p.y += p.vy
            p.life -= dt / p.maxLife
            if (p.life > 0) p else null
        }

        particles.forEach { p ->
            drawCircle(
                color = p.color.copy(alpha = p.life),
                radius = p.size * p.life,
                center = Offset(p.x, p.y)
            )
        }
        // Force recomposition
        time.hashCode()
    }
}

@Composable
fun BackgroundParticles(modifier: Modifier = Modifier) {
    var particles by remember { mutableStateOf(List(50) {
        Particle(
            x = Random.nextFloat(),
            y = Random.nextFloat(),
            vx = (Random.nextFloat() - 0.5f) * 0.002f,
            vy = (Random.nextFloat() - 0.5f) * 0.002f,
            life = 1f,
            maxLife = 1f,
            color = listOf(NeonCyan, NeonPink, NeonPurple).random(),
            size = Random.nextFloat() * 8 + 2
        )
    }) }

    val infiniteTransition = rememberInfiniteTransition(label = "bgParticles")
    val time by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(animation = tween(16, easing = LinearEasing)),
        label = "bgTime"
    )

    Canvas(modifier = modifier.fillMaxSize()) {
        particles = particles.map { p ->
            var nx = p.x + p.vx
            var ny = p.y + p.vy
            if (nx < 0f) nx = 1f
            if (nx > 1f) nx = 0f
            if (ny < 0f) ny = 1f
            if (ny > 1f) ny = 0f
            p.copy(x = nx, y = ny)
        }

        particles.forEach { p ->
            drawCircle(
                color = p.color.copy(alpha = 0.5f),
                radius = p.size,
                center = Offset(p.x * size.width, p.y * size.height)
            )
        }
        time.hashCode() // Force recomposition
    }
}
