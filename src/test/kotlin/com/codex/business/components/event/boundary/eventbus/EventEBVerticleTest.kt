package com.codex.business.components.event.boundary.eventbus

import com.codex.base.CODE_SUCCESS
import com.codex.base.core.settings.Configuration
import com.codex.base.core.settings.DatabindCodecUtils
import com.codex.base.shared.AppResponse
import com.codex.base.shared.PagedContent
import com.codex.base.utils.wrapSuccessInResponse
import com.codex.business.base.core.settings.MockTestBase
import com.codex.business.common.EBAction
import com.codex.business.common.EBAddress
import com.codex.business.components.event.controller.EventController
import com.codex.business.components.eventReaction.dto.EventDto
import com.codex.business.components.event.spec.EventSpec
import com.codex.business.generator
import com.codex.business.mockedAddEventDTO
import com.codex.business.mockedEventDTO
import com.codex.business.mockedUpdateEventDTO
import io.mockk.every
import io.mockk.verifyAll
import io.vertx.core.DeploymentOptions
import io.vertx.core.Vertx
import io.vertx.core.eventbus.DeliveryOptions
import io.vertx.core.eventbus.Message
import io.vertx.core.json.JsonObject
import io.vertx.junit5.VertxExtension
import io.vertx.junit5.VertxTestContext
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.koin.test.mock.declareMock
import java.util.stream.Collectors

@ExtendWith(VertxExtension::class)
class EventEBVerticleTest : MockTestBase() {

    @BeforeEach
    fun setUp(vertx: Vertx, ctx: VertxTestContext) {
        Configuration.loadSystemProperties()
        DatabindCodecUtils.configureSerialization()

        val options = DeploymentOptions()
            .setWorker(true)
        vertx.deployVerticle(EventEBVerticle::class.java, options, ctx.succeedingThenComplete())
    }

    @Test
    fun `it should add a record`(vertx: Vertx, ctx: VertxTestContext) {
        //GIVEN
        val addDTO = mockedAddEventDTO()
        val dto = mockedEventDTO()
        val payload = JsonObject.mapFrom(addDTO)
        val mockEventController = declareMock<EventController> {
            every { add(any()) } returns wrapSuccessInResponse(dto)
        }

        //WHEN
        val options = DeliveryOptions()
            .addHeader("action", EBAction.EventEBAction.CREATE)
        vertx.eventBus().request<JsonObject>(EBAddress.EVENT, payload, options)
            .onComplete(ctx.succeeding<Message<JsonObject>> { msg ->
                val result = AppResponse.fromJson<EventDto>(msg.body())
                ctx.verify {
                    //THEN
                    verifyAll { mockEventController.add(any()) }
                    Assertions.assertThat(result.code).isEqualTo(CODE_SUCCESS)
                    Assertions.assertThat(result.data).isEqualTo(dto)
                    Assertions.assertThat(result).isInstanceOf(AppResponse::class.java)
                    Assertions.assertThat(result.data).isInstanceOf(EventDto::class.java)
                    ctx.completeNow()
                }
            })
    }

    @Test
    fun `it should update a record`(vertx: Vertx, ctx: VertxTestContext) {
        //GIVEN
        val addDTO = mockedUpdateEventDTO()
        val dto = mockedEventDTO()
        val payload = JsonObject.mapFrom(addDTO)
        val mockEventController = declareMock<EventController> {
            every { update(any()) } returns wrapSuccessInResponse(dto)
        }

        //WHEN
        val options = DeliveryOptions()
            .addHeader("action", EBAction.EventEBAction.UPDATE)
        vertx.eventBus().request<JsonObject>(EBAddress.EVENT, payload, options)
            .onComplete(ctx.succeeding<Message<JsonObject>> { msg ->
                val result = AppResponse.fromJson<EventDto>(msg.body())
                ctx.verify {
                    //THEN
                    verifyAll { mockEventController.update(any()) }
                    Assertions.assertThat(result.code).isEqualTo(CODE_SUCCESS)
                    Assertions.assertThat(result.data).isEqualTo(dto)
                    Assertions.assertThat(result).isInstanceOf(AppResponse::class.java)
                    Assertions.assertThat(result.data).isInstanceOf(EventDto::class.java)
                    ctx.completeNow()
                }
            })
    }

    @Test
    fun `it should get a record by id`(vertx: Vertx, ctx: VertxTestContext) {
        //GIVEN
        val dto = mockedEventDTO()
        val payload = JsonObject()
            .put("id", dto.id)
        val mockEventController = declareMock<EventController> {
            every { getById(any()) } returns wrapSuccessInResponse(dto)
        }

        //WHEN
        val options = DeliveryOptions()
            .addHeader("action", EBAction.EventEBAction.GET_BY_ID)
        vertx.eventBus().request<JsonObject>(EBAddress.EVENT, payload, options)
            .onComplete(ctx.succeeding<Message<JsonObject>> { msg ->
                val result: Response<EventDto> = AppResponse.fromJson(msg.body())
                ctx.verify {
                    //THEN
                    verifyAll { mockEventController.findById(any()) }
                    Assertions.assertThat(result.code).isEqualTo(CODE_SUCCESS)
                    Assertions.assertThat(result.data).isEqualTo(dto)
                    Assertions.assertThat(result).isInstanceOf(AppResponse::class.java)
                    Assertions.assertThat(result.data).isInstanceOf(EventDto::class.java)
                    ctx.completeNow()
                }
            })
    }

    @Test
    fun `it should delete a record by id`(vertx: Vertx, ctx: VertxTestContext) {
        //GIVEN
        val dto = mockedEventDTO()
        val payload = JsonObject()
            .put("id", dto.id)
        val mockEventController = declareMock<EventController> {
            every { deleteById(any()) } returns wrapSuccessInResponse(dto)
        }

        //WHEN
        val options = DeliveryOptions()
            .addHeader("action", EBAction.EventEBAction.DELETE_BY_ID)
        vertx.eventBus().request<JsonObject>(EBAddress.EVENT, payload, options)
            .onComplete(ctx.succeeding<Message<JsonObject>> { msg ->
                val result = AppResponse.fromJson<EventDto>(msg.body())
                ctx.verify {
                    //THEN
                    verifyAll { mockEventController.deleteById(any()) }
                    Assertions.assertThat(result.code).isEqualTo(CODE_SUCCESS)
                    Assertions.assertThat(result.data).isEqualTo(dto)
                    Assertions.assertThat(result).isInstanceOf(AppResponse::class.java)
                    Assertions.assertThat(result.data).isInstanceOf(EventDto::class.java)
                    ctx.completeNow()
                }
            })
    }


    @Test
    fun `it should list some record`(vertx: Vertx, ctx: VertxTestContext) {
        //GIVEN
        val pagedContent = PagedContent<EventDto>()
        pagedContent.page = 1
        pagedContent.size = 30
        pagedContent.totalPages = 50
        pagedContent.totalElements = 5
        pagedContent.isFirst = true
        pagedContent.isLast = false
        pagedContent.hasNextPage = true
        pagedContent.hasPreviousPage = false
        pagedContent.data = generator.objects(EventDto::class.java, 1).collect(Collectors.toList())

        val payload = JsonObject()
            .put("page", pagedContent.page)
            .put("size", pagedContent.size)
        val mockEventController = declareMock<EventController> {
            every { list(any(), any()) } returns wrapSuccessInResponse(pagedContent)
        }

        //WHEN
        val options = DeliveryOptions()
            .addHeader("action", EBAction.EventEBAction.LIST)
        vertx.eventBus().request<JsonObject>(EBAddress.EVENT, payload, options)
            .onComplete(ctx.succeeding<Message<JsonObject>> { msg ->
                val result: Response<PagedContent<EventDto>> = AppResponse.fromJson(msg.body())
                println("Result: ${result.data}")
                println("PagedContent: $pagedContent")
                ctx.verify {
                    //THEN
                    verifyAll { mockEventController.list(any(), any()) }
                    Assertions.assertThat(result.code).isEqualTo(CODE_SUCCESS)
                    Assertions.assertThat(result).isInstanceOf(AppResponse::class.java)
                    Assertions.assertThat(result.data).isInstanceOf(PagedContent::class.java)
                    ctx.completeNow()
                }
            })
    }

    @Test
    fun `it should search some record`(vertx: Vertx, ctx: VertxTestContext) {
        //GIVEN
        val pagedContent = PagedContent<EventDto>()
        pagedContent.page = 1
        pagedContent.size = 30
        pagedContent.totalPages = 50
        pagedContent.totalElements = 5
        pagedContent.hasNextPage = true
        pagedContent.hasPreviousPage = false
        pagedContent.data = generator.objects(EventDto::class.java, 5).collect(Collectors.toList())

        val spec = EventSpec()
        spec.page = pagedContent.page
        spec.size = pagedContent.size

        val payload = JsonObject.mapFrom(spec)
        val mockEventController = declareMock<EventController> {
            every { search(any()) } returns wrapSuccessInResponse(pagedContent)
        }

        //WHEN
        val options = DeliveryOptions()
            .addHeader("action", EBAction.EventEBAction.SEARCH)
        vertx.eventBus().request<JsonObject>(EBAddress.EVENT, payload, options)
            .onComplete(ctx.succeeding<Message<JsonObject>> { msg ->
                val result = AppResponse.fromJson<PagedContent<EventDto>>(msg.body())
                ctx.verify {
                    //THEN
                    verifyAll { mockEventController.search(any()) }
                    Assertions.assertThat(result.code).isEqualTo(CODE_SUCCESS)
                    Assertions.assertThat(result).isInstanceOf(AppResponse::class.java)
                    Assertions.assertThat(result.data).isInstanceOf(PagedContent::class.java)
                    ctx.completeNow()
                }
            })
    }
}