package com.johannesqvarford.ptcg.models

import com.google.gson.Gson

inline fun <reified Serializable, Type>
        roundTripJsonSerialize(t: Type, converter: JsonSerializableConverter<Serializable, Type>): Type {
    val gson = Gson()
    val serializable = converter.toJsonSerializable(t)
    val actualSerializableJson = gson.toJson(serializable)
    val actualSerializable: Serializable = gson.fromJson(actualSerializableJson, Serializable::class.java)
    return converter.toType(actualSerializable)
}
