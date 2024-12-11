package com.codex.business.components.eventReaction.dto

import com.codex.business.components.eventReaction.enum.ReactionType


data class AddEventReactionDto(
    var userId: String? = null,
    var eventId: String? = null,
    var type: ReactionType? = null,
)