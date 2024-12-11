package com.codex.base.core.di.modules

import com.codex.base.core.settings.MongoDatabaseFactory
import com.mongodb.client.gridfs.GridFSBucket
import com.mongodb.client.gridfs.GridFSBuckets
import dev.morphia.Datastore
import org.koin.dsl.module


//Binding DB Module
val DB_MODULE = module(createdAtStart = true) {
    single<Datastore> { MongoDatabaseFactory.connect(getProperty("dbConnectionString")) }
    single<GridFSBucket> { GridFSBuckets.create(get<Datastore>().database, getProperty("bucketName")) }
}
