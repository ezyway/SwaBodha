package dev.swabodha.life.settings.data

import android.content.Context
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class ThemePrefs private constructor(context: Context) {

    private val prefs =
        context.getSharedPreferences("appearance_prefs", Context.MODE_PRIVATE)

    private val _mode =
        MutableStateFlow(load())

    val mode: StateFlow<ThemeMode> = _mode

    fun setMode(mode: ThemeMode) {
        prefs.edit().putString(KEY, mode.name).apply()
        _mode.value = mode
    }

    private fun load(): ThemeMode {
        val saved = prefs.getString(KEY, ThemeMode.SYSTEM.name)
        return ThemeMode.valueOf(saved!!)
    }

    companion object {

        private const val KEY = "theme_mode"

        @Volatile
        private var INSTANCE: ThemePrefs? = null

        fun get(context: Context): ThemePrefs {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: ThemePrefs(context.applicationContext)
                    .also { INSTANCE = it }
            }
        }
    }
}
