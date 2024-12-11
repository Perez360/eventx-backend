package com.codex.business.components.eventTag.repo

import com.codex.business.common.enums.Status
import dev.morphia.annotations.*
import dev.morphia.utils.IndexType
import java.time.LocalDateTime


@Entity("tags")
@Indexes(
    Index(
        fields = [
            Field(value = "name", type = IndexType.DESC),
            Field(value = "description", type = IndexType.DESC),
            Field(value = "status", type = IndexType.DESC),
            Field(value = "createdAt", type = IndexType.DESC),
            Field(value = "modifiedAt", type = IndexType.DESC),
        ], options = IndexOptions(background = true)
    )
)
@EntityListeners(EventTagEntityListener::class)
data class EventTag(
    @Id
    var id: String? = null,
    var name: String? = null,
    var description: String? = null,
    var status: Status? = Status.ENABLED,
    var createdAt: LocalDateTime? = null,
    var modifiedAt: LocalDateTime? = null,

    @Version
    var version: Long? = null
)
