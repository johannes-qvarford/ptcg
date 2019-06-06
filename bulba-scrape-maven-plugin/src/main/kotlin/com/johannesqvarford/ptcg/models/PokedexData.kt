package com.johannesqvarford.ptcg.models

data class PokedexData(val number: Int, val height: MeterHeight, val weight: KilogramWeight)
data class MeterHeight(val value: Double)
data class KilogramWeight(val value: Double)
