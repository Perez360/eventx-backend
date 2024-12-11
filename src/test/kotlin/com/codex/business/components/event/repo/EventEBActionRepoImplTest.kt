package com.codex.business.components.event.repo

import com.codex.base.shared.PagedContent
import com.codex.business.base.core.settings.ContainerTestBase
import com.codex.business.components.event.spec.EventSpec
import com.codex.business.faker
import com.codex.business.mockedEvent
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.koin.test.inject
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class EventEBActionRepoImplTest : ContainerTestBase() {
    private val underTest: EventRepo by inject()
    private val savedEvents = mutableListOf<Event>()
    private val logger: Logger = LoggerFactory.getLogger(this::class.java)

    @BeforeEach
    fun prepareWork() {
        repeat(10) {
            val oneEvent = underTest.add(mockedEvent())
            savedEvents.add(oneEvent)
        }
        logger.info("Saved contacts successfully")
    }

    @AfterEach
    fun cleanUp() {
        savedEvents.clear()
        underTest.deleteAll()
    }

    @Test
    fun add() {
        //GIVEN
        val mockedEvent = mockedEvent()
        //WHEN
        val oneEvent = underTest.add(mockedEvent)
        //THEN
        Assertions.assertThat(mockedEvent).isNotNull()
        Assertions.assertThat(mockedEvent).isSameAs(oneEvent)
        Assertions.assertThat(oneEvent).isInstanceOf(Event::class.java)
    }

    @Test
    fun update() {
        //GIVEN
        val savedEvent = savedEvents.random()
        savedEvent.title = faker.name().title()
        savedEvent.description = faker.name().title()
        //WHEN
        val oneEvent = underTest.update(savedEvent)
        //THEN
        Assertions.assertThat(savedEvent.id).isEqualTo(oneEvent.id)
        Assertions.assertThat(savedEvent.title).isEqualTo(oneEvent.title)
        Assertions.assertThat(savedEvent.description).isEqualTo(oneEvent.description)
        Assertions.assertThat(oneEvent).isInstanceOf(Event::class.java)
    }

    @Test
    fun exists() {
        //GIVEN
        val savedEvent = savedEvents.random()
        //WHEN
        val isEventExist = underTest.exists(savedEvent.id!!)
        //THEN
        Assertions.assertThat(isEventExist).isTrue()
    }

    @Test
    fun count() {
        //WHEN
        val count = underTest.count()
        //THEN
        Assertions.assertThat(count).isEqualTo(savedEvents.size.toLong())
    }

    @Test
    fun get() {
        //GIVEN
        val savedEvent = savedEvents.random()
        //WHEN
        val oneEvent = underTest.findById(savedEvent.id!!)
        //THEN
        Assertions.assertThat(oneEvent).isInstanceOf(Event::class.java)
        Assertions.assertThat(oneEvent?.id).isEqualTo(savedEvent.id)
        Assertions.assertThat(oneEvent?.title).isEqualTo(savedEvent.title)
        Assertions.assertThat(oneEvent?.description).isEqualTo(savedEvent.description)
    }

    @Test
    fun search() {
        //GIVEN
        val spec = EventSpec()
        //WHEN
        val pagedContent = underTest.search(spec)
        //THEN
        Assertions.assertThat(pagedContent).isInstanceOf(PagedContent::class.java)
        Assertions.assertThat(pagedContent.data.size).isEqualTo(savedEvents.size)
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
        Assertions.assertThat(pagedContent.data.size).isEqualTo(savedEvents.size)
    }

    @Test
    fun delete() {
        //GIVEN
        val savedEvent = savedEvents.random()
        //WHEN
        val oneEvent = underTest.deleteById(savedEvent.id!!)
        //THEN
        Assertions.assertThat(oneEvent).isInstanceOf(Event::class.java)
        Assertions.assertThat(oneEvent?.id).isEqualTo(savedEvent.id)
        Assertions.assertThat(oneEvent?.title).isEqualTo(savedEvent.title)
        Assertions.assertThat(oneEvent?.description).isEqualTo(savedEvent.description)
    }
}