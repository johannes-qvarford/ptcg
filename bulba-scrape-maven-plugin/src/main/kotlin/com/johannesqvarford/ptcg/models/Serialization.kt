package com.johannesqvarford.ptcg.models

interface JsonSerializableConverter<J, T> {
    fun toJsonSerializable(t: T): J
    fun toType(s: J): T
}
