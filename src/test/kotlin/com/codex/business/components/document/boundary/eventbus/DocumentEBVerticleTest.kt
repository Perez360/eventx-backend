package com.codex.business.components.document.boundary.eventbus

import com.codex.base.CODE_SUCCESS
import com.codex.base.core.settings.Configuration
import com.codex.base.core.settings.DatabindCodecUtils
import com.codex.base.shared.AppResponse
import com.codex.base.shared.PagedContent
import com.codex.base.utils.wrapSuccessInResponse
import com.codex.business.base.core.settings.MockTestBase
import com.codex.business.common.EBAction
import com.codex.business.common.EBAddress
import com.codex.business.components.document.controller.DocumentController
import com.codex.business.components.document.dto.AddDocumentDTO
import com.codex.business.components.document.dto.DocumentDto
import com.codex.business.components.document.spec.DocumentSpec
import com.codex.business.generator
import com.codex.business.mockedAddDocumentDTO
import com.codex.business.mockedDocumentDTO
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
class DocumentEBVerticleTest : MockTestBase() {

    @BeforeEach
    fun setUp(vertx: Vertx, ctx: VertxTestContext) {
        Configuration.loadSystemProperties()
        DatabindCodecUtils.configureSerialization()

        val options = DeploymentOptions()
            .setWorker(true)
        vertx.deployVerticle(DocumentEBVerticle::class.java, options, ctx.succeedingThenComplete())
    }

    @Test
    fun `it should add a record`(vertx: Vertx, ctx: VertxTestContext) {
        //GIVEN
        val addDTO = generator.nextObject(AddDocumentDTO::class.java)
        val dto = generator.nextObject(DocumentDto::class.java)
        val payload = JsonObject.mapFrom(addDTO)
        val mockDocumentController = declareMock<DocumentController> {
            every { add(any()) } returns wrapSuccessInResponse(dto)
        }

        //WHEN
        val options = DeliveryOptions()
            .addHeader("action", EBAction.DocumentEBAction.CREATE)
        vertx.eventBus().request<JsonObject>(EBAddress.DOCUMENT, payload, options)
            .onComplete(ctx.succeeding<Message<JsonObject>> { msg ->
                val result = AppResponse.fromJson<DocumentDto>(msg.body())
                ctx.verify {
                    //THEN
                    verifyAll { mockDocumentController.add(any()) }
                    Assertions.assertThat(result.code).isEqualTo(CODE_SUCCESS)
                    Assertions.assertThat(result.data).isEqualTo(dto)
                    Assertions.assertThat(result).isInstanceOf(AppResponse::class.java)
                    Assertions.assertThat(result.data).isInstanceOf(DocumentDto::class.java)
                    ctx.completeNow()
                }
            })
    }

    @Test
    fun `it should update a record`(vertx: Vertx, ctx: VertxTestContext) {
        //GIVEN
        val addDTO = mockedAddDocumentDTO()
        val dto = mockedDocumentDTO()
        val payload = JsonObject.mapFrom(addDTO)
        val mockDocumentController = declareMock<DocumentController> {
            every { update(any()) } returns wrapSuccessInResponse(dto)
        }

        //WHEN
        val options = DeliveryOptions()
            .addHeader("action", EBAction.DocumentEBAction.UPDATE)
        vertx.eventBus().request<JsonObject>(EBAddress.DOCUMENT, payload, options)
            .onComplete(ctx.succeeding<Message<JsonObject>> { msg ->
                val result = AppResponse.fromJson<DocumentDto>(msg.body())
                ctx.verify {
                    //THEN
                    verifyAll { mockDocumentController.update(any()) }
                    Assertions.assertThat(result.code).isEqualTo(CODE_SUCCESS)
                    Assertions.assertThat(result.data).isEqualTo(dto)
                    Assertions.assertThat(result).isInstanceOf(AppResponse::class.java)
                    Assertions.assertThat(result.data).isInstanceOf(DocumentDto::class.java)
                    ctx.completeNow()
                }
            })
    }

    @Test
    fun `it should get a record by id`(vertx: Vertx, ctx: VertxTestContext) {
        //GIVEN
        val dto = mockedDocumentDTO()
        val payload = JsonObject()
            .put("id", dto.id)
        val mockDocumentController = declareMock<DocumentController> {
            every { getById(any()) } returns wrapSuccessInResponse(dto)
        }

        //WHEN
        val options = DeliveryOptions()
            .addHeader("action", EBAction.DocumentEBAction.GET_BY_ID)
        vertx.eventBus().request<JsonObject>(EBAddress.DOCUMENT, payload, options)
            .onComplete(ctx.succeeding<Message<JsonObject>> { msg ->
                val result: Response<DocumentDto> = AppResponse.fromJson(msg.body())
                ctx.verify {
                    //THEN
                    verifyAll { mockDocumentController.findById(any()) }
                    Assertions.assertThat(result.code).isEqualTo(CODE_SUCCESS)
                    Assertions.assertThat(result.data).isEqualTo(dto)
                    Assertions.assertThat(result).isInstanceOf(AppResponse::class.java)
                    Assertions.assertThat(result.data).isInstanceOf(DocumentDto::class.java)
                    ctx.completeNow()
                }
            })
    }

    @Test
    fun `it should delete a record by id`(vertx: Vertx, ctx: VertxTestContext) {
        //GIVEN
        val dto = mockedDocumentDTO()
        val payload = JsonObject()
            .put("id", dto.id)
        val mockDocumentController = declareMock<DocumentController> {
            every { deleteById(any()) } returns wrapSuccessInResponse(dto)
        }

        //WHEN
        val options = DeliveryOptions()
            .addHeader("action", EBAction.DocumentEBAction.DELETE_BY_ID)
        vertx.eventBus().request<JsonObject>(EBAddress.DOCUMENT, payload, options)
            .onComplete(ctx.succeeding<Message<JsonObject>> { msg ->
                val result = AppResponse.fromJson<DocumentDto>(msg.body())
                ctx.verify {
                    //THEN
                    verifyAll { mockDocumentController.deleteById(any()) }
                    Assertions.assertThat(result.code).isEqualTo(CODE_SUCCESS)
                    Assertions.assertThat(result.data).isEqualTo(dto)
                    Assertions.assertThat(result).isInstanceOf(AppResponse::class.java)
                    Assertions.assertThat(result.data).isInstanceOf(DocumentDto::class.java)
                    ctx.completeNow()
                }
            })
    }


    @Test
    fun `it should list some record`(vertx: Vertx, ctx: VertxTestContext) {
        //GIVEN
        val pagedContent = PagedContent<DocumentDto>()
        pagedContent.page = 1
        pagedContent.size = 30
        pagedContent.totalPages = 50
        pagedContent.totalElements = 5
        pagedContent.isFirst = true
        pagedContent.isLast = false
        pagedContent.hasNextPage = true
        pagedContent.hasPreviousPage = false
        pagedContent.data = generator.objects(DocumentDto::class.java, 1).collect(Collectors.toList())

        val payload = JsonObject()
            .put("page", pagedContent.page)
            .put("size", pagedContent.size)
        val mockDocumentController = declareMock<DocumentController> {
            every { list(any(), any()) } returns wrapSuccessInResponse(pagedContent)
        }

        //WHEN
        val options = DeliveryOptions()
            .addHeader("action", EBAction.DocumentEBAction.LIST)
        vertx.eventBus().request<JsonObject>(EBAddress.DOCUMENT, payload, options)
            .onComplete(ctx.succeeding<Message<JsonObject>> { msg ->
                val result: Response<PagedContent<DocumentDto>> = AppResponse.fromJson(msg.body())
                println("Result: ${result.data}")
                println("PagedContent: $pagedContent")
                ctx.verify {
                    //THEN
                    verifyAll { mockDocumentController.list(any(), any()) }
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
        val pagedContent = PagedContent<DocumentDto>()
        pagedContent.page = 1
        pagedContent.size = 30
        pagedContent.totalPages = 50
        pagedContent.totalElements = 5
        pagedContent.hasNextPage = true
        pagedContent.hasPreviousPage = false
        pagedContent.data = generator.objects(DocumentDto::class.java, 5).collect(Collectors.toList())

        val spec = DocumentSpec()
        spec.page = pagedContent.page
        spec.size = pagedContent.size

        val payload = JsonObject.mapFrom(spec)
        val mockDocumentController = declareMock<DocumentController> {
            every { search(any()) } returns wrapSuccessInResponse(pagedContent)
        }

        //WHEN
        val options = DeliveryOptions()
            .addHeader("action", EBAction.DocumentEBAction.SEARCH)
        vertx.eventBus().request<JsonObject>(EBAddress.DOCUMENT, payload, options)
            .onComplete(ctx.succeeding<Message<JsonObject>> { msg ->
                val result = AppResponse.fromJson<PagedContent<DocumentDto>>(msg.body())
                ctx.verify {
                    //THEN
                    verifyAll { mockDocumentController.search(any()) }
                    Assertions.assertThat(result.code).isEqualTo(CODE_SUCCESS)
                    Assertions.assertThat(result).isInstanceOf(AppResponse::class.java)
                    Assertions.assertThat(result.data).isInstanceOf(PagedContent::class.java)
                    ctx.completeNow()
                }
            })
    }
}