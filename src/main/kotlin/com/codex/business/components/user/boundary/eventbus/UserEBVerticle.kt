package com.codex.business.components.user.boundary.eventbus

import com.codex.base.exceptions.ServiceException
import com.codex.base.shared.AppResponse
import com.codex.base.utils.toJson
import com.codex.business.common.EBAction.UserEBAction
import com.codex.business.common.EBAddress
import com.codex.business.components.user.dto.CreateUserDto
import com.codex.business.components.user.dto.UpdateUserDto
import com.codex.business.components.user.service.UserService
import com.codex.business.components.user.spec.UserSpec
import io.vertx.core.AbstractVerticle
import io.vertx.core.eventbus.Message
import io.vertx.core.json.DecodeException
import io.vertx.core.json.Json
import io.vertx.core.json.JsonObject
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class UserEBVerticle : AbstractVerticle(), KoinComponent {
    private val service: UserService by inject()

    private val logger: Logger = LoggerFactory.getLogger(this::class.java)

    override fun start() {
        logger.info("Started EB consumer verticle for {} service", EBAddress.USER)
        vertx.eventBus().consumer(EBAddress.USER, ::onMessage)
    }

    private fun onMessage(message: Message<JsonObject>) {
        val address = message.address()
        val action = message.headers()["action"]
        val body = message.body()

        logger.info("Receiving message from address: {} for action: {} with body: {}", address, action, body)

        val apiAppResponse = try {
            when (action) {

                UserEBAction.PING -> service.ping()

                UserEBAction.CREATE -> {
                    val dto = Json.decodeValue(body.encode(), CreateUserDto::class.java)
                    service.create(dto)
                }

                UserEBAction.UPDATE -> {
                    val dto = Json.decodeValue(body.encode(), UpdateUserDto::class.java)
                    service.update(dto)
                }

                UserEBAction.GET_BY_ID -> {
                    val id = body.getString("id")
                    service.findById(id)
                }

                UserEBAction.LIST -> {
                    val page = body.getInteger("page", 1)
                    val size = body.getInteger("size", 50)
                    service.list(page, size)
                }

                UserEBAction.SEARCH -> {
                    val userSpec = body.mapTo(UserSpec::class.java)
                    service.search(userSpec)
                }

                UserEBAction.DELETE_BY_ID -> {
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