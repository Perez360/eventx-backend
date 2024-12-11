package com.codex.business.components.kyc.repo

import com.codex.base.shared.PagedContent
import com.codex.business.base.core.settings.ContainerTestBase
import com.codex.business.components.kyc.spec.KycSpec
import com.codex.business.faker
import com.codex.business.mockedKyc
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.koin.test.inject
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class KycEBActionRepoImplTest : ContainerTestBase() {
    private val underTest: KycRepo by inject()
    private val savedKycs = mutableListOf<Kyc>()
    private val logger: Logger = LoggerFactory.getLogger(this::class.java)

    @BeforeEach
    fun prepareWork() {
        repeat(10) {
            val oneKyc = underTest.add(mockedKyc())
            savedKycs.add(oneKyc)
        }
        logger.info("Saved kyc successfully")
    }

    @AfterEach
    fun cleanUp() {
        savedKycs.clear()
        underTest.deleteAll()
    }

    @Test
    fun add() {
        //GIVEN
        val mockedKyc = mockedKyc()
        //WHEN
        val oneKyc = underTest.add(mockedKyc)
        //THEN
        Assertions.assertThat(mockedKyc).isNotNull()
        Assertions.assertThat(mockedKyc).isSameAs(oneKyc)
        Assertions.assertThat(oneKyc).isInstanceOf(Kyc::class.java)
    }

    @Test
    fun update() {
        //GIVEN
        val savedKyc = savedKycs.random()
        savedKyc.firstName = faker.name().firstName()
        savedKyc.lastName = faker.name().lastName()
        savedKyc.otherName = faker.name().name()
        //WHEN
        val oneKyc = underTest.update(savedKyc)
        //THEN
        Assertions.assertThat(savedKyc.id).isEqualTo(oneKyc.id)
        Assertions.assertThat(savedKyc.firstName).isEqualTo(oneKyc.firstName)
        Assertions.assertThat(savedKyc.lastName).isEqualTo(oneKyc.lastName)
        Assertions.assertThat(savedKyc.otherName).isEqualTo(oneKyc.otherName)
        Assertions.assertThat(oneKyc).isInstanceOf(Kyc::class.java)
    }

    @Test
    fun exists() {
        //GIVEN
        val savedKyc = savedKycs.random()
        //WHEN
        val isKycExist = underTest.exists(savedKyc.id!!)
        //THEN
        Assertions.assertThat(isKycExist).isTrue()
    }

    @Test
    fun count() {
        //WHEN
        val count = underTest.count()
        //THEN
        Assertions.assertThat(count).isEqualTo(savedKycs.size.toLong())
    }

    @Test
    fun get() {
        //GIVEN
        val savedKyc = savedKycs.random()
        //WHEN
        val oneKyc = underTest.findById(savedKyc.id!!)
        //THEN
        Assertions.assertThat(oneKyc).isInstanceOf(Kyc::class.java)
        Assertions.assertThat(savedKyc.id).isEqualTo(savedKyc.id)
        Assertions.assertThat(savedKyc.firstName).isEqualTo(oneKyc?.firstName)
        Assertions.assertThat(savedKyc.lastName).isEqualTo(oneKyc?.lastName)
        Assertions.assertThat(savedKyc.otherName).isEqualTo(oneKyc?.otherName)
    }

    @Test
    fun search() {
        //GIVEN
        val spec = KycSpec()
        //WHEN
        val pagedContent = underTest.search(spec)
        //THEN
        Assertions.assertThat(pagedContent).isInstanceOf(PagedContent::class.java)
        Assertions.assertThat(pagedContent.data.size).isEqualTo(savedKycs.size)
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
        Assertions.assertThat(pagedContent.data.size).isEqualTo(savedKycs.size)
    }

    @Test
    fun delete() {
        //GIVEN
        val savedKyc = savedKycs.random()
        //WHEN
        val oneKyc = underTest.deleteById(savedKyc.id!!)
        //THEN
        Assertions.assertThat(oneKyc).isInstanceOf(Kyc::class.java)
        Assertions.assertThat(oneKyc?.id).isEqualTo(savedKyc.id)
        Assertions.assertThat(savedKyc.firstName).isEqualTo(oneKyc?.firstName)
        Assertions.assertThat(savedKyc.lastName).isEqualTo(oneKyc?.lastName)
        Assertions.assertThat(savedKyc.otherName).isEqualTo(oneKyc?.otherName)
    }
}