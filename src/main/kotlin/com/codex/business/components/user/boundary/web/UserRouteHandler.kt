package com.codex.business.components.user.boundary.web

import com.codex.base.enums.SortOrder
import com.codex.base.shared.AppResponse
import com.codex.business.common.EBAction.UserEBAction
import com.codex.business.common.EBAddress
import com.codex.business.common.enums.Status
import com.codex.business.components.user.spec.UserSpec
import io.vertx.core.eventbus.DeliveryOptions
import io.vertx.core.json.JsonObject
import io.vertx.ext.web.RoutingContext
import java.time.LocalDateTime

object UserRouteHandler {
    fun ping(ctx: RoutingContext) {
        val options = DeliveryOptions()
        options.addHeader("action", UserEBAction.PING)
        ctx.vertx().eventBus().request<JsonObject>(EBAddress.USER, JsonObject(), options)
            .onSuccess { ctx.json(it.body()) }
            .onFailure { cause -> ctx.json(AppResponse.Failure("Service could not be reached: ${cause.message}")) }


    }

    fun findById(ctx: RoutingContext) {
        val payload = JsonObject.of("id", ctx.pathParam("id"))
        val options = DeliveryOptions()
        options.addHeader("action", UserEBAction.GET_BY_ID)
        ctx.vertx().eventBus().request<JsonObject>(EBAddress.USER, payload, options)
            .onSuccess { ctx.json(it.body()) }
            .onFailure { cause -> ctx.json(AppResponse.Failure("Failed to execute service: ${cause.message}")) }
    }

    fun create(ctx: RoutingContext) {
        val dto = ctx.body().asJsonObject()
        val options = DeliveryOptions()
        options.addHeader("action", UserEBAction.CREATE)
        ctx.vertx().eventBus().request<JsonObject>(EBAddress.USER, dto, options)
            .onSuccess { ctx.json(it.body()) }
            .onFailure { cause -> ctx.json(AppResponse.Failure("Failed to execute service: ${cause.message}")) }
    }

    fun update(ctx: RoutingContext) {
        val dto = ctx.body().asJsonObject()
        val options = DeliveryOptions()
        options.addHeader("action", UserEBAction.UPDATE)
        ctx.vertx().eventBus().request<JsonObject>(EBAddress.USER, dto, options)
            .onSuccess { ctx.json(it.body()) }
            .onFailure { cause -> ctx.json(AppResponse.Failure("Failed to execute service: ${cause.message}")) }
    }


    fun list(ctx: RoutingContext) {
        val payload = JsonObject.of(
            "page", ctx.request().getParam("page", "1").toIntOrNull(),
            "size", ctx.request().getParam("size", "50").toIntOrNull()
        )
        val options = DeliveryOptions()
        options.addHeader("action", UserEBAction.LIST)
        ctx.vertx().eventBus().request<JsonObject>(EBAddress.USER, payload, options)
            .onSuccess { ctx.json(it.body()) }
            .onFailure { cause -> ctx.json(AppResponse.Failure("Failed to execute service: ${cause.message}")) }
    }

    fun search(ctx: RoutingContext) {
        val spec = UserSpec()
        spec.page = ctx.request().getParam(UserSpec::page.name, "1").toInt()
        spec.size = ctx.request().getParam(UserSpec::size.name, "50").toInt()
        spec.sortBy = ctx.request().getParam(UserSpec::sortBy.name, UserSpec::createdAt.name)
        spec.sortOrder =
            ctx.request().getParam(UserSpec::sortOrder.name, SortOrder.DESC.name)?.let { SortOrder.valueOf(it) }
        spec.firstName = ctx.request().getParam(UserSpec::firstName.name)
        spec.lastName = ctx.request().getParam(UserSpec::lastName.name)
        spec.otherName = ctx.request().getParam(UserSpec::otherName.name)
        spec.role = ctx.request().getParam(UserSpec::role.name)
        spec.email = ctx.request().getParam(UserSpec::email.name)
        spec.status = ctx.request().getParam(UserSpec::status.name)?.let(Status::valueOf)
        spec.startDate = ctx.request().getParam(UserSpec::startDate.name)?.let(LocalDateTime::parse)
        spec.endDate = ctx.request().getParam(UserSpec::endDate.name)?.let(LocalDateTime::parse)
        spec.createdAt = ctx.request().getParam(UserSpec::createdAt.name)?.let(LocalDateTime::parse)
        spec.modifiedAt = ctx.request().getParam(UserSpec::modifiedAt.name)?.let(LocalDateTime::parse)

        val payload = JsonObject.mapFrom(spec)

        val options = DeliveryOptions()
        options.addHeader("action", UserEBAction.SEARCH)
        ctx.vertx().eventBus().request<JsonObject>(EBAddress.USER, payload, options)
            .onSuccess { ctx.json(it.body()) }
            .onFailure { cause -> ctx.json(AppResponse.Failure("Failed to execute service: ${cause.message}")) }
    }

    fun deleteById(ctx: RoutingContext) {
        val payload = JsonObject.of("id", ctx.pathParam("id"))
        val options = DeliveryOptions()
        options.addHeader("action", UserEBAction.DELETE_BY_ID)
        ctx.vertx().eventBus().request<JsonObject>(EBAddress.USER, payload, options)
            .onSuccess { ctx.json(it.body()) }
            .onFailure { cause -> ctx.json(AppResponse.Failure("Failed to execute service: ${cause.message}")) }
    }
}