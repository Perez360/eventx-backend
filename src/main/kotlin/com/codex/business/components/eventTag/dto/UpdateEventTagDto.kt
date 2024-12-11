package com.codex.business.components.eventTag.dto

import com.codex.business.common.enums.Status


data class UpdateEventTagDto(
    var id: String? = null,
    var name: String? = null,
    var description: String? = null,
    var status: Status? = null,
)