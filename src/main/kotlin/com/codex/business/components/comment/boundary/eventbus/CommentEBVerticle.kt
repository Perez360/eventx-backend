package com.codex.business.components.comment.boundary.eventbus

import com.codex.base.exceptions.ServiceException
import com.codex.base.shared.AppResponse
import com.codex.base.utils.toJson
import com.codex.business.common.EBAction.CommentEBAction
import com.codex.business.common.EBAddress
import com.codex.business.components.comment.dto.AddCommentDto
import com.codex.business.components.comment.dto.UpdateCommentDTO
import com.codex.business.components.comment.service.CommentService
import com.codex.business.components.comment.spec.CommentSpec
import io.vertx.core.AbstractVerticle
import io.vertx.core.eventbus.Message
import io.vertx.core.json.DecodeException
import io.vertx.core.json.Json
import io.vertx.core.json.JsonObject
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class CommentEBVerticle : AbstractVerticle(), KoinComponent {
    private val controller: CommentService by inject()

    private val logger: Logger = LoggerFactory.getLogger(this::class.java)

    override fun start() {
        logger.info("Started EB consumer verticle for department {}", EBAddress.COMMENT)
        vertx.eventBus().consumer(EBAddress.COMMENT, ::onMessage)
    }

    private fun onMessage(message: Message<JsonObject>) {
        val address = message.address()
        val action = message.headers()["action"]
        val body = message.body()

        logger.info("Receiving message from address: {} for action: {} with body: {}", address, action, body)

        val apiAppResponse = try {
            when (action) {

//                CommentEBAction.PING -> controller.ping()

                CommentEBAction.CREATE -> {
                    val dto = Json.decodeValue(body.encode(), AddCommentDto::class.java)
                    controller.create(dto)
                }

                CommentEBAction.UPDATE -> {
                    val dto = Json.decodeValue(body.encode(), UpdateCommentDTO::class.java)
                    controller.update(dto)
                }

                CommentEBAction.GET_BY_ID -> {
                    val id = body.getString("id")
                    controller.findById(id)
                }

                CommentEBAction.LIST -> {
                    val page = body.getInteger("page", 1)
                    val size = body.getInteger("size", 50)
                    controller.list(page, size)
                }

                CommentEBAction.SEARCH -> {
                    val spec = body.mapTo(CommentSpec::class.java)
                    controller.search(spec)
                }

                CommentEBAction.DELETE_BY_ID -> {
                    val id = body.getString("id")
                    controller.deleteById(id)
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