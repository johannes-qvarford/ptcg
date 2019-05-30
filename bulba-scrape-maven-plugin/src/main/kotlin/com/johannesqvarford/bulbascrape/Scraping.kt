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

data class Expansion(val name: String)
data class ExpansionsRoot(val expansions: Set<Expansion>)
data class Card(val expansion: String, val index: Int, val name: String)
data class CardsRoot(val cards: Set<Card>)

interface Scraper {
    fun scrapeForExpansions(): ExpansionsRoot
    fun scrapeForCards(root: ExpansionsRoot): Set<CardsRoot>
}

data class BulbapediaScraper(
    val downloader: BulbapediaDownloader,
    val cache: Cache) : Scraper {
    override fun scrapeForCards(root: ExpansionsRoot): Set<CardsRoot> {
         return root.expansions.map{it.name}.map {expansion ->
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
                    .map { Card(expansion = expansion, index = it[2].toInt(), name = it[1].fromBulpapedia() ) }
                    .toList()

                gson.toJson(CardsRoot(cards = cards.toSet())).toUtf8ByteArray()
            }
            gson.fromJson(bytes.toUtf8String(), CardsRoot::class.java)
        }.toSet()
    }

    override fun scrapeForExpansions(): ExpansionsRoot {
        val cacheId = "metadata/expansions.json"
        val gson = Gson()

        val bytes = cache.computeIfAbsent(cacheId) {
            val html = downloader.downloadExpansionsDocument()

            val expansionPattern = Pattern.compile("""<td> <a href="/wiki/([^"]*)_\(TCG\)""")

            val matcher = expansionPattern.matcher(html)
            val expansions = mutableSetOf<Expansion>()

            while (matcher.find()) {
                expansions.add(Expansion(name = matcher.group(1).fromBulpapedia()))
            }

            gson.toJson(ExpansionsRoot(expansions = expansions)).toUtf8ByteArray()
        }
        return gson.fromJson(bytes.toUtf8String(), ExpansionsRoot::class.java)
    }
}




