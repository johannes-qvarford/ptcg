package com.johannesqvarford.bulbascrape

import kotlin.test.Test
import kotlin.test.assertTrue

class BulbapediaScraperTest {

    @Test fun scrapeForExpansions_onFixedHtml_returnsExpansions() {
        val scraper = BulbapediaScraper(downloader = ResourcesBulbapediaDownloader(), cache = NoCache())

        val root = scraper.scrapeForExpansions()
        val someExpansions = setOf(
            Expansion(name = "Base_Set"),
            Expansion(name = "Unified_Minds")
        )

        assertTrue(root.expansions.toSet().containsAll(someExpansions))
    }
}
