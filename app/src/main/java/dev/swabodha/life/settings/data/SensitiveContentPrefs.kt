package dev.swabodha.life.settings.data

import android.content.Context
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class SensitiveContentPrefs private constructor(
    private val context: Context
) {

    companion object {
        private const val PREF_NAME = "privacy_prefs"
        private const val KEY_HIDE_SENSITIVE = "hide_sensitive"

        @Volatile
        private var INSTANCE: SensitiveContentPrefs? = null

        fun get(context: Context): SensitiveContentPrefs {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: SensitiveContentPrefs(
                    context.applicationContext
                ).also { INSTANCE = it }
            }
        }
    }

    private val prefs =
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    private val _enabled =
        MutableStateFlow(prefs.getBoolean(KEY_HIDE_SENSITIVE, false))

    val enabled: StateFlow<Boolean> = _enabled

    fun setEnabled(value: Boolean) {
        prefs.edit().putBoolean(KEY_HIDE_SENSITIVE, value).apply()
        _enabled.value = value
    }

    fun isEnabled(): Boolean {
        return prefs.getBoolean(KEY_HIDE_SENSITIVE, false)
    }
}