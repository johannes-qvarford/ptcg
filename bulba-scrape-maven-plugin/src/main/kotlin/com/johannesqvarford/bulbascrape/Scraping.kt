package com.johannesqvarford.bulbascrape

import com.google.gson.Gson
import java.util.regex.Matcher
import java.util.regex.Pattern

private fun Matcher.listOfGroups(): Sequence<List<String>> {
    val matcher = this
    return sequence {
        while (matcher.find()) {
            yield(matcher.groups().toList())
        }
    }
}

private fun Matcher.groups(): Sequence<String> {
    val matcher = this
    return sequence {
        for (i in 0..matcher.groupCount()) {
            yield(matcher.group(i))
        }
    }
}

data class ExpansionId(val name: String)
data class ExpansionsJsonRoot(val expansionIds: Set<ExpansionId>)
data class CardId(val expansionId: String, val index: Int, val name: String)
data class ExpansionJsonRoot(val cardIds: Set<CardId>)

interface Scraper {
    fun scrapeForExpansionIds(): Set<String>
    fun scrapeExpansionForCards(expansion: String): Set<CardId>
}

data class BulbapediaScraper(
    val downloader: BulbapediaDownloader,
    val cache: Cache) : Scraper {

    override fun scrapeForExpansionIds(): Set<String> {
        val cacheId = "metadata/expansionIds.json"
        val gson = Gson()

        val bytes = cache.computeIfAbsent(cacheId) {
            val html = downloader.downloadExpansionsDocument()

            val expansionPattern = Pattern.compile("""<td> <a href="/wiki/([^"]*)_\(TCG\)""")

            val matcher = expansionPattern.matcher(html)
            val expansions = mutableSetOf<ExpansionId>()

            while (matcher.find()) {
                expansions.add(ExpansionId(name = matcher.group(1).fromBulpapedia()))
            }

            gson.toJson(ExpansionsJsonRoot(expansionIds = expansions)).toUtf8ByteArray()
        }
        return gson.fromJson(bytes.toUtf8String(), ExpansionsJsonRoot::class.java).expansionIds.map { it.name }.toSet()
    }

    override fun scrapeExpansionForCards(expansion: String): Set<CardId> {
        val cacheId = "expansions/$expansion.json"
        val gson = Gson()
        val bytes = cache.computeIfAbsent(cacheId) {
            val html = downloader.downloadExpansionDocument(expansion)

            val cardP = "([^\"]+)"
            val indexP = "([0-9]+)"
            val expansionP = expansion.toBulbapedia()
            val cardPattern = Pattern.compile("<a href=\"/wiki/${cardP}_\\(${expansionP}_$indexP\\)")

            val matcher = cardPattern.matcher(html)
            val cards = matcher.listOfGroups()
                .map { CardId(expansionId = expansion, index = it[2].toInt(), name = it[1].fromBulpapedia() ) }
                .toList()

            gson.toJson(ExpansionJsonRoot(cardIds = cards.toSet())).toUtf8ByteArray()
        }
        return gson.fromJson(bytes.toUtf8String(), ExpansionJsonRoot::class.java).cardIds
    }
}




