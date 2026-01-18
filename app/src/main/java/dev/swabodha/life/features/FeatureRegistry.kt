package dev.swabodha.life.features

import android.content.Context
import dev.swabodha.life.core.features.FeatureEntry
import dev.swabodha.life.settings.data.FeaturePrefs

object FeatureRegistry {

    private val features = mutableListOf<FeatureEntry>()

    fun register(feature: FeatureEntry) {
        features += feature
    }

    fun all(): List<FeatureEntry> = features

    fun enabledDescriptors(context: Context) =
        features
            .map { it.descriptor() }
            .filter { descriptor ->
                FeaturePrefs(context).isEnabled(descriptor.id, descriptor.enabled)
            }

}