package com.johannesqvarford.bulbascrape

import com.google.gson.Gson
import java.util.regex.Pattern

data class Expansion(val name: String)

data class ExpansionsRoot(val expansions: List<Expansion>)

interface Scraper {
    fun scrapeForExpansions(): ExpansionsRoot
}

data class BulbapediaScraper(
    val downloader: BulbapediaDownloader,
    val cache: Cache) : Scraper {

    override fun scrapeForExpansions(): ExpansionsRoot {
        val cacheId = "metadata/expansions.json"
        val gson = Gson()

        val bytes = cache.computeIfAbsent(cacheId) {
            val html = downloader.downloadExpansionsDocument()

            val expansionPattern = Pattern.compile("""<td> <a href="/wiki/([^"]*)_\(TCG\)""")

            val matcher = expansionPattern.matcher(html)
            val expansions = mutableListOf<Expansion>()

            while (matcher.find()) {
                expansions.add(Expansion(name = matcher.group(1)))
            }

            gson.toJson(ExpansionsRoot(expansions = expansions)).toUtf8ByteArray()
        }
        return gson.fromJson(bytes.toUtf8String(), ExpansionsRoot::class.java)
    }
}
