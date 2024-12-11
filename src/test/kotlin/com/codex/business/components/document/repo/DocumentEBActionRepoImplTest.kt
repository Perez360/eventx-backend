package com.codex.business.components.document.repo

import com.codex.base.shared.PagedContent
import com.codex.business.base.core.settings.ContainerTestBase
import com.codex.business.components.document.enums.DocumentType
import com.codex.business.components.document.spec.DocumentSpec
import com.codex.business.faker
import com.codex.business.mockedDocument
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.koin.test.inject
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class DocumentEBActionRepoImplTest : ContainerTestBase() {
    private val underTest: DocumentRepo by inject()
    private val savedDocuments = mutableListOf<Document>()
    private val logger: Logger = LoggerFactory.getLogger(this::class.java)

    @BeforeEach
    fun prepareWork() {
        repeat(10) {
            val oneDocument = underTest.add(mockedDocument())
            savedDocuments.add(oneDocument)
        }
        logger.info("Saved medias successfully")
    }

    @AfterEach
    fun cleanUp() {
        savedDocuments.clear()
        underTest.deleteAll()
    }

    @Test
    fun add() {
        //GIVEN
        val mockedDocument = mockedDocument()
        //WHEN
        val oneDocument = underTest.add(mockedDocument)
        //THEN
        Assertions.assertThat(mockedDocument).isNotNull()
        Assertions.assertThat(mockedDocument).isSameAs(oneDocument)
        Assertions.assertThat(oneDocument).isInstanceOf(Document::class.java)
    }

    @Test
    fun update() {
        //GIVEN
        val savedDocument = savedDocuments.random()
        savedDocument.filename = faker.file().fileName()
        savedDocument.type = DocumentType.entries.random()
        savedDocument.length = Long.MAX_VALUE
        //WHEN
        val oneDocument = underTest.update(savedDocument)
        //THEN
        Assertions.assertThat(savedDocument.id).isEqualTo(oneDocument.id)
        Assertions.assertThat(savedDocument.filename).isEqualTo(oneDocument.filename)
        Assertions.assertThat(savedDocument.type).isEqualTo(oneDocument.type)
        Assertions.assertThat(savedDocument.length).isEqualTo(oneDocument.length)
        Assertions.assertThat(savedDocument.type).isEqualTo(oneDocument.type)
        Assertions.assertThat(savedDocument).isInstanceOf(Document::class.java)
    }

    @Test
    fun exists() {
        //GIVEN
        val savedDocument = savedDocuments.random()
        //WHEN
        val isDocumentExist = underTest.exists(savedDocument.id!!)
        //THEN
        Assertions.assertThat(isDocumentExist).isTrue()
    }

    @Test
    fun count() {
        //WHEN
        val count = underTest.count()
        //THEN
        Assertions.assertThat(count).isEqualTo(savedDocuments.size.toLong())
    }

    @Test
    fun get() {
        //GIVEN
        val savedDocument = savedDocuments.random()
        //WHEN
        val oneDocument = underTest.findById(savedDocument.id!!)
        //THEN
        Assertions.assertThat(oneDocument).isInstanceOf(Document::class.java)
        Assertions.assertThat(oneDocument?.id).isEqualTo(savedDocument.id)
        Assertions.assertThat(oneDocument?.filename).isEqualTo(savedDocument.filename)
        Assertions.assertThat(oneDocument?.type).isEqualTo(savedDocument.type)
        Assertions.assertThat(oneDocument?.length).isEqualTo(savedDocument.length)
    }

    @Test
    fun search() {
        //GIVEN
        val spec = DocumentSpec()
        //WHEN
        val pagedContent = underTest.search(spec)
        //THEN
        Assertions.assertThat(pagedContent).isInstanceOf(PagedContent::class.java)
        Assertions.assertThat(pagedContent.data.size).isEqualTo(savedDocuments.size)
    }

    @Test
    fun list() {
        //GIVEN
        val page = 1
        val size = 50
        //WHEN
        val pagedContent = underTest.list(page, size)
        //THEN
        Assertions.assertThat(pagedContent).isInstanceOf(PagedContent::class.java)
        Assertions.assertThat(pagedContent.data.size).isEqualTo(savedDocuments.size)
    }

    @Test
    fun delete() {
        //GIVEN
        val savedDocument = savedDocuments.random()
        //WHEN
        val oneDocument = underTest.deleteById(savedDocument.id!!)
        //THEN
        Assertions.assertThat(oneDocument).isInstanceOf(Document::class.java)
        Assertions.assertThat(oneDocument?.id).isEqualTo(savedDocument.id)
        Assertions.assertThat(oneDocument?.filename).isEqualTo(savedDocument.filename)
        Assertions.assertThat(oneDocument?.type).isEqualTo(savedDocument.type)
        Assertions.assertThat(oneDocument?.length).isEqualTo(savedDocument.length)
        Assertions.assertThat(oneDocument?.type).isEqualTo(savedDocument.type)
    }
}