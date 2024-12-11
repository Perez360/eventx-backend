package com.codex.business.workers

//
//class RBConsumer(koin: Koin) : AbstractVerticle() {
//    private lateinit var client: RabbitMQClient
//    private val logger = LoggerFactory.getLogger(this::class.java)
//    private val repo: EmailRepo by koin.inject()
//
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
//        client.start()
//            .compose(::consume)
//            .onSuccess {
//                logger.info("Rabbit client has started successfully")
//                startPromise.complete()
//            }.onFailure { cause ->
//                logger.error("Failed to connect to rabbitmq for ${this::class.simpleName} worker", cause)
//                startPromise.fail(cause)
//            }
//    }
//
//    private fun consume(void: Void): Future<Void> {
//        val options = QueueOptions()
//            .setAutoAck(false)
//
//        client.basicConsumer(Config.rbSendMailRequestQueue, options)
//            .onSuccess(::consumerHandler)
//            .onFailure { cause ->
//                logger.error("Failed to consume message on queue: ${Config.rbSendMailRequestQueue}", cause)
//            }
//
//        return Future.succeededFuture(void)
//    }
//
//    private fun consumerHandler(consumer: RabbitMQConsumer) {
//        consumer.handler { msg ->
//            val deliveryTag = msg.envelope().deliveryTag
//            val body = msg.body()
//            val replyProps = msg.properties()
//            val messageId = replyProps.messageId
//
//            logger.info("About to send mail request form mail request with id: $messageId")
//
//            val deliveryOptions = DeliveryOptions()
//                .setSendTimeout(Config.ebSendTimeout)
//
//            vertx.eventBus().request<JsonObject>(EBAddress.EMAILDISPATCHER, body.toJsonObject(), deliveryOptions)
//                .onSuccess {
//                    logger.info("Successfully received response from EmailDispatcher from mail request with id: $messageId")
//                    client.basicAck(deliveryTag, false)
//                        .onSuccess {
//                            logger.info("Acknowledged broker for mail request with id: $messageId.\nUpdating mail in db")
//                            val email = Json.decodeValue(body, Email::class.java)
//                            val updatedEmail = repo.update(email)
//                            logger.info("Successfully updated email: $updatedEmail...")
//                        }
//                }
//                .onFailure { ackCause ->
//                    logger.error(
//                        "Failed to receive mail response from the EmailDispatcher for mail request with id: $messageId.\nRequeueing mail request to broker",
//                        ackCause
//                    )
//                    client.basicNack(deliveryTag, false, true)
//                        .onSuccess { logger.info("Mail request with id: $messageId has been queued") }
//                        .onFailure { nackCause ->
//                            logger.error(
//                                "Failed to queue mail request with id: $messageId",
//                                nackCause
//                            )
//                        }
//                }
//        }
//    }
//
//    private fun rbConnectionCallbackHandler(callbackPromise: Promise<Void>) {
//        client.queueDeclare(Config.rbSendMailRequestQueue, true, false, false)
//            .compose { client.queueDeclare(Config.rbSendMailResponseQueue, true, false, false) }
//            .compose { client.basicQos(1) }
//            .onSuccess { callbackPromise.complete() }
//            .onFailure(callbackPromise::fail)
//    }
//}