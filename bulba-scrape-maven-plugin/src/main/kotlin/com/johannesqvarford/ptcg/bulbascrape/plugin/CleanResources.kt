package com.johannesqvarford.ptcg.bulbascrape.plugin

import org.apache.maven.plugin.AbstractMojo
import org.apache.maven.plugins.annotations.LifecyclePhase
import org.apache.maven.plugins.annotations.Mojo
import org.apache.maven.plugins.annotations.Parameter
import java.io.File

@Mojo(name = "clean", defaultPhase = LifecyclePhase.COMPILE)
//@Execute(goal="clean", phase = LifecyclePhase.CLEAN)
class CleanResources : AbstractMojo() {
    @Parameter(property = "bulbascrape.outputDirectory", defaultValue = "\${project.build.directory}/bulba-scrape")
    var outputDirectory: File? = null

    override fun execute() {
        val outputDirectory = this.outputDirectory!!

        if (!outputDirectory.deleteRecursively()) {
            log.error("Failed to delete bulba-scrape directory.")
        }

        log.info("Executing clean")
    }
}
