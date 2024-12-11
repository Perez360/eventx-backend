package com.codex.business.components.activityLog.repo

import com.codex.base.shared.PagedContent
import com.codex.business.base.core.settings.ContainerTestBase
import com.codex.business.components.activityLog.spec.ActivityLogSpec
import com.codex.business.mockedActivity
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.koin.test.inject
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class ActivityRepoImplTest : ContainerTestBase() {
    private val underTest: ActivityLogRepo by inject()
    private val savedLogs = mutableListOf<ActivityLog>()
    private val logger: Logger = LoggerFactory.getLogger(this::class.java)

    @BeforeEach
    fun prepareWork() {
        repeat(10) {
            val oneLog = underTest.add(mockedActivity())
            savedLogs.add(oneLog)
        }
        logger.info("Saved kyc successfully")
    }

    @AfterEach
    fun cleanUp() {
        savedLogs.clear()
        underTest.deleteAll()
    }

    @Test
    fun add() {
        //GIVEN
        val mockedActivityLog = mockedActivity()
        //WHEN
        val oneLog = underTest.add(mockedActivityLog)
        //THEN
        Assertions.assertThat(mockedActivityLog).isNotNull()
        Assertions.assertThat(mockedActivityLog).isSameAs(oneLog)
        Assertions.assertThat(oneLog).isInstanceOf(ActivityLog::class.java)
    }

    @Test
    fun count() {
        //WHEN
        val count = underTest.count()
        //THEN
        Assertions.assertThat(count).isEqualTo(savedLogs.size.toLong())
    }

    @Test
    fun get() {
        //GIVEN
        val savedLog = savedLogs.random()
        //WHEN
        val oneLog = underTest.findById(savedLog.id!!)
        //THEN
        Assertions.assertThat(oneLog).isInstanceOf(ActivityLog::class.java)
        Assertions.assertThat(savedLog.id).isEqualTo(savedLog.id)
        Assertions.assertThat(savedLog.description).isEqualTo(oneLog?.description)
        Assertions.assertThat(savedLog.type).isEqualTo(oneLog?.type)
    }

    @Test
    fun search() {
        //GIVEN
        val spec = ActivityLogSpec()
        //WHEN
        val pagedContent = underTest.search(spec)
        //THEN
        Assertions.assertThat(pagedContent).isInstanceOf(PagedContent::class.java)
        Assertions.assertThat(pagedContent.data.size).isEqualTo(savedLogs.size)
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
        Assertions.assertThat(pagedContent.data.size).isEqualTo(savedLogs.size)
    }

    @Test
    fun delete() {
        //GIVEN
        val savedKyc = savedLogs.random()
        //WHEN
        val oneLog = underTest.deleteById(savedKyc.id!!)
        //THEN
        Assertions.assertThat(oneLog).isInstanceOf(ActivityLog::class.java)
        Assertions.assertThat(oneLog?.id).isEqualTo(savedKyc.id)
        Assertions.assertThat(savedKyc.description).isEqualTo(oneLog?.description)
        Assertions.assertThat(savedKyc.type).isEqualTo(oneLog?.type)
    }
}