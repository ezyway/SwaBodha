package dev.swabodha.life.features.smoking

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.SmokingRooms
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import dev.swabodha.life.core.features.FeatureDescriptor
import dev.swabodha.life.core.features.FeatureEntry
import dev.swabodha.life.features.smoking.ui.SmokingScreen

class SmokingFeatureEntry : FeatureEntry {

    override fun descriptor() = FeatureDescriptor(
        id = "smoking",
        title = "Smoking",
        icon = Icons.Outlined.SmokingRooms,
        route = "smoking"
    )

    override fun registerNavGraph(builder: NavGraphBuilder) {
        builder.composable("smoking") {
            SmokingScreen()
        }
    }
}
