package com.codex.business.components.event.service

import com.codex.base.shared.AppResponse
import com.codex.base.shared.PagedContent
import com.codex.business.components.event.dto.CreateEventDto
import com.codex.business.components.event.dto.EventDto
import com.codex.business.components.event.dto.UpdateEventDto
import com.codex.business.components.event.spec.EventSpec

interface EventService {
    fun ping(): AppResponse<Unit>

    fun create(dto: CreateEventDto): AppResponse<EventDto>

    fun update(dto: UpdateEventDto): AppResponse<EventDto>

    fun findById(id: String): AppResponse<EventDto>

    fun list(page: Int = 1, size: Int = 50): AppResponse<PagedContent<EventDto>>

    fun search(spec: EventSpec): AppResponse<PagedContent<EventDto>>

    fun deleteById(id: String): AppResponse<EventDto>
}