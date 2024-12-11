package com.codex.business.components.user.dto

import com.codex.business.common.enums.Status
import com.codex.business.components.kyc.dto.KycDto
import com.codex.business.components.userProfile.dto.UserProfileDto
import com.fasterxml.jackson.annotation.JsonIgnore
import io.vertx.core.json.JsonObject
import java.time.LocalDateTime

data class UserDto(
    val id: String? = null,
    val firstName: String? = null,
    val lastName: String? = null,
    val otherName: String? = null,
    val email: String? = null,
    val phone: String? = null,
    @JsonIgnore
    val password: String? = null,
    val status: Status? = null,
    val kyc: KycDto? = null,
    val profile: UserProfileDto? = null,
    val role: String? = null,
    val createdAt: LocalDateTime? = null,
    val modifiedAt: LocalDateTime? = null,
    val version: Long? = null
) {
    fun toJson(): JsonObject = JsonObject.mapFrom(this)
}