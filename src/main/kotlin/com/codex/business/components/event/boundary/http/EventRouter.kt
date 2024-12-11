package com.codex.business.components.event.boundary.http

import io.vertx.core.Vertx
import io.vertx.ext.web.impl.RouterImpl

class EventRouter(vertx: Vertx) : RouterImpl(vertx) {
    init {
        get("/ping").handler(EventRouterHandler::ping)
        post().handler(EventRouterHandler::create)
        put().handler(EventRouterHandler::update)
        get("/list").handler(EventRouterHandler::list)
        get("/q").handler(EventRouterHandler::search)
        get("/:id").handler(EventRouterHandler::findById)
        delete("/:id").handler(EventRouterHandler::deleteById)
    }
}