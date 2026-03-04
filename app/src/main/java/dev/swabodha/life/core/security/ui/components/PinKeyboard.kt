package dev.swabodha.life.core.security.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun PinKeyboard(
    onNumber: (String) -> Unit,
    onDelete: () -> Unit
) {

    Column(
        verticalArrangement = Arrangement.spacedBy(14.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        listOf(
            listOf("1","2","3"),
            listOf("4","5","6"),
            listOf("7","8","9")
        ).forEach { row ->

            Row(horizontalArrangement = Arrangement.spacedBy(20.dp)) {

                row.forEach { key ->
                    PinKey(key) { onNumber(key) }
                }
            }
        }

        Row(horizontalArrangement = Arrangement.spacedBy(20.dp)) {

            Spacer(Modifier.size(64.dp))

            PinKey("0") { onNumber("0") }

            PinKey("⌫") { onDelete() }
        }
    }
}