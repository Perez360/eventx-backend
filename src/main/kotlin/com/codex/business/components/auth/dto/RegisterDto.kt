package com.codex.business.components.auth.dto

data class RegisterDto(
    val identifier: String? = null,
    val username: String? = null,
    val firstName: String? = null,
    val lastName: String? = null,
    val otherName: String? = null,
    val role: String? = null,
    val password: String? = null,
    val confirmPassword: String? = null,
    val termsAccepted: Boolean? = null
)
