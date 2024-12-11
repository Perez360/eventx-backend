package com.codex.business.components.auth.boundary.http

import com.codex.base.shared.AppResponse
import com.codex.business.components.auth.dto.LoginDto
import com.codex.business.components.auth.dto.RegisterDto
import com.codex.business.components.auth.service.AuthService
import com.codex.business.components.event.dto.CreateEventDto
import io.vertx.core.json.JsonObject
import io.vertx.ext.web.RoutingContext
import io.vertx.kotlin.core.json.get
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.slf4j.LoggerFactory
import java.time.LocalDateTime

object AuthRouterHandler : KoinComponent {
    private val service: AuthService by inject()
    private val logger = LoggerFactory.getLogger(this::class.java)

    fun ping(ctx: RoutingContext) {
        logger.info("Ping route triggered")
        ctx.json(service.ping())
    }

    fun login(ctx: RoutingContext) {
        val payload = ctx.body().asJsonObject()
        logger.info("Login route triggered with payload: $payload")
        val validationRes = validateLoginDto(payload)
        if (validationRes != null) {
            ctx.json(AppResponse.Failure(validationRes))
            return
        }

        val dto = payload.mapTo(LoginDto::class.java)
        ctx.json(service.login(dto))
    }

    fun register(ctx: RoutingContext) {
        val payload = ctx.body().asJsonObject()
        logger.info("Register route triggered with payload: $payload")
        val validationRes = validateRegisterDto(payload)
        if (validationRes != null) {
            ctx.json(AppResponse.Failure(validationRes))
            return
        }
        val dto = payload.mapTo(RegisterDto::class.java)
        ctx.json(service.register(dto))
    }

    private fun validateLoginDto(payload: JsonObject?): String? {
        return when {
            payload == null -> "Request body is required"
            payload.getString(LoginDto::identifier.name).isNullOrBlank() -> "Email or Phone is required"
            payload.getString(LoginDto::password.name).isNullOrBlank() -> "Password is required"
            else -> null
        }
    }

    private fun validateRegisterDto(payload: JsonObject?): String? {
        return when {
            payload == null -> "Request body is required"
            payload.getString(CreateEventDto::title.name).isNullOrBlank() -> "Title is required"
            payload.getString(CreateEventDto::venue.name) == null -> "Venue is required"
            payload.get<LocalDateTime?>(CreateEventDto::startDate.name) == null -> "Start date is required"
            payload.get<LocalDateTime?>(CreateEventDto::endDate.name) == null -> "End date is required"
            else -> null
        }
    }
}