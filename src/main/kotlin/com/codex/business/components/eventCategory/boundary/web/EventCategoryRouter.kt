package com.codex.business.components.eventCategory.boundary.web

import io.vertx.core.Vertx
import io.vertx.ext.web.impl.RouterImpl

class EventCategoryRouter(vertx: Vertx) : RouterImpl(vertx) {
    init {
        get("/ping").handler(EventCategoryRouteHandler::ping)
        post().handler(EventCategoryRouteHandler::create)
        put().handler(EventCategoryRouteHandler::update)
        get("/list").handler(EventCategoryRouteHandler::list)
        get("/q").handler(EventCategoryRouteHandler::search)
        get("/:id").handler(EventCategoryRouteHandler::findById)
        delete("/:id").handler(EventCategoryRouteHandler::deleteById)
    }
}