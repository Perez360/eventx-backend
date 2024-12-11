package com.codex.business.components.activityLog.dto

data class AddActivityLogDTO(
    var description: String? = null,
    var data: Map<String, Any>? = null,
    val type: String? = null,
)