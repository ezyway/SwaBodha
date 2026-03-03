package dev.swabodha.life.core.security

import android.content.Context
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class AppLockManager(
    private val context: Context
) : DefaultLifecycleObserver {

    private val prefs = AppLockPrefs.get(context)
    private var backgroundTimestamp: Long = 0L

    private val _locked = MutableStateFlow(false)
    val locked: StateFlow<Boolean> = _locked

    override fun onStop(owner: LifecycleOwner) {
        backgroundTimestamp = System.currentTimeMillis()
    }

    override fun onStart(owner: LifecycleOwner) {
        if (!prefs.isEnabled()) return

        val timeout = prefs.getTimeoutSeconds() * 1000L
        val now = System.currentTimeMillis()

        if (now - backgroundTimestamp > timeout) {
            _locked.value = true
        }
    }

    fun unlock() {
        _locked.value = false
    }
}