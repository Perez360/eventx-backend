package com.codex.base.shared

import io.vertx.core.json.JsonObject

sealed class AppResponse<out T>(val code: String, val message: String, val data: T?) {
    class Success<T>(message: String, data: T?) : AppResponse<T>(Texts.CODE_SUCCESS, message, data)
    class Failure(message: String) : AppResponse<Nothing>(Texts.CODE_FAILURE, message, null)
    class Error(message: String) : AppResponse<Nothing>(Texts.CODE_ERROR, message, null)
    class Unauthorized(message: String) : AppResponse<Nothing>(Texts.CODE_UNAUTHORIZED, message, null)
    class Async(message: String) : AppResponse<Nothing>(Texts.CODE_ASYNC, message, null)

    companion object {
        inline fun <reified T> fromJson(json: JsonObject): AppResponse<T> {
            val code = json.getString("code")
            val message = json.getString("message")
            val data = json.getJsonObject("data")?.mapTo(T::class.java)


            return when (code) {
                Texts.CODE_SUCCESS -> Success(message, data)
                Texts.CODE_FAILURE -> Failure(message)
                Texts.CODE_ERROR -> Error(message)
                Texts.CODE_UNAUTHORIZED -> Unauthorized(message)
                Texts.CODE_ASYNC -> Async(message)
                else -> throw IllegalArgumentException("Unknown response code: $code")
            }
        }
    }
}