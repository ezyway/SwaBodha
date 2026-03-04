package dev.swabodha.life.core.security.ui.components

import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.fragment.app.FragmentActivity

@Composable
fun BiometricButton(onSuccess: () -> Unit) {

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