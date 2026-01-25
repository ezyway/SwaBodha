package dev.swabodha.life.settings.ui

import android.content.Context
import android.os.Build
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

@Composable
fun appVersionSubtitle(): String {
    val context = LocalContext.current
    return rememberAppVersion(context)
}

private fun rememberAppVersion(context: Context): String {
    val pm = context.packageManager
    val packageName = context.packageName

    val packageInfo = if (Build.VERSION.SDK_INT >= 33) {
        pm.getPackageInfo(
            packageName,
            android.content.pm.PackageManager.PackageInfoFlags.of(0)
        )
    } else {
        @Suppress("DEPRECATION")
        pm.getPackageInfo(packageName, 0)
    }

    val versionName = packageInfo.versionName ?: "?"
    val versionCode = if (Build.VERSION.SDK_INT >= 28) {
        packageInfo.longVersionCode
    } else {
        @Suppress("DEPRECATION")
        packageInfo.versionCode.toLong()
    }

    return "v$versionName (build $versionCode)"
}
