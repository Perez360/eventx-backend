package com.codex.business.components.file.boundary.dto


data class FileDTO(
    var filename: String? = null,
    var filePath: String? = null,
    var metadata: Map<String,String>? = null,
    var length: Int? = null,
)