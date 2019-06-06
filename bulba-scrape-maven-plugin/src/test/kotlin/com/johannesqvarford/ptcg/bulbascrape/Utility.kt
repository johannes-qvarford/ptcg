package com.johannesqvarford.ptcg.bulbascrape

class NoCache : Cache {
    override fun computeIfAbsent(id: String, compute: () -> ByteArray): ByteArray {
        return compute()
    }
}

class ResourcesBulbapediaDownloader : BulbapediaDownloader {

    override fun downloadExpansionsDocument() =
        readFromClasspath("wiki/List_of_Pok%C3%A9mon_Trading_Card_Game_expansions").toUtf8String()

    override fun downloadExpansionDocument(expansion: String) =
        readFromClasspath("wiki/Base_Set_(TCG)").toUtf8String()

    override fun downloadCardDocument(expansion: String, index: Int, name: String) =
        readFromClasspath("wiki/Alakazam (Base Set 1)").toUtf8String()

    override fun downloadResource(url: String) =
        readFromClasspath("cdn/180px-AlakazamBaseSet1.jpg")

    private fun readFromClasspath(path: String): ByteArray {
        return this::class.java.classLoader.getResourceAsStream(path)
            .use { it.readAllBytes() }
    }
}

