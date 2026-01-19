package dev.swabodha.life.settings.data

import android.content.Context

class FeatureTogglePrefs(context: Context) {

    private val prefs =
        context.getSharedPreferences("feature_prefs", Context.MODE_PRIVATE)

    fun isEnabled(featureId: String, default: Boolean = true): Boolean =
        prefs.getBoolean(featureId, default)

    fun setEnabled(featureId: String, enabled: Boolean) {
        prefs.edit().putBoolean(featureId, enabled).apply()
    }
}
