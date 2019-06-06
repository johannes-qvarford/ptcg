package com.johannesqvarford.ptcg.models

sealed class DamageIndicator
object NoneDamageIndicator : DamageIndicator()
data class NeutralDamageIndicator(val amount: Int) : DamageIndicator()
data class PositiveDamageIndicator(val amount: Int) : DamageIndicator()
data class NegativeDamageIndicator(val amount: Int) : DamageIndicator()
data class MultiplierDamageIndicator(val multiplier: Int) : DamageIndicator()

enum class DamageIndicatorType { NONE, NEUTRAL, POSITIVE, NEGATIVE, MULTIPLIER }
data class DamageIndicatorJsonObject
    (val type: DamageIndicatorType, val amount: Int? = null, val multiplier: Int? = null)

class DamageIndicatorSerializableConverter : JsonSerializableConverter<DamageIndicatorJsonObject, DamageIndicator> {
    override fun toType(s: DamageIndicatorJsonObject): DamageIndicator {
        return when (s.type) {
            DamageIndicatorType.NONE -> NoneDamageIndicator
            DamageIndicatorType.NEUTRAL -> NeutralDamageIndicator(amount = s.amount!!)
            DamageIndicatorType.POSITIVE -> PositiveDamageIndicator(amount = s.amount!!)
            DamageIndicatorType.NEGATIVE -> NegativeDamageIndicator(amount = s.amount!!)
            DamageIndicatorType.MULTIPLIER -> MultiplierDamageIndicator(multiplier = s.multiplier!!)
        }
    }

    override fun toJsonSerializable(t: DamageIndicator): DamageIndicatorJsonObject {
        return when (t) {
            is NoneDamageIndicator ->
                DamageIndicatorJsonObject(type = DamageIndicatorType.NONE)
            is NeutralDamageIndicator ->
                DamageIndicatorJsonObject(type = DamageIndicatorType.NEUTRAL, amount = t.amount)
            is PositiveDamageIndicator ->
                DamageIndicatorJsonObject(type = DamageIndicatorType.POSITIVE, amount = t.amount)
            is NegativeDamageIndicator ->
                DamageIndicatorJsonObject(type = DamageIndicatorType.NEGATIVE, amount = t.amount)
            is MultiplierDamageIndicator ->
                DamageIndicatorJsonObject(type = DamageIndicatorType.MULTIPLIER, multiplier = t.multiplier)
        }
    }
}
