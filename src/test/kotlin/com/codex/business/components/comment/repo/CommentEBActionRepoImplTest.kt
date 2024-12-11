package com.codex.business.components.comment.repo

import com.codex.base.shared.PagedContent
import com.codex.business.base.core.settings.ContainerTestBase
import com.codex.business.components.comment.spec.CommentSpec
import com.codex.business.mockedComment
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.koin.test.inject
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class CommentEBActionRepoImplTest : ContainerTestBase() {
    private val underTest: CommentRepo by inject()
    private val savedComments = mutableListOf<Comment>()
    private val logger: Logger = LoggerFactory.getLogger(this::class.java)


    @BeforeEach
    fun prepareWork() {
        repeat(10) {
            val oneDepartment = underTest.add(mockedComment())
            savedComments.add(oneDepartment)
        }
        logger.info("Saved comments successfully")
    }

    @AfterEach
    fun cleanUp() {
        savedComments.clear()
        underTest.deleteAll()
    }

    @Test
    fun add() {
        //GIVEN
        val mockedDepartment = mockedComment()
        //WHEN
        val oneDepartment = underTest.add(mockedDepartment)
        //THEN
        Assertions.assertThat(mockedDepartment).isNotNull()
        Assertions.assertThat(mockedDepartment).isSameAs(oneDepartment)
        Assertions.assertThat(oneDepartment).isInstanceOf(Comment::class.java)
    }

    @Test
    fun update() {
        //GIVEN
        val savedComment = savedComments.random()
        savedComment.message = "Nice"
        //WHEN
        val oneComment = underTest.update(savedComment)
        //THEN
        Assertions.assertThat(savedComment.id).isEqualTo(oneComment.id)
        Assertions.assertThat(savedComment.user).isEqualTo(oneComment.user)
        Assertions.assertThat(oneComment).isInstanceOf(Comment::class.java)
    }

    @Test
    fun count() {
        //WHEN
        val count = underTest.count()
        //THEN
        Assertions.assertThat(count).isEqualTo(savedComments.size.toLong())
    }

    @Test
    fun findById() {
        //GIVEN
        val savedDepartment = savedComments.random()
        //WHEN
        val oneDepartment = underTest.findById(savedDepartment.id!!)
        //THEN
        Assertions.assertThat(oneDepartment).isInstanceOf(Comment::class.java)
        Assertions.assertThat(oneDepartment?.id).isEqualTo(savedDepartment.id)
        Assertions.assertThat(oneDepartment?.message).isEqualTo(savedDepartment.message)
    }

    @Test
    fun search() {
        //GIVEN
        val spec = CommentSpec()
        //WHEN
        val pagedContent = underTest.search(spec)
        //THEN
        Assertions.assertThat(pagedContent).isInstanceOf(PagedContent::class.java)
        Assertions.assertThat(pagedContent.data.size).isEqualTo(savedComments.size)
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
        Assertions.assertThat(pagedContent.data.size).isEqualTo(savedComments.size)
    }

    @Test
    fun deleteById() {
        //GIVEN
        val savedDepartment = savedComments.random()
        //WHEN
        val oneDepartment = underTest.deleteById(savedDepartment.id!!)
        //THEN
        Assertions.assertThat(oneDepartment).isInstanceOf(Comment::class.java)
        Assertions.assertThat(oneDepartment?.id).isEqualTo(savedDepartment.id)
        Assertions.assertThat(oneDepartment?.message).isEqualTo(savedDepartment.message)
    }
}