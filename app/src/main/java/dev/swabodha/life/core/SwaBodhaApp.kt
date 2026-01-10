package dev.swabodha.life.core

import android.app.Application
import dev.swabodha.life.core.features.registry.FeatureRegistry
import dev.swabodha.life.features.water.WaterFeatureEntry

import dev.swabodha.life.core.database.DatabaseInitializer
import dev.swabodha.life.features.weight.WeightFeatureEntry

class SwabodhaApp : Application() {

    override fun onCreate() {
        super.onCreate()

        DatabaseInitializer.init(this)

        // FeatureRegistry.register(FinanceFeatureEntry())
        FeatureRegistry.register(WaterFeatureEntry())
        FeatureRegistry.register(WeightFeatureEntry())

    }
}
