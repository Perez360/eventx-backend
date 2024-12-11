package com.codex.business.components.user.service

import com.codex.base.shared.PagedContent
import com.codex.business.base.core.settings.MockTestBase
import com.codex.business.components.kyc.repo.KycRepo
import com.codex.business.components.userProfile.dto.UserDto
import com.codex.business.components.user.repo.User
import com.codex.business.components.user.repo.UserRepo
import com.codex.business.components.userProfile.spec.UserSpec
import com.codex.business.mockedAddUserDto
import com.codex.business.mockedUpdateUserDto
import com.codex.business.mockedUser
import com.codex.business.mockedUserDto
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.mockk.verifyAll
import org.assertj.core.api.Assertions
import org.bson.types.ObjectId
import org.junit.jupiter.api.Test
import org.koin.core.component.inject
import org.koin.test.mock.declareMock

class UserEBActionServiceImplTest : MockTestBase() {
    private val underTest: UserService by inject()

    @Test
    fun `it should add`() {
        //GIVEN
        val user = mockedUser()
        val dto = mockedAddUserDto()
        val mockUserRepo = declareMock<UserRepo> {
            every { add(any()) } returns user
        }

        //WHEN
        val result = underTest.add(dto)

        //THEN
        verify { mockUserRepo.add(any()) }
        Assertions.assertThat(result).isEqualTo(user)
        Assertions.assertThat(result).isInstanceOf(User::class.java)
    }

    @Test
    fun `it should update`() {
        //GIVEN
        val user = mockedUser()
        val dto = mockedUpdateUserDto()
        val mockUserRepo = declareMock<UserRepo> {
            every { getById(any()) } returns user
            every { update(any()) } returns user
        }
        //WHEN
        val result = underTest.update(dto)

        //THEN
        verifyAll {
            mockUserRepo.findById(any())
            mockUserRepo.update(any())
        }
        Assertions.assertThat(result).isEqualTo(user)
        Assertions.assertThat(result).isInstanceOf(User::class.java)
    }

    @Test
    fun `it should fail to update when id does not exist`() {
        //GIVEN
        val dto = mockedUpdateUserDto()
        val mockUserRepo = declareMock<UserRepo> {
            every { getById(any()) } returns null
        }

        //THEN
        Assertions.assertThatThrownBy {
            //WHEN
            underTest.update(dto)
        }
        verify { mockUserRepo.findById(any()) }
        verify(exactly = 0) { mockUserRepo.update(any()) }
    }

    @Test
    fun `it should getById when id exist`() {
        //GIVEN
        val user = mockedUser()
        val mockUserRepo = declareMock<UserRepo> {
            every { getById(any()) } returns user
        }
        //WHEN
        val result = underTest.findById(user.id!!)

        //THEN
        verify { mockUserRepo.findById(any()) }
        Assertions.assertThat(result).isEqualTo(user)
        Assertions.assertThat(result).isInstanceOf(User::class.java)
    }

    @Test
    fun `it should fail to getById when id does not exist`() {
        //GIVEN
        val user = mockedUser()
        val mockUserRepo = declareMock<UserRepo> {
            every { getById(any()) } returns null
        }

        //THEN
        Assertions.assertThatThrownBy {
            //WHEN
            underTest.findById(user.id!!)
        }
        verify { mockUserRepo.findById(any()) }
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
        val data = listOf(mockedUserDto(), mockedUserDto())
        val pagedContent = mockk<PagedContent<UserDto>>()
        val spec = UserSpec()
        spec.page = page
        spec.size = size
        val mockUserRepo = declareMock<UserRepo> {
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
        verify { mockUserRepo.list(any(), any()) }
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
        val data = emptyList<UserDto>()
        val pagedContent = mockk<PagedContent<UserDto>>()
        val spec = UserSpec()
        spec.page = page
        spec.size = size
        val mockUserRepo = declareMock<UserRepo> {
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
        verify { mockUserRepo.list() }
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
        val data = emptyList<UserDto>()
        val pagedContent = mockk<PagedContent<UserDto>>()
        val spec = UserSpec()
        spec.page = page
        spec.size = size
        val mockUserRepo = declareMock<UserRepo> {
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
        verify { mockUserRepo.search(any()) }
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
        val data = listOf(mockedUserDto(), mockedUserDto())
        val pagedContent = mockk<PagedContent<UserDto>>()
        val spec = UserSpec()
        spec.page = page
        spec.size = size
        val mockUserRepo = declareMock<UserRepo> {
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
        verify { mockUserRepo.search(any()) }
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
        val user = mockedUser()
        val mockUserRepo = declareMock<UserRepo> {
            every { deleteById(any()) } returns user
        }
        val mockKycRepo = declareMock<KycRepo> {
            every { deleteById(any()) } returns user.kyc
        }

        //WHEN
        val result = underTest.deleteById(user.id!!)

        //THEN
        verifyAll {
            mockUserRepo.deleteById(any())
            mockKycRepo.deleteById(any())
        }
        Assertions.assertThat(result).isEqualTo(user)
        Assertions.assertThat(result).isInstanceOf(User::class.java)
    }

    @Test
    fun `it should fail to deleteById when id does not exist`() {
        //GIVEN
        val id = ObjectId().toHexString()
        val mockUserRepo = declareMock<UserRepo> {
            every { deleteById(any()) } returns null
        }

        //THEN
        Assertions.assertThatThrownBy {
            //WHEN
            underTest.deleteById(id)
        }
        verify { mockUserRepo.deleteById(any()) }
    }


    @Test
    fun `it should deleteAll`() {
        //GIVEN
        val mockUserRepo = declareMock<UserRepo> {
            every { deleteAll() } returns true
        }

        //WHEN
        val result = underTest.deleteAll()

        //THEN
        verify { mockUserRepo.deleteAll() }
        Assertions.assertThat(result).isTrue()
    }


    @Test
    fun `it should fail to deleteAll`() {
        //GIVEN
        val mockUserRepo = declareMock<UserRepo> {
            every { deleteAll() } returns false
        }

        //WHEN
        val result = underTest.deleteAll()

        //THEN
        verify { mockUserRepo.deleteAll() }
        Assertions.assertThat(result).isFalse()
    }
}