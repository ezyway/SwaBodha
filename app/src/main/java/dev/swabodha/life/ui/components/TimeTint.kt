package dev.swabodha.life.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.graphics.Color
import java.util.Calendar

@Composable
fun rememberTimeTint(): Color {
    val hour = remember {
        Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
    }

    return when (hour) {
        in 5..11 -> MaterialTheme.colorScheme.primary.copy(alpha = 0.18f)
        in 12..16 -> MaterialTheme.colorScheme.tertiary.copy(alpha = 0.16f)
        in 17..21 -> MaterialTheme.colorScheme.secondary.copy(alpha = 0.16f)
        else -> MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.12f)
    }
}
