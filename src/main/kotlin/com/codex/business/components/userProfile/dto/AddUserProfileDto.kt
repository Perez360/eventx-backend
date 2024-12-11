package com.codex.business.components.userProfile.dto


data class AddUserProfileDto(
    var firstName: String? = null,
    var lastName: String? = null,
    var otherName: String? = null,
    var email: String? = null,
    var password: String? = null,
    var role: String? = null
)