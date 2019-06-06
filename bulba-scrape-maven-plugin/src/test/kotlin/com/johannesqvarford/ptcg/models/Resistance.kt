package com.johannesqvarford.ptcg.models

import kotlin.test.Test
import kotlin.test.assertEquals

class ResistanceTest {

    @Test
    fun identicalResults_whenRoundTripSerializing() {
        val expectedSources = listOf(
            NoneResistance,
            AmountResistance(amount = -30)
        )

        val actualResults = expectedSources
            .map { roundTripJsonSerialize(it, ResistanceSerializableConverter()) }
            .toList()

        assertEquals(actual = actualResults, expected = expectedSources)
    }
}
