package dev.swabodha.life.features.todo

import android.R
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ListAlt
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import dev.swabodha.life.core.features.FeatureDescriptor
import dev.swabodha.life.core.features.FeatureEntry
import dev.swabodha.life.features.todo.ui.TodoScreen

class TodoFeatureEntry : FeatureEntry {

    override fun descriptor() = FeatureDescriptor(
        id = "todo",
        title = "Todo",
        icon = Icons.AutoMirrored.Outlined.ListAlt,
        route = "todo"
    )

    override fun registerNavGraph(builder: NavGraphBuilder) {
        builder.composable("todo") {
            TodoScreen()
        }
    }
}
