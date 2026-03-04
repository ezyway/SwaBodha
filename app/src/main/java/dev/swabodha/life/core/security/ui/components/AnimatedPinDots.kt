package dev.swabodha.life.core.security.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp

@Composable
fun AnimatedPinDots(count: Int) {

    Row(
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        repeat(6) { index ->

            val filled = index < count

            val scale by animateFloatAsState(
                targetValue = if (filled) 1f else 0.4f,
                label = ""
            )

            Box(
                modifier = Modifier.size(20.dp), // fixed outer size
                contentAlignment = Alignment.Center
            ) {

                Box(
                    modifier = Modifier
                        .size(12.dp)
                        .graphicsLayer {
                            scaleX = scale
                            scaleY = scale
                        }
                        .background(
                            MaterialTheme.colorScheme.primary,
                            CircleShape
                        )
                )
            }
        }
    }
}