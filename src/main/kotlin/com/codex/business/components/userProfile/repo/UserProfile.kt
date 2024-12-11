package com.codex.business.components.userProfile.repo

import dev.morphia.annotations.*
import dev.morphia.utils.IndexType
import java.time.LocalDateTime


@Entity
@Indexes(
    Index(
        fields = [
            Field(value = "username", type = IndexType.DESC),
            Field(value = "location", type = IndexType.DESC),
            Field(value = "about", type = IndexType.DESC),
            Field(value = "bio", type = IndexType.DESC),
            Field(value = "score", type = IndexType.DESC),
            Field(value = "avatar", type = IndexType.DESC),
            Field(value = "createdAt", type = IndexType.DESC),
            Field(value = "modifiedAt", type = IndexType.DESC),
        ], options = IndexOptions(background = true)
    )
)
@EntityListeners(UserProfileEntityListener::class)
data class UserProfile(
    @Id
    var id: String? = null,
    var username: String? = null,
    var userId: String? = null,
    var location: String? = null,
    var about: String? = null,
    var bio: String? = null,
    var score: Long? = null,
    var avatar: String? = null,
    var createdAt: LocalDateTime? = null,
    var modifiedAt: LocalDateTime? = null,
    var version: Long? = null
)
