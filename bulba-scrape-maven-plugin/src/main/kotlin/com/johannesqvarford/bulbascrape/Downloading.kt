package com.johannesqvarford.bulbascrape

import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.util.*

private const val WIKI_URL = "https://bulbapedia.bulbagarden.net/wiki"

interface BulbapediaDownloader {
    fun downloadExpansionsDocument(): String

    fun downloadExpansionDocument(expansion: String): String

    fun downloadCardDocument(expansion: String, index: Int, name: String): String

    fun downloadResource(url: String): ByteArray
}

data class WebBulbapediaDownloader(val client: HttpClient, val cache: Cache) : BulbapediaDownloader {

    override fun downloadExpansionsDocument() = getWiki("List_of_Pok%C3%A9mon_Trading_Card_Game_expansions")

    override fun downloadExpansionDocument(expansion: String) = getWiki("${expansion}_(TCG)")

    override fun downloadCardDocument(expansion: String, index: Int, name: String) = getWiki("${name}_(${expansion}_$index)")

    private fun getWiki(relativePath: String): String = String(get("$WIKI_URL/$relativePath"), Charsets.UTF_8)

    override fun downloadResource(url: String): ByteArray = get(url)

    fun get(url: String): ByteArray {
        val base64Url = Base64.getUrlEncoder().encodeToString(url.toByteArray(Charsets.UTF_8))
        val cacheId = "bulbapedia/$base64Url"
        return cache.computeIfAbsent(cacheId) {
            val request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build()

            val response = client.send(request, HttpResponse.BodyHandlers.ofByteArray())
            response.body()
        }
    }
}
