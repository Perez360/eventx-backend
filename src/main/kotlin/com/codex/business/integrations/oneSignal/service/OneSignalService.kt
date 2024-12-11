package com.codex.business.integrations.oneSignal.service

import io.vertx.core.eventbus.Message
import io.vertx.core.json.JsonObject

interface OneSignalService {
    fun pushNotification(msg: Message<JsonObject>)
}