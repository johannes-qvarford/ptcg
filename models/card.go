package models

// Card Represents a PTCG Card.
type Card struct {
	ID       int
	Name     string
	ImageURL string
}

var cards = []Card{
	Card{ID: 1, Name: "Alakazam", ImageURL: "https://cdn.bulbagarden.net/upload/9/94/AlakazamBaseSet1.jpg"},
}

// Cards Returns the global list of available cards.
func Cards() []Card {
	return cards
}
