package com.codex.business.components.event.dto

import com.codex.business.components.event.enums.EventStatus
import java.time.LocalDateTime


data class UpdateEventDto(
    var id: String? = null,
    var title: String? = null,
    var description: String? = null,
    var venue: String? = null,
    var isPublic: Boolean? = null,
    var status: EventStatus? = null,
    var startDate: LocalDateTime? = null,
    var endDate: LocalDateTime? = null,
    var tags: MutableSet<String>? = null,
    var categories: MutableSet<String>? = null,
)