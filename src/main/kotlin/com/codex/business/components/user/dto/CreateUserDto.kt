package com.codex.business.components.user.dto


data class CreateUserDto(
    var firstName: String? = null,
    var lastName: String? = null,
    var otherName: String? = null,
    var email: String? = null,
    var phone: String? = null,
    var password: String? = null,
    var role: String? = null
)