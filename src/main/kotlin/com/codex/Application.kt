package com.codex

import com.codex.base.core.settings.Boostrap
import io.vertx.core.Future
import io.vertx.core.Launcher
import io.vertx.core.Promise

class Application : Boostrap() {

    override fun start(startPromise: Promise<Void>) {
        super.start()
        Future.all(verticles)
            .onSuccess {
                logger.info("All verticle(s) were deployed successfully. We are live!")
                startPromise.complete()
            }
            .onFailure { cause ->
                logger.error("Failed to deploy all verticle(s)", cause)
                startPromise.fail(cause)
            }
    }


    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
//    val vertx = Vertx.vertx()
//    vertx.createHttpServer().requestHandler { request ->
//        val response: HttpServerResponse = request.response()
//        response.setChunked(true)
//        println("Hi...")
//
//        response.headers().add("Content-Type", "text/event-stream;charset=UTF-8")
//        response.headers().add("Connection", "keep-alive")
//        vertx.setPeriodic(5000) {
//            response.write(JsonObject().put("msg", "Hi").encode())
//        }
//    }.listen(8080)

            Launcher.executeCommand("run", Application::class.java.name)
        }
    }
}
