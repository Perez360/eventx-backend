package com.codex

import io.vertx.core.Vertx
import io.vertx.core.http.HttpHeaders
import io.vertx.ext.web.impl.RouterImpl

class PingRouter(vertx: Vertx) : RouterImpl(vertx) {
    init {
        get("/ping").handler { ctx ->
            ctx.response()
                .putHeader(HttpHeaders.CONTENT_TYPE, "text/plain")
                .end("Hello vertx!")
        }
    }
}