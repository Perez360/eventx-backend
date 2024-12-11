package com.codex.business.components.file.boundary.web

import io.vertx.core.Vertx
import io.vertx.ext.web.impl.RouterImpl

class FileRouter(vertx: Vertx) : RouterImpl(vertx) {
    init {
        post("/upload").handler(FileRouteHandler::upload)
        get("/download/:filename").handler(FileRouteHandler::download)
    }
}