package com.codex.business.base.core.settings

import com.codex.base.core.settings.Configuration
import com.codex.business.base.core.di.modules.dbModule
import com.codex.business.base.core.di.modules.repoModule
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.extension.RegisterExtension
import org.koin.logger.slf4jLogger
import org.koin.test.KoinTest
import org.koin.test.junit5.KoinTestExtension
import org.testcontainers.containers.MongoDBContainer
import org.testcontainers.containers.wait.strategy.HttpWaitStrategy

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
open class ContainerTestBase : KoinTest {

    private val mongoContainer: MongoDBContainer =
        MongoDBContainer("mongo:latest")
            .withAccessToHost(true)
            .waitingFor(HttpWaitStrategy())
            .withReuse(true)

    @JvmField
    @RegisterExtension
    val koinTestExtension = KoinTestExtension.create {
        slf4jLogger()
        createEagerInstances()
        allowOverride(true)
        modules(dbModule(mongoContainer.connectionString), repoModule)
    }

    @BeforeAll
    fun setUp() {
        mongoContainer.start()
        Configuration.loadSystemProperties()
    }

    @AfterAll
    fun tearDown() {
        mongoContainer.stop()
    }
}