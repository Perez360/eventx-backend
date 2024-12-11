package com.codex.business.components.eventReaction.repo

import com.codex.base.shared.PagedContent
import com.codex.business.components.eventReaction.dto.EventReactionDto
import com.codex.business.components.eventReaction.spec.EventReactionSpec

interface EventReactionRepo {
    fun create(eventReaction: EventReaction): EventReaction

    fun update(media: EventReaction): EventReaction

    fun findById(id: String): EventReaction?

    fun exists(id: String): Boolean

    fun count(): Long

    fun list(page: Int = 1, size: Int = 50): PagedContent<EventReactionDto>

    fun search(spec: EventReactionSpec): PagedContent<EventReactionDto>

    fun deleteById(id: String): EventReaction?
}