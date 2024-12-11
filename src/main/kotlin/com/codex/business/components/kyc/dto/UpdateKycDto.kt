package com.codex.business.components.kyc.dto

import com.codex.business.components.user.enum.VerificationStatus


data class UpdateKycDto(
    var id: String? = null,
    var firstName: String? = null,
    var lastName: String? = null,
    var otherName: String? = null,
    var nationality: String? = null,
    var status: VerificationStatus? = null,
)