package com.codex.business.components.comment.boundary.web

import io.vertx.core.Vertx
import io.vertx.ext.web.impl.RouterImpl

class CommentRouter(vertx: Vertx) : RouterImpl(vertx) {
    init {
        get("/ping").handler(CommentRouteHandler::ping)
        post().handler(CommentRouteHandler::create)
        put().handler(CommentRouteHandler::update)
        get("/list").handler(CommentRouteHandler::list)
        get("/sse").handler(CommentRouteHandler::sse)
        get("/q").handler(CommentRouteHandler::search)
        get("/:id").handler(CommentRouteHandler::findById)
        delete("/:id").handler(CommentRouteHandler::deleteById)
    }
}