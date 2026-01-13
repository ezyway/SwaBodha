package dev.swabodha.life.features.weight

import android.R
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.FitnessCenter
import androidx.compose.material.icons.outlined.MonitorWeight
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import dev.swabodha.life.core.features.FeatureDescriptor
import dev.swabodha.life.core.features.FeatureEntry
import dev.swabodha.life.features.weight.ui.WeightScreen

class WeightFeatureEntry : FeatureEntry {

    override fun descriptor() = FeatureDescriptor(
        id = "weight",
        title = "Weight",
        icon = Icons.Outlined.MonitorWeight,
        route = "weight"
    )

    override fun registerNavGraph(builder: NavGraphBuilder) {
        builder.composable("weight") {
            WeightScreen()
        }
    }
}
