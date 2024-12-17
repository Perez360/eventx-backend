package com.codex.base.core.settings

import com.codex.base.core.di.DependencyInjection
import com.codex.base.shared.Texts
import com.codex.business.components.comment.boundary.eventbus.CommentEBVerticle
import com.codex.business.components.event.boundary.eventbus.EventEBVerticle
import com.codex.business.components.category.boundary.eventbus.CategoryEBVerticle
import com.codex.business.components.file.boundary.eventbus.FileEBVerticle
import com.codex.business.components.kyc.boundary.eventbus.KycEBVerticle
import com.codex.business.components.user.boundary.eventbus.UserEBVerticle
import com.codex.business.workers.WebServer
import io.vertx.core.AbstractVerticle
import io.vertx.core.DeploymentOptions
import io.vertx.core.Future
import io.vertx.core.Verticle
import org.slf4j.Logger
import org.slf4j.LoggerFactory

abstract class Boostrap : AbstractVerticle() {
    protected val logger: Logger = LoggerFactory.getLogger(Boostrap::class.java)
    protected val verticles = mutableListOf<Future<String>>()

    override fun start() {
        DatabindCodecUtils.configureSerialization()
        Configuration.loadSystemProperties()
        DependencyInjection.init(vertx)

        verticles.addAll(
            listOf(
                deployVerticle(
                    UserEBVerticle::class.java, DeploymentOptions()
                        .setInstances(Texts.numberOfServiceInstances)
                        .setWorker(Texts.isWorkerService)
                ),
                deployVerticle(
                    CommentEBVerticle::class.java, DeploymentOptions()
                        .setInstances(Texts.numberOfServiceInstances)
                        .setWorker(Texts.isWorkerService)
                ),
                deployVerticle(
                    FileEBVerticle::class.java, DeploymentOptions()
                        .setInstances(Texts.numberOfServiceInstances)
                        .setWorker(Texts.isWorkerService)
                ),
                deployVerticle(
                    EventEBVerticle::class.java, DeploymentOptions()
                        .setInstances(Texts.numberOfServiceInstances)
                        .setWorker(Texts.isWorkerService)
                ),
                deployVerticle(
                    CategoryEBVerticle::class.java, DeploymentOptions()
                        .setInstances(Texts.numberOfServiceInstances)
                        .setWorker(Texts.isWorkerService)
                ),
                deployVerticle(
                    KycEBVerticle::class.java, DeploymentOptions()
                        .setInstances(Texts.numberOfServiceInstances)
                        .setWorker(Texts.isWorkerService)
                ),
                deployVerticle(
                    WebServer::class.java, DeploymentOptions()
                        .setInstances(Texts.numberOfServiceInstances)
                        .setWorker(Texts.isWorkerService)
                ),
            )
        )
    }

    private fun deployVerticle(verticleClass: Class<out Verticle>, options: DeploymentOptions): Future<String> {
        return vertx.deployVerticle(verticleClass.name, options)
    }
}