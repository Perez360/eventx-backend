package com.codex.base.shared

object Texts {

    const val CODE_SUCCESS = "00"
    const val CODE_FAILURE = "01"
    const val CODE_ERROR = "02"
    const val CODE_ASYNC = "03"
    const val CODE_UNAUTHORIZED = "04"

    const val DATE_TIME_PATTERN = "yyyy-MM-dd'T'HH:mm:ss.SSS a"
    const val DATE_PATTERN = "yyyy-MM-dd"
    const val TIME_PATTERN = "HH:mm:ss.SSS a"

    val SERVER_PORT: Int = System.getProperty("app.server.port").toInt()
    val DATABASE_NAME: String = System.getProperty("app.datasource.dbName", "vertx_db")
    val DATABASE_CONNECTION_STRING: String =
        System.getProperty("app.datasource.connectionString", "mongodb://127.0.0.1:27017")
    val STORAGE_BUCKET_NAME: String = System.getProperty("app.datasource.bucketName", "bucket")
    val KEYCLOAK_SERVER_URL: String = System.getProperty("app.keycloak.serverUrl", "http://127.0.0.1:8080")
    val KEYCLOAK_SECRET: String? = System.getProperty("app.keycloak.secret")
    val KEYCLOAK_CLIENT_ID: String? = System.getProperty("app.keycloak.clientId")
    val KEYCLOAK_ADMIN_USER: String? = System.getProperty("app.keycloak.admin-user")
    val KEYCLOAK_ADMIN_PASS: String? = System.getProperty("app.keycloak.admin-pass")
    val KEYCLOAK_REALM: String? = System.getProperty("app.keycloak.realm")

    val ebSendTimeout = System.getProperty("app.eventbus.timeout", "60000").toLong()
    val isWorkerService = System.getProperty("app.service.isWorker", "true").toBooleanStrict()
    val numberOfServiceInstances: Int = System.getProperty("app.service.instances", "1").toInt()
    val numberOfJobSchedulerInstances: Int = System.getProperty("app.jobScheduler.instances", "1").toInt()
    val numberOfEmailDispatcherInstances: Int = System.getProperty("app.emailDispatcher.instances", "1").toInt()
    val numberOfWebServerInstances: Int = System.getProperty("app.webServer.instances", "1").toInt()
    val numberOfWebClientInstances: Int = System.getProperty("app.webClient.instances", "1").toInt()
    val numberOfRBConsumerInstances: Int = System.getProperty("app.rbConsumer.instances", "1").toInt()

    val SMTP_HOST: String = System.getProperty("app.smtp.host")
    val SMTP_PORT = System.getProperty("app.smtp.port").toInt()
    val SMTP_USERNAME: String = System.getProperty("app.smtp.username")
    val SMTP_PASSWORD: String = System.getProperty("app.smtp.password")

    val RABBITMQ_HOST: String = System.getProperty("app.rabbitmq.host", "127.0.0.1")
    val RABBITMQ_PORT = System.getProperty("app.rabbitmq.port", "5672").toInt()
    val RABBITMQ_USERNAME: String = System.getProperty("app.rabbitmq.username")
    val RABBITMQ_PASSWORD: String = System.getProperty("app.rabbitmq.password")
    val RABBITMQ_VHOST: String = System.getProperty("app.rabbitmq.vhost", "/")
    val rbSendMailRequestQueue: String = System.getProperty("app.rabbitmq.mailRequestQueue")
    val rbSendMailResponseQueue: String = System.getProperty("app.rabbitmq.mailResponseQueue")

    val redisServerUrl: String = System.getProperty("app.redis.serverUrl")

    val httpConnectionTimeout: Int = System.getProperty("app.http.connectionTimeout", "60").toInt()
    val sseConnectionTimeout: Long = System.getProperty("app.sse.connectionTimeout", "5000").toLong()

    val ONESIGNAL_APP_ID: String = System.getProperty("app.onesignal.appId")
    val ONESIGNAL_URL: String = System.getProperty("app.onesignal.url")
}
