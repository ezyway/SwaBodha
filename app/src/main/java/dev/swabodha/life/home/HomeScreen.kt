package dev.swabodha.life.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import dev.swabodha.life.core.features.registry.FeatureRegistry
import dev.swabodha.life.core.ui.components.FeatureTile

@Composable
fun HomeScreen(navController: NavController) {
    val features = FeatureRegistry.enabledDescriptors()

    Column(
        modifier = Modifier.padding(16.dp)
    ) {
        Text(
            text = "SwaBodha",
            modifier = Modifier.padding(bottom = 16.dp)
        )

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(features) { feature ->
                FeatureTile(feature) {
                    navController.navigate(feature.route)
                }
            }
        }
    }
}
