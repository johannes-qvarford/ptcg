package com.johannesqvarford.bulbascrape

import kotlin.test.Test
import kotlin.test.assertTrue

class BulbapediaScraperTest {

    @Test fun scrapeForExpansions_onFixedHtml_returnsExpansions() {
        val scraper = newScraper()

        val root = scraper.scrapeForExpansions()
        val someExpansions = setOf(
            Expansion(name = "Base Set"),
            Expansion(name = "Unified Minds")
        )

        assertTrue(root.expansions.toSet().containsAll(someExpansions))
    }

    @Test fun scrapeForCards_onFixedBaseSetHtml_returnsBaseSetCards() {
        val scraper = newScraper()
        val expansionsRoot = ExpansionsRoot(
            expansions = setOf(Expansion(name = "Base Set"))
        )

        val roots: Set<CardsRoot> = scraper.scrapeForCards(expansionsRoot)
        val someCards = setOf(
            Card(expansion = "Base Set", index = 1, name = "Alakazam"),
            Card(expansion = "Base Set", index = 102, name = "Water Energy")
        )

        val cards: Set<Card> = roots.firstOrNull()!!.cards
        assertTrue(cards.toSet().containsAll(someCards))
    }

    private fun newScraper(): Scraper = BulbapediaScraper(downloader = ResourcesBulbapediaDownloader(), cache = NoCache())
}

