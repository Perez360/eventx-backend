package com.codex.business.components.userProfile.repo

import com.codex.base.shared.PagedContent
import com.codex.business.components.userProfile.dto.UserProfileDto
import com.codex.business.components.userProfile.spec.UserProfileSpec

interface UserProfileRepo {
    fun create(userProfile: UserProfile): UserProfile

    fun update(userProfile: UserProfile): UserProfile

    fun findById(id: String): UserProfile?

    fun exists(id: String): Boolean

    fun count(): Long

    fun list(page: Int = 1, size: Int = 50): PagedContent<UserProfileDto>

    fun search(spec: UserProfileSpec): PagedContent<UserProfileDto>

    fun deleteById(id: String): UserProfile?

    fun deleteAll(): Boolean
}