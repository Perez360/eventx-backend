package com.codex.business.components.comment.dto

import com.codex.business.components.user.dto.UserDto
import io.vertx.core.json.JsonObject
import java.time.LocalDateTime

data class CommentDto(
    val id: String? = null,
    val message: String? = null,
    val user: UserDto? = null,
    val createdAt: LocalDateTime? = null,
    var modifiedAt: LocalDateTime? = null,
    val version: Long? = null
) {
    fun toJson(): JsonObject = JsonObject.mapFrom(this)
}