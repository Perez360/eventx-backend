package com.codex.business.components.event.service

import com.codex.base.shared.PagedContent
import com.codex.business.base.core.settings.MockTestBase
import com.codex.business.components.eventReaction.dto.EventDto
import com.codex.business.components.event.repo.Event
import com.codex.business.components.event.repo.EventRepo
import com.codex.business.components.event.spec.EventSpec
import com.codex.business.mockedAddEventDTO
import com.codex.business.mockedEvent
import com.codex.business.mockedEventDTO
import com.codex.business.mockedUpdateEventDTO
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.mockk.verifyAll
import org.assertj.core.api.Assertions
import org.bson.types.ObjectId
import org.junit.jupiter.api.Test
import org.koin.core.component.inject
import org.koin.test.mock.declareMock

class EventEBActionServiceImplTest : MockTestBase() {
    private val underTest: EventService by inject()

    @Test
    fun `it should add`() {
        //GIVEN
        val event = mockedEvent()
        val dto = mockedAddEventDTO()
        val mockEventRepo = declareMock<EventRepo> {
            every { add(any()) } returns event
        }

        //WHEN
        val result = underTest.add(dto)

        //THEN
        verify { mockEventRepo.add(any()) }
        Assertions.assertThat(result).isEqualTo(event)
        Assertions.assertThat(result).isInstanceOf(Event::class.java)
    }

    @Test
    fun `it should update`() {
        //GIVEN
        val event = mockedEvent()
        val dto = mockedUpdateEventDTO()
        val mockEventRepo = declareMock<EventRepo> {
            every { getById(any()) } returns event
            every { update(any()) } returns event
        }
        //WHEN
        val result = underTest.update(dto)

        //THEN
        verifyAll {
            mockEventRepo.findById(any())
            mockEventRepo.update(any())
        }
        Assertions.assertThat(result).isEqualTo(event)
        Assertions.assertThat(result).isInstanceOf(Event::class.java)
    }

    @Test
    fun `it should fail to update when id does not exist`() {
        //GIVEN
        val dto = mockedUpdateEventDTO()
        val mockEventRepo = declareMock<EventRepo> {
            every { getById(any()) } returns null
        }

        //THEN
        Assertions.assertThatThrownBy {
            //WHEN
            underTest.update(dto)
        }
        verify { mockEventRepo.findById(any()) }
        verify(exactly = 0) { mockEventRepo.update(any()) }
    }

    @Test
    fun `it should getById when id exist`() {
        //GIVEN
        val event = mockedEvent()
        val mockEventRepo = declareMock<EventRepo> {
            every { getById(any()) } returns event
        }
        //WHEN
        val result = underTest.findById(event.id!!)

        //THEN
        verify { mockEventRepo.findById(any()) }
        Assertions.assertThat(result).isEqualTo(event)
        Assertions.assertThat(result).isInstanceOf(Event::class.java)
    }

    @Test
    fun `it should fail to getById when id does not exist`() {
        //GIVEN
        val event = mockedEvent()
        val mockEventRepo = declareMock<EventRepo> {
            every { getById(any()) } returns null
        }

        //THEN
        Assertions.assertThatThrownBy {
            //WHEN
            underTest.findById(event.id!!)
        }
        verify { mockEventRepo.findById(any()) }
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
        val data = listOf(mockedEventDTO(), mockedEventDTO())
        val pagedContent = mockk<PagedContent<EventDto>>()
        val spec = EventSpec()
        spec.page = page
        spec.size = size
        val mockEventRepo = declareMock<EventRepo> {
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
        verify { mockEventRepo.list(any(), any()) }
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
        val data = emptyList<EventDto>()
        val pagedContent = mockk<PagedContent<EventDto>>()
        val spec = EventSpec()
        spec.page = page
        spec.size = size
        val mockEventRepo = declareMock<EventRepo> {
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
        verify { mockEventRepo.list() }
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
        val data = emptyList<EventDto>()
        val pagedContent = mockk<PagedContent<EventDto>>()
        val spec = EventSpec()
        spec.page = page
        spec.size = size
        val mockEventRepo = declareMock<EventRepo> {
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
        verify { mockEventRepo.search(any()) }
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
        val data = listOf(mockedEventDTO(), mockedEventDTO())
        val pagedContent = mockk<PagedContent<EventDto>>()
        val spec = EventSpec()
        spec.page = page
        spec.size = size
        val mockEventRepo = declareMock<EventRepo> {
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
        verify { mockEventRepo.search(any()) }
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
        val event = mockedEvent()
        val mockEventRepo = declareMock<EventRepo> {
            every { deleteById(any()) } returns event
        }

        //WHEN
        val result = underTest.deleteById(event.id!!)

        //THEN
        verify { mockEventRepo.deleteById(any()) }
        Assertions.assertThat(result).isEqualTo(event)
        Assertions.assertThat(result).isInstanceOf(Event::class.java)
    }

    @Test
    fun `it should fail to deleteById when id does not exist`() {
        //GIVEN
        val id = ObjectId().toHexString()
        val mockEventRepo = declareMock<EventRepo> {
            every { deleteById(any()) } returns null
        }

        //THEN
        Assertions.assertThatThrownBy {
            //WHEN
            underTest.deleteById(id)
        }
        verify { mockEventRepo.deleteById(any()) }
    }


    @Test
    fun `it should deleteAll`() {
        //GIVEN
        val mockEventRepo = declareMock<EventRepo> {
            every { deleteAll() } returns true
        }

        //WHEN
        val result = underTest.deleteAll()

        //THEN
        verify { mockEventRepo.deleteAll() }
        Assertions.assertThat(result).isTrue()
    }


    @Test
    fun `it should fail to deleteAll`() {
        //GIVEN
        val mockEventRepo = declareMock<EventRepo> {
            every { deleteAll() } returns false
        }

        //WHEN
        val result = underTest.deleteAll()

        //THEN
        verify { mockEventRepo.deleteAll() }
        Assertions.assertThat(result).isFalse()
    }
}