package com.codex.business.components.event.repo

import com.codex.base.shared.PagedContent
import com.codex.business.components.event.dto.EventDto
import com.codex.business.components.event.spec.EventSpec

interface EventRepo {
    fun create(event: Event): Event

    fun update(event: Event): Event

    fun findById(id: String): Event?

    fun exists(id: String): Boolean

    fun count(): Long

    fun list(page: Int = 1, size: Int = 50): PagedContent<EventDto>

    fun search(spec: EventSpec): PagedContent<EventDto>

    fun deleteById(id: String): Event?

    fun deleteAll(): Boolean
}