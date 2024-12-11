package com.codex.business.components.event.boundary.eventbus

import com.codex.base.exceptions.ServiceException
import com.codex.base.shared.AppResponse
import com.codex.base.utils.toJson
import com.codex.business.common.EBAction.EventEBAction
import com.codex.business.common.EBAddress
import com.codex.business.components.event.dto.CreateEventDto
import com.codex.business.components.event.dto.UpdateEventDto
import com.codex.business.components.event.service.EventService
import com.codex.business.components.event.spec.EventSpec
import io.vertx.core.AbstractVerticle
import io.vertx.core.eventbus.Message
import io.vertx.core.json.DecodeException
import io.vertx.core.json.Json
import io.vertx.core.json.JsonObject
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class EventEBVerticle : AbstractVerticle(), KoinComponent {
    private val service: EventService by inject()

    private val logger: Logger = LoggerFactory.getLogger(this::class.java)


    override fun start() {
        logger.info("Started EB consumer verticle for {} service", EBAddress.EVENT)
        vertx.eventBus().consumer(EBAddress.EVENT, ::onMessage)
    }

    private fun onMessage(message: Message<JsonObject>) {
        val address = message.address()
        val action = message.headers()["action"]
        val body = message.body()

        logger.info("Receiving message from address: {} for action: {} with body: {}", address, action, body)

        val apiAppResponse = try {
            when (action) {

                EventEBAction.PING -> service.ping()

                EventEBAction.CREATE -> {
                    val dto = Json.decodeValue(body.encode(), CreateEventDto::class.java)
                    service.create(dto)
                }

                EventEBAction.UPDATE -> {
                    val dto = Json.decodeValue(body.encode(), UpdateEventDto::class.java)
                    service.update(dto)
                }

                EventEBAction.GET_BY_ID -> {
                    val id = body.getString("id")
                    service.findById(id)
                }

                EventEBAction.LIST -> {
                    val page = body.getInteger("page", 1)
                    val size = body.getInteger("size", 50)
                    service.list(page, size)
                }

                EventEBAction.SEARCH -> {
                    val spec = body.mapTo(EventSpec::class.java)
                    service.search(spec)
                }

                EventEBAction.DELETE_BY_ID -> {
                    val id = body.getString("id")
                    service.deleteById(id)
                }

                else -> {
                    val errMsg = "No handler found for action-> [$action]"
                    logger.error(errMsg)
                    AppResponse.Error(errMsg)
                }
            }
        } catch (decEx: DecodeException) {
            logger.error("$action could not be performed", decEx)
            AppResponse.Error("Error decoding payload when performing action-> [$action]: ${decEx.message}")
        } catch (ex: ServiceException) {
            logger.error("Failed to process request", ex)
            AppResponse.Failure("Failed to process request: ${ex.message}")
        } catch (ex: Exception) {
            logger.error("$action could not be performed", ex)
            AppResponse.Error("$action could not be performed: ${ex.message}")
        }
        message.reply(apiAppResponse.toJson())
    }
}