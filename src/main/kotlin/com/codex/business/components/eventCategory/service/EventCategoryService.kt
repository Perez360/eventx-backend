package com.codex.business.components.eventCategory.service

import com.codex.base.shared.AppResponse
import com.codex.base.shared.PagedContent
import com.codex.business.components.eventCategory.dto.AddEventCategoryDto
import com.codex.business.components.eventCategory.dto.EventCategoryDto
import com.codex.business.components.eventCategory.dto.UpdateEventCategoryDto
import com.codex.business.components.eventCategory.repo.EventCategory
import com.codex.business.components.eventCategory.spec.EventCategorySpec

interface EventCategoryService {
    fun ping(): AppResponse<Unit>

    fun create(dto: AddEventCategoryDto): AppResponse<EventCategory>

    fun update(dto: UpdateEventCategoryDto): AppResponse<EventCategory>

    fun findById(id: String): AppResponse<EventCategory>

    fun list(page: Int = 1, size: Int = 50): AppResponse<PagedContent<EventCategoryDto>>

    fun search(spec: EventCategorySpec): AppResponse<PagedContent<EventCategoryDto>>

    fun deleteById(id: String): AppResponse<EventCategory>
}