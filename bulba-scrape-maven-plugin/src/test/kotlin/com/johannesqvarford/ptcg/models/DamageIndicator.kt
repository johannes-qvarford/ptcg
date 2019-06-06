package com.johannesqvarford.ptcg.models

import kotlin.test.Test
import kotlin.test.assertEquals

class DamageIndicatorTest {
    @Test
    fun producesIdenticalTarget_whenRoundTripSerializing() {
        val expectedSources = listOf(
            NoneDamageIndicator,
            NeutralDamageIndicator(amount = 10),
            PositiveDamageIndicator(amount = 20),
            NegativeDamageIndicator(amount = 30),
            MultiplierDamageIndicator(multiplier = 2)
        )

        val actualResults = expectedSources
            .map { roundTripJsonSerialize(it, DamageIndicatorSerializableConverter()) }
            .toList()

        assertEquals(actual = actualResults, expected = expectedSources)
    }
}
