package dev.swabodha.life.features.water

import android.R
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import dev.swabodha.life.core.features.FeatureDescriptor
import dev.swabodha.life.core.features.FeatureEntry
import dev.swabodha.life.features.water.ui.WaterScreen

class WaterFeatureEntry : FeatureEntry {

    override fun descriptor() = FeatureDescriptor(
        id = "water",
        title = "Water",
        iconRes = R.drawable.ic_menu_gallery, // temp
        route = "water"
    )

    override fun registerNavGraph(builder: NavGraphBuilder) {
        builder.composable("water") {
            WaterScreen()
        }
    }
}
