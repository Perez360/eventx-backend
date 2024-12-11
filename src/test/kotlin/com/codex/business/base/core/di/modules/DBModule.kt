package com.codex.business.base.core.di.modules

import com.codex.base.core.settings.MongoDatabaseFactory
import dev.morphia.Datastore
import org.koin.core.module.Module
import org.koin.dsl.module

fun dbModule(connectionString: String): Module = module {
    /*Binding and Scoping Datastore*/
    single<Datastore> { MongoDatabaseFactory.connect(connectionString) }
}