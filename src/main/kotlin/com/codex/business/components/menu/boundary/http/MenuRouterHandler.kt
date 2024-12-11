package com.codex.business.components.menu.boundary.http

import com.codex.base.shared.AppResponse
import com.codex.business.components.menu.dto.CreateMenuDto
import com.codex.business.components.menu.dto.UpdateMenuDto
import com.codex.business.components.menu.service.MenuService
import io.vertx.core.json.JsonObject
import io.vertx.ext.web.RoutingContext
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.slf4j.LoggerFactory

object MenuRouterHandler : KoinComponent {
    private val service: MenuService by inject()
    private val logger = LoggerFactory.getLogger(this::class.java)

    fun ping(ctx: RoutingContext) {
        logger.info("Ping route triggered")
        ctx.json(service.ping())
    }

    fun findById(ctx: RoutingContext) {
        logger.info("Find menu by Id route triggered with id: ${ctx.pathParam("id")}")
        ctx.json(service.findById(ctx.pathParam("id")))
    }

    fun create(ctx: RoutingContext) {
        logger.info("Create menu route triggered with payload: ${ctx.body().asJsonObject()}")
        val payload = ctx.body().asJsonObject()
        val validationRes = validateAddEventDto(payload)
        if (validationRes != null) {
            ctx.json(AppResponse.Failure(validationRes))
            return
        }
        val dto = payload.mapTo(CreateMenuDto::class.java)
        ctx.json(service.create(dto))
    }

    fun update(ctx: RoutingContext) {
        logger.info("Update menu route triggered with payload: ${ctx.body().asJsonObject()}")
        val payload = ctx.body().asJsonObject()

        val validationRes = validateUpdateEventDto(payload)
        if (validationRes != null) {
            ctx.json(AppResponse.Failure(validationRes))
            return
        }
        val dto = payload.mapTo(UpdateMenuDto::class.java)
        ctx.json(service.update(dto))
    }


    fun list(ctx: RoutingContext) {
        logger.info("List menus route triggered with query: ${ctx.queryParams()}")
        val page = ctx.request().getParam("page", "1").toInt()
        val size = ctx.request().getParam("size", "50").toInt()
        ctx.json(service.list(page, size))
    }

    fun deleteById(ctx: RoutingContext) {
        logger.info("Delete menu by Id route triggered id: ${ctx.pathParam("id")}")
        ctx.json(service.deleteById(ctx.pathParam("id")))
    }

    private fun validateAddEventDto(payload: JsonObject): String? {
        return when {
            payload.getString(CreateMenuDto::name.name).isNullOrBlank() -> "Name is required"
            payload.getString(CreateMenuDto::link.name) == null -> "Link is required"
            else -> null
        }
    }

    private fun validateUpdateEventDto(payload: JsonObject): String? {
        return when {
            payload.getString(UpdateMenuDto::id.name).isNullOrBlank() -> "Id is required"
            payload.getString(UpdateMenuDto::name.name).isNullOrBlank() -> "Name is required"
            payload.getString(UpdateMenuDto::link.name) == null -> "Link is required"
            else -> null
        }
    }
}