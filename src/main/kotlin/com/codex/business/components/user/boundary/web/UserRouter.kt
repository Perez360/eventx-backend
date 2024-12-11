package com.codex.business.components.user.boundary.web

import io.vertx.core.Vertx
import io.vertx.ext.web.impl.RouterImpl

class UserRouter(vertx: Vertx) : RouterImpl(vertx) {
    init {
        get("/ping").handler(UserRouteHandler::ping)
        post().handler(UserRouteHandler::create)
        put().handler(UserRouteHandler::update)
        get("/list").handler(UserRouteHandler::list)
        get("/q").handler(UserRouteHandler::search)
        get("/:id").handler(UserRouteHandler::findById)
        delete("/:id").handler(UserRouteHandler::deleteById)
    }
}