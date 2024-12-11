package com.codex.base.core.settings

import io.vertx.core.Future
import io.vertx.core.Promise
import org.slf4j.LoggerFactory
import java.nio.file.Paths
import java.util.*

object Configuration {
    private val logger = LoggerFactory.getLogger(Configuration::class.java)

    fun loadSystemProperties() {
        val properties = Properties()
        val internalConfig = this::class.java.classLoader.getResource("application.properties")
        val externalConfig: String? = System.getProperty("app.config.path")

        logger.info("Loading system properties")

        if (internalConfig != null) {
            logger.info("Internal configuration file found")
            internalConfig.openStream().use(properties::load)
        }

        if (externalConfig?.isNotBlank() == true) {
            logger.info("Found external file: $externalConfig")

            val path = Paths.get(externalConfig)
            logger.info("External configuration file found; overriding previous config file")
            path.toFile().inputStream().use(properties::load)
        }

        if (properties.isEmpty) {
            logger.warn(
                "No properties were loaded. System will run with default values. " +
                        "Run the jar with app.config.path to indicate your properties file " +
                        "or use -Dapp.[config].[attribute] to set specific configuration to run."
            )
        }
        System.getProperties().putAll(properties)
    }


    fun validateSystemPropertiesFuture(properties: Set<String>): Future<String> {
        val requiredPropertiesPromise = Promise.promise<String>()

        val systemProps = System.getProperties()
        val condition = properties.all { key -> systemProps.containsKey(key) }
        val missingKeys = properties.filter { key -> key !in systemProps.keys }

        if (condition) {
            requiredPropertiesPromise.complete("All required properties exist.")
        } else {
            requiredPropertiesPromise.fail("Some required config properties are missing. $missingKeys")
        }

        return requiredPropertiesPromise.future()
    }


    val requiredConfigProperties = setOf(
        "app.server.port"
    )
}


