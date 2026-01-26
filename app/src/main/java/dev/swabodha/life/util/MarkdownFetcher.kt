package dev.swabodha.life.util

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.IOException

private val markdownHttpClient by lazy {
    OkHttpClient()
}

suspend fun fetchMarkdown(url: String): String {
    return withContext(Dispatchers.IO) {
        val request = Request.Builder()
            .url(url)
            .build()

        markdownHttpClient
            .newCall(request)
            .execute()
            .use { response ->
                if (!response.isSuccessful) {
                    throw IOException("HTTP ${response.code} for $url")
                }
                response.body?.string().orEmpty()
            }
    }
}
