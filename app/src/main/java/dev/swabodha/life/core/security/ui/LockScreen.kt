package dev.swabodha.life.core.security.ui


import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.fragment.app.FragmentActivity
import dev.swabodha.life.core.security.AppLockPrefs

@Composable
fun LockScreen(
    onUnlock: () -> Unit
) {

    val context = LocalContext.current
    val prefs = remember { AppLockPrefs.get(context) }

    var pin by remember { mutableStateOf("") }
    var error by remember { mutableStateOf(false) }

    Surface(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize()
        ) {

            Text("App Locked")

            Spacer(Modifier.height(16.dp))

            OutlinedTextField(
                value = pin,
                onValueChange = {
                    if (it.length <= 6) pin = it
                },
                label = { Text("Enter PIN") },
                isError = error
            )

            Spacer(Modifier.height(16.dp))

            Button(onClick = {
                if (prefs.verifyPin(pin)) {
                    error = false
                    onUnlock()
                } else {
                    error = true
                }
            }) {
                Text("Unlock")
            }

            Spacer(Modifier.height(16.dp))

            BiometricButton(onSuccess = onUnlock)
        }
    }
}

@Composable
private fun BiometricButton(onSuccess: () -> Unit) {

    val context = LocalContext.current
    val activity = context as FragmentActivity

    val manager = BiometricManager.from(context)

    if (manager.canAuthenticate(
            BiometricManager.Authenticators.BIOMETRIC_STRONG
        ) == BiometricManager.BIOMETRIC_SUCCESS
    ) {

        Button(onClick = {

            val prompt = BiometricPrompt(
                activity,
                activity.mainExecutor,
                object : BiometricPrompt.AuthenticationCallback() {
                    override fun onAuthenticationSucceeded(
                        result: BiometricPrompt.AuthenticationResult
                    ) {
                        onSuccess()
                    }
                }
            )

            val info = BiometricPrompt.PromptInfo.Builder()
                .setTitle("Unlock App")
                .setSubtitle("Authenticate to continue")
                .setAllowedAuthenticators(
                    BiometricManager.Authenticators.BIOMETRIC_STRONG or
                            BiometricManager.Authenticators.DEVICE_CREDENTIAL
                )
                .build()

            prompt.authenticate(info)

        }) {
            Text("Use Biometrics")
        }
    }
}