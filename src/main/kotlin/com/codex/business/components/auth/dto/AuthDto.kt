package com.codex.business.components.auth.dto

import com.codex.business.components.user.dto.UserDto

data class AuthDto(
    val token: String? = null,
    val user: UserDto?=null
)
