package com.codex.business.components.comment.service

import com.codex.base.shared.PagedContent
import com.codex.business.*
import com.codex.business.base.core.settings.MockTestBase
import com.codex.business.components.comment.dto.CommentDto
import com.codex.business.components.comment.repo.Comment
import com.codex.business.components.comment.repo.CommentRepo
import com.codex.business.components.comment.spec.CommentSpec
import com.codex.business.components.event.repo.EventRepo
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.mockk.verifyAll
import org.assertj.core.api.Assertions
import org.bson.types.ObjectId
import org.junit.jupiter.api.Test
import org.koin.core.component.inject
import org.koin.test.mock.declareMock

class CommentEBActionServiceImplTest : MockTestBase() {
    private val underTest: CommentService by inject()

    @Test
    fun `it should add`() {
        //GIVEN
        val comment = mockedComment()
        val event = mockedEvent()
        val dto = mockedAddCommentDTO()
        val mockCommentRepo = declareMock<CommentRepo> {
            every { add(any()) } returns comment
        }
        val mockEventRepo = declareMock<EventRepo> {
            every { getById(any()) } returns event
            every { update(any()) } returns event
        }

        //WHEN
        val result = underTest.add(dto)

        //THEN
        verifyAll {
            mockCommentRepo.add(any())
            mockEventRepo.findById(any())
            mockEventRepo.update(any())
        }
        Assertions.assertThat(result).isEqualTo(comment)
        Assertions.assertThat(result).isInstanceOf(Comment::class.java)
    }

    @Test
    fun `it should update`() {
        //GIVEN
        val comment = mockedComment()
        val dto = mockedUpdateCommentDTO()
        val mockCommentRepo = declareMock<CommentRepo> {
            every { getById(any()) } returns comment
            every { update(any()) } returns comment
        }
        //WHEN
        val result = underTest.update(dto)

        //THEN
        verifyAll {
            mockCommentRepo.findById(any())
            mockCommentRepo.update(any())
        }
        Assertions.assertThat(result).isEqualTo(comment)
        Assertions.assertThat(result).isInstanceOf(Comment::class.java)
    }

    @Test
    fun `it should fail to update when id does not exist`() {
        //GIVEN
        val dto = mockedUpdateCommentDTO()
        val mockCommentRepo = declareMock<CommentRepo> {
            every { getById(any()) } returns null
        }

        //THEN
        Assertions.assertThatThrownBy {
            //WHEN
            underTest.update(dto)
        }
        verify { mockCommentRepo.findById(any()) }
        verify(exactly = 0) { mockCommentRepo.update(any()) }
    }

    @Test
    fun `it should getById when id exist`() {
        //GIVEN
        val comment = mockedComment()
        val mockCommentRepo = declareMock<CommentRepo> {
            every { getById(any()) } returns comment
        }
        //WHEN
        val result = underTest.findById(comment.id!!)

        //THEN
        verify { mockCommentRepo.findById(any()) }
        Assertions.assertThat(result).isEqualTo(comment)
        Assertions.assertThat(result).isInstanceOf(Comment::class.java)
    }

    @Test
    fun `it should fail to getById when id does not exist`() {
        //GIVEN
        val comment = mockedComment()
        val mockCommentRepo = declareMock<CommentRepo> {
            every { getById(any()) } returns null
        }

        //THEN
        Assertions.assertThatThrownBy {
            //WHEN
            underTest.findById(comment.id!!)
        }
        verify { mockCommentRepo.findById(any()) }
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
        val data = listOf(mockedCommentDTO(), mockedCommentDTO())
        val pagedContent = mockk<PagedContent<CommentDto>>()
        val spec = CommentSpec()
        spec.page = page
        spec.size = size
        val mockCommentRepo = declareMock<CommentRepo> {
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
        verify { mockCommentRepo.list(any(), any()) }
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
        val data = emptyList<CommentDto>()
        val pagedContent = mockk<PagedContent<CommentDto>>()
        val spec = CommentSpec()
        spec.page = page
        spec.size = size
        val mockCommentRepo = declareMock<CommentRepo> {
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
        verify { mockCommentRepo.list() }
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
        val data = emptyList<CommentDto>()
        val pagedContent = mockk<PagedContent<CommentDto>>()
        val spec = CommentSpec()
        spec.page = page
        spec.size = size
        val mockCommentRepo = declareMock<CommentRepo> {
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
        verify { mockCommentRepo.search(any()) }
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
        val data = listOf(mockedCommentDTO(), mockedCommentDTO())
        val pagedContent = mockk<PagedContent<CommentDto>>()
        val spec = CommentSpec()
        spec.page = page
        spec.size = size
        val mockCommentRepo = declareMock<CommentRepo> {
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
        verify { mockCommentRepo.search(any()) }
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
        val comment = mockedComment()
        val mockCommentRepo = declareMock<CommentRepo> {
            every { deleteById(any()) } returns comment
        }

        //WHEN
        val result = underTest.deleteById(comment.id!!)

        //THEN
        verify { mockCommentRepo.deleteById(any()) }
        Assertions.assertThat(result).isEqualTo(comment)
        Assertions.assertThat(result).isInstanceOf(Comment::class.java)
    }

    @Test
    fun `it should fail to deleteById when id does not exist`() {
        //GIVEN
        val id = ObjectId().toHexString()
        val mockCommentRepo = declareMock<CommentRepo> {
            every { deleteById(any()) } returns null
        }

        //THEN
        Assertions.assertThatThrownBy {
            //WHEN
            underTest.deleteById(id)
        }
        verify { mockCommentRepo.deleteById(any()) }
    }


    @Test
    fun `it should deleteAll`() {
        //GIVEN
        val mockCommentRepo = declareMock<CommentRepo> {
            every { deleteAll() } returns true
        }

        //WHEN
        val result = underTest.deleteAll()

        //THEN
        verify { mockCommentRepo.deleteAll() }
        Assertions.assertThat(result).isTrue()
    }


    @Test
    fun `it should fail to deleteAll`() {
        //GIVEN
        val mockCommentRepo = declareMock<CommentRepo> {
            every { deleteAll() } returns false
        }

        //WHEN
        val result = underTest.deleteAll()

        //THEN
        verify { mockCommentRepo.deleteAll() }
        Assertions.assertThat(result).isFalse()
    }
}