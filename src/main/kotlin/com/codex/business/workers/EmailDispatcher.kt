package com.codex.business.workers

//
//class EmailDispatcher : AbstractVerticle() {
//    private lateinit var client: MailClient
//    private val logger = LoggerFactory.getLogger(this::class.java)
//
//    override fun start() {
//        logger.info("${this::class.simpleName} worker is ready!!")
//
//        val config = MailConfig()
//            .setHostname(Config.smtpHost)
//            .setPort(Config.smtpPort)
//            .setUsername(Config.smtpUsername)
//            .setPassword(Config.smtpPassword)
//            .setStarttls(StartTLSOptions.REQUIRED)
//
//        client = MailClient.create(vertx, config)
//
//        vertx.eventBus().consumer(EBAddress.EMAILDISPATCHER, ::onMessage)
//    }
//
//    private fun onMessage(msg: Message<JsonObject>) {
//        val replyAddress = msg.replyAddress()
//        val headers = msg.headers()
//        val body = msg.body()
//
//        logger.info("Received message from {} with headers: {} and body: {}", replyAddress, headers, body)
//
//        val mailDTO = Json.decodeValue(body.encode(), EmailDTO::class.java)
//
//        val mailMessage = MailMessage()
//            .setFrom(Config.smtpUsername)
//            .setTo(mailDTO.to)
//            .setText(mailDTO.message)
//
//        client.sendMail(mailMessage)
//            .onSuccess { result ->
//                logger.info("Mail sent successfully with id: ${result.messageID}...")
//                msg.reply(result.toJson())
//            }
//            .onFailure { cause ->
//                logger.error("Failed to send email", cause)
//                msg.fail(500, "Failed to send mail.")
//            }
//    }
//}