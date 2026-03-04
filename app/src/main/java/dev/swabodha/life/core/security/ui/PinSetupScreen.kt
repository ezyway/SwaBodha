package dev.swabodha.life.core.security.ui

import androidx.biometric.BiometricManager
import androidx.compose.animation.core.*
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
import dev.swabodha.life.core.security.ui.components.PinKeyboard
import kotlinx.coroutines.delay

private enum class Stage {
    Verify,
    Create,
    Confirm
}

@Composable
fun PinSetupScreen(
    onComplete: () -> Unit
) {

    val context = LocalContext.current
    val prefs = remember { AppLockPrefs.get(context) }
    val haptic = LocalHapticFeedback.current

    var stage by remember {
        mutableStateOf(
            if (prefs.hasPin()) Stage.Verify else Stage.Create
        )
    }

    var firstPin by remember { mutableStateOf("") }
    var pin by remember { mutableStateOf("") }
    var error by remember { mutableStateOf(false) }

    val shakeOffset = remember { Animatable(0f) }
    val dotsScale = remember { Animatable(1f) }

    val biometricAvailable = remember {
        BiometricManager.from(context)
            .canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_WEAK) ==
                BiometricManager.BIOMETRIC_SUCCESS
    }

    suspend fun shake() {
        repeat(3) {
            shakeOffset.animateTo(10f, tween(50))
            shakeOffset.animateTo(-10f, tween(50))
        }
        shakeOffset.animateTo(0f)
    }

    suspend fun resetDots() {
        dotsScale.animateTo(0.6f, tween(120))
        dotsScale.animateTo(1f, tween(120))
    }

    suspend fun successBounce() {
        dotsScale.animateTo(1.2f, tween(120))
        dotsScale.animateTo(1f, tween(120))
    }

    val title = when (stage) {
        Stage.Verify -> "Enter current PIN"
        Stage.Create -> "Create new PIN"
        Stage.Confirm -> "Confirm new PIN"
    }

    val subtitle = when (stage) {
        Stage.Verify -> "Verify your current PIN"
        Stage.Create -> "Use a 6-digit PIN to secure the app"
        Stage.Confirm -> "Re-enter your new PIN"
    }

    val errorText = when (stage) {
        Stage.Verify -> "Incorrect PIN"
        Stage.Confirm -> "PINs do not match"
        else -> ""
    }

    Surface(Modifier.fillMaxSize()) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
                .offset(x = shakeOffset.value.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(Modifier.height(80.dp))

            Text(
                text = title,
                style = MaterialTheme.typography.headlineMedium
            )

            Spacer(Modifier.height(8.dp))

            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodyMedium,
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

            Spacer(Modifier.height(20.dp))

            if (error) {
                Text(
                    errorText,
                    color = MaterialTheme.colorScheme.error
                )
            }

            Spacer(Modifier.weight(1f))

            PinKeyboard(
                onNumber = {
                    if (pin.length < 6) {
                        pin += it
                        haptic.performHapticFeedback(
                            androidx.compose.ui.hapticfeedback.HapticFeedbackType.TextHandleMove
                        )
                    }
                    error = false
                },
                onDelete = {
                    if (pin.isNotEmpty()) {
                        pin = pin.dropLast(1)
                    }
                }
            )

            Spacer(Modifier.height(20.dp))

            if (stage == Stage.Verify && biometricAvailable) {

                TextButton(
                    onClick = {
                        onComplete()
                    }
                ) {
                    Text("Use biometric instead")
                }
            }
        }
    }

    LaunchedEffect(pin) {

        if (pin.length == 6) {

            delay(150)

            when (stage) {

                Stage.Verify -> {

                    if (prefs.verifyPin(pin)) {

                        resetDots()

                        stage = Stage.Create
                        pin = ""
                        error = false

                    } else {

                        error = true
                        pin = ""
                        shake()
                    }
                }

                Stage.Create -> {

                    firstPin = pin

                    resetDots()

                    pin = ""
                    stage = Stage.Confirm
                }

                Stage.Confirm -> {

                    if (pin == firstPin) {

                        successBounce()

                        prefs.setPin(pin)
                        prefs.setEnabled(true)

                        delay(120)

                        onComplete()

                    } else {

                        error = true
                        pin = ""
                        shake()
                    }
                }
            }
        }
    }
}