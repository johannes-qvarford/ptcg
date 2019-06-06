package com.johannesqvarford.ptcg.models

sealed class Card
data class PokemonCard(
    val cardId: CardId,
    val evolutionStage: EvolutionStage,
    val typeId: TypeId,
    val hitPoints: Int,
    val weakness: Weakness,
    val resistance: Resistance,
    val retreatCost: Int,
    val rarity: Rarity,
    val cardNumber: CardNumber,
    val pokemonPower: PokemonPower,
    val moves: List<Move>,
    val pokedexData: PokedexData
) : Card()

// TODO: Implement these.
object TrainerCard : Card()

object EnergyCard : Card()


enum class CardType { POKEMON, TRAINER, ENERGY }
data class PokemonCardJsonObject(
    val cardId: CardId,
    val evolutionStage: EvolutionStageJsonObject,
    val typeId: TypeId,
    val hitPoints: Int,
    val weakness: WeaknessJsonObject,
    val resistance: ResistanceJsonObject,
    val retreatCost: Int,
    val rarity: Rarity,
    val cardNumber: CardNumber,
    val pokemonPower: PokemonPower,
    val moves: List<MoveJsonObject>,
    val pokedexData: PokedexData
)

data class CardJsonObject(val type: CardType, val pokemon: PokemonCardJsonObject? = null)

class CardSerializableConverter : JsonSerializableConverter<CardJsonObject, Card> {
    val weakness = WeaknessSerializableConverter()
    val resistance = ResistanceSerializableConverter()
    val move = MoveSerializableConverter()
    val evolutionStage = EvolutionStageSerializableConverter()

    override fun toType(s: CardJsonObject): Card {
        fun toPokemonCard(p: PokemonCardJsonObject): PokemonCard {
            return PokemonCard(
                cardId = p.cardId,
                typeId = p.typeId,
                weakness = weakness.toType(p.weakness),
                retreatCost = p.retreatCost,
                resistance = resistance.toType(p.resistance),
                rarity = p.rarity,
                moves = p.moves.map { move.toType(it) }.toList(),
                evolutionStage = evolutionStage.toType(p.evolutionStage),
                cardNumber = p.cardNumber,
                hitPoints = p.hitPoints,
                pokedexData = p.pokedexData,
                pokemonPower = p.pokemonPower
            )
        }

        return when (s.type) {
            CardType.POKEMON -> toPokemonCard(s.pokemon!!)
            CardType.ENERGY -> EnergyCard
            CardType.TRAINER -> TrainerCard
        }
    }


    override fun toJsonSerializable(t: Card): CardJsonObject {
        fun toPokemonCardJsonObject(p: PokemonCard): PokemonCardJsonObject {
            return PokemonCardJsonObject(
                cardId = p.cardId,
                typeId = p.typeId,
                weakness = weakness.toJsonSerializable(p.weakness),
                retreatCost = p.retreatCost,
                resistance = resistance.toJsonSerializable(p.resistance),
                rarity = p.rarity,
                moves = p.moves.map { move.toJsonSerializable(it) }.toList(),
                evolutionStage = evolutionStage.toJsonSerializable(p.evolutionStage),
                cardNumber = p.cardNumber,
                hitPoints = p.hitPoints,
                pokedexData = p.pokedexData,
                pokemonPower = p.pokemonPower
            )
        }

        return when (t) {
            is PokemonCard -> CardJsonObject(type = CardType.POKEMON, pokemon = toPokemonCardJsonObject(t))
            is EnergyCard -> CardJsonObject(type = CardType.ENERGY)
            is TrainerCard -> CardJsonObject(type = CardType.TRAINER)
        }
    }
}
