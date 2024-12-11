package com.codex.base.core.di.modules

//val RABBITMQ_MODULE = module {
//
//    single<RabbitMQOptions> {
//        RabbitMQOptions().setHost(Config.rbHost).setPort(Config.rbPort).setUser(Config.rbUsername)
//            .setPassword(Config.rbPassword).setVirtualHost(Config.rbVhost)
//            .setAutomaticRecoveryEnabled(true)
//    }
//
//    single<RabbitMQClient> {
//        RabbitMQClient.create(get(), get<RabbitMQOptions>()).apply {
//            addConnectionEstablishedCallback { callbackPromise ->
//                queueDeclare(Config.rbSendMailRequestQueue, true, false, false)
//                    .compose { queueDeclare(Config.rbSendMailResponseQueue, true, false, false) }
//                    .onSuccess {
//                        logger.info("Configuration is set for rabbitmq client. Ready to publish and consume messages...")
//                        callbackPromise.complete()
//                    }.onFailure(callbackPromise::fail)
//            }
//        }.also {
//            it.start()
//                .onSuccess {
//                    logger.info("Rabbitmq client connected successfully")
//                }.onFailure { cause -> logger.error("Failed to connect to broker: ${cause.message}") }
//        }
//    }
//}