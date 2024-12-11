package com.codex.business.components.user.boundary.eventbus

import com.codex.base.CODE_SUCCESS
import com.codex.base.core.settings.Configuration
import com.codex.base.core.settings.DatabindCodecUtils
import com.codex.base.shared.AppResponse
import com.codex.base.shared.PagedContent
import com.codex.base.utils.wrapSuccessInResponse
import com.codex.business.base.core.settings.MockTestBase
import com.codex.business.common.EBAction
import com.codex.business.common.EBAddress
import com.codex.business.components.user.controller.UserController
import com.codex.business.components.userProfile.dto.UserDto
import com.codex.business.components.userProfile.spec.UserSpec
import com.codex.business.generator
import com.codex.business.mockedAddUserDto
import com.codex.business.mockedUpdateUserDto
import com.codex.business.mockedUserDto
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
class UserEBVerticleTest : MockTestBase() {

    @BeforeEach
    fun setUp(vertx: Vertx, ctx: VertxTestContext) {
        Configuration.loadSystemProperties()
        DatabindCodecUtils.configureSerialization()

        val options = DeploymentOptions()
            .setWorker(true)
        vertx.deployVerticle(UserEBVerticle::class.java, options, ctx.succeedingThenComplete())
    }

    @Test
    fun `it should add a record`(vertx: Vertx, ctx: VertxTestContext) {
        //GIVEN
        val addDTO = mockedAddUserDto()
        val dto = mockedUserDto()
        val payload = JsonObject.mapFrom(addDTO)
        val mockUserController = declareMock<UserController> {
            every { add(any()) } returns wrapSuccessInResponse(dto)
        }

        //WHEN
        val options = DeliveryOptions()
            .addHeader("action", EBAction.UserEBAction.CREATE)
        vertx.eventBus().request<JsonObject>(EBAddress.USER, payload, options)
            .onComplete(ctx.succeeding<Message<JsonObject>> { msg ->
                val result = AppResponse.fromJson<UserDto>(msg.body())
                ctx.verify {
                    //THEN
                    verifyAll { mockUserController.add(any()) }
                    Assertions.assertThat(result.code).isEqualTo(CODE_SUCCESS)
                    Assertions.assertThat(result.data).isEqualTo(dto)
                    Assertions.assertThat(result).isInstanceOf(AppResponse::class.java)
                    Assertions.assertThat(result.data).isInstanceOf(UserDto::class.java)
                    ctx.completeNow()
                }
            })
    }

    @Test
    fun `it should update a record`(vertx: Vertx, ctx: VertxTestContext) {
        //GIVEN
        val addDTO = mockedUpdateUserDto()
        val dto = mockedUserDto()
        val payload = JsonObject.mapFrom(addDTO)
        val mockUserController = declareMock<UserController> {
            every { update(any()) } returns wrapSuccessInResponse(dto)
        }

        //WHEN
        val options = DeliveryOptions()
            .addHeader("action", EBAction.UserEBAction.UPDATE)
        vertx.eventBus().request<JsonObject>(EBAddress.USER, payload, options)
            .onComplete(ctx.succeeding<Message<JsonObject>> { msg ->
                val result = AppResponse.fromJson<UserDto>(msg.body())
                ctx.verify {
                    //THEN
                    verifyAll { mockUserController.update(any()) }
                    Assertions.assertThat(result.code).isEqualTo(CODE_SUCCESS)
                    Assertions.assertThat(result.data).isEqualTo(dto)
                    Assertions.assertThat(result).isInstanceOf(AppResponse::class.java)
                    Assertions.assertThat(result.data).isInstanceOf(UserDto::class.java)
                    ctx.completeNow()
                }
            })
    }

    @Test
    fun `it should get a record by id`(vertx: Vertx, ctx: VertxTestContext) {
        //GIVEN
        val dto = mockedUserDto()
        val payload = JsonObject()
            .put("id", dto.id)
        val mockUserController = declareMock<UserController> {
            every { getById(any()) } returns wrapSuccessInResponse(dto)
        }

        //WHEN
        val options = DeliveryOptions()
            .addHeader("action", EBAction.UserEBAction.GET_BY_ID)
        vertx.eventBus().request<JsonObject>(EBAddress.USER, payload, options)
            .onComplete(ctx.succeeding<Message<JsonObject>> { msg ->
                val result: Response<UserDto> = AppResponse.fromJson(msg.body())
                ctx.verify {
                    //THEN
                    verifyAll { mockUserController.findById(any()) }
                    Assertions.assertThat(result.code).isEqualTo(CODE_SUCCESS)
                    Assertions.assertThat(result.data).isEqualTo(dto)
                    Assertions.assertThat(result).isInstanceOf(AppResponse::class.java)
                    Assertions.assertThat(result.data).isInstanceOf(UserDto::class.java)
                    ctx.completeNow()
                }
            })
    }

    @Test
    fun `it should delete a record by id`(vertx: Vertx, ctx: VertxTestContext) {
        //GIVEN
        val dto = mockedUserDto()
        val payload = JsonObject()
            .put("id", dto.id)
        val mockUserController = declareMock<UserController> {
            every { deleteById(any()) } returns wrapSuccessInResponse(dto)
        }

        //WHEN
        val options = DeliveryOptions()
            .addHeader("action", EBAction.UserEBAction.DELETE_BY_ID)
        vertx.eventBus().request<JsonObject>(EBAddress.USER, payload, options)
            .onComplete(ctx.succeeding<Message<JsonObject>> { msg ->
                val result = AppResponse.fromJson<UserDto>(msg.body())
                ctx.verify {
                    //THEN
                    verifyAll { mockUserController.deleteById(any()) }
                    Assertions.assertThat(result.code).isEqualTo(CODE_SUCCESS)
                    Assertions.assertThat(result.data).isEqualTo(dto)
                    Assertions.assertThat(result).isInstanceOf(AppResponse::class.java)
                    Assertions.assertThat(result.data).isInstanceOf(UserDto::class.java)
                    ctx.completeNow()
                }
            })
    }


    @Test
    fun `it should list some record`(vertx: Vertx, ctx: VertxTestContext) {
        //GIVEN
        val pagedContent = PagedContent<UserDto>()
        pagedContent.page = 1
        pagedContent.size = 30
        pagedContent.totalPages = 50
        pagedContent.totalElements = 5
        pagedContent.isFirst = true
        pagedContent.isLast = false
        pagedContent.hasNextPage = true
        pagedContent.hasPreviousPage = false
        pagedContent.data = generator.objects(UserDto::class.java, 1).collect(Collectors.toList())

        val payload = JsonObject()
            .put("page", pagedContent.page)
            .put("size", pagedContent.size)
        val mockUserController = declareMock<UserController> {
            every { list(any(), any()) } returns wrapSuccessInResponse(pagedContent)
        }

        //WHEN
        val options = DeliveryOptions()
            .addHeader("action", EBAction.UserEBAction.LIST)
        vertx.eventBus().request<JsonObject>(EBAddress.USER, payload, options)
            .onComplete(ctx.succeeding<Message<JsonObject>> { msg ->
                val result: Response<PagedContent<UserDto>> = AppResponse.fromJson(msg.body())
                println("Result: ${result.data}")
                println("PagedContent: $pagedContent")
                ctx.verify {
                    //THEN
                    verifyAll { mockUserController.list(any(), any()) }
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
        val pagedContent = PagedContent<UserDto>()
        pagedContent.page = 1
        pagedContent.size = 30
        pagedContent.totalPages = 50
        pagedContent.totalElements = 5
        pagedContent.hasNextPage = true
        pagedContent.hasPreviousPage = false
        pagedContent.data = generator.objects(UserDto::class.java, 5).collect(Collectors.toList())

        val spec = UserSpec()
        spec.page = pagedContent.page
        spec.size = pagedContent.size

        val payload = JsonObject.mapFrom(spec)
        val mockUserController = declareMock<UserController> {
            every { search(any()) } returns wrapSuccessInResponse(pagedContent)
        }

        //WHEN
        val options = DeliveryOptions()
            .addHeader("action", EBAction.UserEBAction.SEARCH)
        vertx.eventBus().request<JsonObject>(EBAddress.USER, payload, options)
            .onComplete(ctx.succeeding<Message<JsonObject>> { msg ->
                val result = AppResponse.fromJson<PagedContent<UserDto>>(msg.body())
                ctx.verify {
                    //THEN
                    verifyAll { mockUserController.search(any()) }
                    Assertions.assertThat(result.code).isEqualTo(CODE_SUCCESS)
                    Assertions.assertThat(result).isInstanceOf(AppResponse::class.java)
                    Assertions.assertThat(result.data).isInstanceOf(PagedContent::class.java)
                    ctx.completeNow()
                }
            })
    }
}