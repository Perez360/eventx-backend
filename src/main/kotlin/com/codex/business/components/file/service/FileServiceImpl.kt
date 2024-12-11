package com.codex.business.components.file.service

import com.codex.base.exceptions.ServiceException
import com.codex.base.shared.AppResponse
import com.codex.business.components.file.boundary.dto.FileDTO
import com.codex.business.components.file.repo.FileRepo
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.slf4j.LoggerFactory

class FileServiceImpl : FileService, KoinComponent {
    private val repo: FileRepo by inject()
    private val logger = LoggerFactory.getLogger(this::class.java)

    override fun upload(dto: FileDTO): AppResponse<String> {
        val data = repo.save(dto)
        return AppResponse.Success("File uploaded successfully", data)
    }

    override fun download(file: String): ByteArray? {
        return repo.get(file)
            ?: throw ServiceException("No file found with name $file")
    }
}