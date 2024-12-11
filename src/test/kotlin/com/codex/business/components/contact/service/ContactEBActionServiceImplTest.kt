package com.codex.business.components.contact.service

import com.codex.base.shared.PagedContent
import com.codex.business.*
import com.codex.business.base.core.settings.MockTestBase
import com.codex.business.components.contact.dto.ContactDTO
import com.codex.business.components.contact.repo.Contact
import com.codex.business.components.contact.repo.ContactRepo
import com.codex.business.components.contact.spec.ContactSpec
import com.codex.business.components.kyc.repo.KycRepo
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.mockk.verifyAll
import org.assertj.core.api.Assertions
import org.bson.types.ObjectId
import org.junit.jupiter.api.Test
import org.koin.core.component.inject
import org.koin.test.mock.declareMock

class ContactEBActionServiceImplTest : MockTestBase() {
    private val underTest: ContactService by inject()

    @Test
    fun `it should add`() {
        //GIVEN
        val contact = mockedContact()
        val kyc = mockedKyc()
        val dto = mockedAddContactDTO()
        val mockContactRepo = declareMock<ContactRepo> {
            every { add(any()) } returns contact
        }
        val mockKycRepo = declareMock<KycRepo> {
            every { getById(any()) } returns kyc
        }

        //WHEN
        val result = underTest.add(dto)

        //THEN
        verifyAll {
            mockContactRepo.add(any())
            mockKycRepo.findById(any())
        }
        Assertions.assertThat(result).isEqualTo(contact)
        Assertions.assertThat(result).isInstanceOf(Contact::class.java)
    }

    @Test
    fun `it should update`() {
        //GIVEN
        val contact = mockedContact()
        val dto = mockedUpdateContactDTO()
        val mockContactRepo = declareMock<ContactRepo> {
            every { getById(any()) } returns contact
            every { update(any()) } returns contact
        }
        //WHEN
        val result = underTest.update(dto)

        //THEN
        verifyAll {
            mockContactRepo.findById(any())
            mockContactRepo.update(any())
        }
        Assertions.assertThat(result).isEqualTo(contact)
        Assertions.assertThat(result).isInstanceOf(Contact::class.java)
    }

    @Test
    fun `it should fail to update when id does not exist`() {
        //GIVEN
        val dto = mockedUpdateContactDTO()
        val mockContactRepo = declareMock<ContactRepo> {
            every { getById(any()) } returns null
        }

        //THEN
        Assertions.assertThatThrownBy {
            //WHEN
            underTest.update(dto)
        }
        verify { mockContactRepo.findById(any()) }
        verify(exactly = 0) { mockContactRepo.update(any()) }
    }

    @Test
    fun `it should getById when id exist`() {
        //GIVEN
        val contact = mockedContact()
        val mockContactRepo = declareMock<ContactRepo> {
            every { getById(any()) } returns contact
        }
        //WHEN
        val result = underTest.findById(contact.id!!)

        //THEN
        verify { mockContactRepo.findById(any()) }
        Assertions.assertThat(result).isEqualTo(contact)
        Assertions.assertThat(result).isInstanceOf(Contact::class.java)
    }

    @Test
    fun `it should fail to getById when id does not exist`() {
        //GIVEN
        val contact = mockedContact()
        val mockContactRepo = declareMock<ContactRepo> {
            every { getById(any()) } returns null
        }

        //THEN
        Assertions.assertThatThrownBy {
            //WHEN
            underTest.findById(contact.id!!)
        }
        verify { mockContactRepo.findById(any()) }
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
        val data = listOf(mockedContactDTO(), mockedContactDTO())
        val pagedContent = mockk<PagedContent<ContactDTO>>()
        val spec = ContactSpec()
        spec.page = page
        spec.size = size
        val mockContactRepo = declareMock<ContactRepo> {
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
        verify { mockContactRepo.list(any(), any()) }
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
        val data = emptyList<ContactDTO>()
        val pagedContent = mockk<PagedContent<ContactDTO>>()
        val spec = ContactSpec()
        spec.page = page
        spec.size = size
        val mockContactRepo = declareMock<ContactRepo> {
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
        verify { mockContactRepo.list() }
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
        val data = emptyList<ContactDTO>()
        val pagedContent = mockk<PagedContent<ContactDTO>>()
        val spec = ContactSpec()
        spec.page = page
        spec.size = size
        val mockContactRepo = declareMock<ContactRepo> {
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
        verify { mockContactRepo.search(any()) }
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
        val data = listOf(mockedContactDTO(), mockedContactDTO())
        val pagedContent = mockk<PagedContent<ContactDTO>>()
        val spec = ContactSpec()
        spec.page = page
        spec.size = size
        val mockContactRepo = declareMock<ContactRepo> {
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
        verify { mockContactRepo.search(any()) }
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
        val contact = mockedContact()
        val mockContactRepo = declareMock<ContactRepo> {
            every { deleteById(any()) } returns contact
        }

        //WHEN
        val result = underTest.deleteById(contact.id!!)

        //THEN
        verify { mockContactRepo.deleteById(any()) }
        Assertions.assertThat(result).isEqualTo(contact)
        Assertions.assertThat(result).isInstanceOf(Contact::class.java)
    }

    @Test
    fun `it should fail to deleteById when id does not exist`() {
        //GIVEN
        val id = ObjectId().toHexString()
        val mockContactRepo = declareMock<ContactRepo> {
            every { deleteById(any()) } returns null
        }

        //THEN
        Assertions.assertThatThrownBy {
            //WHEN
            underTest.deleteById(id)
        }
        verify { mockContactRepo.deleteById(any()) }
    }


    @Test
    fun `it should deleteAll`() {
        //GIVEN
        val mockContactRepo = declareMock<ContactRepo> {
            every { deleteAll() } returns true
        }

        //WHEN
        val result = underTest.deleteAll()

        //THEN
        verify { mockContactRepo.deleteAll() }
        Assertions.assertThat(result).isTrue()
    }


    @Test
    fun `it should fail to deleteAll`() {
        //GIVEN
        val mockContactRepo = declareMock<ContactRepo> {
            every { deleteAll() } returns false
        }

        //WHEN
        val result = underTest.deleteAll()

        //THEN
        verify { mockContactRepo.deleteAll() }
        Assertions.assertThat(result).isFalse()
    }
}