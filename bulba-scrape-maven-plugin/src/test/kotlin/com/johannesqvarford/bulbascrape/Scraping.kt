package com.johannesqvarford.bulbascrape

import com.google.gson.*
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class BulbapediaScraperTest {

    @Test fun scrapeForExpansions_onFixedHtml_returnsExpansions() {
        val scraper = newScraper()

        val actualExpansionIds = scraper.scrapeForExpansionIds()
        val someExpectedExpansionIds = setOf(
            "Base Set",
            "Unified Minds"
        )

        assertTrue(actualExpansionIds.containsAll(someExpectedExpansionIds))
    }

    @Test fun scrapeForCards_onFixedBaseSetHtml_returnsBaseSetCards() {
        val scraper = newScraper()

        val actualCardIds: Set<CardId> = scraper.scrapeExpansionForCards("Base Set")
        val someExpectedCards = setOf(
            CardId(expansionId = "Base Set", index = 1, name = "Alakazam"),
            CardId(expansionId = "Base Set", index = 102, name = "Water Energy")
        )

        assertTrue(actualCardIds.containsAll(someExpectedCards))
    }

    @Test fun parseCardInfo_onFixedAlakazamHtml_returnsAlakazamCardInfo() {
        val scraper = newScraper()
        val cardId = CardId(expansionId = "Base Set", index = 1, name = "Alakazam")

        val cardInfo: CardInfo = scraper.parseCard(card)

        // Energy card info and Trainer card info is also available.
        assertEquals(expected = PokemonCardInfo(
            cardId = CardId(expansionId = "Base Set", index = 1, name = "Alakazam"),
            cardName = "Alakazam",
            evolutionStage = EvolutionStage2(evolvesFrom = "Kadabra"),
            typeId = TypeId.PSYCHIC,
            hitPoints = 80,

            weakness = MultiplierWeakness(typeId = TypeId.PSYCHIC, multiplier = 2),
            resistance = NoneResistance(),
            retreatCost = 3,

            // Need to choose expansion information based on passed in expansion Id.
            // The same url can refer to the "same" card in different expansions with different
            expansionId = "Base Set",
            rarity = Rarity.RareHolo,
            cardNumber = CardNumber(index = 1, expansionSize = 102),

            pokemonPower = PokemonPower(name = "Damage Swap",
                description = " As often as you like during your turn (before your attack), "
                        + "you may move 1 damage counter from 1 of your Pokémon to another as long as you don't Knock Out that Pokémon. "
                        + "This power can't be used if Alakazam is Asleep, Confused, or Paralyzed. "),
            moves = listOf(Move(
                moveCost = listOf(EnergyCost(amount = 3, typeId = TypeId.PSYCHIC)),
                damageIndicator = NeutralDamageIndicator(amount = 30)
            )),

            pokedexData = PokedexData(
                number = 65,
                height = MeterHeight(value = 1.5),
                weight = KilogramWeight(value = 48.0)
            )

        ), actual = cardInfo)
    }

    private fun newScraper(): Scraper = BulbapediaScraper(downloader = ResourcesBulbapediaDownloader(), cache = NoCache())
}

fun <T: Enum<T>> JsonElement.serializationType(f: (String) -> T): T {
    val o = this.asJsonObject
    val typeString = o.get("type").asString
    return f(typeString)
}

inline fun <reified T> JsonDeserializationContext.deserialize(src: JsonElement) = this.deserialize<T>(src, T::class.java)

enum class EvolutionStageType {
    BASE,
    STAGE1,
    STAGE2,
}

sealed class EvolutionStage(val type: EvolutionStageType, val evolvesFrom: String?) {
    fun toSumType() = when (type) {
        EvolutionStageType.BASE -> BaseEvolutionStage
        EvolutionStageType.STAGE1 -> EvolutionStage1(evolvesFrom!!)
        EvolutionStageType.STAGE2 -> EvolutionStage2(evolvesFrom!!)
    }
}

object BaseEvolutionStage : EvolutionStage(EvolutionStageType.BASE, evolvesFrom = null)
class EvolutionStage1(evolvesFrom: String): EvolutionStage(EvolutionStageType.STAGE1, evolvesFrom = evolvesFrom)
class EvolutionStage2(evolvesFrom: String): EvolutionStage(EvolutionStageType.STAGE2, evolvesFrom =  evolvesFrom)


enum class TypeId {
    COLORLESS,
    GRASS,
    FIRE,
    WATER,
    LIGHTNING,
    FIGHTING,
    PSYCHIC
}


data class CardNumber(val index: Int, val expansionSize: Int)

enum class Rarity {
    RareHolo,
    Rare,
    Uncommon,
    Common,
}

