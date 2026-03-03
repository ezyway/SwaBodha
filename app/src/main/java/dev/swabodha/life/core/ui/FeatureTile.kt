package dev.swabodha.life.core.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.swabodha.life.core.features.FeatureDescriptor

@Composable
fun FeatureTile(
    feature: FeatureDescriptor,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp),
        shape = MaterialTheme.shapes.large,
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp,
            pressedElevation = 6.dp
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            // ===== Icon =====
            Icon(
                imageVector = feature.icon,
                contentDescription = feature.title,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(36.dp)
            )

            Spacer(Modifier.width(16.dp))

            // ===== Title =====
            Text(
                text = feature.title,
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}