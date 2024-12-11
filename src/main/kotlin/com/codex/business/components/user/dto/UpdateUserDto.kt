package com.codex.business.components.user.dto

import com.codex.business.common.enums.Status


data class UpdateUserDto(
    val id: String? = null,
    val firstName: String? = null,
    val lastName: String? = null,
    val otherName: String? = null,
    val status: Status? = null,
    val role: String? = null
)