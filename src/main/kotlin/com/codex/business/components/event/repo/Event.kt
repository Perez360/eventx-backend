package com.codex.business.components.event.repo

import com.codex.business.components.comment.repo.Comment
import com.codex.business.components.event.enums.EventStatus
import com.codex.business.components.eventCategory.repo.EventCategory
import com.codex.business.components.eventTag.repo.EventTag
import dev.morphia.annotations.*
import dev.morphia.utils.IndexType
import java.time.LocalDateTime


@Entity("events")
@Indexes(
    Index(
        fields = [
            Field(value = "title", type = IndexType.DESC),
            Field(value = "description", type = IndexType.DESC),
            Field(value = "venue", type = IndexType.DESC),
            Field(value = "isPublic", type = IndexType.DESC),
            Field(value = "status", type = IndexType.DESC),
            Field(value = "startDate", type = IndexType.DESC),
            Field(value = "endDate", type = IndexType.DESC),
            Field(value = "createdAt", type = IndexType.DESC),
            Field(value = "modifiedAt", type = IndexType.DESC),
        ], options = IndexOptions(background = true)
    )
)
@EntityListeners(EventEntityListener::class)
data class Event(
    @Id
    var id: String? = null,
    var title: String? = null,
    var description: String? = null,
    var venue: String? = null,
    var isPublic: Boolean? = null,
    var reference: String? = null,
    var status: EventStatus? = null,
    var imageLinks: MutableSet<String>? = null,
    @Reference(idOnly = true, ignoreMissing = true)
    val comments: MutableSet<Comment>? = mutableSetOf(),
    @Reference(idOnly = true, ignoreMissing = true)
    var categories: MutableSet<EventCategory>? = mutableSetOf(),
    @Reference(idOnly = true, ignoreMissing = true)
    var tags: MutableSet<EventTag>? = mutableSetOf(),
    var startDate: LocalDateTime? = null,
    var endDate: LocalDateTime? = null,
    var createdAt: LocalDateTime? = LocalDateTime.now(),
    var modifiedAt: LocalDateTime? = null,

    @Version
    var version: Long? = null
)
