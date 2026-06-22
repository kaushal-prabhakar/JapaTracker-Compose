package com.kaushal.japacountercompose.ui.feature.details

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.kaushal.japacountercompose.R
import com.kaushal.japacountercompose.ui.theme.BrandColor
import com.kaushal.japacountercompose.ui.theme.SuccessGreen
import kotlin.random.Random

@Composable
fun CelebrationOverlay(visible: Boolean) {
    AnimatedVisibility(
        visible = visible,
        enter = fadeIn() + scaleIn(),
        exit = fadeOut() + scaleOut()
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.3f)),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .background(Color.White, RoundedCornerShape(24.dp))
                    .padding(32.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Done,
                    contentDescription = null,
                    tint = SuccessGreen,
                    modifier = Modifier.size(64.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = stringResource(id = R.string.celebration_congratulations),
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = SuccessGreen,
                        fontFamily = FontFamily.Monospace
                    )
                )
                Text(
                    text = stringResource(id = R.string.celebration_goal_reached),
                    style = MaterialTheme.typography.bodyLarge.copy(
                        color = Color.Gray,
                        fontFamily = FontFamily.Monospace
                    )
                )
            }

            // Simple Particle Effect
            repeat(20) {
                ConfettiParticle()
            }
        }
    }
}

@Composable
private fun ConfettiParticle() {
    val xPos = remember { Random.nextInt(0, 1000).toFloat() }
    val yPos = remember { Animatable(-100f) }
    val color = remember {
        listOf(SuccessGreen, BrandColor, Color.Yellow, Color.Cyan).random()
    }

    LaunchedEffect(Unit) {
        yPos.animateTo(
            targetValue = 2000f,
            animationSpec = tween(
                durationMillis = Random.nextInt(1000, 3000),
                easing = LinearEasing
            )
        )
    }

    Box(
        modifier = Modifier
            .offset(x = (xPos / 10).dp, y = (yPos.value / 10).dp)
            .size(8.dp)
            .background(color, RoundedCornerShape(50))
    )
}
