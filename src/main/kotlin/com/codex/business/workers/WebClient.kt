package com.codex.business.workers
//
//import com.codex.base.core.settings.Config
//import com.codex.base.exceptions.GlobalExceptionHandler
//import com.codex.base.exceptions.ServiceException
//import com.codex.base.utils.toJson
//import com.codex.base.utils.wrapErrorInResponse
//import com.codex.base.utils.wrapFailureInResponse
//import com.codex.base.utils.wrapSuccessInResponse
//import com.codex.business.commons.EBAddress
//import com.codex.business.commons.enums.SupportedHttpMethod
//import io.vertx.core.AbstractVerticle
//import io.vertx.core.buffer.Buffer
//import io.vertx.core.eventbus.Message
//import io.vertx.core.http.HttpHeaders
//import io.vertx.core.http.HttpMethod
//import io.vertx.core.json.DecodeException
//import io.vertx.core.json.JsonObject
//import io.vertx.ext.web.client.HttpResponse
//import io.vertx.ext.web.client.WebClient
//import io.vertx.ext.web.client.WebClientOptions
//import org.slf4j.Logger
//import org.slf4j.LoggerFactory
//
//class WebClient : AbstractVerticle() {
//    private val logger: Logger = LoggerFactory.getLogger(this::class.java)
//    private lateinit var client: WebClient
//
//    override fun start() {
//        logger.info("Started EB consumer verticle for webclient {}", EBAddress.WEB_CLIENT)
//
//        vertx.eventBus().consumer(EBAddress.WEB_CLIENT, ::onMessage)
//            .exceptionHandler(GlobalExceptionHandler::handler)
//
//        val options = WebClientOptions()
//            .setSsl(true)
//            .setVerifyHost(true)
//            .setLogActivity(true)
//            .setName(this::class.simpleName)
//            .setConnectTimeout(Config.httpConnectionTimeout)
//
//        client = WebClient.create(vertx, options)
//    }
//
//    private fun onMessage(message: Message<JsonObject>) {
//
//        val address = message.address()
//        val headers = message.headers()
//        val request = message.body().mapTo(HttpRequest::class.java)
//
//        logger.info("Receiving request from address {} for request details-> {}", address, request)
//
//        try {
//            val httpRequest = client.requestAbs(HttpMethod(request.method?.name), request.url)
//            httpRequest.putHeaders(headers)
//            request.queryParams?.forEach(httpRequest::createQueryParam)
//
//            when (request.method) {
//
//                SupportedHttpMethod.GET -> {
//                    httpRequest.send()
//                        .onSuccess { res -> onReqSuccess(res, message) }
//                        .onFailure { cause -> onReqFailure(cause, message) }
//                }
//
//                SupportedHttpMethod.PUT,
//                SupportedHttpMethod.POST,
//                SupportedHttpMethod.PATCH -> {
//                    httpRequest.sendJsonObject(request.body)
//                        .onSuccess { res -> onReqSuccess(res, message) }
//                        .onFailure { cause -> onReqFailure(cause, message) }
//                }
//
//                else -> {
//                    val errMsg = "404- No handler found for method-> [${request.method}]"
//                    logger.error(errMsg)
//                    message.reply(wrapErrorInResponse<String>(errMsg).toJson())
//                }
//            }
//        } catch (decEx: DecodeException) {
//            logger.error("${request.method} could not be performed", decEx)
//            message.reply(wrapErrorInResponse<String>("Error decoding payload when performing method-> [${request.method}]: ${decEx.message}").toJson())
//        } catch (ex: ServiceException) {
//            logger.error("${request.method} Failed to process request", ex)
//            message.reply(wrapFailureInResponse<String>(ex.message).toJson())
//        } catch (ex: Exception) {
//            logger.error("${request.method} could not be performed", ex)
//            message.reply(wrapErrorInResponse<String>(ex.message).toJson())
//        }
//    }
//
//    private fun onReqFailure(cause: Throwable, message: Message<JsonObject>) {
//        logger.error("Error occurred whiles processing request: ${cause.message}")
//        message.reply(wrapFailureInResponse<String>(cause.message).toJson())
//    }
//
//    private fun onReqSuccess(response: HttpResponse<Buffer>, message: Message<JsonObject>) {
//        logger.info("Request processed successfully with response: ${response.bodyAsString()}")
//
//        val apiResponse = when (response.headers().get(HttpHeaders.CONTENT_TYPE)) {
//            "application/json" -> wrapSuccessInResponse(response.bodyAsJsonObject())
//            "text/plain",
//            "text/css",
//            "text/csv",
//            "text/html",
//            "text/javascript",
//                -> wrapSuccessInResponse(response.bodyAsString())
//
//            else -> wrapSuccessInResponse(response.body())
//        }
//        message.reply(apiResponse.toJson())
//    }
//}