package com.codex.business.components.kyc.dto

import com.codex.business.components.user.enum.VerificationStatus
import java.time.LocalDate
import java.time.LocalDateTime


data class KycDto(
    var id: String? = null,
    var userId: String? = null,
    var firstName: String? = null,
    var lastName: String? = null,
    var otherName: String? = null,
    var nationality: String? = null,
    var dateOfBirth: LocalDate? = null,
    var status: VerificationStatus? = null,
    var createdAt: LocalDateTime? = null,
    var modifiedAt: LocalDateTime? = null,
    var version: Long? = null
)