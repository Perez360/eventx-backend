package com.codex.base.utils

import org.modelmapper.ModelMapper
import org.modelmapper.config.Configuration
import org.modelmapper.convention.MatchingStrategies

object Mapper {
    val mapper = ModelMapper().apply {
        configuration.setSkipNullEnabled(true)
        configuration.setMatchingStrategy(MatchingStrategies.LOOSE)
        configuration.setFieldAccessLevel(Configuration.AccessLevel.PRIVATE)
        configuration.setFieldMatchingEnabled(true)
        configuration.setSkipNullEnabled(true)
    }

    inline fun <S, reified T> convert(source: S): T = mapper.map(source, T::class.java)
}