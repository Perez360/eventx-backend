package com.codex.business.components.eventTag.dto

import com.codex.business.common.enums.Status
import io.vertx.core.json.JsonObject
import java.time.LocalDateTime


data class EventTagDto(
    var id: String? = null,
    var name: String? = null,
    var description: String? = null,
    var status: Status? = null,
    val createdAt: LocalDateTime? = null,
    var modifiedAt: LocalDateTime? = null,
    val version: Long? = null
) {
    fun toJson(): JsonObject = JsonObject.mapFrom(this)
}