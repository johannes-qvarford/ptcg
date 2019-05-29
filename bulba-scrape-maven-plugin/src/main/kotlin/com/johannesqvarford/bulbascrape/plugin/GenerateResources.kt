package com.johannesqvarford.bulbascrape.plugin

import org.apache.maven.plugin.AbstractMojo
import org.apache.maven.plugins.annotations.Mojo
import org.apache.maven.plugins.annotations.Parameter
import java.io.File

@Mojo(name = "generate")
public class GenerateResources : AbstractMojo() {
    @Parameter(property = "bulbascrape.outputDirectory", defaultValue = "\${project.build.directory}/bulba-scrape")
    var outputDirectory: File? = null

    override fun execute() {
        val outputDirectory = this.outputDirectory!!

        if (!outputDirectory.mkdirs() && !outputDirectory.isDirectory) {
            log.error(String.format("Could not create output directory '%s'", outputDirectory))
            return
        }

        log.info("Output directory created.")

        return
    }
}
