package com.codex.business.components.eventCategory.dto

import com.codex.business.common.enums.Status


data class UpdateEventCategoryDto(
    var id: String? = null,
    var name: String? = null,
    var description: String? = null,
    var status: Status? = null,
)