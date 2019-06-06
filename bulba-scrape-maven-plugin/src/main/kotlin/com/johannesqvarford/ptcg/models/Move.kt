package com.johannesqvarford.ptcg.models

data class Move(val moveCost: List<EnergyCost>, val damageIndicator: DamageIndicator)

data class MoveJsonObject(val moveCost: List<EnergyCost>, val damageIndicator: DamageIndicatorJsonObject)

class MoveSerializableConverter : JsonSerializableConverter<MoveJsonObject, Move> {
    private val damageIndicatorSerializableConverter = DamageIndicatorSerializableConverter()

    override fun toType(s: MoveJsonObject): Move {
        return Move(
            moveCost = s.moveCost,
            damageIndicator = damageIndicatorSerializableConverter.toType(s.damageIndicator)
        )
    }

    override fun toJsonSerializable(t: Move): MoveJsonObject {
        return MoveJsonObject(
            moveCost = t.moveCost,
            damageIndicator = damageIndicatorSerializableConverter.toJsonSerializable(t.damageIndicator)
        )
    }
}
