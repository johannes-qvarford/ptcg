package com.johannesqvarford.ptcg.bulbascrape

import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.util.*
import kotlin.collections.HashMap
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class MapCacheTest {

    @Test fun computeIfPresent_returnsExistingValue_ifAlreadyExistsInOriginalMap() {
        val v = byteArrayOf(10)
        val map = mutableMapOf("id" to v)
        val cache = MapCache(map)

        val actual = cache.computeIfAbsent("id") { byteArrayOf(11) }

        assertEquals(expected = v, actual = actual)
    }

    @Test fun computeIfPresent_returnsComputedValue_ifNotExistsInOriginalMap() {
        val new = byteArrayOf(10)
        val map = HashMap<String, ByteArray>()
        val cache = MapCache(map)

        val actual = cache.computeIfAbsent("new"){ new }

        assertEquals(expected = new, actual = actual)
    }

    @Test fun computeIfPresent_modifiesOriginalMap_ifValueIsComputed() {
        val new = byteArrayOf(10)
        val map = HashMap<String, ByteArray>()
        val cache = MapCache(map)

        cache.computeIfAbsent("new") { new }
        val actual = map["new"]

        assertNotNull(actual)
        assertEquals(expected = new, actual = actual)
    }
}

class FilesystemCacheTest {
    @Test fun computeIfPresent_returnsFileContent_ifFileCanBeRead() {
        val directory = temporaryDirectory()
        val fileContent = byteArrayOf(10)
        writeTemporaryFile(directory, Paths.get("file"), fileContent)
        val cache = FilesystemCache(directory)

        val actual = cache.computeIfAbsent("file") { byteArrayOf(11) }

        assertTrue(Arrays.equals(fileContent, actual))
    }

    @Test fun computeIfPresent_returnsComputedValue_ifFileCannotBeRead() {
        val directory = temporaryDirectory()
        val fileContent = byteArrayOf(10)
        val computed = byteArrayOf(11)
        writeTemporaryFile(directory, Paths.get("file"), fileContent)
        val cache = FilesystemCache(directory)

        val actual = cache.computeIfAbsent("non-existant-file") { computed }

        assertTrue(Arrays.equals(computed, actual))
    }

    @Test fun computeIfPresent_createsFileWithComputedValue_ifFileCannotBeRead() {
        val directory = temporaryDirectory()
        val computed = byteArrayOf(11)
        val cache = FilesystemCache(directory)

        cache.computeIfAbsent("new-file") { computed }
        val actual = directory.resolve("new-file").toFile().readBytes()

        assertTrue(Arrays.equals(computed, actual))
    }

    private fun temporaryDirectory() = Files.createTempDirectory("FilesystemCacheTest")

    private fun writeTemporaryFile(directory: Path, filename: Path, content: ByteArray) {
        val p = Files.createFile(directory.resolve(filename))
        val f = p.toFile()
        f.writeBytes(content)
    }
}
