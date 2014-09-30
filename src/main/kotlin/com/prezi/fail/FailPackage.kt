package com.prezi.fail

import com.prezi.fail.sarge.Sarge
import com.prezi.changelog.ChangelogClient
import java.io.File
import java.io.FileInputStream
import org.slf4j.LoggerFactory
import com.prezi.fail.sarge.SargeConfig
import java.util.Properties

private fun usage(exitCode: Int = 0) {
    println("Usage: fail <tag> <sapper> <duration_seconds> [sapper_arg_1 [sapper_arg_2 ... ]]")
    System.exit(exitCode)
}

fun main(args: Array<String>) {
    loadUserProperties()
    if (args.count() < 3) {
        println("Not enough arguments.")
        usage(1)
    }
    if (args.contains("--help")) {
        usage()
    }
    // TODO: add logic for choosing action based on args[0] here
    Sarge().charge(args[0], args[1], args[2], args.drop(3))
}

private fun loadUserProperties() {
    val logger = LoggerFactory.getLogger("main")!!
    val file = File("${System.getenv("HOME")}/.fail.properties")
    if (file.exists()) {
        val appliedProperties: MutableMap<String, String> = hashMapOf()
        val properties = Properties()
        val inputStream = FileInputStream(file)
        properties.load(inputStream)
        inputStream.close()
        properties.forEach { _entry ->
            [suppress("UNCHECKED_CAST")] val entry = _entry as Map.Entry<String, String>
            if (System.getProperty(entry.key) == null) {
                System.setProperty(entry.key, entry.value)
                appliedProperties.put(entry.key, entry.value)
            }
        }
        logger.info("Loaded properties file ${file.canonicalPath}")
        appliedProperties.forEach { entry -> logger.debug("${file.canonicalPath}: ${entry.key} = ${entry.value}") }
    }
}

