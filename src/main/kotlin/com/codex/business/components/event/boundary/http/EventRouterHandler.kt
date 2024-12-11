package com.codex.business.components.event.boundary.http

import com.codex.base.enums.SortOrder
import com.codex.base.shared.AppResponse
import com.codex.business.components.event.dto.CreateEventDto
import com.codex.business.components.event.dto.UpdateEventDto
import com.codex.business.components.event.enums.EventStatus
import com.codex.business.components.event.service.EventService
import com.codex.business.components.event.spec.EventSpec
import io.vertx.core.json.JsonObject
import io.vertx.ext.web.RoutingContext
import io.vertx.kotlin.core.json.get
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.slf4j.LoggerFactory
import java.time.LocalDateTime

object EventRouterHandler : KoinComponent {
    private val service: EventService by inject()
    private val logger = LoggerFactory.getLogger(this::class.java)

    fun ping(ctx: RoutingContext) {
        logger.info("Ping route triggered")
        ctx.json(service.ping())
    }

    fun findById(ctx: RoutingContext) {
        logger.info("Find event by Id route triggered with id: ${ctx.pathParam("id")}")
        ctx.json(service.findById(ctx.pathParam("id")))
    }

    fun create(ctx: RoutingContext) {
        logger.info("Create event route triggered with payload: ${ctx.body().asJsonObject()}")
        val payload = ctx.body().asJsonObject()
        val validationRes = validateAddEventDto(payload)
        if (validationRes != null) {
            ctx.json(AppResponse.Failure(validationRes))
            return
        }
        val dto = payload.mapTo(CreateEventDto::class.java)
        ctx.json(service.create(dto))
    }

    fun update(ctx: RoutingContext) {
        logger.info("Update event route triggered with payload: ${ctx.body().asJsonObject()}")
        val payload = ctx.body().asJsonObject()

        val validationRes = validateUpdateEventDto(payload)
        if (validationRes != null) {
            ctx.json(AppResponse.Failure(validationRes))
            return
        }
        val dto = payload.mapTo(UpdateEventDto::class.java)
        ctx.json(service.update(dto))
    }


    fun list(ctx: RoutingContext) {
        logger.info("List events route triggered with query: ${ctx.queryParams()}")
        val page = ctx.request().getParam("page", "1").toInt()
        val size = ctx.request().getParam("size", "50").toInt()
        ctx.json(service.list(page, size))
    }

    fun search(ctx: RoutingContext) {
        logger.info("Search events route triggered with query: ${ctx.queryParams()}")
        val spec = EventSpec()
        spec.page = ctx.request().getParam(EventSpec::page.name, "1").toInt()
        spec.size = ctx.request().getParam(EventSpec::size.name, "50").toInt()
        spec.sortBy = ctx.request().getParam(EventSpec::sortBy.name, EventSpec::createdAt.name)
        spec.sortOrder = ctx.request().getParam(EventSpec::sortOrder.name, SortOrder.DESC.name)?.let(SortOrder::valueOf)
        spec.title = ctx.request().getParam(EventSpec::title.name)
        spec.description = ctx.request().getParam(EventSpec::description.name)
        spec.isPublic = ctx.request().getParam(EventSpec::isPublic.name)?.toBooleanStrict()
        spec.status = ctx.request().getParam(EventSpec::status.name)?.let(EventStatus::valueOf)
        spec.venue = ctx.request().getParam(EventSpec::venue.name)
        spec.startDate = ctx.request().getParam(EventSpec::startDate.name)?.let(LocalDateTime::parse)
        spec.endDate = ctx.request().getParam(EventSpec::endDate.name)?.let(LocalDateTime::parse)
        spec.createdAt = ctx.request().getParam(EventSpec::createdAt.name)?.let(LocalDateTime::parse)
        spec.modifiedAt = ctx.request().getParam(EventSpec::modifiedAt.name)?.let(LocalDateTime::parse)
        ctx.json(service.search(spec))
    }

    fun deleteById(ctx: RoutingContext) {
        logger.info("delete event by Id route triggered id: ${ctx.pathParam("id")}")
        ctx.json(service.deleteById(ctx.pathParam("id")))
    }

    private fun validateAddEventDto(payload: JsonObject): String? {
        return when {
            payload.getString(CreateEventDto::title.name).isNullOrBlank() -> "Title is required"
            payload.getString(CreateEventDto::venue.name) == null -> "Venue is required"
            payload.get<LocalDateTime?>(CreateEventDto::startDate.name) == null -> "Start date is required"
            payload.get<LocalDateTime?>(CreateEventDto::endDate.name) == null -> "End date is required"
            else -> null
        }
    }

    private fun validateUpdateEventDto(payload: JsonObject): String? {
        return when {
            payload.getString(UpdateEventDto::id.name).isNullOrBlank() -> "Id is required"
            payload.getString(UpdateEventDto::title.name).isNullOrBlank() -> "Title is required"
            payload.getString(UpdateEventDto::venue.name) == null -> "Venue is required"
            payload.get<LocalDateTime?>(UpdateEventDto::startDate.name) == null -> "Start date is required"
            payload.get<LocalDateTime?>(UpdateEventDto::endDate.name) == null -> "End date is required"
            else -> null
        }
    }
}