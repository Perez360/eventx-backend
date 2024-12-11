package com.codex.business.components.eventCategory.boundary.web

import com.codex.base.enums.SortOrder
import com.codex.base.shared.AppResponse
import com.codex.business.common.EBAction.EventCategoryEBAction
import com.codex.business.common.EBAddress
import com.codex.business.common.enums.Status
import com.codex.business.components.eventCategory.spec.EventCategorySpec
import io.vertx.core.eventbus.DeliveryOptions
import io.vertx.core.json.JsonObject
import io.vertx.ext.web.RoutingContext
import java.time.LocalDateTime

object EventCategoryRouteHandler {
    fun ping(ctx: RoutingContext) {
        val options = DeliveryOptions()
        options.addHeader("action", EventCategoryEBAction.PING)
        ctx.vertx().eventBus().request<JsonObject>(EBAddress.EVENT_CATEGORY, JsonObject(), options)
            .onSuccess { ctx.json(it.body()) }
            .onFailure { cause -> ctx.json(AppResponse.Failure("Service could not be reached: ${cause.message}")) }


    }

    fun findById(ctx: RoutingContext) {
        val payload = JsonObject.of("id", ctx.pathParam("id"))
        val options = DeliveryOptions()
        options.addHeader("action", EventCategoryEBAction.GET_BY_ID)
        ctx.vertx().eventBus().request<JsonObject>(EBAddress.EVENT_CATEGORY, payload, options)
            .onSuccess { ctx.json(it.body()) }
            .onFailure { cause -> ctx.json(AppResponse.Failure("Failed to execute service: ${cause.message}")) }
    }

    fun create(ctx: RoutingContext) {
        val dto = ctx.body().asJsonObject()
        val options = DeliveryOptions()
        options.addHeader("action", EventCategoryEBAction.CREATE)
        ctx.vertx().eventBus().request<JsonObject>(EBAddress.EVENT_CATEGORY, dto, options)
            .onSuccess { ctx.json(it.body()) }
            .onFailure { cause -> ctx.json(AppResponse.Failure("Failed to execute service: ${cause.message}")) }
    }

    fun update(ctx: RoutingContext) {
        val dto = ctx.body().asJsonObject()
        val options = DeliveryOptions()
        options.addHeader("action", EventCategoryEBAction.UPDATE)
        ctx.vertx().eventBus().request<JsonObject>(EBAddress.EVENT_CATEGORY, dto, options)
            .onSuccess { ctx.json(it.body()) }
            .onFailure { cause -> ctx.json(AppResponse.Failure("Failed to execute service: ${cause.message}")) }
    }


    fun list(ctx: RoutingContext) {
        val payload = JsonObject.of(
            "page", ctx.request().getParam("page", "1").toIntOrNull(),
            "size", ctx.request().getParam("size", "50").toIntOrNull()
        )
        val options = DeliveryOptions()
        options.addHeader("action", EventCategoryEBAction.LIST)
        ctx.vertx().eventBus().request<JsonObject>(EBAddress.EVENT_CATEGORY, payload, options)
            .onSuccess { ctx.json(it.body()) }
            .onFailure { cause -> ctx.json(AppResponse.Failure("Failed to execute service: ${cause.message}")) }
    }

    fun search(ctx: RoutingContext) {

        val spec = EventCategorySpec()
        spec.page = ctx.request().getParam(EventCategorySpec::page.name, "1").toInt()
        spec.size = ctx.request().getParam(EventCategorySpec::size.name, "50").toInt()
        spec.sortBy = ctx.request().getParam(EventCategorySpec::sortBy.name, EventCategorySpec::createdAt.name)
        spec.sortOrder =
            ctx.request().getParam(EventCategorySpec::sortOrder.name, SortOrder.DESC.name)?.let(SortOrder::valueOf)
        spec.name = ctx.request().getParam(EventCategorySpec::name.name)
        spec.description = ctx.request().getParam(EventCategorySpec::description.name)
        spec.status = ctx.request().getParam(EventCategorySpec::status.name, Status.ENABLED.name)?.let(Status::valueOf)
        spec.startDate = ctx.request().getParam(EventCategorySpec::startDate.name)?.let(LocalDateTime::parse)
        spec.endDate = ctx.request().getParam(EventCategorySpec::endDate.name)?.let(LocalDateTime::parse)
        spec.createdAt = ctx.request().getParam(EventCategorySpec::createdAt.name)?.let(LocalDateTime::parse)
        spec.modifiedAt = ctx.request().getParam(EventCategorySpec::modifiedAt.name)?.let(LocalDateTime::parse)

        val payload = JsonObject.mapFrom(spec)

        val options = DeliveryOptions()
        options.addHeader("action", EventCategoryEBAction.SEARCH)
        ctx.vertx().eventBus().request<JsonObject>(EBAddress.EVENT_CATEGORY, payload, options)
            .onSuccess { ctx.json(it.body()) }
            .onFailure { cause -> ctx.json(AppResponse.Failure("Failed to execute service: ${cause.message}")) }
    }

    fun deleteById(ctx: RoutingContext) {
        val payload = JsonObject.of("id", ctx.pathParam("id"))
        val options = DeliveryOptions()
        options.addHeader("action", EventCategoryEBAction.DELETE_BY_ID)
        ctx.vertx().eventBus().request<JsonObject>(EBAddress.EVENT_CATEGORY, payload, options)
            .onSuccess { ctx.json(it.body()) }
            .onFailure { cause -> ctx.json(AppResponse.Failure("Failed to execute service: ${cause.message}")) }
    }
}