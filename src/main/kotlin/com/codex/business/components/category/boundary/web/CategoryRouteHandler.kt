package com.codex.business.components.category.boundary.web

import com.codex.base.enums.SortOrder
import com.codex.base.shared.AppResponse
import com.codex.business.common.EBAction.CategoryEBAction
import com.codex.business.common.EBAddress
import com.codex.business.common.enums.Status
import com.codex.business.components.category.spec.CategorySpec
import io.vertx.core.eventbus.DeliveryOptions
import io.vertx.core.json.JsonObject
import io.vertx.ext.web.RoutingContext
import java.time.LocalDateTime

object CategoryRouteHandler {
    fun ping(ctx: RoutingContext) {
        val options = DeliveryOptions()
        options.addHeader("action", CategoryEBAction.PING)
        ctx.vertx().eventBus().request<JsonObject>(EBAddress.CATEGORY, JsonObject(), options)
            .onSuccess { ctx.json(it.body()) }
            .onFailure { cause -> ctx.json(AppResponse.Failure("Service could not be reached: ${cause.message}")) }


    }

    fun findById(ctx: RoutingContext) {
        val payload = JsonObject.of("id", ctx.pathParam("id"))
        val options = DeliveryOptions()
        options.addHeader("action", CategoryEBAction.GET_BY_ID)
        ctx.vertx().eventBus().request<JsonObject>(EBAddress.CATEGORY, payload, options)
            .onSuccess { ctx.json(it.body()) }
            .onFailure { cause -> ctx.json(AppResponse.Failure("Failed to execute service: ${cause.message}")) }
    }

    fun create(ctx: RoutingContext) {
        val dto = ctx.body().asJsonObject()
        val options = DeliveryOptions()
        options.addHeader("action", CategoryEBAction.CREATE)
        ctx.vertx().eventBus().request<JsonObject>(EBAddress.CATEGORY, dto, options)
            .onSuccess { ctx.json(it.body()) }
            .onFailure { cause -> ctx.json(AppResponse.Failure("Failed to execute service: ${cause.message}")) }
    }

    fun update(ctx: RoutingContext) {
        val dto = ctx.body().asJsonObject()
        val options = DeliveryOptions()
        options.addHeader("action", CategoryEBAction.UPDATE)
        ctx.vertx().eventBus().request<JsonObject>(EBAddress.CATEGORY, dto, options)
            .onSuccess { ctx.json(it.body()) }
            .onFailure { cause -> ctx.json(AppResponse.Failure("Failed to execute service: ${cause.message}")) }
    }


    fun list(ctx: RoutingContext) {
        val payload = JsonObject.of(
            "page", ctx.request().getParam("page", "1").toIntOrNull(),
            "size", ctx.request().getParam("size", "50").toIntOrNull()
        )
        val options = DeliveryOptions()
        options.addHeader("action", CategoryEBAction.LIST)
        ctx.vertx().eventBus().request<JsonObject>(EBAddress.CATEGORY, payload, options)
            .onSuccess { ctx.json(it.body()) }
            .onFailure { cause -> ctx.json(AppResponse.Failure("Failed to execute service: ${cause.message}")) }
    }

    fun search(ctx: RoutingContext) {

        val spec = CategorySpec()
        spec.page = ctx.request().getParam(CategorySpec::page.name, "1").toInt()
        spec.size = ctx.request().getParam(CategorySpec::size.name, "50").toInt()
        spec.sortBy = ctx.request().getParam(CategorySpec::sortBy.name, CategorySpec::createdAt.name)
        spec.sortOrder =
            ctx.request().getParam(CategorySpec::sortOrder.name, SortOrder.DESC.name)?.let(SortOrder::valueOf)
        spec.name = ctx.request().getParam(CategorySpec::name.name)
        spec.description = ctx.request().getParam(CategorySpec::description.name)
        spec.status = ctx.request().getParam(CategorySpec::status.name, Status.ENABLED.name)?.let(Status::valueOf)
        spec.startDate = ctx.request().getParam(CategorySpec::startDate.name)?.let(LocalDateTime::parse)
        spec.endDate = ctx.request().getParam(CategorySpec::endDate.name)?.let(LocalDateTime::parse)
        spec.createdAt = ctx.request().getParam(CategorySpec::createdAt.name)?.let(LocalDateTime::parse)
        spec.modifiedAt = ctx.request().getParam(CategorySpec::modifiedAt.name)?.let(LocalDateTime::parse)

        val payload = JsonObject.mapFrom(spec)

        val options = DeliveryOptions()
        options.addHeader("action", CategoryEBAction.SEARCH)
        ctx.vertx().eventBus().request<JsonObject>(EBAddress.CATEGORY, payload, options)
            .onSuccess { ctx.json(it.body()) }
            .onFailure { cause -> ctx.json(AppResponse.Failure("Failed to execute service: ${cause.message}")) }
    }

    fun deleteById(ctx: RoutingContext) {
        val payload = JsonObject.of("id", ctx.pathParam("id"))
        val options = DeliveryOptions()
        options.addHeader("action", CategoryEBAction.DELETE_BY_ID)
        ctx.vertx().eventBus().request<JsonObject>(EBAddress.CATEGORY, payload, options)
            .onSuccess { ctx.json(it.body()) }
            .onFailure { cause -> ctx.json(AppResponse.Failure("Failed to execute service: ${cause.message}")) }
    }
}