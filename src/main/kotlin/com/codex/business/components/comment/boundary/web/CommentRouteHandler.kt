package com.codex.business.components.comment.boundary.web

import com.codex.base.shared.Texts
import com.codex.base.enums.Operator
import com.codex.base.enums.SortOrder
import com.codex.base.shared.AppResponse
import com.codex.business.common.EBAction.CommentEBAction
import com.codex.business.common.EBAddress
import com.codex.business.components.comment.spec.CommentSpec
import io.vertx.core.eventbus.DeliveryOptions
import io.vertx.core.http.HttpServerResponse
import io.vertx.core.json.JsonObject
import io.vertx.ext.web.RoutingContext
import java.time.LocalDateTime


object CommentRouteHandler {
    fun ping(ctx: RoutingContext) {
        val options = DeliveryOptions()
        options.addHeader("action", CommentEBAction.PING)
        ctx.vertx().eventBus().request<JsonObject>(EBAddress.COMMENT, JsonObject(), options)
            .onSuccess { ctx.json(it.body()) }
            .onFailure { ctx.json(AppResponse.Failure("Service could not be reached: ${it.message}")) }
    }

    fun findById(ctx: RoutingContext) {
        val payload = JsonObject.of("id", ctx.pathParam("id"))
        val options = DeliveryOptions()
        options.addHeader("action", CommentEBAction.GET_BY_ID)
        ctx.vertx().eventBus().request<JsonObject>(EBAddress.COMMENT, payload, options)
            .onSuccess { ctx.json(it.body()) }
            .onFailure { ctx.json(AppResponse.Failure("Failed to execute service: ${it.message}")) }
    }

    fun create(ctx: RoutingContext) {
        val dto = ctx.body().asJsonObject()
        val options = DeliveryOptions()
        options.addHeader("action", CommentEBAction.CREATE)
        ctx.vertx().eventBus().request<JsonObject>(EBAddress.COMMENT, dto, options)
            .onSuccess { ctx.json(it.body()) }
            .onFailure { ctx.json(AppResponse.Failure("Failed to execute service: ${it.message}")) }
    }

    fun update(ctx: RoutingContext) {
        val dto = ctx.body().asJsonObject()
        val options = DeliveryOptions()
        options.addHeader("action", CommentEBAction.UPDATE)
        ctx.vertx().eventBus().request<JsonObject>(EBAddress.COMMENT, dto, options)
            .onSuccess { ctx.json(it.body()) }
            .onFailure { ctx.json(AppResponse.Failure("Failed to execute service: ${it.message}")) }
    }


    fun list(ctx: RoutingContext) {
        val payload = JsonObject.of(
            "page", ctx.request().getParam("page", "1").toIntOrNull(),
            "size", ctx.request().getParam("size", "50").toIntOrNull()
        )
        val options = DeliveryOptions()
        options.addHeader("action", CommentEBAction.LIST)
        ctx.vertx().eventBus().request<JsonObject>(EBAddress.COMMENT, payload, options)
            .onSuccess { ctx.json(it.body()) }
            .onFailure { ctx.json(AppResponse.Failure("Failed to execute service: ${it.message}")) }
    }

    fun sse(ctx: RoutingContext) {
        val response: HttpServerResponse = ctx.response()
        response.setChunked(true)
        response.putHeader("Content-Type", "text/event-stream;charset=UTF-8")
        response.putHeader("Connection", "keep-alive")
        val payload = JsonObject.of(
            "page", ctx.request().getParam("page", "1").toIntOrNull(),
            "size", ctx.request().getParam("size", "50").toIntOrNull()
        )
        val options = DeliveryOptions()
        options.addHeader("action", CommentEBAction.LIST)
        val timerId = ctx.vertx().setPeriodic(Texts.sseConnectionTimeout) {
            ctx.vertx().eventBus().request<JsonObject>(EBAddress.COMMENT, payload, options)
                .onSuccess { ctx.response().write(it.body().encodePrettily()) }
                .onFailure { ctx.json(AppResponse.Failure("Failed to execute service: ${it.message}")) }
        }

        // Cancel the periodic timer when the connection is closed
        response.closeHandler {
            ctx.vertx().cancelTimer(timerId)
        }
    }

    fun search(ctx: RoutingContext) {

        val spec = CommentSpec()
        spec.page = ctx.request().getParam(CommentSpec::page.name, "1").toInt()
        spec.size = ctx.request().getParam(CommentSpec::size.name, "50").toInt()
        spec.sortBy = ctx.request().getParam(CommentSpec::sortBy.name, CommentSpec::createdAt.name)
        spec.sortOrder =
            ctx.request().getParam(CommentSpec::sortOrder.name, SortOrder.DESC.name)?.let(SortOrder::valueOf)
        spec.operator = ctx.request().getParam(CommentSpec::operator.name, Operator.AND.name)?.let(Operator::valueOf)
        spec.message = ctx.request().getParam(CommentSpec::message.name)
        spec.show = ctx.request().getParam(CommentSpec::show.name)?.toBooleanStrict()
        spec.userId = ctx.request().getParam(CommentSpec::userId.name)
        spec.eventId = ctx.request().getParam(CommentSpec::eventId.name)
        spec.startDate = ctx.request().getParam(CommentSpec::startDate.name)?.let(LocalDateTime::parse)
        spec.endDate = ctx.request().getParam(CommentSpec::endDate.name)?.let(LocalDateTime::parse)
        spec.createdAt = ctx.request().getParam(CommentSpec::createdAt.name)?.let(LocalDateTime::parse)
        spec.modifiedAt = ctx.request().getParam(CommentSpec::modifiedAt.name)?.let(LocalDateTime::parse)

        val payload = JsonObject.mapFrom(spec)

        val options = DeliveryOptions()
        options.addHeader("action", CommentEBAction.SEARCH)
        ctx.vertx().eventBus().request<JsonObject>(EBAddress.COMMENT, payload, options)
            .onSuccess { ctx.json(it.body()) }
            .onFailure { ctx.json(AppResponse.Failure("Failed to execute service: ${it.message}")) }
    }

    fun deleteById(ctx: RoutingContext) {
        val payload = JsonObject.of("id", ctx.pathParam("id"))
        val options = DeliveryOptions()
        options.addHeader("action", CommentEBAction.DELETE_BY_ID)
        ctx.vertx().eventBus().request<JsonObject>(EBAddress.COMMENT, payload, options)
            .onSuccess { ctx.json(it.body()) }
            .onFailure { ctx.json(AppResponse.Failure("Failed to execute service: ${it.message}")) }
    }
}