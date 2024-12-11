package com.codex.business.components.comment.repo

import com.codex.business.components.user.repo.User
import dev.morphia.annotations.*
import dev.morphia.utils.IndexType
import java.time.LocalDateTime


@Entity("comments")
@Indexes(
    Index(
        fields = [
            Field(value = "message", type = IndexType.DESC),
            Field(value = "createdAt", type = IndexType.DESC),
            Field(value = "modifiedAt", type = IndexType.DESC),
        ], options = IndexOptions(background = true)
    )
)
@EntityListeners(CommentEntityListener::class)
data class Comment(
    @Id
    var id: String? = null,
    var message: String? = null,
    var reaction: String? = null,
    var eventId: String? = null,
    @Reference(idOnly = true)
    var user: User? = null,
    var createdAt: LocalDateTime? = LocalDateTime.now(),
    var modifiedAt: LocalDateTime? = null,

    @Version
    var version: Long? = null
)
