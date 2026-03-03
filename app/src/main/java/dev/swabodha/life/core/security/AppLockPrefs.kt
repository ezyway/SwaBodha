package dev.swabodha.life.core.security

import android.content.Context
import java.security.MessageDigest
import kotlin.apply

class AppLockPrefs private constructor(
    private val context: Context
) {

    companion object {
        private const val PREF = "app_lock_prefs"
        private const val KEY_ENABLED = "enabled"
        private const val KEY_PIN_HASH = "pin_hash"
        private const val KEY_TIMEOUT = "timeout_minutes"
        private const val KEY_TIMEOUT_SECONDS = "timeout_seconds"

        @Volatile
        private var INSTANCE: AppLockPrefs? = null

        fun get(context: Context): AppLockPrefs {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: AppLockPrefs(
                    context.applicationContext
                ).also { INSTANCE = it }
            }
        }
    }

    private val prefs =
        context.getSharedPreferences(PREF, Context.MODE_PRIVATE)

    fun isEnabled() = prefs.getBoolean(KEY_ENABLED, false)

    fun setEnabled(value: Boolean) {
        prefs.edit().putBoolean(KEY_ENABLED, value).apply()
    }

    fun setPin(pin: String) {
        prefs.edit().putString(KEY_PIN_HASH, sha256(pin)).apply()
    }

    fun verifyPin(pin: String): Boolean {
        val saved = prefs.getString(KEY_PIN_HASH, null) ?: return false
        return saved == sha256(pin)
    }

    fun hasPin(): Boolean {
        return prefs.getString(KEY_PIN_HASH, null) != null
    }

    fun setTimeoutSeconds(seconds: Int) {
        prefs.edit().putInt(KEY_TIMEOUT_SECONDS, seconds).apply()
    }

    fun getTimeoutSeconds(): Int {
        return prefs.getInt(KEY_TIMEOUT_SECONDS, 60)
    }

    private fun sha256(text: String): String {
        val bytes = MessageDigest.getInstance("SHA-256")
            .digest(text.toByteArray())
        return bytes.joinToString("") { "%02x".format(it) }
    }
}