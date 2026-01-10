package dev.swabodha.life.core.features.registry

import dev.swabodha.life.core.features.FeatureEntry

object FeatureRegistry {

    private val features = mutableListOf<FeatureEntry>()

    fun register(feature: FeatureEntry) {
        features += feature
    }

    fun all(): List<FeatureEntry> = features

    fun enabledDescriptors() =
        features.map { it.descriptor() }.filter { it.enabled }
}
