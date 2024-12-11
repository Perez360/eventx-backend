package com.codex.business.components.kyc.boundary.web

import io.vertx.core.Vertx
import io.vertx.ext.web.impl.RouterImpl

class KycRouter(vertx: Vertx) : RouterImpl(vertx) {
    init {
        get("/ping").handler(KycRouteHandler::ping)
        post().handler(KycRouteHandler::create)
        put().handler(KycRouteHandler::update)
        get("/list").handler(KycRouteHandler::list)
        get("/q").handler(KycRouteHandler::search)
        get("/:id").handler(KycRouteHandler::findById)
        delete("/:id").handler(KycRouteHandler::deleteById)
    }

}