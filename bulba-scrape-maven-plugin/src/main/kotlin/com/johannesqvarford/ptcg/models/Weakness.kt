package com.johannesqvarford.ptcg.models

sealed class Weakness
object NoneWeakness : Weakness()
data class MultiplierWeakness(val multiplier: Int, val typeId: TypeId) : Weakness()

enum class WeaknessType { NONE, MULTIPLIER }
data class WeaknessJsonObject(val type: WeaknessType, val multiplier: Int? = null, val typeId: TypeId? = null)

class WeaknessSerializableConverter :
    JsonSerializableConverter<WeaknessJsonObject, Weakness> {
    override fun toType(s: WeaknessJsonObject): Weakness {
        return when (s.type) {
            WeaknessType.NONE -> NoneWeakness
            WeaknessType.MULTIPLIER -> MultiplierWeakness(
                multiplier = s.multiplier!!,
                typeId = s.typeId!!
            )
        }
    }

    override fun toJsonSerializable(t: Weakness): WeaknessJsonObject {
        return when (t) {
            is NoneWeakness ->
                WeaknessJsonObject(type = WeaknessType.NONE)
            is MultiplierWeakness ->
                WeaknessJsonObject(
                    type = WeaknessType.MULTIPLIER,
                    multiplier = t.multiplier,
                    typeId = t.typeId
                )
        }
    }
}
