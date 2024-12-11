package com.codex.base.core.settings

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import io.vertx.core.json.jackson.DatabindCodec

object DatabindCodecUtils {
    private fun configureMapper(mapper: ObjectMapper) {
        mapper.findAndRegisterModules()
            .enable(SerializationFeature.INDENT_OUTPUT)
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
            .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
            .disable(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES)
            .disable(DeserializationFeature.FAIL_ON_NULL_CREATOR_PROPERTIES)
            .enable(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS)
    }

    fun configureSerialization() {
        configureMapper(DatabindCodec.mapper())
        configureMapper(DatabindCodec.prettyMapper())
    }
}
