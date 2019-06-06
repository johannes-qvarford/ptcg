package com.johannesqvarford.ptcg.models

import kotlin.test.Test
import kotlin.test.assertEquals

class WeaknessTest {

    @Test
    fun producesIdenticalResult_whenRoundTripSerializing() {
        val expectedSources = listOf(
            MultiplierWeakness(multiplier = 2, typeId = TypeId.PSYCHIC),
            NoneWeakness
        )

        val actualResults = expectedSources
            .map { roundTripJsonSerialize(it, WeaknessSerializableConverter()) }
            .toList()

        assertEquals(actual = actualResults, expected = expectedSources)
    }
}


