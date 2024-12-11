package com.codex.business.components.auth.service

import com.codex.base.shared.AppResponse
import com.codex.business.components.auth.dto.AuthDto
import com.codex.business.components.auth.dto.LoginDto
import com.codex.business.components.auth.dto.RegisterDto
import com.codex.business.components.user.dto.UserDto

interface AuthService {
    fun ping(): AppResponse<String>
    fun login(dto: LoginDto): AppResponse<AuthDto>
    fun register(dto: RegisterDto): AppResponse<UserDto>
}