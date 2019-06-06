package com.johannesqvarford.ptcg.models

sealed class Resistance
object NoneResistance : Resistance()
data class AmountResistance(val amount: Int) : Resistance()

enum class ResistanceType { NONE, AMOUNT }
data class ResistanceJsonObject(val type: ResistanceType, val amount: Int? = null)

class ResistanceSerializableConverter :
    JsonSerializableConverter<ResistanceJsonObject, Resistance> {
    override fun toType(s: ResistanceJsonObject): Resistance {
        return when (s.type) {
            ResistanceType.NONE -> NoneResistance
            ResistanceType.AMOUNT -> AmountResistance(
                s.amount!!
            )
        }
    }

    override fun toJsonSerializable(t: Resistance): ResistanceJsonObject {
        return when (t) {
            is NoneResistance -> ResistanceJsonObject(
                type = ResistanceType.NONE
            )
            is AmountResistance -> ResistanceJsonObject(
                type = ResistanceType.AMOUNT,
                amount = t.amount
            )
        }
    }
}
