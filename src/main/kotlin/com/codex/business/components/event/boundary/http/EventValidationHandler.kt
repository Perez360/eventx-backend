package com.codex.business.components.event.boundary.http

import com.codex.base.shared.AppResponse
import com.codex.business.components.event.dto.CreateEventDto
import com.codex.business.components.event.dto.UpdateEventDto
import io.vertx.core.Handler
import io.vertx.core.http.HttpMethod
import io.vertx.core.json.JsonObject
import io.vertx.ext.web.RoutingContext
import io.vertx.kotlin.core.json.get
import java.time.LocalDateTime

object EventValidationHandler : Handler<RoutingContext> {
    override fun handle(ctx: RoutingContext) {
        when (ctx.request().method()) {
            HttpMethod.POST -> validateAddEventDto(
                ctx.body()?.asJsonObject()
            )?.let { ctx.json(AppResponse.Failure(it)) }

            HttpMethod.PUT -> validateUpdateEventDto(
                ctx.body()?.asJsonObject()
            )?.let { ctx.json(AppResponse.Failure(it)) }

            else -> ctx.next()
        }
    }

    private fun validateAddEventDto(payload: JsonObject?): String? {
        return when {
            payload == null -> "Body is required"
            payload.getString(CreateEventDto::title.name).isNullOrBlank() -> "Title is required"
            payload.getString(CreateEventDto::venue.name) == null -> "Venue is required"
            payload.get<LocalDateTime?>(CreateEventDto::startDate.name) == null -> "Start date is required"
            payload.get<LocalDateTime?>(CreateEventDto::endDate.name) == null -> "End date is required"
            else -> null
        }
    }

    private fun validateUpdateEventDto(payload: JsonObject?): String? {
        return when {
            payload == null -> "Body is required"
            payload.getString(UpdateEventDto::id.name).isNullOrBlank() -> "Id is required"
            payload.getString(UpdateEventDto::title.name).isNullOrBlank() -> "Title is required"
            payload.getString(UpdateEventDto::venue.name) == null -> "Venue is required"
            payload.get<LocalDateTime?>(UpdateEventDto::startDate.name) == null -> "Start date is required"
            payload.get<LocalDateTime?>(UpdateEventDto::endDate.name) == null -> "End date is required"
            else -> null
        }
    }
}