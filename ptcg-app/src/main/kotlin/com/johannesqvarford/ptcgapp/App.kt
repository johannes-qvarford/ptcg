package com.johannesqvarford.ptcgapp

import spark.ModelAndView
import spark.Response
import spark.Service.ignite
import spark.template.velocity.VelocityTemplateEngine

data class Pack(val id: Int, val cardIds: List<String>)


val packs = listOf(
        Pack(1, (1..10).map{"base1-$it"}.toList()),
        Pack(2, (11..20).map{"base1-$it"}.toList())
)

fun Response.notFound(message: String): ModelAndView {
    this.status(404)
    return ModelAndView(mapOf("message" to message), "notFound.vm")
}

fun main() {

    val http = ignite()



    http.get("/packs/:id", { req, resp ->
        val idParam = req.params("id")


        val id = idParam.toIntOrNull()
        id ?: return@get resp.notFound("Could not find pack with id $id")
        val pack = packs.find{it.id==id} ?: return@get resp.notFound("Could not find pack with id $id")

        ModelAndView(mapOf("model" to pack), "packs.vm")
    }, VelocityTemplateEngine())

    System.out.println( "Hello World!" )
}
