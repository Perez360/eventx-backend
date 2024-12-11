package com.codex.business.integrations.oneSignal.service

import com.codex.base.shared.AppResponse
import com.codex.base.shared.Texts
import com.codex.base.utils.toJson
import com.codex.business.integrations.oneSignal.boundary.eventbus.OneSignalEBVerticle
import io.vertx.core.Vertx
import io.vertx.core.eventbus.Message
import io.vertx.core.json.JsonObject
import io.vertx.ext.web.client.WebClient
import io.vertx.ext.web.client.WebClientOptions
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.slf4j.LoggerFactory

class OneSignalServiceImpl : OneSignalService, KoinComponent {
    private val vertx: Vertx by inject()
    private val logger = LoggerFactory.getLogger(OneSignalEBVerticle::class.java)

    override fun pushNotification(msg: Message<JsonObject>) {
        val body = msg.body()
        logger.info("Received push notification request with payload: $body")

        val client = WebClient.create(vertx, WebClientOptions().setSsl(true))
        client.post(Texts.ONESIGNAL_URL)
            .sendJsonObject(body)
            .onSuccess {
                logger.info("Push notification sent successfully")
                msg.reply(AppResponse.Success("Push notification sent successfully", null).toJson())
            }
            .onFailure { cause ->
                logger.error("An error occurred whiles sending push notification", cause)
                msg.reply(AppResponse.Failure("Failed to send push notification: $cause"))
            }
    }
}