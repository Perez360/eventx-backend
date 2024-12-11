package com.codex.business.workers
//
//import com.codex.base.core.settings.Config
//import com.codex.business.components.department.dto.EmailDTO
//import com.codex.business.components.department.repo.EmailRepo
//import com.codex.business.components.department.spec.EmailSpec
//import com.rabbitmq.client.AMQP.BasicProperties
//import io.vertx.core.AbstractVerticle
//import io.vertx.core.Promise
//import io.vertx.rabbitmq.RabbitMQClient
//import io.vertx.rabbitmq.RabbitMQOptions
//import org.koin.core.Koin
//import org.slf4j.LoggerFactory
//import java.time.Duration
//
//class JobScheduler(koin: Koin) : AbstractVerticle() {
//    private val logger = LoggerFactory.getLogger(this::class.java)
//    private val repo: EmailRepo by koin.inject()
//    private lateinit var client: RabbitMQClient
//
//    override fun start(startPromise: Promise<Void>) {
//        logger.info("${this::class.simpleName} worker is ready!!")
//
//        val config = RabbitMQOptions()
//            .setHost(Config.rbHost)
//            .setPort(Config.rbPort)
//            .setUser(Config.rbUsername)
//            .setPassword(Config.rbPassword)
//            .setVirtualHost(Config.rbVhost)
//
//        client = RabbitMQClient.create(vertx, config)
//
//        client.addConnectionEstablishedCallback(::rbConnectionCallbackHandler)
//
//        client.start().onSuccess {
//            logger.info("Rabbit client has started successfully")
//            startPromise.complete()
//        }.onFailure { cause ->
//            logger.error("Failed to connect to rabbitmq for ${this::class.simpleName} worker", cause)
//            startPromise.fail(cause)
//        }
//
//        vertx.setPeriodic(Duration.ofMinutes(1).toMillis(), ::job)
//    }
//
//    private fun job(id: Long) {
//        logger.info("Job id: $id")
//
//        val foundEmails = fetchPendingEmails()
//        if (foundEmails.isNotEmpty()) {
//            pushMailToBroker(foundEmails.first())
//        }
//
//    }
//
//    private fun pushMailToBroker(dto: EmailDTO) {
//        client.confirmSelect()
//            .compose {
//                val props = BasicProperties.Builder().messageId(dto.id).build()
//                client.basicPublish("", Config.rbSendMailRequestQueue, props, dto.toJson().toBuffer())
//            }
//            .compose { client.waitForConfirms() }
//            .onSuccess { logger.info("Successfully published mail to broker with messageId: ${dto.id}") }
//            .onFailure { cause -> logger.error("Failed to publish mail to broker with messageId: ${dto.id}", cause) }
//    }
//
//    private fun rbConnectionCallbackHandler(callbackPromise: Promise<Void>) {
//        client.queueDeclare(Config.rbSendMailRequestQueue, true, false, false)
//            .compose { client.queueDeclare(Config.rbSendMailResponseQueue, true, false, false) }
//            .onSuccess { callbackPromise.complete() }
//            .onFailure(callbackPromise::fail)
//    }
//
//
//    private fun fetchPendingEmails(): List<EmailDTO> {
//        val spec = EmailSpec()
//        spec.status = EmailStatus.PENDING
//        val listOfUnsentEmails = repo.search(spec)
//
//        if (listOfUnsentEmails.data.isEmpty()) {
//            logger.info("No pending email(s) found in db")
//        } else {
//            logger.info("List of pending emails found: ${listOfUnsentEmails.data}")
//        }
//
//        return listOfUnsentEmails.data
//    }
//}