package com.codex.business.components.category.dto

import com.codex.business.common.enums.Status


data class UpdateCategoryDto(
    var id: String? = null,
    var name: String? = null,
    var description: String? = null,
    var status: Status? = null,
)