package com.codex.business.components.menu.dto

import com.codex.business.common.enums.Status

data class UpdateMenuDto(
    var id: String? = null,
    var name: String? = null,
    var description: String? = null,
    var link: String? = null,
    var icon: String? = null,
    var status: Status? = null,
    var parentId: String? = null,
    var permissions: MutableSet<String>? = null,
)