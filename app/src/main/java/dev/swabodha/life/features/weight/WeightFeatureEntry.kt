package dev.swabodha.life.features.weight

import android.R
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import dev.swabodha.life.core.features.FeatureDescriptor
import dev.swabodha.life.core.features.FeatureEntry
import dev.swabodha.life.features.weight.ui.WeightScreen

class WeightFeatureEntry : FeatureEntry {

    override fun descriptor() = FeatureDescriptor(
        id = "weight",
        title = "Weight",
        iconRes = R.drawable.ic_menu_edit,
        route = "weight"
    )

    override fun registerNavGraph(builder: NavGraphBuilder) {
        builder.composable("weight") {
            WeightScreen()
        }
    }
}
