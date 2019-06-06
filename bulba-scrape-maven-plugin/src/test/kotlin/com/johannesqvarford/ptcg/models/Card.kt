package com.johannesqvarford.ptcg.models

import kotlin.test.Test
import kotlin.test.assertEquals

class CardTest {
    @Test
    fun producesIdenticalTarget_whenRoundTripSerializing() {
        val expectedSources = listOf(
            PokemonCard(
                cardId = CardId(expansionId = "Base Set", index = 1, name = "Alakazam"),
                typeId = TypeId.PSYCHIC,
                cardNumber = CardNumber(expansionSize = 102),
                evolutionStage = EvolutionStage2(evolvesFrom = "Kadabra"),
                hitPoints = 80,
                moves = listOf(
                    Move(
                        moveCost = listOf(EnergyCost(amount = 3, typeId = TypeId.PSYCHIC)),
                        damageIndicator = NeutralDamageIndicator(amount = 30)
                    )
                ),
                pokedexData = PokedexData(
                    number = 65,
                    height = MeterHeight(value = 1.5),
                    weight = KilogramWeight(value = 48.0)
                ),
                pokemonPower = PokemonPower(
                    name = "Damage Swap",
                    description = " As often as you like during your turn (before your attack), "
                            + "you may move 1 damage counter from 1 of your Pokémon to another as long as you don't Knock Out that Pokémon. "
                            + "This power can't be used if Alakazam is Asleep, Confused, or Paralyzed. "
                ),
                rarity = Rarity.Rare,
                resistance = NoneResistance,
                retreatCost = 3,
                weakness = MultiplierWeakness(multiplier = 2, typeId = TypeId.PSYCHIC)
            )
        )

        val actualResults = expectedSources
            .map { roundTripJsonSerialize(it, CardSerializableConverter()) }
            .toList()

        assertEquals(actual = actualResults, expected = expectedSources)
    }
}
