package com.codex.business.components.event.dto

import com.codex.business.components.comment.dto.CommentDto
import com.codex.business.components.event.enums.EventStatus
import com.codex.business.components.category.dto.CategoryDto
import com.codex.business.components.eventTag.dto.EventTagDto
import java.time.LocalDateTime


data class EventDto(
    var id: String? = null,
    var title: String? = null,
    var description: String? = null,
    var venue: String? = null,
    var isPublic: Boolean? = null,
    var imageLink: MutableSet<String>? = null,
    var reference: String? = null,
    var status: EventStatus? = null,
    var startDate: LocalDateTime? = null,
    var endDate: LocalDateTime? = null,
    var comments: MutableSet<CommentDto>? = null,
    var tags: MutableSet<EventTagDto>? = null,
    var categories: MutableSet<CategoryDto>? = null,
    val createdAt: LocalDateTime? = null,
    var modifiedAt: LocalDateTime? = null,
    val version: Long? = null
)