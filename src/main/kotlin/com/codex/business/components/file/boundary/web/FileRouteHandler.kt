package com.codex.business.components.file.boundary.web

import com.codex.base.shared.AppResponse
import com.codex.business.common.EBAction.FileEBAction
import com.codex.business.common.EBAddress
import com.codex.business.components.file.boundary.dto.FileDTO
import io.vertx.core.buffer.Buffer
import io.vertx.core.eventbus.DeliveryOptions
import io.vertx.core.json.JsonObject
import io.vertx.ext.web.RoutingContext

object FileRouteHandler {
    fun upload(ctx: RoutingContext) {
        val files = ctx.fileUploads()
        if (files.isEmpty()) ctx.json(AppResponse.Failure("No file upload"))


        val file = files.first()
        val payload = JsonObject.of(
            FileDTO::filePath.name,
            file.uploadedFileName(),
            FileDTO::filename.name,
            file.fileName(),
            FileDTO::length.name,
            file.size(),
            FileDTO::metadata.name,
            mapOf(
                "type" to file.contentType(),
                "charset" to file.charSet(),
                "name" to file.name(),
                "content-transfer-encoding" to file.contentTransferEncoding()
            )
        )
        val options = DeliveryOptions()
        options.addHeader("action", FileEBAction.UPLOAD)
        ctx.vertx().eventBus().request<JsonObject>(EBAddress.FILE, payload.toBuffer(), options)
            .onSuccess { ctx.json(it.body()) }
            .onFailure { ctx.json(AppResponse.Failure("Failed to execute service: ${it.message}")) }
    }

    fun download(ctx: RoutingContext) {
        val payload = Buffer.buffer(ctx.pathParam("filename"))
        val options = DeliveryOptions()
        options.addHeader("action", FileEBAction.DOWNLOAD)
        ctx.vertx().eventBus().request<Buffer>(EBAddress.FILE, payload, options)
            .onSuccess { ctx.end(it.body()) }
            .onFailure { ctx.json(AppResponse.Failure("Failed to execute service: ${it.message}")) }
    }

}