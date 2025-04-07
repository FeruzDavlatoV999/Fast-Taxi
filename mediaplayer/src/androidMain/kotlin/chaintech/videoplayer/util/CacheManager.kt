package chaintech.videoplayer.util

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import androidx.media3.common.util.UnstableApi
import androidx.media3.database.DefaultDatabaseProvider
import androidx.media3.datasource.cache.LeastRecentlyUsedCacheEvictor
import androidx.media3.datasource.cache.SimpleCache
import java.io.File


@UnstableApi
internal object CacheManager {
    private var cache: SimpleCache? = null
    private var databaseProvider: DefaultDatabaseProvider? = null

    @Synchronized
    fun getCache(context: Context): SimpleCache {
        if (cache == null) {
            val cacheSize = 100 * 1024 * 1024 // 100 MB
            val cacheDir = File(context.cacheDir, "video_cache").apply {
                if (!exists()) mkdirs()
            }
            // Initialize DefaultDatabaseProvider
            if (databaseProvider == null) {
                databaseProvider = DefaultDatabaseProvider(createSQLiteOpenHelper(context))
            }
            cache = SimpleCache(
                cacheDir,
                LeastRecentlyUsedCacheEvictor(cacheSize.toLong()),
                databaseProvider!!
            )
        }
        return cache!!
    }

    fun release() {
        try {
            cache?.release()
        } catch (_: Exception) {

        } finally {
            cache = null // Allow garbage collection
            databaseProvider = null // Allow garbage collection
        }
    }

    private fun createSQLiteOpenHelper(context: Context): SQLiteOpenHelper {
        return object : SQLiteOpenHelper(
            context,
            "media3_cache.db",
            null,
            1
        ) {
            override fun onCreate(db: SQLiteDatabase?) {
                // Called when the database is created for the first time.
            }

            override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
                // Handle database schema upgrades if needed.
            }
        }
    }

}