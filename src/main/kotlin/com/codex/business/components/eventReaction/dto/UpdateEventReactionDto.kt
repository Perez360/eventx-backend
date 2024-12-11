package com.codex.business.components.eventReaction.dto

import com.codex.business.components.eventReaction.enum.ReactionType


data class UpdateEventReactionDto(
    var id: String? = null,
    var userId: String? = null,
    var eventId: String? = null,
    var type: ReactionType? = null,
)