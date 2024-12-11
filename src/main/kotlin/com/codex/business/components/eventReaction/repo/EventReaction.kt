package com.codex.business.components.eventReaction.repo

import com.codex.business.components.eventReaction.enum.ReactionType
import dev.morphia.annotations.*
import dev.morphia.utils.IndexType
import java.time.LocalDateTime

@Entity("reactions")
@Indexes(
    Index(
        fields = [
            Field(value = "userId", type = IndexType.DESC),
            Field(value = "eventId", type = IndexType.DESC),
            Field(value = "type", type = IndexType.DESC),
            Field(value = "createdAt", type = IndexType.DESC),
            Field(value = "modifiedAt", type = IndexType.DESC),
        ], options = IndexOptions(background = true)
    )
)
@EntityListeners(EventReactionEntityListener::class)
data class EventReaction(
    @Id
    var id: String? = null,
    var userId: String? = null,
    var eventId: String? = null,
    var type: ReactionType? = null,
    var createdAt: LocalDateTime? = null,
    var modifiedAt: LocalDateTime? = null,

    @Version
    var version: Long? = null
){
    override fun toString(): String {
        return "EventReaction(" +
                "id=$id, " +
                "userId=$userId, " +
                "eventId=$eventId, " +
                "type=$type, " +
                "createdAt=$createdAt, " +
                "modifiedAt=$modifiedAt, " +
                "version=$version" +
                ")"
    }
}