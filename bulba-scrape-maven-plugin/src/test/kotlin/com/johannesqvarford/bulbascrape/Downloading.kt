package com.johannesqvarford.bulbascrape

import java.net.http.HttpClient
import kotlin.test.Test
import kotlin.test.assertTrue

class WebBulbapediaDownloaderTest {

    @Test fun downloadExpansionsDocument_containsBaseSetExpansionLink() {
        val downloader = newDownloader()

        val document = downloader.downloadExpansionsDocument()

        assertTrue(document.contains("/wiki/Base_Set_(TCG)"))
    }

    @Test fun downloadExpansionDocument_forBaseSet_containsAlakazamCardLink() {
        val downloader = newDownloader()

        val document: String = downloader.downloadExpansionDocument("Base_Set")

        assertTrue(document.contains("/wiki/Alakazam_(Base_Set_1)"))
    }

    @Test fun downloadCardDocument_forAlakazamBaseSet1_containsImageLink() {
        val downloader = newDownloader()

        val document: String = downloader.downloadCardDocument(name = "Alakazam", expansion = "Base_Set", index = 1)

        assertTrue(document.contains("/wiki/File:AlakazamBaseSet1.jpg"))
    }

    // https://cdn.bulbagarden.net/upload/thumb/9/94/AlakazamBaseSet1.jpg/180px-AlakazamBaseSet1.jpg
    @Test fun downloadResource_forAlakazamBaseSet1Image_downloadsImage() {
        val downloader = newDownloader()

        val resource: ByteArray = downloader.downloadResource(
            url = "https://cdn.bulbagarden.net/upload/thumb/9/94/AlakazamBaseSet1.jpg/180px-AlakazamBaseSet1.jpg")

        assertTrue(resource.count() > 0)
    }

    private fun newDownloader(): BulbapediaDownloader {
        return WebBulbapediaDownloader(HttpClient.newHttpClient(), NoCache())
    }
}
