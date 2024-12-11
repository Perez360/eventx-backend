package com.codex.business.components.userProfile.dto

import io.vertx.core.json.JsonObject
import java.time.LocalDateTime

data class UserProfileDto(
    val id: String? = null,
    val firstName: String? = null,
    val lastName: String? = null,
    val otherName: String? = null,
    val email: String? = null,
//    @JsonIgnore
    val password: String? = null,
    val role: String? = null,
    val createdAt: LocalDateTime? = null,
    val modifiedAt: LocalDateTime? = null,
    val version: Long? = null
) {
    fun toJson(): JsonObject = JsonObject.mapFrom(this)
}