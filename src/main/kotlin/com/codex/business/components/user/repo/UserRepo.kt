package com.codex.business.components.user.repo

import com.codex.base.shared.PagedContent
import com.codex.business.components.user.dto.UserDto
import com.codex.business.components.user.spec.UserSpec

interface UserRepo {
    fun create(user: User): User

    fun update(user: User): User

    fun findById(id: String): User?

    fun count(): Long

    fun list(page: Int = 1, size: Int = 50): PagedContent<UserDto>

    fun search(spec: UserSpec): PagedContent<UserDto>

    fun deleteById(id: String): User?

    fun deleteAll(): Boolean
}