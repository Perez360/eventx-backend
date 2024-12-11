package com.codex.business.components.kyc.dto

import java.time.LocalDate


data class CreateKycDto(
    var userId: String? = null,
    var firstName: String? = null,
    var lastName: String? = null,
    var otherName: String? = null,
    var nationality: String? = null,
    var dateOfBirth: LocalDate? = null,
)