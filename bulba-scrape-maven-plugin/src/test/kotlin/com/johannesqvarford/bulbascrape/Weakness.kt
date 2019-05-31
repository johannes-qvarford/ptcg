package com.johannesqvarford.bulbascrape

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import java.lang.NullPointerException
import java.lang.RuntimeException
import java.lang.reflect.Type

sealed class Weakness
object NoneWeakness : Weakness()
data class MultiplierWeakness(val multiplier: Int, val typeId: TypeId) : Weakness()

class WeaknessBiSerializer: JsonSerializer<Weakness>, JsonDeserializer<Weakness> {
    override fun deserialize(src: JsonElement?, t: Type?, c: JsonDeserializationContext?): Weakness {
        return when (src?.serializationType(WeaknessType::valueOf)) {
            WeaknessType.NONE -> NoneWeakness
            WeaknessType.MULTIPLIER -> c!!.deserialize(src)
            null -> throw NullPointerException("Cannot deserialize null to Weakness.")
        }
    }

    override fun serialize(src: Weakness?, t: Type?, c: JsonSerializationContext?): JsonElement {
        return when (src) {
            is NoneWeakness -> c!!.serialize(
                WeaknessJsonObject(
                    WeaknessType.NONE,
                    null,
                    null
                )
            )
            is MultiplierWeakness -> c!!.serialize(
                WeaknessJsonObject(
                    WeaknessType.MULTIPLIER,
                    multiplier = src.multiplier,
                    typeId = src.typeId
                )
            )
            null -> throw NullPointerException("Cannot serialize null Weakness.")
        }
    }
}

private data class WeaknessJsonObject(val type: WeaknessType, val multiplier: Int?, val typeId: TypeId?)
private enum class WeaknessType { NONE, MULTIPLIER }
