package com.codex.base.core.settings

import io.vertx.core.Promise
import io.vertx.core.Verticle
import io.vertx.core.spi.VerticleFactory
import org.koin.core.Koin
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.lang.reflect.InvocationTargetException
import java.util.concurrent.Callable

class CustomVerticleFactory(private var koin: Koin) : VerticleFactory {
    private val logger: Logger = LoggerFactory.getLogger(this::class.java)

    private val prefix = "java"

    override fun prefix(): String = prefix

    override fun createVerticle(verticleName: String, loader: ClassLoader, promise: Promise<Callable<Verticle>>) {
        val resolvedClassName = VerticleFactory.removePrefix(verticleName)

        val clazz = Class.forName(resolvedClassName, true, loader)
        val constructor = try {
            clazz.getConstructor(Koin::class.java)
        } catch (ex: Exception) {
            clazz.getConstructor()
        }
        try {
            promise.complete { (if (constructor.parameterCount > 0) constructor.newInstance(koin) else constructor.newInstance()) as Verticle }
        } catch (invEx: InvocationTargetException) {
            logger.error("Failed to create instance for verticle with class name: $resolvedClassName", invEx)
            promise.fail(invEx)
        } catch (ex: Exception) {
            logger.error("Failed to create instance for verticle with class name: $resolvedClassName", ex)
            promise.fail(ex)
        }
    }
}