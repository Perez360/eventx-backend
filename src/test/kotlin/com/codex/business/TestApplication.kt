package com.codex.business

import com.codex.Application
import io.vertx.core.Vertx
import io.vertx.junit5.VertxExtension
import io.vertx.junit5.VertxTestContext
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.koin.test.KoinTest

@ExtendWith(VertxExtension::class)
class TestApplication : KoinTest {

    @Test
    fun deploy_verticle(vertx: Vertx, ctx: VertxTestContext) {
        vertx.deployVerticle(Application())
            .onComplete(ctx.succeedingThenComplete())
    }
}
