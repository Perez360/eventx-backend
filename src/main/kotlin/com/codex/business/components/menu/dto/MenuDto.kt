package com.codex.business.components.menu.dto

import com.codex.business.common.enums.Status
import java.time.LocalDateTime

data class MenuDto(
    var id: String? = null,
    var name: String? = null,
    var description: String? = null,
    var link: String? = null,
    var icon: String? = null,
    var parentId: String? = null,
    var status: Status? = null,
    var permissions: Set<String>? = null,
    var menus: MutableSet<MenuDto>? = null,
    var createdAt: LocalDateTime? = null,
    var modifiedAt: LocalDateTime? = null,
)