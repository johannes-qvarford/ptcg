package com.johannesqvarford.ptcg.models

import kotlin.test.Test
import kotlin.test.assertEquals

class EvolutionStageTest {
    @Test
    fun producesIdenticalTarget_whenRoundTripSerializing() {
        val expectedSources = listOf(
            BaseEvolutionStage as EvolutionStage,
            EvolutionStage1(evolvesFrom = "Abra"),
            EvolutionStage2(evolvesFrom = "Kadabra")
        )

        val actualResults = expectedSources
            .map {
                roundTripJsonSerialize(
                    it,
                    EvolutionStageSerializableConverter()
                )
            }
            .toList()

        assertEquals(expected = expectedSources, actual = actualResults)
    }
}
