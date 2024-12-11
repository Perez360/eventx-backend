package com.codex.business.components.eventCategory.repo

import com.codex.base.shared.PagedContent
import com.codex.business.components.eventCategory.dto.EventCategoryDto
import com.codex.business.components.eventCategory.spec.EventCategorySpec

interface EventCategoryRepo {
    fun create(eventCategory: EventCategory): EventCategory

    fun update(eventCategory: EventCategory): EventCategory

    fun findById(id: String): EventCategory?

    fun exists(id: String): Boolean

    fun count(): Long

    fun list(page: Int = 1, size: Int = 50): PagedContent<EventCategoryDto>

    fun search(spec: EventCategorySpec): PagedContent<EventCategoryDto>

    fun deleteById(id: String): EventCategory?

    fun deleteAll(): Boolean
}