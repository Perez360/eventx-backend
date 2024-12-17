package com.codex.business.components.category.boundary.web

import io.vertx.core.Vertx
import io.vertx.ext.web.impl.RouterImpl

class CategoryRouter(vertx: Vertx) : RouterImpl(vertx) {
    init {
        get("/ping").handler(CategoryRouteHandler::ping)
        post().handler(CategoryRouteHandler::create)
        put().handler(CategoryRouteHandler::update)
        get("/list").handler(CategoryRouteHandler::list)
        get("/q").handler(CategoryRouteHandler::search)
        get("/:id").handler(CategoryRouteHandler::findById)
        delete("/:id").handler(CategoryRouteHandler::deleteById)
    }
}