package com.codex.business.components.kyc.boundary.web

import com.codex.base.enums.SortOrder
import com.codex.base.shared.AppResponse
import com.codex.business.common.EBAction.KycEBAction
import com.codex.business.common.EBAddress
import com.codex.business.components.kyc.spec.KycSpec
import com.codex.business.components.user.enum.VerificationStatus
import io.vertx.core.eventbus.DeliveryOptions
import io.vertx.core.json.JsonObject
import io.vertx.ext.web.RoutingContext
import org.slf4j.MDC
import java.time.LocalDate
import java.time.LocalDateTime

object KycRouteHandler {
    fun ping(ctx: RoutingContext) {
        val options = DeliveryOptions()
        options.addHeader("action", KycEBAction.PING)
        ctx.vertx().eventBus().request<JsonObject>(EBAddress.KYC, JsonObject(), options)
            .onSuccess { ctx.json(it.body()) }
            .onFailure { ctx.json(AppResponse.Failure("Service could not be reached: ${it.message}")) }
    }

    fun findById(ctx: RoutingContext) {
        val payload = JsonObject.of("id", ctx.pathParam("id"))
        val options = DeliveryOptions()
        options.addHeader("action", KycEBAction.GET_BY_ID)
        ctx.vertx().eventBus().request<JsonObject>(EBAddress.KYC, payload, options)
            .onSuccess { ctx.json(it.body()) }
            .onFailure { ctx.json(AppResponse.Failure("Failed to execute service: ${it.message}")) }
    }

    fun create(ctx: RoutingContext) {
        val dto = ctx.body().asJsonObject()
        val options = DeliveryOptions()
        options.addHeader("action", KycEBAction.CREATE)
        ctx.vertx().eventBus().request<JsonObject>(EBAddress.KYC, dto, options)
            .onSuccess { ctx.json(it.body()) }
            .onFailure { ctx.json(AppResponse.Failure("Failed to execute service: ${it.message}")) }
    }

    fun update(ctx: RoutingContext) {
        val dto = ctx.body().asJsonObject()
        val options = DeliveryOptions()
        options.addHeader("action", KycEBAction.UPDATE)
        ctx.vertx().eventBus().request<JsonObject>(EBAddress.KYC, dto, options)
            .onSuccess { ctx.json(it.body()) }
            .onFailure { ctx.json(AppResponse.Failure("Failed to execute service: ${it.message}")) }
    }


    fun list(ctx: RoutingContext) {
        MDC.put("requestId", "1234567890aaa")
        val payload = JsonObject.of(
            "page", ctx.request().getParam("page", "1").toIntOrNull(),
            "size", ctx.request().getParam("size", "50").toIntOrNull()
        )
        val options = DeliveryOptions()
        options.addHeader("action", KycEBAction.LIST)
        ctx.vertx().eventBus().request<JsonObject>(EBAddress.KYC, payload, options)
            .onSuccess { ctx.json(it.body()) }
            .onFailure { ctx.json(AppResponse.Failure("Failed to execute service: ${it.message}")) }
    }

    fun search(ctx: RoutingContext) {
        val spec = KycSpec()
        spec.page = ctx.request().getParam(KycSpec::page.name, "1").toInt()
        spec.size = ctx.request().getParam(KycSpec::size.name, "50").toInt()
        spec.sortBy = ctx.request().getParam(KycSpec::sortBy.name, KycSpec::createdAt.name)
        spec.sortOrder =
            ctx.request().getParam(KycSpec::sortOrder.name, SortOrder.DESC.name)?.let { SortOrder.valueOf(it) }
        spec.firstName = ctx.request().getParam(KycSpec::firstName.name)
        spec.lastName = ctx.request().getParam(KycSpec::lastName.name)
        spec.otherName = ctx.request().getParam(KycSpec::otherName.name)
        spec.occupation = ctx.request().getParam(KycSpec::occupation.name)
        spec.status = ctx.request().getParam(KycSpec::status.name)?.let(VerificationStatus::valueOf)
        spec.startDate = ctx.request().getParam(KycSpec::startDate.name)?.let(LocalDateTime::parse)
        spec.dateOfBirth = ctx.request().getParam(KycSpec::dateOfBirth.name)?.let(LocalDate::parse)
        spec.endDate = ctx.request().getParam(KycSpec::endDate.name)?.let(LocalDateTime::parse)
        spec.createdAt = ctx.request().getParam(KycSpec::createdAt.name)?.let(LocalDateTime::parse)
        spec.modifiedAt = ctx.request().getParam(KycSpec::modifiedAt.name)?.let(LocalDateTime::parse)

        val payload = JsonObject.mapFrom(spec)

        val options = DeliveryOptions()
        options.addHeader("action", KycEBAction.SEARCH)
        ctx.vertx().eventBus().request<JsonObject>(EBAddress.KYC, payload, options)
            .onSuccess { ctx.json(it.body()) }
            .onFailure { ctx.json(AppResponse.Failure("Failed to execute service: ${it.message}")) }
    }

    fun deleteById(ctx: RoutingContext) {
        val payload = JsonObject.of("id", ctx.pathParam("id"))
        val options = DeliveryOptions()
        options.addHeader("action", KycEBAction.DELETE_BY_ID)
        ctx.vertx().eventBus().request<JsonObject>(EBAddress.KYC, payload, options)
            .onSuccess { ctx.json(it.body()) }
            .onFailure { ctx.json(AppResponse.Failure("Failed to execute service: ${it.message}")) }
    }
}