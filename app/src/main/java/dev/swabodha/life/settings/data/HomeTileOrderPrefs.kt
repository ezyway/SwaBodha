package dev.swabodha.life.settings.data

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

private val Context.dataStore by preferencesDataStore(name = "home_tile_prefs")

class HomeTileOrderPrefs(private val context: Context) {

    private val ORDER_KEY = stringPreferencesKey("home_tile_order")

    fun getOrder(): List<String> = runBlocking {
        val prefs = context.dataStore.data.first()
        prefs[ORDER_KEY]
            ?.split(",")
            ?.filter { it.isNotBlank() }
            ?: emptyList()
    }

    suspend fun setOrder(ids: List<String>) {
        context.dataStore.edit { prefs ->
            prefs[ORDER_KEY] = ids.joinToString(",")
        }
    }
}
