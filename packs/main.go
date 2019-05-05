package packs

import (
	"bytes"
	"fmt"
	"strconv"

	"html/template"

	"github.com/gin-gonic/gin"
	"johannesqvarford.com/ptcg/models"
)

// Register routes and templates for /packs
func Register(r *gin.RouterGroup, t *template.Template) {
	cardInPackTmpl := template.Must(t.ParseFiles("layout.html", "packs/card-in-pack.html"))

	r.GET(":pack-id/cards/:card-index", func(c *gin.Context) {

		notFound := func(err error) bool {
			if err != nil {
				c.Status(404)
				fmt.Fprint(c.Writer, err)
				return true
			}
			return false
		}

		packID, err := strconv.Atoi(c.Param("pack-id"))
		if notFound(err) {
			return
		}

		cardIndex, err := strconv.Atoi(c.Param("card-index"))
		if notFound(err) {
			return
		}
		if cardIndex < 0 || cardIndex > 9 {
			notFound(fmt.Errorf("Invalid card index: %d", cardIndex))
			return
		}

		pack, err := models.PackWithID(packID)
		if notFound(err) {
			return
		}

		packCards, err := models.CardsInPack(pack)
		if notFound(err) {
			return
		}

		card := packCards[cardIndex]

		var b bytes.Buffer
		err = cardInPackTmpl.Execute(&b, &struct {
			Card models.Card
		}{card})
		if err != nil {
			c.Status(500)
			fmt.Fprint(c.Writer, err)
		}
		fmt.Fprint(c.Writer, string(b.Bytes()))
	})
}
