package com.codex.business.components.kyc.service

import com.codex.base.shared.PagedContent
import com.codex.business.*
import com.codex.business.base.core.settings.MockTestBase
import com.codex.business.components.kyc.dto.KycDto
import com.codex.business.components.kyc.repo.Kyc
import com.codex.business.components.kyc.repo.KycRepo
import com.codex.business.components.kyc.spec.KycSpec
import com.codex.business.components.user.repo.UserRepo
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.mockk.verifyAll
import org.assertj.core.api.Assertions
import org.bson.types.ObjectId
import org.junit.jupiter.api.Test
import org.koin.core.component.inject
import org.koin.test.mock.declareMock

class KycEBActionServiceImplTest : MockTestBase() {
    private val underTest: KycService by inject()

    @Test
    fun `it should add when kyc not exists`() {
        //GIVEN
        val kyc = mockedKyc()
        val user = mockedUser()
        val dto = mockedAddKycDTO()
        val mockUserRepo = declareMock<UserRepo> {
            every { getById(any()) } returns user
            every { update(any()) } returns user
        }
        val mockKycRepo = declareMock<KycRepo> {
            every { add(any()) } returns kyc
        }

        //WHEN
        val result = underTest.add(dto)

        //THEN
        verifyAll {
            mockUserRepo.findById(any())
            mockUserRepo.update(any())
            mockKycRepo.add(any())
        }
        Assertions.assertThat(result).isEqualTo(kyc)
        Assertions.assertThat(result).isInstanceOf(Kyc::class.java)
    }

    @Test
    fun `it should fail to add when already exists`() {
        //GIVEN
        val kyc = mockedKyc()
        val user = mockedUser()
        val dto = mockedAddKycDTO()
        val mockUserRepo = declareMock<UserRepo> {
            every { getById(any()) } returns user
            every { update(any()) } returns user
        }
        val mockKycRepo = declareMock<KycRepo> {
            every { add(any()) } returns kyc
        }

        //THEN
        Assertions.assertThatThrownBy {
            //WHEN
            underTest.add(dto)
        }
        verify(exactly = 0) { mockUserRepo.update(any()) }
        verify {
            mockKycRepo.findById(any())
        }
    }

    @Test
    fun `it should update`() {
        //GIVEN
        val kyc = mockedKyc()
        val dto = mockedUpdateKycDTO()
        val mockKycRepo = declareMock<KycRepo> {
            every { getById(any()) } returns kyc
            every { getById(any()) } returns kyc
            every { update(any()) } returns kyc
        }
        //WHEN
        val result = underTest.update(dto)

        //THEN
        verifyAll {
            mockKycRepo.findById(any())
            mockKycRepo.update(any())
        }
        Assertions.assertThat(result).isEqualTo(kyc)
        Assertions.assertThat(result).isInstanceOf(Kyc::class.java)
    }

    @Test
    fun `it should fail to update when id does not exist`() {
        //GIVEN
        val dto = mockedUpdateKycDTO()
        val mockKycRepo = declareMock<KycRepo> {
            every { getById(any()) } returns null
        }

        //THEN
        Assertions.assertThatThrownBy {
            //WHEN
            underTest.update(dto)
        }
        verify { mockKycRepo.findById(any()) }
        verify(exactly = 0) { mockKycRepo.update(any()) }
    }

    @Test
    fun `it should getById when id exist`() {
        //GIVEN
        val kyc = mockedKyc()
        val mockKycRepo = declareMock<KycRepo> {
            every { getById(any()) } returns kyc
        }
        //WHEN
        val result = underTest.findById(kyc.id!!)

        //THEN
        verify { mockKycRepo.findById(any()) }
        Assertions.assertThat(result).isEqualTo(kyc)
        Assertions.assertThat(result).isInstanceOf(Kyc::class.java)
    }

    @Test
    fun `it should fail to getById when id does not exist`() {
        //GIVEN
        val kyc = mockedKyc()
        val mockKycRepo = declareMock<KycRepo> {
            every { getById(any()) } returns null
        }

        //THEN
        Assertions.assertThatThrownBy {
            //WHEN
            underTest.findById(kyc.id!!)
        }
        verify { mockKycRepo.findById(any()) }
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
        val data = listOf(mockedKycDTO(), mockedKycDTO())
        val pagedContent = mockk<PagedContent<KycDto>>()
        val spec = KycSpec()
        spec.page = page
        spec.size = size
        val mockKycRepo = declareMock<KycRepo> {
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
        verify { mockKycRepo.list(any(), any()) }
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
        val data = emptyList<KycDto>()
        val pagedContent = mockk<PagedContent<KycDto>>()
        val spec = KycSpec()
        spec.page = page
        spec.size = size
        val mockKycRepo = declareMock<KycRepo> {
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
        verify { mockKycRepo.list() }
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
        val data = emptyList<KycDto>()
        val pagedContent = mockk<PagedContent<KycDto>>()
        val spec = KycSpec()
        spec.page = page
        spec.size = size
        val mockKycRepo = declareMock<KycRepo> {
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
        verify { mockKycRepo.search(any()) }
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
        val data = listOf(mockedKycDTO(), mockedKycDTO())
        val pagedContent = mockk<PagedContent<KycDto>>()
        val spec = KycSpec()
        spec.page = page
        spec.size = size
        val mockKycRepo = declareMock<KycRepo> {
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
        verify { mockKycRepo.search(any()) }
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
        val kyc = mockedKyc()
        val mockKycRepo = declareMock<KycRepo> {
            every { deleteById(any()) } returns kyc
        }

        //WHEN
        val result = underTest.deleteById(kyc.id!!)

        //THEN
        verify { mockKycRepo.deleteById(any()) }
        Assertions.assertThat(result).isEqualTo(kyc)
        Assertions.assertThat(result).isInstanceOf(Kyc::class.java)
    }

    @Test
    fun `it should fail to deleteById when id does not exist`() {
        //GIVEN
        val id = ObjectId().toHexString()
        val mockKycRepo = declareMock<KycRepo> {
            every { deleteById(any()) } returns null
        }

        //THEN
        Assertions.assertThatThrownBy {
            //WHEN
            underTest.deleteById(id)
        }
        verify { mockKycRepo.deleteById(any()) }
    }


    @Test
    fun `it should deleteAll`() {
        //GIVEN
        val mockKycRepo = declareMock<KycRepo> {
            every { deleteAll() } returns true
        }

        //WHEN
        val result = underTest.deleteAll()

        //THEN
        verify { mockKycRepo.deleteAll() }
        Assertions.assertThat(result).isTrue()
    }


    @Test
    fun `it should fail to deleteAll`() {
        //GIVEN
        val mockKycRepo = declareMock<KycRepo> {
            every { deleteAll() } returns false
        }

        //WHEN
        val result = underTest.deleteAll()

        //THEN
        verify { mockKycRepo.deleteAll() }
        Assertions.assertThat(result).isFalse()
    }
}