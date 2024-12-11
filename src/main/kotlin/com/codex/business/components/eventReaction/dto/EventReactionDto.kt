package com.codex.business.components.eventReaction.dto

import com.codex.business.components.eventReaction.enum.ReactionType
import java.time.LocalDateTime


data class EventReactionDto(
    var id: String? = null,
    var userId: String? = null,
    var eventId: String? = null,
    var type: ReactionType? = null,
    val createdAt: LocalDateTime? = null,
    var modifiedAt: LocalDateTime? = null,
    val version: Long? = null
)