package dev.swabodha.life.features.todo

import android.R
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import dev.swabodha.life.core.features.FeatureDescriptor
import dev.swabodha.life.core.features.FeatureEntry
import dev.swabodha.life.features.todo.ui.TodoScreen

class TodoFeatureEntry : FeatureEntry {

    override fun descriptor() = FeatureDescriptor(
        id = "todo",
        title = "Todo",
        iconRes = R.drawable.ic_menu_agenda,
        route = "todo"
    )

    override fun registerNavGraph(builder: NavGraphBuilder) {
        builder.composable("todo") {
            TodoScreen()
        }
    }
}
