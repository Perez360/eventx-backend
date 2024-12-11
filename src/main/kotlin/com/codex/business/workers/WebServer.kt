package com.codex.business.workers

import com.codex.PingRouter
import com.codex.base.exceptions.GlobalExceptionHandler
import com.codex.base.shared.Texts
import com.codex.business.common.Routes
import com.codex.business.components.comment.boundary.web.CommentRouter
import com.codex.business.components.event.boundary.http.EventRouter
import com.codex.business.components.eventCategory.boundary.web.EventCategoryRouter
import com.codex.business.components.file.boundary.web.FileRouter
import com.codex.business.components.kyc.boundary.web.KycRouter
import com.codex.business.components.menu.boundary.http.MenuRouter
import com.codex.business.components.user.boundary.web.UserRouter
import io.vertx.core.AbstractVerticle
import io.vertx.core.Promise
import io.vertx.core.http.HttpMethod
import io.vertx.core.http.HttpServer
import io.vertx.core.http.HttpServerOptions
import io.vertx.ext.web.Router
import io.vertx.ext.web.handler.BodyHandler
import io.vertx.ext.web.handler.CorsHandler
import io.vertx.ext.web.handler.LoggerHandler
import org.slf4j.Logger
import org.slf4j.LoggerFactory


class WebServer : AbstractVerticle() {
    private lateinit var httpServer: HttpServer
    private val logger: Logger = LoggerFactory.getLogger(this::class.java)

    override fun start(startPromise: Promise<Void>) {


        val options = HttpServerOptions()
            .setCompressionSupported(true)
            .setLogActivity(true)

        vertx.createHttpServer(options)
            .requestHandler(mountRouters())
            .listen(Texts.SERVER_PORT).onSuccess {
                httpServer = it
                logger.info("Web Server started and running on port: ${it.actualPort()}")
                startPromise.complete()
            }.onFailure { cause ->
                logger.error("Failed to start web server: ${cause.message}")
                startPromise.fail(cause)
            }
    }

    private fun mountRouters(): Router {
        val router = Router.router(vertx)

        router.route().handler(configureCors())
        router.route().handler(BodyHandler.create())
        router.route().failureHandler(GlobalExceptionHandler)
        router.route().handler(LoggerHandler.create())
        router.route("/*").subRouter(PingRouter(vertx))

        router.route(Routes.User.path).subRouter(UserRouter(vertx))
        router.route(Routes.Kyc.path).subRouter(KycRouter(vertx))
        router.route(Routes.Comment.path).subRouter(CommentRouter(vertx))
        router.route(Routes.Event.path).subRouter(EventRouter(vertx))
        router.route(Routes.EventCategory.path).subRouter(EventCategoryRouter(vertx))
        router.route(Routes.File.path).subRouter(FileRouter(vertx))
        router.route(Routes.Menu.path).subRouter(MenuRouter(vertx))
        return router
    }


    private fun configureCors(): CorsHandler {
        return CorsHandler.create().addOrigin("*").allowedMethods(
            setOf(HttpMethod.GET, HttpMethod.POST, HttpMethod.PUT, HttpMethod.DELETE)
        )
    }

    override fun stop() {
        logger.info("Stopping web server...")
        httpServer.close()
    }
}