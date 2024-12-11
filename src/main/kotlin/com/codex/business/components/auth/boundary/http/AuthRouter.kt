package com.codex.business.components.auth.boundary.http

import io.vertx.core.Vertx
import io.vertx.ext.web.impl.RouterImpl

class AuthRouter(vertx: Vertx) : RouterImpl(vertx) {
    init {
        get("/ping").handler(AuthRouterHandler::ping)
        post("/register").handler(AuthRouterHandler::register)
        post("/login").handler(AuthRouterHandler::login)
    }

}