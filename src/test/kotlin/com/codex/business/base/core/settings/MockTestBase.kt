package com.codex.business.base.core.settings

import com.codex.business.base.core.di.modules.serviceModule
import io.mockk.mockkClass
import org.junit.jupiter.api.extension.RegisterExtension
import org.koin.logger.slf4jLogger
import org.koin.test.KoinTest
import org.koin.test.junit5.KoinTestExtension
import org.koin.test.junit5.mock.MockProviderExtension

// Koin runs test instance per-method by default
open class MockTestBase : KoinTest {
    @JvmField
    @RegisterExtension
    val koinTestExtension = KoinTestExtension.create {
        slf4jLogger()
        modules(serviceModule)
    }

    @JvmField
    @RegisterExtension
    val mockProviderExtension = MockProviderExtension.create { clazz ->
        mockkClass(clazz, relaxed = true)
    }
}