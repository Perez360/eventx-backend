package com.codex.business.components.menu.dto

import com.codex.business.common.enums.Status

data class CreateMenuDto(
    var name: String? = null,
    var description: String? = null,
    var link: String? = null,
    var icon: String? = null,
    var parentId: String? = null,
    var status: Status? = Status.ENABLED,
    var permissions: MutableSet<String>? = null,
)