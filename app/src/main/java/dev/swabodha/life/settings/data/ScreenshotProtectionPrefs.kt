package dev.swabodha.life.settings.data

import android.content.Context
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class ScreenshotProtectionPrefs(context: Context) {

    private val prefs =
        context.getSharedPreferences("privacy_prefs", Context.MODE_PRIVATE)

    private val _enabled =
        MutableStateFlow(prefs.getBoolean(KEY, false))

    val enabled: StateFlow<Boolean> = _enabled

    fun setEnabled(value: Boolean) {
        prefs.edit().putBoolean(KEY, value).apply()
        _enabled.value = value
    }

    companion object {
        private const val KEY = "screenshot_protection_enabled"
    }
}
