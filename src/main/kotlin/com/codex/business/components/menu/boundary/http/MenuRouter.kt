package com.codex.business.components.menu.boundary.http

import io.vertx.core.Vertx
import io.vertx.ext.web.impl.RouterImpl

class MenuRouter(vertx: Vertx) : RouterImpl(vertx) {
    init {
        get("/ping").handler(MenuRouterHandler::ping)
        post().handler(MenuRouterHandler::create)
        put().handler(MenuRouterHandler::update)
        get("/list").handler(MenuRouterHandler::list)
        get("/:id").handler(MenuRouterHandler::findById)
        delete("/:id").handler(MenuRouterHandler::deleteById)
    }
}