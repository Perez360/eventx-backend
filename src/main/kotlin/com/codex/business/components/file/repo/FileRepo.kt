package com.codex.business.components.file.repo

import com.codex.business.components.file.boundary.dto.FileDTO


interface FileRepo {
    fun save(file: FileDTO): String

    fun get(fileName: String): ByteArray?
}