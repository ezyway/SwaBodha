package dev.swabodha.life.core.security.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.swabodha.life.core.security.AppLockPrefs

@Composable
fun PinSetupScreen(
    onComplete: () -> Unit
) {

    val context = androidx.compose.ui.platform.LocalContext.current
    val prefs = remember { AppLockPrefs.get(context) }

    var step by remember { mutableStateOf(1) }
    var firstPin by remember { mutableStateOf("") }
    var pin by remember { mutableStateOf("") }
    var error by remember { mutableStateOf(false) }

    Surface(Modifier.fillMaxSize()) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize()
        ) {

            Text(if (step == 1) "Create PIN" else "Confirm PIN")

            Spacer(Modifier.height(16.dp))

            OutlinedTextField(
                value = pin,
                onValueChange = {
                    if (it.length <= 6) pin = it.filter { c -> c.isDigit() }
                },
                label = { Text("6-digit PIN") },
                isError = error
            )

            Spacer(Modifier.height(16.dp))

            Button(onClick = {

                if (pin.length < 4) {
                    error = true
                    return@Button
                }

                if (step == 1) {
                    firstPin = pin
                    pin = ""
                    step = 2
                } else {
                    if (pin == firstPin) {
                        prefs.setPin(pin)
                        prefs.setEnabled(true)
                        onComplete()
                    } else {
                        error = true
                        pin = ""
                    }
                }

            }) {
                Text("Continue")
            }
        }
    }
}