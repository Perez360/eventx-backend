package com.codex.business.components.user.repo

import com.codex.base.shared.PagedContent
import com.codex.business.base.core.settings.ContainerTestBase
import com.codex.business.components.userProfile.spec.UserSpec
import com.codex.business.faker
import com.codex.business.mockedUser
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.koin.test.inject
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class UserEBActionRepoImplTest : ContainerTestBase() {
    private val underTest: UserRepo by inject()
    private val savedUsers = mutableListOf<User>()
    private val logger: Logger = LoggerFactory.getLogger(this::class.java)

    @BeforeEach
    fun prepareWork() {
        repeat(10) {
            val oneUser = underTest.add(mockedUser())
            savedUsers.add(oneUser)
        }
        logger.info("Saved users successfully")
    }

    @AfterEach
    fun cleanUp() {
        savedUsers.clear()
        underTest.deleteAll()
    }

    @Test
    fun add() {
        //GIVEN
        val mockedUser = mockedUser()
        //WHEN
        val oneUser = underTest.add(mockedUser)
        //THEN
        Assertions.assertThat(mockedUser).isNotNull()
        Assertions.assertThat(mockedUser).isSameAs(oneUser)
        Assertions.assertThat(oneUser).isInstanceOf(User::class.java)
    }

    @Test
    fun update() {
        //GIVEN
        val savedUser = savedUsers.random()
        savedUser.firstName = faker.name().title()
        //WHEN
        val oneUser = underTest.update(savedUser)
        //THEN
        Assertions.assertThat(savedUser.id).isEqualTo(oneUser.id)
        Assertions.assertThat(savedUser.firstName).isEqualTo(oneUser.firstName)
        Assertions.assertThat(savedUser.lastName).isEqualTo(oneUser.lastName)
        Assertions.assertThat(savedUser.otherName).isEqualTo(oneUser.otherName)
        Assertions.assertThat(oneUser).isInstanceOf(User::class.java)
    }


    @Test
    fun count() {
        //WHEN
        val count = underTest.count()
        //THEN
        Assertions.assertThat(count).isEqualTo(savedUsers.size.toLong())
    }

    @Test
    fun findById() {
        //GIVEN
        val savedUser = savedUsers.random()
        //WHEN
        val oneUser = underTest.findById(savedUser.id!!)
        //THEN
        Assertions.assertThat(oneUser).isInstanceOf(User::class.java)
        Assertions.assertThat(oneUser?.id).isEqualTo(savedUser.id)
        Assertions.assertThat(oneUser?.firstName).isEqualTo(savedUser.firstName)
        Assertions.assertThat(oneUser?.lastName).isEqualTo(savedUser.lastName)
        Assertions.assertThat(oneUser?.otherName).isEqualTo(savedUser.otherName)
    }

    @Test
    fun search() {
        //GIVEN
        val spec = UserSpec()
        //WHEN
        val pagedContent = underTest.search(spec)
        //THEN
        Assertions.assertThat(pagedContent).isInstanceOf(PagedContent::class.java)
        Assertions.assertThat(pagedContent.data.size).isEqualTo(savedUsers.size)
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
        Assertions.assertThat(pagedContent.data.size).isEqualTo(savedUsers.size)
    }

    @Test
    fun delete() {
        //GIVEN
        val savedUser = savedUsers.random()
        //WHEN
        val oneUser = underTest.deleteById(savedUser.id!!)
        //THEN
        Assertions.assertThat(oneUser).isInstanceOf(User::class.java)
        Assertions.assertThat(oneUser?.id).isEqualTo(savedUser.id)
        Assertions.assertThat(oneUser?.firstName).isEqualTo(savedUser.firstName)
        Assertions.assertThat(oneUser?.lastName).isEqualTo(savedUser.lastName)
        Assertions.assertThat(oneUser?.otherName).isEqualTo(savedUser.otherName)
    }
}