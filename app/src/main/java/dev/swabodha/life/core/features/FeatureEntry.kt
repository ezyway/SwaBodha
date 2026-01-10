package dev.swabodha.life.core.features

import androidx.navigation.NavGraphBuilder

interface FeatureEntry {
    fun descriptor(): FeatureDescriptor
    fun registerNavGraph(builder: NavGraphBuilder)
}
