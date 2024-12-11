package com.codex.base.core.settings

import com.codex.base.shared.Texts
import com.codex.business.components.activityLog.repo.ActivityLog
import com.codex.business.components.comment.repo.Comment
import com.codex.business.components.event.repo.Event
import com.codex.business.components.eventCategory.repo.EventCategory
import com.codex.business.components.eventTag.repo.EventTag
import com.codex.business.components.kyc.repo.Kyc
import com.codex.business.components.user.repo.User
import com.codex.business.components.userProfile.repo.UserProfile
import com.mongodb.ConnectionString
import com.mongodb.MongoClientSettings
import com.mongodb.ServerApi
import com.mongodb.ServerApiVersion
import com.mongodb.client.MongoClients
import dev.morphia.Datastore
import dev.morphia.Morphia
import dev.morphia.config.ManualMorphiaConfig
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.security.SecureRandom
import javax.net.ssl.SSLContext

object MongoDatabaseFactory {
    private val logger: Logger = LoggerFactory.getLogger(this::class.java)

    fun connect(connectionString: String): Datastore {

        val sslContext: SSLContext = SSLContext.getInstance("TLSv1.2")
        sslContext.init(null, null, SecureRandom())

        val serverApi = ServerApi.builder()
            .version(ServerApiVersion.V1)
            .build()
        val settings = MongoClientSettings.builder()
            .applyConnectionString(ConnectionString(connectionString))
            .applyToSslSettings { builder ->
                builder.enabled(true)
            }
            .build()


        val mongoClient = MongoClients.create(settings)

        val packages = listOf(
            User::class.java.packageName,
            UserProfile::class.java.packageName,
            Kyc::class.java.packageName,
            Comment::class.java.packageName,
            Event::class.java.packageName,
            EventTag::class.java.packageName,
            EventCategory::class.java.packageName,
            ActivityLog::class.java.packageName,
        )
        val config = ManualMorphiaConfig.configure()
            .database(Texts.DATABASE_NAME)
            .storeEmpties(true)
            .storeNulls(true)
            .applyIndexes(true)
            .applyDocumentValidations(true)
            .packages(packages)

        val datastore: Datastore = Morphia.createDatastore(mongoClient, config)

        logger.info("Database Successfully configured")
        return datastore
    }
}