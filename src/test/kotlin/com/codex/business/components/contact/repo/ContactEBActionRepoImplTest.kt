package com.codex.business.components.contact.repo

import com.codex.base.shared.PagedContent
import com.codex.business.base.core.settings.ContainerTestBase
import com.codex.business.components.contact.enum.ContactType
import com.codex.business.components.contact.spec.ContactSpec
import com.codex.business.faker
import com.codex.business.mockedContact
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.koin.test.inject
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class ContactEBActionRepoImplTest : ContainerTestBase() {
    private val underTest: ContactRepo by inject()
    private val savedContacts = mutableListOf<Contact>()
    private val logger: Logger = LoggerFactory.getLogger(this::class.java)

    @BeforeEach
    fun prepareWork() {
        repeat(10) {
            val oneContact = underTest.add(mockedContact())
            savedContacts.add(oneContact)
        }
        logger.info("Saved contacts successfully")
    }

    @AfterEach
    fun cleanUp() {
        savedContacts.clear()
        underTest.deleteAll()
    }

    @Test
    fun add() {
        //GIVEN
        val mockedContact = mockedContact()
        //WHEN
        val oneContact = underTest.add(mockedContact)
        //THEN
        Assertions.assertThat(mockedContact).isNotNull()
        Assertions.assertThat(mockedContact).isSameAs(oneContact)
        Assertions.assertThat(oneContact).isInstanceOf(Contact::class.java)
    }

    @Test
    fun update() {
        //GIVEN
        val savedContact = savedContacts.random()
        savedContact.content = faker.phoneNumber().phoneNumber()
        savedContact.type = ContactType.MOBILE
        //WHEN
        val oneContact = underTest.update(savedContact)
        //THEN
        Assertions.assertThat(savedContact.id).isEqualTo(oneContact.id)
        Assertions.assertThat(savedContact.type).isEqualTo(oneContact.type)
        Assertions.assertThat(savedContact.content).isEqualTo(oneContact.content)
        Assertions.assertThat(oneContact).isInstanceOf(Contact::class.java)
    }

    @Test
    fun exists() {
        //GIVEN
        val savedContact = savedContacts.random()
        //WHEN
        val isContactExist = underTest.exists(savedContact.id!!)
        //THEN
        Assertions.assertThat(isContactExist).isTrue()
    }

    @Test
    fun count() {
        //WHEN
        val count = underTest.count()
        //THEN
        Assertions.assertThat(count).isEqualTo(savedContacts.size.toLong())
    }

    @Test
    fun get() {
        //GIVEN
        val savedContact = savedContacts.random()
        //WHEN
        val oneContact = underTest.findById(savedContact.id!!)
        //THEN
        Assertions.assertThat(oneContact).isInstanceOf(Contact::class.java)
        Assertions.assertThat(oneContact?.id).isEqualTo(savedContact.id)
        Assertions.assertThat(oneContact?.content).isEqualTo(savedContact.content)
        Assertions.assertThat(oneContact?.type).isEqualTo(savedContact.type)
    }

    @Test
    fun search() {
        //GIVEN
        val spec = ContactSpec()

        //WHEN
        val pagedContent = underTest.search(spec)
        //THEN
        Assertions.assertThat(pagedContent).isInstanceOf(PagedContent::class.java)
        Assertions.assertThat(pagedContent.data.size).isEqualTo(savedContacts.size)
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
        Assertions.assertThat(pagedContent.data.size).isEqualTo(savedContacts.size)
    }

    @Test
    fun delete() {
        //GIVEN
        val savedContact = savedContacts.random()
        //WHEN
        val oneContact = underTest.deleteById(savedContact.id!!)
        //THEN
        Assertions.assertThat(oneContact).isInstanceOf(Contact::class.java)
        Assertions.assertThat(oneContact?.id).isEqualTo(savedContact.id)
        Assertions.assertThat(oneContact?.content).isEqualTo(savedContact.content)
        Assertions.assertThat(oneContact?.type).isEqualTo(savedContact.type)
    }
}