package dev.swabodha.life.features

import android.content.Context
import dev.swabodha.life.core.features.FeatureEntry
import dev.swabodha.life.settings.data.FeaturePrefs
import dev.swabodha.life.settings.data.HomeTileOrderPrefs

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

    fun enabledDescriptorsOrdered(context: Context) =
        run {
            val enabled = enabledDescriptors(context)
            val order = HomeTileOrderPrefs(context).getOrder()

            val orderMap = order.withIndex().associate { it.value to it.index }

            enabled.sortedWith(
                compareBy { orderMap[it.id] ?: Int.MAX_VALUE }
            )
        }
}