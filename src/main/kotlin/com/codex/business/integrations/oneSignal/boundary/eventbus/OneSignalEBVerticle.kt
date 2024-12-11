package com.codex.business.integrations.oneSignal.boundary.eventbus

import com.codex.base.shared.AppResponse
import com.codex.business.common.EBAction
import com.codex.business.common.EBAddress
import com.codex.business.integrations.oneSignal.service.OneSignalService
import io.vertx.core.AbstractVerticle
import io.vertx.core.eventbus.Message
import io.vertx.core.json.JsonObject
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.slf4j.LoggerFactory

class OneSignalEBVerticle : AbstractVerticle(), KoinComponent {
    private val logger = LoggerFactory.getLogger(OneSignalEBVerticle::class.java)
    private val service: OneSignalService by inject()


    override fun start() {
        logger.info("Started EB consumer verticle for {} service", EBAddress.ONE_SIGNAL)
        vertx.eventBus().consumer(EBAddress.ONE_SIGNAL, ::onMessage)
    }

    private fun onMessage(msg: Message<JsonObject>) {
        val action = msg.headers()["action"]

        when (action) {
            EBAction.OneSignalEBAction.PUSH_NOTIFICATION -> {
                service.pushNotification(msg)
            }

            else -> {
                val errMsg = "No handler found for action-> [$action]"
                logger.error(errMsg)
                AppResponse.Error(errMsg)
            }
        }
    }
}