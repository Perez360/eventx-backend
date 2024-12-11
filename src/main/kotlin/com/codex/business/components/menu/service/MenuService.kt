package com.codex.business.components.menu.service

import com.codex.base.shared.AppResponse
import com.codex.business.components.menu.dto.CreateMenuDto
import com.codex.business.components.menu.dto.MenuDto
import com.codex.business.components.menu.dto.UpdateMenuDto

interface MenuService {
    fun ping(): AppResponse<Unit>

    fun create(dto: CreateMenuDto): AppResponse<MenuDto>

    fun update(dto: UpdateMenuDto): AppResponse<MenuDto>

    fun findById(id: String): AppResponse<MenuDto>

    fun list(page: Int = 1, size: Int = 50): AppResponse<List<MenuDto>>

    fun deleteById(id: String): AppResponse<MenuDto>
}