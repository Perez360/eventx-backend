package com.codex.business.components.media.repo

import com.codex.base.shared.PagedContent
import com.codex.business.base.core.settings.ContainerTestBase
import com.codex.business.components.media.spec.MediaSpec
import com.codex.business.faker
import com.codex.business.mockedMedia
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.koin.test.inject
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class MediaEBActionRepoImplTest : ContainerTestBase() {
    private val underTest: MediaRepo by inject()
    private val savedMedias = mutableListOf<Media>()
    private val logger: Logger = LoggerFactory.getLogger(this::class.java)

    @BeforeEach
    fun prepareWork() {
        repeat(10) {
            val oneMedia = underTest.add(mockedMedia())
            savedMedias.add(oneMedia)
        }
        logger.info("Saved medias successfully")
    }

    @AfterEach
    fun cleanUp() {
        savedMedias.clear()
        underTest.deleteAll()
    }

    @Test
    fun add() {
        //GIVEN
        val mockedMedia = mockedMedia()
        //WHEN
        val oneMedia = underTest.add(mockedMedia)
        //THEN
        Assertions.assertThat(mockedMedia).isNotNull()
        Assertions.assertThat(mockedMedia).isSameAs(oneMedia)
        Assertions.assertThat(oneMedia).isInstanceOf(Media::class.java)
    }

    @Test
    fun update() {
        //GIVEN
        val savedMedia = savedMedias.random()
        savedMedia.filename = faker.file().fileName()
        savedMedia.type = faker.file().mimeType()
        savedMedia.length = Long.MAX_VALUE
        //WHEN
        val oneMedia = underTest.update(savedMedia)
        //THEN
        Assertions.assertThat(savedMedia.id).isEqualTo(oneMedia.id)
        Assertions.assertThat(savedMedia.filename).isEqualTo(oneMedia.filename)
        Assertions.assertThat(savedMedia.type).isEqualTo(oneMedia.type)
        Assertions.assertThat(savedMedia.length).isEqualTo(oneMedia.length)
        Assertions.assertThat(savedMedia.type).isEqualTo(oneMedia.type)
        Assertions.assertThat(savedMedia).isInstanceOf(Media::class.java)
    }

    @Test
    fun exists() {
        //GIVEN
        val savedMedia = savedMedias.random()
        //WHEN
        val isMediaExist = underTest.exists(savedMedia.id!!)
        //THEN
        Assertions.assertThat(isMediaExist).isTrue()
    }

    @Test
    fun count() {
        //WHEN
        val count = underTest.count()
        //THEN
        Assertions.assertThat(count).isEqualTo(savedMedias.size.toLong())
    }

    @Test
    fun get() {
        //GIVEN
        val savedMedia = savedMedias.random()
        //WHEN
        val oneMedia = underTest.findById(savedMedia.id!!)
        //THEN
        Assertions.assertThat(oneMedia).isInstanceOf(Media::class.java)
        Assertions.assertThat(oneMedia?.id).isEqualTo(savedMedia.id)
        Assertions.assertThat(oneMedia?.filename).isEqualTo(savedMedia.filename)
        Assertions.assertThat(oneMedia?.type).isEqualTo(savedMedia.type)
        Assertions.assertThat(oneMedia?.length).isEqualTo(savedMedia.length)
    }

    @Test
    fun search() {
        //GIVEN
        val spec = MediaSpec()
        //WHEN
        val pagedContent = underTest.search(spec)
        //THEN
        Assertions.assertThat(pagedContent).isInstanceOf(PagedContent::class.java)
        Assertions.assertThat(pagedContent.data.size).isEqualTo(savedMedias.size)
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
        Assertions.assertThat(pagedContent.data.size).isEqualTo(savedMedias.size)
    }

    @Test
    fun delete() {
        //GIVEN
        val savedMedia = savedMedias.random()
        //WHEN
        val oneMedia = underTest.deleteById(savedMedia.id!!)
        //THEN
        Assertions.assertThat(oneMedia).isInstanceOf(Media::class.java)
        Assertions.assertThat(oneMedia?.id).isEqualTo(savedMedia.id)
        Assertions.assertThat(oneMedia?.filename).isEqualTo(savedMedia.filename)
        Assertions.assertThat(oneMedia?.type).isEqualTo(savedMedia.type)
        Assertions.assertThat(oneMedia?.length).isEqualTo(savedMedia.length)
        Assertions.assertThat(oneMedia?.type).isEqualTo(savedMedia.type)
    }
}