package com.codex.business.components.activityLog.dto

import java.time.LocalDateTime

data class ActivityLogDTO(
    var id: String? = null,
    var description: String? = null,
    var data: Map<String, Any>? = null,
    val type: String? = null,
    var createdAt: LocalDateTime? = null,
    var version: Long? = null
)