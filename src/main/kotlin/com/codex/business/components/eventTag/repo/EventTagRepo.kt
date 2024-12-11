package com.codex.business.components.eventTag.repo

import com.codex.base.shared.PagedContent
import com.codex.business.components.eventTag.dto.EventTagDto
import com.codex.business.components.eventTag.spec.EventTagSpec

interface EventTagRepo {
    fun create(eventTag: EventTag): EventTag

    fun update(eventTag: EventTag): EventTag

    fun findById(id: String): EventTag?

    fun exists(id: String): Boolean

    fun count(): Long

    fun list(page: Int = 1, size: Int = 50): PagedContent<EventTagDto>

    fun search(spec: EventTagSpec): PagedContent<EventTagDto>

    fun deleteById(id: String): EventTag?

    fun deleteAll(): Boolean
}