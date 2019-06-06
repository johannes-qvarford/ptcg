package com.johannesqvarford.ptcg.models

sealed class EvolutionStage
object BaseEvolutionStage : EvolutionStage()
data class EvolutionStage1(val evolvesFrom: String) : EvolutionStage()
data class EvolutionStage2(val evolvesFrom: String) : EvolutionStage()

enum class EvolutionStageType {
    BASE,
    STAGE1,
    STAGE2,
}

data class EvolutionStageJsonObject(val type: EvolutionStageType, val evolvesFrom: String? = null)

class EvolutionStageSerializableConverter :
    JsonSerializableConverter<EvolutionStageJsonObject, EvolutionStage> {
    override fun toType(s: EvolutionStageJsonObject): EvolutionStage {
        return when (s.type) {
            EvolutionStageType.BASE -> BaseEvolutionStage
            EvolutionStageType.STAGE1 -> EvolutionStage1(
                evolvesFrom = s.evolvesFrom!!
            )
            EvolutionStageType.STAGE2 -> EvolutionStage2(
                evolvesFrom = s.evolvesFrom!!
            )
        }
    }

    override fun toJsonSerializable(t: EvolutionStage): EvolutionStageJsonObject {
        return when (t) {
            is BaseEvolutionStage -> EvolutionStageJsonObject(
                type = EvolutionStageType.BASE
            )
            is EvolutionStage1 -> EvolutionStageJsonObject(
                type = EvolutionStageType.STAGE1,
                evolvesFrom = t.evolvesFrom
            )
            is EvolutionStage2 -> EvolutionStageJsonObject(
                type = EvolutionStageType.STAGE2,
                evolvesFrom = t.evolvesFrom
            )
        }
    }
}
