package dev.swabodha.life.features.smoking

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import dev.swabodha.life.core.features.FeatureDescriptor
import dev.swabodha.life.core.features.FeatureEntry
import dev.swabodha.life.features.smoking.ui.SmokingScreen

class SmokingFeatureEntry : FeatureEntry {

    override fun descriptor() = FeatureDescriptor(
        id = "smoking",
        title = "Smoking",
        iconRes = android.R.drawable.ic_menu_close_clear_cancel,
        route = "smoking"
    )

    override fun registerNavGraph(builder: NavGraphBuilder) {
        builder.composable("smoking") {
            SmokingScreen()
        }
    }
}
