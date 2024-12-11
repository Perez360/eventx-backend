package com.codex.base.exceptions

import com.codex.base.shared.AppResponse
import com.codex.base.utils.toJson
import dev.morphia.mapping.validation.ConstraintViolationException
import io.vertx.core.Handler
import io.vertx.core.buffer.Buffer
import io.vertx.ext.web.RoutingContext
import org.slf4j.LoggerFactory
import java.time.DateTimeException
import java.time.format.DateTimeParseException

object GlobalExceptionHandler : Handler<RoutingContext> {
    private val logger = LoggerFactory.getLogger(this::class.java)

    override fun handle(ctx: RoutingContext) {
        val cause = ctx.failure()

        val status: Int
        val response: AppResponse<Nothing>

        when (cause) {
            is ServiceException,
            is IllegalArgumentException,
            is DateTimeParseException,
            is DateTimeException,
            is ConstraintViolationException -> {
                status = 400
                response = AppResponse.Failure("An error occurred: ${cause.message}")
            }

            else -> {
                status = 500
                response = AppResponse.Error("An unexpected error occurred: ${cause.message}")
            }
        }

        logger.error("[GlobalExceptionHandler]: MESSAGE: {} || HTTP STATUS: {}", response.message, status, cause)
        ctx.response().setStatusCode(status)
            .putHeader("content-type","application/json")
            .end(response.toJson().encodePrettily())
    }
}