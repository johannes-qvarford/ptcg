package models

import "fmt"

// Pack Represent a pack of cards.
type Pack struct {
	id    int
	cards [10]int
}

type packNotFound struct {
	id int
}

func (e *packNotFound) Error() string {
	return fmt.Sprintf("Pack %d not found", e.id)
}

type cardNotFound struct {
	id int
}

func (e *cardNotFound) Error() string {
	return fmt.Sprintf("Card %d not found", e.id)
}

var packs = []Pack{
	Pack{id: 1, cards: [10]int{1, 1, 1, 1, 1, 1, 1, 1, 1, 1}},
}

// Packs Returns the global list of available packs.
func Packs() []Pack {
	return packs
}

// PackWithID Returns the Pack with the given ID from the global list.
func PackWithID(id int) (Pack, error) {
	for _, e := range packs {
		if e.id == id {
			return e, nil
		}
	}
	return Pack{}, &packNotFound{id}
}

// CardsInPack Returns the resolved Cards in the Pack.
func CardsInPack(pack Pack) ([]Card, error) {
	var inPack []Card
	for _, packCardID := range pack.cards {
		found := false
		for _, card := range Cards() {
			if packCardID == card.ID {
				inPack = append(inPack, card)
				found = true
			}
		}
		if !found {
			return inPack, &cardNotFound{id: packCardID}
		}
	}
	return inPack, nil
}
