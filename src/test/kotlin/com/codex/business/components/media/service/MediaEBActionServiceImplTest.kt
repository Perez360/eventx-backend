package com.codex.business.components.media.service

import com.codex.base.shared.PagedContent
import com.codex.business.base.core.settings.MockTestBase
import com.codex.business.components.media.dto.MediaDTO
import com.codex.business.components.media.repo.Media
import com.codex.business.components.media.repo.MediaRepo
import com.codex.business.components.media.spec.MediaSpec
import com.codex.business.mockedAddMediaDTO
import com.codex.business.mockedMedia
import com.codex.business.mockedMediaDTO
import com.codex.business.mockedUpdateMediaDTO
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.mockk.verifyAll
import org.assertj.core.api.Assertions
import org.bson.types.ObjectId
import org.junit.jupiter.api.Test
import org.koin.test.inject
import org.koin.test.mock.declareMock

class MediaEBActionServiceImplTest : MockTestBase() {
    private val underTest: MediaService by inject()

    @Test
    fun `it should add`() {
        //GIVEN
        val media = mockedMedia()
        val dto = mockedAddMediaDTO()
        val mockMediaRepo = declareMock<MediaRepo> {
            every { add(any()) } returns media
        }

        //WHEN
        val result = underTest.add(dto)

        //THEN
        verify { mockMediaRepo.add(any()) }
        Assertions.assertThat(result).isEqualTo(media)
        Assertions.assertThat(result).isInstanceOf(Media::class.java)
    }

    @Test
    fun `it should update`() {
        //GIVEN
        val media = mockedMedia()
        val dto = mockedUpdateMediaDTO()
        val mockMediaRepo = declareMock<MediaRepo> {
            every { getById(any()) } returns media
            every { update(any()) } returns media
        }
        //WHEN
        val result = underTest.update(dto)

        //THEN
        verifyAll {
            mockMediaRepo.findById(any())
            mockMediaRepo.update(any())
        }
        Assertions.assertThat(result).isEqualTo(media)
        Assertions.assertThat(result).isInstanceOf(Media::class.java)
    }

    @Test
    fun `it should fail to update when id does not exist`() {
        //GIVEN
        val dto = mockedUpdateMediaDTO()
        val mockMediaRepo = declareMock<MediaRepo> {
            every { getById(any()) } returns null
        }

        //THEN
        Assertions.assertThatThrownBy {
            //WHEN
            underTest.update(dto)
        }
        verify { mockMediaRepo.findById(any()) }
        verify(exactly = 0) { mockMediaRepo.update(any()) }
    }

    @Test
    fun `it should getById when id exist`() {
        //GIVEN
        val media = mockedMedia()
        val mockMediaRepo = declareMock<MediaRepo> {
            every { getById(any()) } returns media
        }
        //WHEN
        val result = underTest.findById(media.id!!)

        //THEN
        verify { mockMediaRepo.findById(any()) }
        Assertions.assertThat(result).isEqualTo(media)
        Assertions.assertThat(result).isInstanceOf(Media::class.java)
    }

    @Test
    fun `it should fail to getById when id does not exist`() {
        //GIVEN
        val media = mockedMedia()
        val mockMediaRepo = declareMock<MediaRepo> {
            every { getById(any()) } returns null
        }

        //THEN
        Assertions.assertThatThrownBy {
            //WHEN
            underTest.findById(media.id!!)
        }
        verify { mockMediaRepo.findById(any()) }
    }

    @Test
    fun `it should list some records`() {
        //GIVEN
        val page = 1
        val size = 50
        val totalPages = 50
        val totalElements = 500L
        val hasNextPage = true
        val hasPreviousPage = false
        val data = listOf(mockedMediaDTO(), mockedMediaDTO())
        val pagedContent = mockk<PagedContent<MediaDTO>>()
        val spec = MediaSpec()
        spec.page = page
        spec.size = size
        val mockMediaRepo = declareMock<MediaRepo> {
            every { pagedContent.page } returns page
            every { pagedContent.size } returns size
            every { pagedContent.totalPages } returns totalPages
            every { pagedContent.totalElements } returns totalElements
            every { pagedContent.hasNextPage } returns hasNextPage
            every { pagedContent.hasPreviousPage } returns hasPreviousPage
            every { pagedContent.data } returns data
            every { list(any(), any()) } returns pagedContent
        }


        //WHEN
        val result = underTest.list(page, size)

        //THEN
        verify { mockMediaRepo.list(any(), any()) }
        Assertions.assertThat(result).isEqualTo(pagedContent)
        Assertions.assertThat(result.page).isEqualTo(page)
        Assertions.assertThat(result.size).isEqualTo(size)
        Assertions.assertThat(result.totalPages).isEqualTo(totalPages)
        Assertions.assertThat(result.totalElements).isEqualTo(totalElements)
        Assertions.assertThat(result.hasNextPage).isEqualTo(hasNextPage)
        Assertions.assertThat(result.hasPreviousPage).isEqualTo(hasPreviousPage)
        Assertions.assertThat(result.data).isEqualTo(data)
        Assertions.assertThat(result.data).isNotEmpty()
        Assertions.assertThat(result).isInstanceOf(PagedContent::class.java)
    }

    @Test
    fun `it should list no record`() {
        //GIVEN
        val page = 1
        val size = 50
        val totalPages = 0
        val totalElements = 0L
        val hasNextPage = false
        val hasPreviousPage = false
        val data = emptyList<MediaDTO>()
        val pagedContent = mockk<PagedContent<MediaDTO>>()
        val spec = MediaSpec()
        spec.page = page
        spec.size = size
        val mockMediaRepo = declareMock<MediaRepo> {
            every { pagedContent.page } returns page
            every { pagedContent.size } returns size
            every { pagedContent.totalPages } returns totalPages
            every { pagedContent.totalElements } returns totalElements
            every { pagedContent.hasNextPage } returns hasNextPage
            every { pagedContent.hasPreviousPage } returns hasPreviousPage
            every { pagedContent.data } returns data
            every { list(any(), any()) } returns pagedContent
        }

        //WHEN
        val result = underTest.list()

        //THEN
        verify { mockMediaRepo.list() }
        Assertions.assertThat(result).isEqualTo(pagedContent)
        Assertions.assertThat(result.page).isEqualTo(page)
        Assertions.assertThat(result.size).isEqualTo(size)
        Assertions.assertThat(result.totalPages).isEqualTo(totalPages)
        Assertions.assertThat(result.totalElements).isEqualTo(totalElements)
        Assertions.assertThat(result.hasNextPage).isEqualTo(hasNextPage)
        Assertions.assertThat(result.hasPreviousPage).isEqualTo(hasPreviousPage)
        Assertions.assertThat(result.data).isEqualTo(data)
        Assertions.assertThat(result.data).isEmpty()
        Assertions.assertThat(result).isInstanceOf(PagedContent::class.java)
    }


    @Test
    fun `it should search no record`() {
        //GIVEN
        val page = 1
        val size = 50
        val totalPages = 0
        val totalElements = 0L
        val hasNextPage = false
        val hasPreviousPage = false
        val data = emptyList<MediaDTO>()
        val pagedContent = mockk<PagedContent<MediaDTO>>()
        val spec = MediaSpec()
        spec.page = page
        spec.size = size
        val mockMediaRepo = declareMock<MediaRepo> {
            every { pagedContent.page } returns page
            every { pagedContent.size } returns size
            every { pagedContent.totalPages } returns totalPages
            every { pagedContent.totalElements } returns totalElements
            every { pagedContent.hasNextPage } returns hasNextPage
            every { pagedContent.hasPreviousPage } returns hasPreviousPage
            every { pagedContent.data } returns data
            every { search(any()) } returns pagedContent
        }

        //WHEN
        val result = underTest.search(spec)

        //THEN
        verify { mockMediaRepo.search(any()) }
        Assertions.assertThat(result).isEqualTo(pagedContent)
        Assertions.assertThat(result.page).isEqualTo(page)
        Assertions.assertThat(result.size).isEqualTo(size)
        Assertions.assertThat(result.totalPages).isEqualTo(totalPages)
        Assertions.assertThat(result.totalElements).isEqualTo(totalElements)
        Assertions.assertThat(result.hasNextPage).isEqualTo(hasNextPage)
        Assertions.assertThat(result.hasPreviousPage).isEqualTo(hasPreviousPage)
        Assertions.assertThat(result.data).isEqualTo(data)
        Assertions.assertThat(result.data).isEmpty()
        Assertions.assertThat(result).isInstanceOf(PagedContent::class.java)
    }

    @Test
    fun `it should search some records`() {
        //GIVEN
        val page = 1
        val size = 50
        val totalPages = 50
        val totalElements = 500L
        val hasNextPage = true
        val hasPreviousPage = false
        val data = listOf(mockedMediaDTO(), mockedMediaDTO())
        val pagedContent = mockk<PagedContent<MediaDTO>>()
        val spec = MediaSpec()
        spec.page = page
        spec.size = size
        val mockMediaRepo = declareMock<MediaRepo> {
            every { pagedContent.page } returns page
            every { pagedContent.size } returns size
            every { pagedContent.totalPages } returns totalPages
            every { pagedContent.totalElements } returns totalElements
            every { pagedContent.hasNextPage } returns hasNextPage
            every { pagedContent.hasPreviousPage } returns hasPreviousPage
            every { pagedContent.data } returns data
            every { search(any()) } returns pagedContent
        }

        //WHEN
        val result = underTest.search(spec)

        //THEN
        verify { mockMediaRepo.search(any()) }
        Assertions.assertThat(result).isEqualTo(pagedContent)
        Assertions.assertThat(result.page).isEqualTo(page)
        Assertions.assertThat(result.size).isEqualTo(size)
        Assertions.assertThat(result.totalPages).isEqualTo(totalPages)
        Assertions.assertThat(result.totalElements).isEqualTo(totalElements)
        Assertions.assertThat(result.hasNextPage).isEqualTo(hasNextPage)
        Assertions.assertThat(result.hasPreviousPage).isEqualTo(hasPreviousPage)
        Assertions.assertThat(result.data).isEqualTo(data)
        Assertions.assertThat(result.data).isNotEmpty()
        Assertions.assertThat(result).isInstanceOf(PagedContent::class.java)
    }


    @Test
    fun `it should deleteById when id exists`() {
        //GIVEN
        val media = mockedMedia()
        val mockMediaRepo = declareMock<MediaRepo> {
            every { deleteById(any()) } returns media
        }

        //WHEN
        val result = underTest.deleteById(media.id!!)

        //THEN
        verify { mockMediaRepo.deleteById(any()) }
        Assertions.assertThat(result).isEqualTo(media)
        Assertions.assertThat(result).isInstanceOf(Media::class.java)
    }

    @Test
    fun `it should fail to deleteById when id does not exist`() {
        //GIVEN
        val id = ObjectId().toHexString()
        val mockMediaRepo = declareMock<MediaRepo> {
            every { deleteById(any()) } returns null
        }

        //THEN
        Assertions.assertThatThrownBy {
            //WHEN
            underTest.deleteById(id)
        }
        verify { mockMediaRepo.deleteById(any()) }
    }


    @Test
    fun `it should deleteAll`() {
        //GIVEN
        val mockMediaRepo = declareMock<MediaRepo> {
            every { deleteAll() } returns true
        }

        //WHEN
        val result = underTest.deleteAll()

        //THEN
        verify { mockMediaRepo.deleteAll() }
        Assertions.assertThat(result).isTrue()
    }


    @Test
    fun `it should fail to deleteAll`() {
        //GIVEN
        val mockMediaRepo = declareMock<MediaRepo> {
            every { deleteAll() } returns false
        }

        //WHEN
        val result = underTest.deleteAll()

        //THEN
        verify { mockMediaRepo.deleteAll() }
        Assertions.assertThat(result).isFalse()
    }
}