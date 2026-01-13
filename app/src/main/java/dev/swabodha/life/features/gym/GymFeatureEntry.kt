package dev.swabodha.life.features.gym

import android.R
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.FitnessCenter
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import dev.swabodha.life.core.features.FeatureDescriptor
import dev.swabodha.life.core.features.FeatureEntry
import dev.swabodha.life.features.gym.ui.GymScreen

class GymFeatureEntry : FeatureEntry {

    override fun descriptor() = FeatureDescriptor(
        id = "gym",
        title = "Gym",
        icon = Icons.Outlined.FitnessCenter,
        route = "gym"
    )

    override fun registerNavGraph(builder: NavGraphBuilder) {
        builder.composable("gym") {
            GymScreen()
        }
    }
}
