package com.codex.business.components.userProfile.dto

import com.codex.business.common.enums.Status


data class UpdateUserProfileDto(
    val id: String? = null,
    val firstName: String? = null,
    val lastName: String? = null,
    val otherName: String? = null,
    val status: Status? = null,
    val role: String? = null
)