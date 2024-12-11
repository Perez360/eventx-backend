package com.codex.business.components.file.boundary.eventbus

import com.codex.base.exceptions.ServiceException
import com.codex.base.shared.AppResponse
import com.codex.base.utils.toJson
import com.codex.business.common.EBAction.FileEBAction
import com.codex.business.common.EBAddress
import com.codex.business.components.file.boundary.dto.FileDTO
import com.codex.business.components.file.service.FileService
import io.vertx.core.AbstractVerticle
import io.vertx.core.buffer.Buffer
import io.vertx.core.eventbus.Message
import io.vertx.core.json.DecodeException
import io.vertx.core.json.Json
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class FileEBVerticle : AbstractVerticle(), KoinComponent {
    private val service: FileService by inject()

    private val logger: Logger = LoggerFactory.getLogger(this::class.java)

    override fun start() {
        logger.info("Started EB consumer verticle for file {}", EBAddress.FILE)
        vertx.eventBus().consumer(EBAddress.FILE, ::onMessage)
    }

    private fun onMessage(message: Message<Buffer>) {
        val address = message.address()
        val action = message.headers()["action"]
        val body = message.body()

        logger.info("Receiving message from address: {} for action: {} with body: {}", address, action, body)

        try {
            when (action) {

                FileEBAction.UPLOAD -> {
                    val dto = Json.decodeValue(body, FileDTO::class.java)
                    message.reply(service.upload(dto).toJson().toBuffer())
                }

                FileEBAction.DOWNLOAD -> {
                    message.reply(Buffer.buffer(service.download(body.toString())))
                }

                else -> {
                    val errMsg = "No handler found for action-> [$action]"
                    logger.error(errMsg)
                    message.reply(AppResponse.Failure(errMsg).toJson().toBuffer())
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
    }
}