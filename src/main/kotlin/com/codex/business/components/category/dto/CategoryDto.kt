package com.codex.business.components.category.dto

import com.codex.business.common.enums.Status
import io.vertx.core.json.JsonObject
import java.time.LocalDateTime


data class CategoryDto(
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