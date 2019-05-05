package main

import (
	"html/template"
	"log"

	"github.com/gin-gonic/gin"
	"johannesqvarford.com/ptcg/packs"
)

func main() {
	main := template.New("main")
	main, err := main.Parse(`{{define "main" }} {{ template "layout" . }} {{ end }}`)
	if err != nil {
		log.Fatal(err)
	}

	r := gin.Default()
	r.Static("/static", "./static")
	packs.Register(r.Group("/packs"), main)
	r.Run()
}
