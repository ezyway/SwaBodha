package dev.swabodha.life.ui.components

import androidx.compose.material3.*
import androidx.compose.runtime.*
import kotlinx.coroutines.launch

@Composable
fun rememberSnackbarController(): SnackbarController {
    val hostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    return remember(hostState) {
        SnackbarController(hostState) { message, actionLabel, onAction ->
            scope.launch {
                val result = hostState.showSnackbar(
                    message = message,
                    actionLabel = actionLabel,
                    duration = SnackbarDuration.Short
                )

                if (result == SnackbarResult.ActionPerformed) {
                    onAction?.invoke()
                }
            }
        }
    }
}

class SnackbarController(
    val hostState: SnackbarHostState,
    private val showInternal: (
        message: String,
        actionLabel: String?,
        onAction: (() -> Unit)?
    ) -> Unit
) {

    fun show(
        message: String,
        actionLabel: String? = null,
        onAction: (() -> Unit)? = null
    ) {
        showInternal(message, actionLabel, onAction)
    }

    fun comingSoon() {
        show("This feature is coming soon ..!", "OK")
    }
}