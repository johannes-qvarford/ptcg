package com.johannesqvarford.ptcg.models

import kotlin.test.Test
import kotlin.test.assertEquals

class MoveTest {

    @Test
    fun identicalResults_whenRoundTripSerializing() {
        val expectedSources = listOf(
            Move(
                moveCost = listOf(EnergyCost(amount = 1, typeId = TypeId.FIGHTING)),
                damageIndicator = NoneDamageIndicator
            ),
            Move(
                moveCost = listOf(EnergyCost(amount = 2, typeId = TypeId.COLORLESS)),
                damageIndicator = MultiplierDamageIndicator(multiplier = 2)
            )
        )

        val actualResults = expectedSources
            .map { roundTripJsonSerialize(it, MoveSerializableConverter()) }
            .toList()

        assertEquals(actual = actualResults, expected = expectedSources)
    }
}
