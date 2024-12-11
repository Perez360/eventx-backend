package com.codex.business.components.file.service

import com.codex.base.shared.AppResponse
import com.codex.business.components.file.boundary.dto.FileDTO

interface FileService {
    fun upload(dto: FileDTO): AppResponse<String>

    fun download(file: String): ByteArray?
}