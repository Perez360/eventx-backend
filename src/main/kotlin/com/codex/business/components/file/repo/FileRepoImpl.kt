package com.codex.business.components.file.repo

import com.codex.business.components.file.boundary.dto.FileDTO
import com.mongodb.MongoGridFSException
import com.mongodb.client.gridfs.GridFSBucket
import com.mongodb.client.gridfs.model.GridFSUploadOptions
import org.bson.Document
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.io.FileInputStream

class FileRepoImpl : FileRepo, KoinComponent {
    private val bucket: GridFSBucket by inject()

    override fun save(file: FileDTO): String {
        val doc = Document(file.metadata)
        val options = GridFSUploadOptions()
            .metadata(doc)
            .chunkSizeBytes(file.length)

        return bucket.uploadFromStream(file.filename!!, FileInputStream(file.filePath!!), options).toHexString()
    }

    override fun get(fileName: String): ByteArray? = try {
        bucket
            .openDownloadStream(fileName)
            .readAllBytes()
    } catch (mongoEx: MongoGridFSException) {
        null
    }

}