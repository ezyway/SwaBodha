package dev.swabodha.life.core.security.ui

import androidx.biometric.BiometricManager
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.offset
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.unit.dp
import dev.swabodha.life.core.security.AppLockPrefs
import dev.swabodha.life.core.security.ui.components.AnimatedPinDots
import dev.swabodha.life.core.security.ui.components.BiometricButton
import dev.swabodha.life.core.security.ui.components.PinKeyboard
import kotlinx.coroutines.delay

@Composable
fun LockScreen(
    onUnlock: () -> Unit
) {

    val context = LocalContext.current
    val prefs = remember { AppLockPrefs.get(context) }

    val haptic = LocalHapticFeedback.current

    var pin by remember { mutableStateOf("") }
    var error by remember { mutableStateOf(false) }

    val shake = remember { Animatable(0f) }
    val dotsScale = remember { Animatable(1f) }

    val biometricAvailable = remember {
        BiometricManager.from(context)
            .canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_WEAK) ==
                BiometricManager.BIOMETRIC_SUCCESS
    }

    suspend fun successBounce() {
        dotsScale.animateTo(1.2f, tween(120))
        dotsScale.animateTo(1f, tween(120))
    }

    suspend fun shakeError() {
        repeat(3) {
            shake.animateTo(10f, tween(50))
            shake.animateTo(-10f, tween(50))
        }
        shake.animateTo(0f)
    }

    Surface(Modifier.fillMaxSize()) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
                .offset(x = shake.value.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(Modifier.height(80.dp))

            Text(
                "App Locked",
                style = MaterialTheme.typography.headlineMedium
            )

            Spacer(Modifier.height(16.dp))

            Text(
                "Enter your PIN to continue",
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(Modifier.height(40.dp))

            Box(
                modifier = Modifier.graphicsLayer {
                    scaleX = dotsScale.value
                    scaleY = dotsScale.value
                }
            ) {
                AnimatedPinDots(pin.length)
            }

            Spacer(Modifier.height(16.dp))

            if (error) {
                Text(
                    "Incorrect PIN",
                    color = MaterialTheme.colorScheme.error
                )
            }

            Spacer(Modifier.weight(1f))

            PinKeyboard(
                onNumber = {

                    if (pin.length < 6) {
                        pin += it

                        // light tap feedback
                        haptic.performHapticFeedback(
                            androidx.compose.ui.hapticfeedback.HapticFeedbackType.TextHandleMove
                        )
                    }

                    error = false
                },
                onDelete = {
                    if (pin.isNotEmpty()) {
                        pin = pin.dropLast(1)

                        haptic.performHapticFeedback(
                            androidx.compose.ui.hapticfeedback.HapticFeedbackType.TextHandleMove
                        )
                    }
                }
            )

            Spacer(Modifier.height(20.dp))

            if (biometricAvailable) {
                BiometricButton(onSuccess = onUnlock)
            }

            Spacer(Modifier.height(20.dp))
        }
    }

    LaunchedEffect(pin) {

        if (pin.length == 6) {

            // allow last dot animation
            delay(150)

            if (prefs.verifyPin(pin)) {
                error = false
                successBounce()
                delay(120)
                onUnlock()
            } else {
                error = true
                pin = ""
                haptic.performHapticFeedback(
                    androidx.compose.ui.hapticfeedback.HapticFeedbackType.LongPress
                )
                shakeError()
            }
        }
    }
}