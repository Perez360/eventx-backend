package com.codex.business.components.user.service

import com.codex.base.shared.AppResponse
import com.codex.base.shared.PagedContent
import com.codex.business.components.user.dto.CreateUserDto
import com.codex.business.components.user.dto.UpdateUserDto
import com.codex.business.components.user.dto.UserDto
import com.codex.business.components.user.spec.UserSpec

interface UserService {
    fun ping(): AppResponse<Unit>

    fun create(dto: CreateUserDto): AppResponse<UserDto>

    fun update(dto: UpdateUserDto): AppResponse<UserDto>

    fun findById(id: String): AppResponse<UserDto>

    fun list(page: Int = 1, size: Int = 50): AppResponse<PagedContent<UserDto>>

    fun search(spec: UserSpec): AppResponse<PagedContent<UserDto>>

    fun deleteById(id: String): AppResponse<UserDto>
}