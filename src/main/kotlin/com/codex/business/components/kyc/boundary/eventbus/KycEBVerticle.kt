package com.codex.business.components.kyc.boundary.eventbus

import com.codex.base.exceptions.ServiceException
import com.codex.base.shared.AppResponse
import com.codex.base.utils.toJson
import com.codex.business.common.EBAction.KycEBAction
import com.codex.business.common.EBAddress
import com.codex.business.components.kyc.dto.CreateKycDto
import com.codex.business.components.kyc.dto.UpdateKycDto
import com.codex.business.components.kyc.service.KycService
import com.codex.business.components.kyc.spec.KycSpec
import io.vertx.core.AbstractVerticle
import io.vertx.core.eventbus.Message
import io.vertx.core.json.DecodeException
import io.vertx.core.json.Json
import io.vertx.core.json.JsonObject
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class KycEBVerticle : AbstractVerticle(), KoinComponent {
    private val service: KycService by inject()

    private val logger: Logger = LoggerFactory.getLogger(this::class.java)

    override fun start() {
        logger.info("Started EB consumer verticle for kyc {}", EBAddress.KYC)
        vertx.eventBus().consumer(EBAddress.KYC, ::onMessage)
    }

    private fun onMessage(message: Message<JsonObject>) {
        val address = message.address()
        val action = message.headers()["action"]
        val body = message.body()

        logger.info("Receiving message from address: {} for action: {} with body: {}", address, action, body)

        val apiAppResponse = try {
            when (action) {

                KycEBAction.PING -> service.ping()

                KycEBAction.CREATE -> {
                    val dto = Json.decodeValue(body.encode(), CreateKycDto::class.java)
                    service.create(dto)
                }

                KycEBAction.UPDATE -> {
                    val dto = Json.decodeValue(body.encode(), UpdateKycDto::class.java)
                    service.update(dto)
                }

                KycEBAction.GET_BY_ID -> {
                    val id = body.getString("id")
                    service.findById(id)
                }

                KycEBAction.LIST -> {
                    val page = body.getInteger("page", 1)
                    val size = body.getInteger("size", 50)
                    service.list(page, size)
                }

                KycEBAction.SEARCH -> {
                    val contactSpec = body.mapTo(KycSpec::class.java)
                    service.search(contactSpec)
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