package com.johannesqvarford.bulbascrape

import java.io.IOException
import java.nio.file.Path

interface Cache {
    fun computeIfAbsent(id: String, compute: () -> ByteArray): ByteArray
}

data class FilesystemCache(private val rootDirectory: Path) : Cache {
    override fun computeIfAbsent(id: String, compute: () -> ByteArray): ByteArray {
        val f = rootDirectory.resolve(id).toFile()
        try {
            return f.readBytes()
        } catch (e: IOException) {
            // assume file didn't exist.
        }

        val computed = compute()
        ensureRootDirectoryExists()
        f.parentFile.mkdirs()
        f.writeBytes(computed)

        return computed
    }

    private fun ensureRootDirectoryExists() {
        val f = rootDirectory.toFile()
        if (!f.mkdirs() && !f.isDirectory) {
            throw IOException("Could not create root directory '$rootDirectory'")
        }
    }
}

data class MapCache(private val map: MutableMap<String, ByteArray>) : Cache {

    override fun computeIfAbsent(id: String, compute: () -> ByteArray): ByteArray {
        val v = map[id] ?: compute()
        map[id] = v
        return v
    }
}
