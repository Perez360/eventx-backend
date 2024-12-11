package com.codex.business.components.event.controller

import com.codex.base.CODE_SUCCESS
import com.codex.base.exceptions.ServiceException
import com.codex.base.shared.AppResponse
import com.codex.base.shared.PagedContent
import com.codex.business.base.core.settings.MockTestBase
import com.codex.business.components.eventReaction.dto.EventDto
import com.codex.business.components.event.service.EventService
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

class EventEBActionControllerImplTest : MockTestBase() {
    private val underTest: EventController by inject()

    @Test
    fun `it should add`() {
        //GIVEN
        val event = mockedEvent()
        val dto = mockedAddEventDTO()
        val mockEventService = declareMock<EventService> {
            every { add(any()) } returns event
        }

        //WHEN
        val result = underTest.add(dto)

        //THEN
        verify { mockEventService.add(any()) }
        Assertions.assertThat(result.data?.id).isEqualTo(event.id)
        Assertions.assertThat(result.data).isNotNull()
        Assertions.assertThat(result.code).isEqualTo(CODE_SUCCESS)
        Assertions.assertThat(result).isInstanceOf(AppResponse::class.java)
    }

    @Test
    fun `it should update`() {
        //GIVEN
        val event = mockedEvent()
        val dto = mockedUpdateEventDTO()
        val mockEventService = declareMock<EventService> {
            every { update(any()) } returns event
        }
        //WHEN
        val result = underTest.update(dto)

        //THEN
        verifyAll {
            mockEventService.update(any())
        }
        Assertions.assertThat(result.data?.id).isEqualTo(event.id)
        Assertions.assertThat(result.data).isNotNull()
        Assertions.assertThat(result.code).isEqualTo(CODE_SUCCESS)
        Assertions.assertThat(result).isInstanceOf(AppResponse::class.java)
        Assertions.assertThat(result).isInstanceOf(AppResponse::class.java)
    }

    @Test
    fun `it should fail to update when id does not exist`() {
        //GIVEN
        val event = mockedEvent()
        val dto = mockedUpdateEventDTO()
        val mockEventService = declareMock<EventService> {
            every { update(any()) } throws ServiceException("No record found with id: ${event.id}")
        }

        //THEN
        Assertions.assertThatThrownBy {
            //WHEN
            underTest.update(dto)
        }
        verify { mockEventService.update(any()) }
    }

    @Test
    fun `it should getById when id exist`() {
        //GIVEN
        val event = mockedEvent()
        val mockEventService = declareMock<EventService> {
            every { getById(any()) } returns event
        }
        //WHEN
        val result = underTest.findById(event.id!!)

        //THEN
        verify { mockEventService.findById(any()) }
        Assertions.assertThat(result.data?.id).isEqualTo(event.id)
        Assertions.assertThat(result.data).isNotNull()
        Assertions.assertThat(result.code).isEqualTo(CODE_SUCCESS)
        Assertions.assertThat(result).isInstanceOf(AppResponse::class.java)
    }

    @Test
    fun `it should fail to getById when id does not exist`() {
        //GIVEN
        val event = mockedEvent()
        val mockEventService = declareMock<EventService> {
            every { getById(any()) } throws ServiceException("No record found with id: ${event.id}")
        }

        //THEN
        Assertions.assertThatThrownBy {
            //WHEN
            underTest.findById(event.id!!)
        }
        verify { mockEventService.findById(any()) }
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
        val mockEventService = declareMock<EventService> {
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
        verify { mockEventService.list(any(), any()) }
        Assertions.assertThat(result.data).isEqualTo(pagedContent)
        Assertions.assertThat(result.code).isEqualTo(CODE_SUCCESS)
        Assertions.assertThat(result.data?.page).isEqualTo(page)
        Assertions.assertThat(result.data?.size).isEqualTo(size)
        Assertions.assertThat(result.data?.totalPages).isEqualTo(totalPages)
        Assertions.assertThat(result.data?.totalElements).isEqualTo(totalElements)
        Assertions.assertThat(result.data?.hasNextPage).isEqualTo(hasNextPage)
        Assertions.assertThat(result.data?.hasPreviousPage).isEqualTo(hasPreviousPage)
        Assertions.assertThat(result.data?.data).isEqualTo(data)
        Assertions.assertThat(result.data?.data).isNotEmpty()
        Assertions.assertThat(result).isInstanceOf(AppResponse::class.java)
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
        val mockEventService = declareMock<EventService> {
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
        verify { mockEventService.list() }
        Assertions.assertThat(result.data).isEqualTo(pagedContent)
        Assertions.assertThat(result.code).isEqualTo(CODE_SUCCESS)
        Assertions.assertThat(result.data?.page).isEqualTo(page)
        Assertions.assertThat(result.data?.size).isEqualTo(size)
        Assertions.assertThat(result.data?.totalPages).isEqualTo(totalPages)
        Assertions.assertThat(result.data?.totalElements).isEqualTo(totalElements)
        Assertions.assertThat(result.data?.hasNextPage).isEqualTo(hasNextPage)
        Assertions.assertThat(result.data?.hasPreviousPage).isEqualTo(hasPreviousPage)
        Assertions.assertThat(result.data?.data).isEqualTo(data)
        Assertions.assertThat(result.data?.data).isEmpty()
        Assertions.assertThat(result).isInstanceOf(AppResponse::class.java)
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
        val mockEventService = declareMock<EventService> {
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
        verify { mockEventService.search(any()) }
        Assertions.assertThat(result.data).isEqualTo(pagedContent)
        Assertions.assertThat(result.data?.page).isEqualTo(page)
        Assertions.assertThat(result.data?.size).isEqualTo(size)
        Assertions.assertThat(result.data?.totalPages).isEqualTo(totalPages)
        Assertions.assertThat(result.data?.totalElements).isEqualTo(totalElements)
        Assertions.assertThat(result.data?.hasNextPage).isEqualTo(hasNextPage)
        Assertions.assertThat(result.data?.hasPreviousPage).isEqualTo(hasPreviousPage)
        Assertions.assertThat(result.data?.data).isEqualTo(data)
        Assertions.assertThat(result.data?.data).isEmpty()
        Assertions.assertThat(result).isInstanceOf(AppResponse::class.java)
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
        val mockEventService = declareMock<EventService> {
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
        verify { mockEventService.search(any()) }
        Assertions.assertThat(result.data).isEqualTo(pagedContent)
        Assertions.assertThat(result.data?.page).isEqualTo(page)
        Assertions.assertThat(result.data?.size).isEqualTo(size)
        Assertions.assertThat(result.data?.totalPages).isEqualTo(totalPages)
        Assertions.assertThat(result.data?.totalElements).isEqualTo(totalElements)
        Assertions.assertThat(result.data?.hasNextPage).isEqualTo(hasNextPage)
        Assertions.assertThat(result.data?.hasPreviousPage).isEqualTo(hasPreviousPage)
        Assertions.assertThat(result.data?.data).isEqualTo(data)
        Assertions.assertThat(result.data?.data).isNotEmpty()
        Assertions.assertThat(result).isInstanceOf(AppResponse::class.java)
    }


    @Test
    fun `it should deleteById when id exists`() {
        //GIVEN
        val event = mockedEvent()
        val mockEventService = declareMock<EventService> {
            every { deleteById(any()) } returns event
        }

        //WHEN
        val result = underTest.deleteById(event.id!!)

        //THEN
        verify { mockEventService.deleteById(any()) }
        Assertions.assertThat(result.data).isNotNull()
        Assertions.assertThat(result.data?.id).isEqualTo(event.id)
        Assertions.assertThat(result).isInstanceOf(AppResponse::class.java)
    }

    @Test
    fun `it should fail to deleteById when id does not exist`() {
        //GIVEN
        val id = ObjectId().toHexString()
        val mockEventService = declareMock<EventService> {
            every { deleteById(any()) } throws ServiceException("No record found with id: $id")
        }

        //THEN
        Assertions.assertThatThrownBy {
            //WHEN
            underTest.deleteById(id)
        }
        verify { mockEventService.deleteById(any()) }
    }


//    @Test
//    fun `it should deleteAll`() {
//        //GIVEN
//        val mockEventService = declareMock<EventService> {
//            every { deleteAll() } returns true
//        }
//
//        //WHEN
//        val result = underTest.deleteAll()
//
//        //THEN
//        verify { mockEventService.deleteAll() }
//        Assertions.assertThat(result).isTrue()
//    }

//
//    @Test
//    fun `it should fail to deleteAll`() {
//        //GIVEN
//        val mockEventService = declareMock<EventService> {
//            every { deleteAll() } returns false
//        }
//
//        //WHEN
//        val result = underTest.deleteAll()
//
//        //THEN
//        verify { mockEventService.deleteAll() }
//        Assertions.assertThat(result).isFalse()
//    }
}