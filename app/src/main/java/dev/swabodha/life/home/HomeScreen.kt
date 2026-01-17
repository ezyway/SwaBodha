package dev.swabodha.life.home

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.Today
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import dev.swabodha.life.features.FeatureRegistry
import dev.swabodha.life.core.ui.components.FeatureTile
import dev.swabodha.life.navigation.Routes
import kotlinx.coroutines.delay
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController) {
    val features = FeatureRegistry.enabledDescriptors()

    val greeting = rememberGreeting()
    val headerTint = rememberTimeTint()


    Scaffold { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {

            // ===== Header =====
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                headerTint,
                                MaterialTheme.colorScheme.background
                            )
                        )
                    )
            ) {
                // Settings icon (top-right)
                IconButton(
                    onClick = { navController.navigate(Routes.SETTINGS) },
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(top = 12.dp, end = 12.dp)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Settings,
                        contentDescription = "Settings",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                // Header content
                Column(
                    modifier = Modifier.padding(horizontal = 24.dp, vertical = 32.dp)
                ) {
                    Text(
                        text = greeting,
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary
                    )

                    Spacer(Modifier.height(6.dp))

                    Text(
                        text = "SwaBodha",
                        style = MaterialTheme.typography.headlineMedium
                    )

                    Spacer(Modifier.height(8.dp))

                    Text(
                        text = "Small mindful actions for today.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            // ===== Content =====
            Surface(
                modifier = Modifier
                    .fillMaxSize()
                    .offset(y = (-12).dp),
                shape = MaterialTheme.shapes.extraLarge,
                tonalElevation = 3.dp
            ) {
                Column {

                    // ===== Today Section =====
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp, vertical = 20.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Today,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary
                        )

                        Spacer(Modifier.width(12.dp))

                        Column {
                            Text(
                                text = "Today",
                                style = MaterialTheme.typography.titleMedium
                            )
                            Text(
                                text = "Focus on what matters now",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    // ===== Animated Feature Grid =====
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        contentPadding = PaddingValues(
                            start = 24.dp,
                            end = 24.dp,
                            bottom = 24.dp
                        ),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        itemsIndexed(features) { index, feature ->
                            AnimatedFeatureTile(index) {
                                FeatureTile(feature) {
                                    navController.navigate(feature.route)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun AnimatedFeatureTile(
    index: Int,
    content: @Composable () -> Unit
) {
    var visible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        delay(index * 60L)
        visible = true
    }

    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(tween(300)) +
                slideInVertically(
                    initialOffsetY = { it / 6 },
                    animationSpec = tween(300, easing = FastOutSlowInEasing)
                )
    ) {
        content()
    }
}

@Composable
private fun rememberGreeting(): String {
    val hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
    return when (hour) {
        in 5..11 -> "Good morning"
        in 12..16 -> "Good afternoon"
        in 17..21 -> "Good evening"
        else -> "Welcome back"
    }
}

@Composable
private fun rememberTimeTint(): Color {
    val hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
    return when (hour) {
        in 5..11 -> MaterialTheme.colorScheme.primary.copy(alpha = 0.18f)
        in 12..16 -> MaterialTheme.colorScheme.tertiary.copy(alpha = 0.16f)
        in 17..21 -> MaterialTheme.colorScheme.secondary.copy(alpha = 0.16f)
        else -> MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.12f)
    }
}
