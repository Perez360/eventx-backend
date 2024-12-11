package com.codex.base.core.di

import com.codex.base.core.di.modules.DB_MODULE
import com.codex.base.core.di.modules.SERVICE_MODULE
import com.codex.base.shared.Texts
import io.vertx.core.Vertx
import org.koin.core.context.startKoin
import org.koin.dsl.module
import org.koin.logger.slf4jLogger


object DependencyInjection {
    fun init(vertx: Vertx) {
        val vertxModule = module(createdAtStart = true) { single<Vertx> { vertx } }
        startKoin {
            properties(
                mapOf(
                    "dbConnectionString" to Texts.DATABASE_CONNECTION_STRING,
                    "bucketName" to Texts.STORAGE_BUCKET_NAME,
                )
            )
            printLogger()
            slf4jLogger()
            modules(vertxModule, DB_MODULE, SERVICE_MODULE)
        }
    }
}